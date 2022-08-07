package com.hsbc.intl.currency.series.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
/*
 * Payment Transaction Data object.
 * */
@Data
@Builder
public class PaymentDTO {
    private String currencyCd;
    private BigDecimal paymentAmt;
}
