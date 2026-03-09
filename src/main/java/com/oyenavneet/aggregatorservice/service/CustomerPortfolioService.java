package com.oyenavneet.aggregatorservice.service;

import com.oyenavneet.aggregatorservice.client.CustomerServiceClient;
import com.oyenavneet.aggregatorservice.client.StockServiceClient;
import com.oyenavneet.aggregatorservice.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {

    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public CustomerPortfolioService(StockServiceClient stockServiceClient, CustomerServiceClient customerServiceClient) {
        this.stockServiceClient = stockServiceClient;
        this.customerServiceClient = customerServiceClient;
    }


    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerServiceClient.getCustomerInformation(customerId);
    }


    public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest tradeRequest) {
        return this.stockServiceClient.getStockPrice(tradeRequest.ticker())
                .map(StockPriceResponse::price)
                .map(price -> this.toStockTradeRequest(tradeRequest, price))
                .flatMap(req -> this.customerServiceClient.trade(customerId, req));

    }

    private StockTradeRequest toStockTradeRequest(TradeRequest tradeRequest, Integer price) {
        return new StockTradeRequest(
                tradeRequest.ticker(),
                price,
                tradeRequest.quantity(),
                tradeRequest.action()
        );
    }
}
