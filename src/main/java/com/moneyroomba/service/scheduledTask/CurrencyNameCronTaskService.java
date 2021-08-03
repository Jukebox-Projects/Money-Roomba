package com.moneyroomba.service.scheduledTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moneyroomba.domain.Currency;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.service.CurrencyService;
import com.moneyroomba.service.exception.Currency.NoCurrencysFoundException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyNameCronTaskService {

    private final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    public static final String ACCESS_KEY = "63d4eb7a1386f547deaa11455230786d";
    public static final String BASE_URL = "http://api.exchangeratesapi.io/";
    public static final String ENDPOINT = "v1/symbols";
    public static final String BASE_CURRENCY = "USD";

    private final RestTemplate restTemplate;

    public CurrencyNameCronTaskService(CurrencyRepository currencyRepository, RestTemplateBuilder restTemplateBuilder) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    // sendLiveRequest() function is created to request and retrieve the data
    private ObjectNode sendAPIRequest() throws Exception {
        // The following line initializes the HttpGet Object with the URL in order to send a request
        String url = BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY;
        String result = this.restTemplate.getForObject(url, String.class);
        return new ObjectMapper().readValue(result, ObjectNode.class);
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void populateCurrencyData() throws Exception {
        ObjectNode requestString = this.sendAPIRequest();
        String symbols = requestString.get("symbols").toString();
        Map currencys = new ObjectMapper().readValue(symbols, Map.class);

        if (currencys.isEmpty()) {
            log.error("Could not found any currencys on {}", new Date());
            throw new NoCurrencysFoundException("Could not retrieve any currencys");
        }
        currencys.forEach(
            (key, currencyName) -> {
                try {
                    // The BASE CURRENCY code is removed from currency original key
                    String currencyCode = key.toString();
                    String name = currencyName.toString();
                    Optional<Currency> currency = currencyRepository.findOneByCode(currencyCode);

                    currency.ifPresent(value -> currencyRepository.save(updateCurrency(value, name)));
                } catch (Exception e) {
                    log.error("Error updating currency: {}. Error: {}", key, e.getMessage());
                }
            }
        );
    }

    private Currency updateCurrency(Currency currency, String name) {
        currency.setName(name);
        return currency;
    }
}
