package com.moneyroomba.service;

import com.moneyroomba.config.Constants;
import com.moneyroomba.domain.Authority;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.repository.AuthorityRepository;
import com.moneyroomba.repository.PersistentTokenRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.dto.AdminUserDTO;
import com.moneyroomba.service.dto.UserDTO;
import com.moneyroomba.service.exception.NoSuchElementFoundException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    private final PersistentTokenRepository persistentTokenRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        PasswordEncoder passwordEncoder,
        PersistentTokenRepository persistentTokenRepository,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager
    ) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.persistentTokenRepository = persistentTokenRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail, String password) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);

                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .map(
                            userDetails -> {
                                userDetails.setIsTemporaryPassword(true);

                                return userDetails;
                            }
                        );

                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );

        User newUser = new User();
        UserDetails userDetails = new UserDetails();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is active **
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(null);
        //newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);

        //User Details
        userDetails.setPhone(userDTO.getPhone());
        userDetails.setCountry(userDTO.getCountry().toUpperCase());
        userDetails.setInternalUser(newUser);
        userDetails.setIsActive(true);
        userDetails.setIsTemporaryPassword(false);
        userDetails.setNotifications(true);
        userDetailsRepository.save(userDetails);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        UserDetails userDetails = new UserDetails();
        user.setLogin(userDTO.getEmail().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);

        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);

        //User Details
        userDetails.setPhone(userDTO.getPhone());
        userDetails.setCountry(userDTO.getCountry().toUpperCase());
        userDetails.setInternalUser(user);
        userDetails.setIsActive(true);
        userDetails.setIsTemporaryPassword(false);
        userDetails.setNotifications(userDTO.getNotifications());
        userDetailsRepository.save(userDetails);
        return user;
    }

    public User createUser(AdminUserDTO userDTO, String password) {
        User user = new User();
        UserDetails userDetails = new UserDetails();
        user.setLogin(userDTO.getEmail().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);

        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);

        //User Details
        userDetails.setPhone(userDTO.getPhone());
        userDetails.setCountry(userDTO.getCountry().toUpperCase());
        userDetails.setInternalUser(user);
        userDetails.setIsActive(true);
        userDetails.setIsTemporaryPassword(true);
        userDetails.setNotifications(userDTO.getNotifications());
        userDetailsRepository.save(userDetails);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();

                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .ifPresent(
                            userDetails -> {
                                userDetails.setPhone(userDTO.getPhone());
                                userDetails.setCountry(userDTO.getCountry());
                                userDetails.setNotifications(userDTO.getNotifications());
                                log.debug("Changed Information for UserDetails: {}", userDetails);
                            }
                        );

                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public Optional<AdminUserDTO> updateActivation(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setActivated(userDTO.isActivated());
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        log.debug("Llego acà", login);
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    user.setActivated(false);
                    //userRepository.save(user);
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                }
            );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(
        String firstName,
        String lastName,
        String email,
        String langKey,
        String imageUrl,
        String phone,
        String country,
        boolean notification
    ) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                        user.setLogin(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);

                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .ifPresent(
                            userDetails -> {
                                userDetails.setPhone(phone);
                                userDetails.setCountry(country);
                                userDetails.setNotifications(notification);
                                log.debug("Changed Information for UserDetails: {}", userDetails);
                            }
                        );

                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    public void updateUser(String code) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setActivationKey(code.replace("-", "").substring(0, 19));
                    Set<Authority> authorities = new HashSet<>();
                    authorityRepository.findById(AuthoritiesConstants.PREMIUM_USER).ifPresent(authorities::add);
                    authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
                    user.setAuthorities(authorities);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);

                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .map(
                            userDetails -> {
                                userDetails.setIsTemporaryPassword(false);

                                return userDetails;
                            }
                        );
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository
            .findByTokenDateBefore(now.minusMonths(1))
            .forEach(
                token -> {
                    log.debug("Deleting token {}", token.getSeries());
                    User user = token.getUser();
                    user.getPersistentTokens().remove(token);
                    persistentTokenRepository.delete(token);
                }
            );
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    private Optional<String> getCurrentUserLogin() {
        return SecurityUtils.getCurrentUserLogin();
    }

    @Transactional(readOnly = true)
    public boolean currentUserIsAdmin() {
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN);
    }

    @Transactional(readOnly = true)
    public boolean currentUserIsRegularUser() {
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER);
    }

    /*
    @Transactional(readOnly = true)
    public boolean currentUserIsPremiumUser() {
        return SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PREMIUM_USER);
    }*/

    @Transactional(readOnly = true)
    public Optional<User> getUser() {
        return userRepository.findOneByLogin(
            this.getCurrentUserLogin().orElseThrow(() -> new NoSuchElementFoundException("No Login found"))
        );
    }

    @Transactional(readOnly = true)
    public Optional<UserDetails> getUserDetailsByLogin() {
        Optional<User> user = userRepository.findOneByLogin(
            this.getCurrentUserLogin().orElseThrow(() -> new NoSuchElementFoundException("No Login found"))
        );

        return userDetailsRepository.findOneByInternalUser(user.orElseThrow(() -> new NoSuchElementFoundException("No User found")));
    }

    @Transactional(readOnly = true)
    public Optional<UserDetails> getUserDetailsByLogin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);

        return userDetailsRepository.findOneByInternalUser(user.orElseThrow(() -> new NoSuchElementFoundException("No User found")));
    }

    public void generateApiKey() {
        Optional
            .of(
                userRepository.findOneByLogin(
                    this.getCurrentUserLogin().orElseThrow(() -> new NoSuchElementFoundException("No Login found"))
                )
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .map(
                            userDetails -> {
                                String apiKey = UUID.randomUUID().toString().replace("-", "").substring(0, 15);
                                userDetails.setApiKey(apiKey);
                                log.debug("Changed Information for UserDetails: {}", userDetails);
                                return userDetails;
                            }
                        );
                    return user;
                }
            );
    }

    public void deleteApiKey() {
        Optional
            .of(
                userRepository.findOneByLogin(
                    this.getCurrentUserLogin().orElseThrow(() -> new NoSuchElementFoundException("No Login found"))
                )
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    userDetailsRepository
                        .findOneByInternalUserId(user.getId())
                        .map(
                            userDetails -> {
                                userDetails.setApiKey("");
                                log.debug("Changed Information for UserDetails: {}", userDetails);
                                return userDetails;
                            }
                        );
                    return user;
                }
            );
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserFromAdminUserDTO(AdminUserDTO userDTO) {
        return userRepository.findOneByLogin(userDTO.getLogin());
    }

    @Transactional(readOnly = true)
    public Optional<AdminUserDTO> getAdminUserDTOFromUser(User user) {
        return userRepository.findOneByLogin(user.getLogin()).map(AdminUserDTO::new);
    }
}
