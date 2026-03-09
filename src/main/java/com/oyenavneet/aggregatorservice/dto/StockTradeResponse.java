package com.oyenavneet.aggregatorservice.dto;

import com.oyenavneet.aggregatorservice.domain.Ticker;
import com.oyenavneet.aggregatorservice.domain.TradeAction;

public record StockTradeResponse(
        Integer customerId,
        Ticker ticker,
        Integer price,
        Integer quantity,
        TradeAction action,
        Integer totalPrice,
        Integer balance
) {
}
