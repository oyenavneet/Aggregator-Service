package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;
import com.oyenavneet.aggregatorservice.domain.TradeAction;

public record TradeRequest(
        Ticker ticker,
        TradeAction action,
        Integer quantity
) {
}
