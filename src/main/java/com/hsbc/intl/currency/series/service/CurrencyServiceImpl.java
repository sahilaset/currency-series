package com.hsbc.intl.currency.series.service;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.store.CurrencyBalanceStore;
import com.hsbc.intl.currency.series.store.InMemoryDataStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/*
 * Currency Services implementation.
 * */
public class CurrencyServiceImpl implements CurrencyService{

    private CurrencyBalanceStore currencyBalanceStore;

    public CurrencyServiceImpl(){
        currencyBalanceStore = new InMemoryDataStore();
    }
    /*
     * Process the payment transaction to update currency balance.
     * */
    @Override
    public void processPaymentTransaction(PaymentDTO paymentDTO) {
        currencyBalanceStore.addPaymentTransactionToBalance(paymentDTO);
    }
    /*
     * Process the bulk payment transaction.
     * */
    @Override
    public void processBulkPaymentTransaction(List<PaymentDTO> paymentDTOList) {
        currencyBalanceStore.createInitialCurrencyBalStore(paymentDTOList);
    }
    /*
     * Store the bulk conversion rate.
     * */
    @Override
    public void processBulkConversionRate(List<String> paymentDataList) {
        currencyBalanceStore.storeCurrencyConversionValueToUSD(paymentDataList);
    }
    /*
     * Get all the currency conversion rate available.
     * */
    @Override
    public Map<String, BigDecimal> getAllCurrencyConversionRate() {
        return currencyBalanceStore.getAllCurrencyConversionRate();
    }
    /*
     * Get all the currency balances updated so far.
     * */
    @Override
    public Map<String, CurrencyBalanceDTO> getAllCurrencyBalances() {
        return currencyBalanceStore.getAllCurrencyBalances();
    }
}
