package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;

import java.time.LocalDateTime;

public record StockPriceResponse(
        Ticker ticker,
        Integer price
) {
}
