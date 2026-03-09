package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(
        Ticker ticker,
        Integer price,
        LocalDateTime time

) {
}
