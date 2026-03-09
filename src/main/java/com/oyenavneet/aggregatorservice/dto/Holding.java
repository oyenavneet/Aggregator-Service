package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;

public record Holding(
        Ticker ticker,
        Integer quantity
) {
}
