package backendcodingchallenge.service.external;

import backendcodingchallenge.model.external.Rates;
import backendcodingchallenge.service.exceptions.ExternalApiInvalidAnswerException;
import backendcodingchallenge.service.exceptions.ExternalApiNotOkStatusException;
import backendcodingchallenge.service.exceptions.UnknownCurrencyException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class RatesFetcherFixerIoImplTest {

    @InjectMocks
    private RatesFetcherFixerIoImpl ratesFetcher;

    @Mock
    private RestTemplate externalRest;

    @Mock
    ResponseEntity<Rates> responseEntity;

    private Rates rates;

    @Before
    public void initMocks() {
        rates = Rates.builder().base("EUR").rates(new HashMap<String, Double>(){{put("GBP", 0.5);}}).build();
        MockitoAnnotations.initMocks(this);
        when(externalRest.getForEntity(anyString(), eq(Rates.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(rates);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    }

    @Test
    public void getRateToGBPOK() {
        double rate = ratesFetcher.getRateToGBP("EUR");

        assertThat(rate).isEqualTo(0.5);
    }

    @Test
    public void getRateToUnknownCurrencyKO() {
        Exception thrownException = null;

        try {
            ratesFetcher.getRateToGBP("USD");
        } catch(Exception ex) {
            thrownException = ex;
        }

        assertThat(thrownException).isInstanceOf(UnknownCurrencyException.class);
        verifyNoMoreInteractions(externalRest);
    }

    @Test(expected = RestClientException.class)
    public void getRateToGBPRestClientExceptionKO() {
        when(externalRest.getForEntity(anyString(), eq(Rates.class))).thenThrow(RestClientException.class);

        ratesFetcher.getRateToGBP("EUR");
    }

    @Test(expected = ExternalApiInvalidAnswerException.class)
    public void getRateToGBPEmptyRatesKO() {
        rates.getRates().clear();

        ratesFetcher.getRateToGBP("EUR");
    }

    @Test
    public void getRateToGBPApiNotFoundKO() {
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Exception thrownException = null;

        try {
            ratesFetcher.getRateToGBP("EUR");
        } catch(Exception ex) {
            thrownException = ex;
        }

        assertThat(thrownException).isInstanceOf(ExternalApiNotOkStatusException.class);
        assertThat(thrownException.getMessage()).isEqualTo("The currency conversion API returned a HTTP code 404");
    }
}
