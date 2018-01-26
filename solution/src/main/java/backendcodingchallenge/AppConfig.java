package backendcodingchallenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig
{

    @Value("${config.external-apis.connect-timeout}")
    private int connectTimeout;

    @Value("${config.external-apis.read-timeout}")
    private int readTimeout;

    //A RestTemplate for all external API calls
    //Has timeouts set from application.yml
    @Bean
    public RestTemplate externalRest()
    {
        RestTemplate restTemplate = new RestTemplate();
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(connectTimeout);
        ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setReadTimeout(readTimeout);
        return restTemplate;
    }
}