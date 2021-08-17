package com.moneyroomba.web.rest;

import com.moneyroomba.domain.*;
import com.moneyroomba.domain.enumeration.LicenseCreateMethod;
import com.moneyroomba.domain.enumeration.MovementType;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.repository.SystemSettingRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.*;
import com.moneyroomba.service.dto.PayPalDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PayPalResource {

    private final Logger log = LoggerFactory.getLogger(PayPalResource.class);

    private static final String ENTITY_NAME = "license";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;
    private final CurrencyRepository currencyRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final UserService userService;
    private final LicenseService licenseService;
    private final TransactionService transactionService;
    private final WalletRepository walletRepository;
    private final MailService mailService;

    public PayPalResource(
        InvoiceService invoiceService,
        CurrencyRepository currencyRepository,
        SystemSettingRepository systemSettingRepository,
        UserService userService,
        LicenseService licenseService,
        TransactionService transactionService,
        WalletRepository walletRepository,
        MailService mailService
    ) {
        this.invoiceService = invoiceService;
        this.currencyRepository = currencyRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.userService = userService;
        this.licenseService = licenseService;
        this.transactionService = transactionService;
        this.walletRepository = walletRepository;
        this.mailService = mailService;
    }

    @PostMapping("/paypal")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody PayPalDTO payPalDTO) {
        boolean isGift = false;
        isGift = payPalDTO.getIsGiftString().equalsIgnoreCase("true");
        Invoice invoice = new Invoice();
        invoice.setCompanyName("MoneyRoomba");
        Optional<Currency> currencyRepo = currencyRepository.findOneByCode("USD");
        Currency currency = currencyRepo.isEmpty() ? null : currencyRepo.get();
        invoice.setCurrency(currency);
        invoice.setDateCreated(LocalDate.now());
        invoice.setItemQuantity(1);
        invoice.setPurchaseDescription((isGift ? "MoneyRoomba Premium Gift" : "MoneyRoomba Premium"));
        Optional<SystemSetting> priceSetting = systemSettingRepository.findOneByKey("price");
        Optional<SystemSetting> taxSetting = systemSettingRepository.findOneByKey("tax");
        double price = priceSetting.isPresent() ? priceSetting.get().getValue() : 0;
        double tax = taxSetting.isPresent() ? taxSetting.get().getValue() : 0;
        double total = (price * (tax / 100)) + price;
        invoice.setItemPrice(price);
        invoice.setSubTotal(price);
        invoice.setTax(tax);
        invoice.setTotal(total);
        Optional<User> optionalUser = userService.getUser();
        User user = optionalUser.isPresent() ? optionalUser.get() : null;
        if (user != null) {
            invoice.setUserEmail(user.getEmail());
            invoice.setUserName(user.getFirstName());
            invoice.setUserLastName(user.getLastName());
        } else {
            invoice.setUserEmail("");
            invoice.setUserName("");
            invoice.setUserLastName("");
        }
        invoiceService.save(invoice);

        License license = new License();
        license.setCode(UUID.randomUUID());
        license.setCreateMethod(LicenseCreateMethod.MANUAL);
        license.setIsActive(true);
        license.setIsAssigned(isGift ? false : true);
        license.setInvoice(invoice);
        licenseService.save(license);

        Transaction transaction = new Transaction();
        transaction.setOriginalAmount(total);
        transaction.setName((isGift ? "MoneyRoomba Premium Gift" : "MoneyRoomba Premium"));
        transaction.setMovementType(MovementType.EXPENSE);
        transaction.setState(TransactionState.PENDING_APPROVAL);
        transaction.setAddToReports(true);
        transaction.setCurrency(currency);
        transaction.setDateAdded(LocalDate.now());
        transaction.setSourceUser(userService.getUserDetailsByLogin().get());
        transaction.setTransactionType(TransactionType.API);
        transaction.setScheduled(false);
        transaction.setIncomingTransaction(false);
        List<Wallet> wallets = walletRepository.findAllByUser(userService.getUserDetailsByLogin().get());
        if (wallets.size() > 0) {
            transaction.setWallet(wallets.get(0));
            transaction.setAttachment(null);
            transactionService.create(transaction);
        }
        mailService.sendInvoice(invoice, user, license);

        if (!isGift) {
            licenseService.activate(license);
            return ResponseEntity
                .ok()
                .headers(
                    HeaderUtil.createAlert(applicationName, applicationName + "." + ENTITY_NAME + ".bought", license.getCode().toString())
                )
                .build();
        } else {
            mailService.sendGiftCode(user, license, payPalDTO);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert(applicationName, applicationName + "." + ENTITY_NAME + ".gift", ""))
                .build();
        }
    }
}
