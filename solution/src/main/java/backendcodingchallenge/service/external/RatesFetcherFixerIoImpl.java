package backendcodingchallenge.service.external;

import backendcodingchallenge.model.external.Rates;
import backendcodingchallenge.service.exceptions.ExternalApiInvalidAnswerException;
import backendcodingchallenge.service.exceptions.ExternalApiNotOkStatusException;
import backendcodingchallenge.service.exceptions.UnknownCurrencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


// Implementation of the RatesFetcher interface using api.fixer.io

// TODO : Use the date returned by the API to load a new rate only when it's relevant ?

@Service
public class RatesFetcherFixerIoImpl implements IRatesFetcher {

    @Autowired
    private RestTemplate externalRest;

    @Override
    public double getRateToGBP(String currency)
            throws UnknownCurrencyException, ExternalApiNotOkStatusException, ExternalApiInvalidAnswerException {
        if(!currency.equals("EUR")) {
            throw new UnknownCurrencyException("This currency isn't implemented yet !");
        }

        ResponseEntity<Rates> response = externalRest.getForEntity("https://api.fixer.io/latest?symbols=EUR,GBP", Rates.class);
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new ExternalApiNotOkStatusException("The currency conversion API returned a HTTP code "+ response.getStatusCode());
        }

        Rates ratesReturned = response.getBody();
        if(!ratesReturned.getRates().containsKey("GBP")) {
            throw new ExternalApiInvalidAnswerException("The currency conversion API returned an invalid message");
        }

        return ratesReturned.getRates().get("GBP");
    }
}
