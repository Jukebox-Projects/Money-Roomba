package com.moneyroomba.service;

import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.dto.ContactDTO;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.ehcache.spi.resilience.ResilienceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ContactService {

    private static class ContactServiceException extends RuntimeException {

        private ContactServiceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(ContactService.class);

    private UserDetailsRepository userDetailsRepository;

    private UserRepository userRepository;

    private static final String ENTITY_NAME = "contact";

    public ContactService(UserDetailsRepository userDetailsRepository, UserRepository userRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
    }

    public boolean save(String contactIdentifier) {
        UserDetails contact;
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "Current user has no details on its account, could not complete action",
                            ENTITY_NAME,
                            "nonuserfound"
                        )
                )
        );
        Optional<UserDetails> currentLoggedUser = userDetailsRepository.findOneByInternalUser(user.get());

        try {
            Optional<UserDetails> contactEmailSearch = userDetailsRepository.findOneByInternalUser(
                userRepository.findOneByLogin(contactIdentifier).get()
            );
            contact = contactEmailSearch.get();
        } catch (NoSuchElementException e) {
            log.error("NO ES UN CORREO VALIDO", e);
            try {
                Optional<UserDetails> contactPhoneSearch = userDetailsRepository.findOneByPhone(contactIdentifier);
                contact = contactPhoneSearch.get();
            } catch (NoSuchElementException ex) {
                throw new BadRequestAlertException("No user found", ENTITY_NAME, "notvalidcontact");
            }
        }

        System.out.println("CONTACT TO BE ADDED:" + contact.toString());

        Set<UserDetails> contactList = currentLoggedUser.get().getUserDetails();

        contactList.add(contact);

        currentLoggedUser.get().setUserDetails(contactList);

        userDetailsRepository.save(currentLoggedUser.get());

        return true;
    }

    public List<ContactDTO> findAll() {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "Current user has no details on its account, could not complete action",
                            ENTITY_NAME,
                            "nonuserfound"
                        )
                )
        );
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByInternalUser(user.get());
        List<ContactDTO> res = new ArrayList<ContactDTO>();
        if (!userDetails.get().getUserDetails().isEmpty()) {
            for (UserDetails contact : userDetails.get().getUserDetails()) {
                ContactDTO newContact = new ContactDTO(
                    contact.getInternalUser().getFirstName() + " " + contact.getInternalUser().getLastName(),
                    contact.getInternalUser().getLogin(),
                    contact.getPhone()
                );
                res.add(newContact);
            }
        }
        return res;
    }

    public void delete(String contactIdentifier) {
        Optional<User> user = userRepository.findOneByLogin(
            SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(
                    () ->
                        new BadRequestAlertException(
                            "Current user has no details on its account, could not complete action",
                            ENTITY_NAME,
                            "nonuserfound"
                        )
                )
        );
        Optional<UserDetails> currentLoggedUser = userDetailsRepository.findOneByInternalUser(user.get());
        Set<UserDetails> contactList = currentLoggedUser.get().getUserDetails();
        // SACAR CONTACTO DE LISTA Y GUARDAR...
    }
}
