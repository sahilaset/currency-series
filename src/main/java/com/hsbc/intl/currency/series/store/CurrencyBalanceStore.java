package com.hsbc.intl.currency.series.store;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/*
 * Currency Balance store could be implemented in different ways.
 * */
public interface CurrencyBalanceStore {
    void addPaymentTransactionToBalance(PaymentDTO paymentDTO);
    Map<String, CurrencyBalanceDTO> getAllCurrencyBalances();
    void createInitialCurrencyBalStore(List<PaymentDTO> paymentDTOList);
    void storeCurrencyConversionValueToUSD(List<String> fileDataList);
    Map<String, BigDecimal> getAllCurrencyConversionRate();
}
