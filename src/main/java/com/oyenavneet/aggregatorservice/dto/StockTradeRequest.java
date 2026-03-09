package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;
import com.oyenavneet.aggregatorservice.domain.TradeAction;

public record StockTradeRequest(
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action
) {
}
