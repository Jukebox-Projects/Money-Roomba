package com.moneyroomba.web.rest;

import com.moneyroomba.domain.PersistentToken;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.repository.PersistentTokenRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.MailService;
import com.moneyroomba.service.UserService;
import com.moneyroomba.service.dto.AdminUserDTO;
import com.moneyroomba.service.dto.PasswordChangeDTO;
import com.moneyroomba.web.rest.errors.*;
import com.moneyroomba.web.rest.vm.KeyAndPasswordVM;
import com.moneyroomba.web.rest.vm.ManagedUserVM;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final UserService userService;

    private final MailService mailService;

    private final PersistentTokenRepository persistentTokenRepository;

    public AccountResource(
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        UserDetailsRepository userDetailsRepository1,
        UserService userService,
        MailService mailService,
        PersistentTokenRepository persistentTokenRepository
    ) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl(),
            userDTO.getPhone(),
            userDTO.getCountry()
        );
        mailService.sendProfileChange(user.get());
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"))
        );

        if (isPasswordInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        mailService.sendProfileChange(user.get());
    }

    /**
     * {@code GET  /account/sessions} : get the current open sessions.
     *
     * @return the current open sessions.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the current open sessions couldn't be retrieved.
     */
    @GetMapping("/account/sessions")
    public List<PersistentToken> getCurrentSessions() {
        return persistentTokenRepository.findByUser(
            userRepository
                .findOneByLogin(
                    SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"))
                )
                .orElseThrow(() -> new AccountResourceException("User could not be found"))
        );
    }

    @GetMapping("/account/userDetails")
    public UserDetails getUserDetails() {
        return userService.getUserDetailsByLogin().get();
    }

    @GetMapping("/account/userDetails/{login}")
    public UserDetails getUserDetails(@PathVariable String login) throws UnsupportedEncodingException {
        return userService.getUserDetailsByLogin(login).get();
    }

    @DeleteMapping("/account")
    public void deleteAccount() {
        log.debug("Current user login", SecurityUtils.getCurrentUserLogin());
        userService.deleteUser(
            SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"))
        );
    }

    /**
     * {@code DELETE  /account/sessions?series={series}} : invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     *
     * @param series the series of an existing session.
     * @throws UnsupportedEncodingException if the series couldn't be URL decoded.
     */
    @DeleteMapping("/account/sessions/{series}")
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                u ->
                    persistentTokenRepository
                        .findByUser(u)
                        .stream()
                        .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                        .findAny()
                        .ifPresent(t -> persistentTokenRepository.deleteById(decodedSeries))
            );
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        String password = java.util.UUID.randomUUID().toString().substring(0, 6);
        Optional<User> user = userService.requestPasswordReset(mail, password);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get(), password);
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordInvalid(String password) {
        return (
            StringUtils.isEmpty(password) || !password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$.!%*?&])[A-Za-z\\d$@$!%*?&]{8,25}")
        );
    }
}
