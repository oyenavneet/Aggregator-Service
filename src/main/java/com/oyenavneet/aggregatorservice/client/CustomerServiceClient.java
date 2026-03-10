package com.oyenavneet.aggregatorservice.client;

import com.oyenavneet.aggregatorservice.dto.CustomerInformation;
import com.oyenavneet.aggregatorservice.dto.StockTradeRequest;
import com.oyenavneet.aggregatorservice.dto.StockTradeResponse;
import com.oyenavneet.aggregatorservice.exceptions.ApplicationExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class CustomerServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceClient.class);
    private final WebClient webClient;

    public CustomerServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.webClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .onErrorResume(NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId));
    }


    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
        logger.info("CustomerServiceClient::trade {}", customerId);
        return this.webClient.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorResume(NotFound.class, ex -> ApplicationExceptions.customerNotFound(customerId))
                .onErrorResume(BadRequest.class, this::handleException);
    }


    private <T> Mono<T> handleException(BadRequest exception) {
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        logger.error("Customer service problem details: {}", pd);
        return ApplicationExceptions.invalidTradeRequest(message);
    }
}
