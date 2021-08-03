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
public class CurrencyConversionCronTaskService {

    private final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    public static final String ACCESS_KEY = "fd9257c1dfe7e45cf94c013a415d9880";
    public static final String BASE_URL = "http://apilayer.net/";
    public static final String ENDPOINT = "api/live";
    public static final String BASE_CURRENCY = "USD";

    private final RestTemplate restTemplate;

    public CurrencyConversionCronTaskService(CurrencyRepository currencyRepository, RestTemplateBuilder restTemplateBuilder) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    // sendLiveRequest() function is created to request and retrieve the data
    private ObjectNode sendAPIRequest() throws Exception {
        // The following line initializes the HttpGet Object with the URL in order to
        // send a request
        String url = BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY;
        String result = this.restTemplate.getForObject(url, String.class);
        return new ObjectMapper().readValue(result, ObjectNode.class);
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void populateCurrencyData() throws Exception {
        ObjectNode requestString = this.sendAPIRequest();
        String quotes = requestString.get("quotes").toString();

        Map currencys = new ObjectMapper().readValue(quotes, Map.class);

        //CurrencyExchangeRateDTO[] currencys = objectMapper.readValue(quotes, CurrencyExchangeRateDTO[].class);

        if (currencys.isEmpty()) {
            log.error("Could not found any currencys on {}", new Date());
            throw new NoCurrencysFoundException("Could not retrieve any currencys");
        }
        currencys.forEach(
            (key, exchange) -> {
                try {
                    // The BASE CURRENCY code is removed from currency original key
                    String currencyCode = key.toString().substring(key.toString().indexOf(BASE_CURRENCY) + 3);
                    Float conversionRate = Float.parseFloat(exchange.toString());
                    Optional<Currency> currency = currencyRepository.findOneByCode(currencyCode);
                    Currency updatedCurrency = new Currency();

                    if (currency.isPresent()) {
                        updatedCurrency = updateCurrency(currency.get(), conversionRate);
                    } else {
                        updatedCurrency = createCurrency(currencyCode, updatedCurrency, conversionRate);
                    }
                    currencyRepository.save(updatedCurrency);
                } catch (Exception e) {
                    log.error("Error updating currency: {}. Error: {}", key, e.getMessage());
                }
            }
        );
    }

    private Currency updateCurrency(Currency currency, Float conversionRate) {
        currency.setConversionRate(conversionRate);
        return currency;
    }

    private Currency createCurrency(String currencyCode, Currency currency, Float conversionRate) {
        currency.setCode(currencyCode);
        currency.setConversionRate(conversionRate);
        currency.setName(currencyCode);
        currency.setIsActive(true);
        currency.setAdminCreated(false);
        return currency;
    }
}
