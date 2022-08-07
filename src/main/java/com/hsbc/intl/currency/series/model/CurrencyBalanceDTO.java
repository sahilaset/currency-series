package com.hsbc.intl.currency.series.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/*
 * Currency and its balance data object.
 * */
@Data
@Builder
public class CurrencyBalanceDTO {
    private String currencyCd;
    private BigDecimal balanceAmt;
}
