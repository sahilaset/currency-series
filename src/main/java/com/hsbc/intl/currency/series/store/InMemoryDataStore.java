package com.hsbc.intl.currency.series.store;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.util.CurrencySeriesUtil;
import org.apache.commons.lang3.math.NumberUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/*
 * Inmemory data store to store the currency and its balances.
 * */
public class InMemoryDataStore implements CurrencyBalanceStore {
    private Map<String, CurrencyBalanceDTO> inMemoryDataStore;
    private Map<String, BigDecimal> currencyConversion;
    public InMemoryDataStore(){
        inMemoryDataStore = new ConcurrentHashMap<>();
        currencyConversion = new ConcurrentHashMap<>();
    }
    /*
     * Add payment data to the currency balance.
     * */
    @Override
    public void addPaymentTransactionToBalance(PaymentDTO paymentDTO) {
        CurrencyBalanceDTO balanceStore = null;
        String currencyCd = paymentDTO.getCurrencyCd().toLowerCase();
        if(inMemoryDataStore.containsKey(currencyCd)){
            balanceStore = inMemoryDataStore.get(currencyCd);
            synchronized (balanceStore){
                balanceStore.setBalanceAmt(balanceStore.getBalanceAmt().
                        add(paymentDTO.getPaymentAmt()));
            }
        }else{
            balanceStore = CurrencyBalanceDTO.builder()
                    .currencyCd(currencyCd)
                    .balanceAmt(paymentDTO.getPaymentAmt())
                    .build();
        }
        inMemoryDataStore.put(currencyCd, balanceStore);
    }
    /*
     * Get all Currency Balances to print in the console which have balances not equal to zero.
     * */
    @Override
    public Map<String, CurrencyBalanceDTO> getAllCurrencyBalances() {
        return inMemoryDataStore.entrySet().stream().filter(entry -> !BigDecimal.ZERO.equals(entry.getValue()
                        .getBalanceAmt()))
                .collect(Collectors.toMap(entry -> entry.getKey().toUpperCase(),
                        entry-> entry.getValue()));
    }
    /*
     * Store the initial currency payment transaction to create store.
     * */
    @Override
    public void createInitialCurrencyBalStore(List<PaymentDTO> paymentDTOList) {
        paymentDTOList.stream().forEach(paymentDTO -> {
            this.addPaymentTransactionToBalance(paymentDTO);
        });
    }
    /*
     * Store the currency conversion rate to USD.
     * */
    @Override
    public void storeCurrencyConversionValueToUSD(List<String> fileDataList) {
        fileDataList.stream().forEach(currencyData ->{
            String[] currencyDataSplit = currencyData.split("\\s+");
            String currencyCd = currencyDataSplit[0];
            if(!CurrencySeriesUtil.validCurrencyCdLengthMatchesRequirement(currencyCd)){
                throw new IllegalArgumentException("Currency Name is incorrect");
            }
            currencyConversion.put(currencyDataSplit[0], NumberUtils.createBigDecimal(currencyDataSplit[1]));
        });
    }

    /*
     * Get all the currency conversion rate stored in the data store.
     * */
    @Override
    public Map<String, BigDecimal> getAllCurrencyConversionRate() {
        return currencyConversion;
    }
}
