package com.oyenavneet.aggregatorservice.client;

import com.oyenavneet.aggregatorservice.domain.Ticker;
import com.oyenavneet.aggregatorservice.dto.PriceUpdate;
import com.oyenavneet.aggregatorservice.dto.StockPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

public class StockServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceClient.class);
    private Flux<PriceUpdate> priceUpdatesFlux;


    private final WebClient webClient;

    public StockServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return this.webClient.get()
                .uri("/stock/{}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    public Flux<PriceUpdate> priceUpdateStream() {
        if (Objects.isNull(this.priceUpdatesFlux)) {
            this.priceUpdatesFlux = this.getPriceUpdates();
        }
        return this.priceUpdatesFlux;
    }

    private Flux<PriceUpdate> getPriceUpdates() {
        return this.webClient.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(PriceUpdate.class)
                .retryWhen(retry())
                .cache(1); // using cache we can make hot publisher, whenever subscriber call it get 1 lastest item

    }

    private Retry retry() {
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
                .doBeforeRetry(rs -> logger.error("stock service price stream call fail. retrying: {}", rs.failure().getMessage()));
    }

}
