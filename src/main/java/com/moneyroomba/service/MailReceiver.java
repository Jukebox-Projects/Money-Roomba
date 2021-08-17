package com.moneyroomba.service;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.EventType;
import com.moneyroomba.domain.enumeration.SourceEntity;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.service.dto.factura.TiqueteElectronico;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import java.io.*;
import java.util.Optional;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailReceiver {

    private final Logger log = LoggerFactory.getLogger(MailReceiver.class);
    //@Value("${mailReceiver.hostName}")
    private String hostName = "imap.gmail.com";
    //@Value("${mailReceiver.port}")
    private String port = "993";
    //@Value("${mailReceiver.userName}")
    private String userName = "moneyroomba@gmail.com";
    //@Value("${mailReceiver.password}")
    private String password = "CashMoneyProductions123";

    private IMAPStore store;

    private final InvoiceXMLService invoiceXMLService;
    private final TransactionService transactionService;
    private final EventService eventService;
    private final UserService userService;

    public MailReceiver(
        InvoiceXMLService invoiceXMLService,
        TransactionService transactionService,
        EventService eventService,
        UserService userService
    ) {
        this.invoiceXMLService = invoiceXMLService;
        this.transactionService = transactionService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void receiveMail() throws MessagingException, IOException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imap");
        props.put("mail.imap.host", hostName);
        props.put("mail.imap.port", port);
        props.put("mail.imap.auth.login.disable", "true");
        props.put("mail.imap.auth.plain.disable", "false");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");

        Session session = Session.getInstance(
            props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            }
        );

        store = (IMAPStore) session.getStore("imap");
        store.connect(hostName, userName, password);
        openFolder();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void openFolder() throws MessagingException, IOException {
        IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
        String subject = null;
        if (!folder.isOpen()) folder.open(Folder.READ_WRITE);
        Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        log.debug("No of Messages : " + folder.getMessageCount());
        log.debug("No of Unread Messages : " + folder.getUnreadMessageCount());
        log.debug(messages.length + "");
        if (messages.length > 0) {
            for (Message msg : messages) {
                log.debug("*****************************************************************************");
                log.debug("Message Number: " + msg.getMessageNumber());

                /*
                log.debug("Subject: " + subject);
                log.debug("From: " + msg.getFrom()[0]);
                log.debug("Body: \n" + msg.getContent());
                log.debug(msg.getContentType());
*/

                Multipart multipart = (Multipart) msg.getContent();

                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (
                        !Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                        StringUtils.isBlank(bodyPart.getFileName()) &&
                        !bodyPart.getContentType().contains("TEXT/XML")
                    ) {
                        continue; // dealing with XML attachments only
                    }
                    InputStream is = bodyPart.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    Address[] froms = msg.getFrom();
                    String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                    Optional<UserDetails> user = userService.getUserDetailsByLogin(email);
                    TiqueteElectronico deserializedData = invoiceXMLService.save(sb.toString());

                    if (user.isEmpty()) {
                        log.debug("Error login no encontrado: \n" + email);
                        msg.setFlag(Flags.Flag.DELETED, true);
                        continue;
                    }

                    if (deserializedData == null) {
                        log.debug("\n Correo con XML invalido: \n");
                        eventService.createEventAndNotification(
                            EventType.INVALID_ATTACHMENT,
                            user.get().getInternalUser().getId(),
                            SourceEntity.TRANSACTION,
                            user.get()
                        );
                        msg.setFlag(Flags.Flag.DELETED, true);
                        continue;
                    }
                    log.debug("Creando: \n" + email);

                    if (!transactionService.canAddMoreImportedTransactions(email)) {
                        log.debug("No puede agregar más: \n" + email);
                        eventService.createEventAndNotification(
                            EventType.INVALID_ATTACHMENT,
                            user.get().getInternalUser().getId(),
                            SourceEntity.TRANSACTION,
                            user.get()
                        );
                        msg.setFlag(Flags.Flag.DELETED, true);
                        continue;
                    }

                    Transaction transaction = transactionService.saveXML(deserializedData, email, TransactionType.EMAIL);
                    if (transaction == null) {
                        log.debug("Error OTRO: \n" + email);
                        eventService.createEventAndNotification(
                            EventType.INVALID_ATTACHMENT,
                            user.get().getInternalUser().getId(),
                            SourceEntity.TRANSACTION,
                            user.get()
                        );
                        msg.setFlag(Flags.Flag.DELETED, true);
                        continue;
                    }

                    eventService.createEventAndNotification(
                        EventType.POSSIBLE_TRANSACTION_ADDED_EMAIL,
                        user.get().getInternalUser().getId(),
                        SourceEntity.TRANSACTION,
                        user.get()
                    );
                    log.debug("Transaction creada para " + email + "\n");
                }
            }
        }
    }
}
