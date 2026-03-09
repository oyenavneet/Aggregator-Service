package com.oyenavneet.aggregatorservice.controller;

import com.oyenavneet.aggregatorservice.dto.CustomerInformation;
import com.oyenavneet.aggregatorservice.dto.StockTradeResponse;
import com.oyenavneet.aggregatorservice.dto.TradeRequest;
import com.oyenavneet.aggregatorservice.service.CustomerPortfolioService;
import com.oyenavneet.aggregatorservice.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {

    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerPortfolioService.getCustomerInformation(customerId);
    }


    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> requestMono) {

        return requestMono.transform(RequestValidator.validate())
                .flatMap(req -> this.customerPortfolioService.trade(customerId, req));

    }
}
