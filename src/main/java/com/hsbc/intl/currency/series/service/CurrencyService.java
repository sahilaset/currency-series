package com.hsbc.intl.currency.series.service;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    void processPaymentTransaction(PaymentDTO paymentDTO);
    void processBulkPaymentTransaction(List<PaymentDTO> paymentDTOList);
    void processBulkConversionRate(List<String> paymentDataList);
    Map<String, BigDecimal> getAllCurrencyConversionRate();
    Map<String, CurrencyBalanceDTO> getAllCurrencyBalances();
}
