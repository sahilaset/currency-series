package com.hsbc.intl.currency.series.store;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryDataStoreTest {

    private CurrencyBalanceStore currencyBalanceStore;

    @Before
    public void setUp(){
        currencyBalanceStore = new InMemoryDataStore();
    }

    @Test
    public void testAddPaymentTransactionToBalance(){
        //given
        PaymentDTO dummy = this.createDummyPayment("HKD", "110");
        //execute
        currencyBalanceStore.addPaymentTransactionToBalance(dummy);
        //verify
        Map<String, CurrencyBalanceDTO> result = currencyBalanceStore.getAllCurrencyBalances();
        Assert.assertEquals(1, result.size());
        result.forEach((key, value)->{
            Assert.assertEquals("HKD", key);
            Assert.assertEquals(NumberUtils.createBigDecimal("110"), value.getBalanceAmt());
        });
    }

    @Test
    public void testProcessBulkPaymentTransaction(){
        //given
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        paymentDTOList.add(this.createDummyPayment("HKD", "1000"));
        paymentDTOList.add(this.createDummyPayment("USD", "1000"));
        paymentDTOList.add(this.createDummyPayment("USD", "100"));
        //execute
        currencyBalanceStore.createInitialCurrencyBalStore(paymentDTOList);
        //verify
        Map<String, CurrencyBalanceDTO> result = currencyBalanceStore.getAllCurrencyBalances();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(NumberUtils.createBigDecimal("1000"), result.get("HKD").getBalanceAmt());
        Assert.assertEquals(NumberUtils.createBigDecimal("1100"), result.get("USD").getBalanceAmt());
    }

    @Test(expected = NullPointerException.class)
    public void testProcessBulkPaymentTransactionWithNullAmt(){
        //given
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        paymentDTOList.add(this.createDummyPayment("HKD", null));
        paymentDTOList.add(this.createDummyPayment("USD", null));
        paymentDTOList.add(this.createDummyPayment("USD", null));
        //execute
        currencyBalanceStore.createInitialCurrencyBalStore(paymentDTOList);
        //verify
    }

    @Test
    public void testProcessBulkConversionRate(){
        //given
        List<String> currencyConversionList = new ArrayList<>();
        currencyConversionList.add("HKD 10");
        currencyConversionList.add("GBP 2");
        //execute
        currencyBalanceStore.storeCurrencyConversionValueToUSD(currencyConversionList);
        //verify
        Map<String, BigDecimal> result = currencyBalanceStore.getAllCurrencyConversionRate();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(NumberUtils.createBigDecimal("10"), result.get("HKD"));
        Assert.assertEquals(NumberUtils.createBigDecimal("2"), result.get("GBP"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessBulkConversionRateWithInvalidCurrency(){
        //given
        List<String> currencyConversionList = new ArrayList<>();
        currencyConversionList.add("HKD1 10");
        currencyConversionList.add("GBP 2");
        //execute
        currencyBalanceStore.storeCurrencyConversionValueToUSD(currencyConversionList);
        //verify
    }


    private PaymentDTO createDummyPayment(String currencyCd, String amt){
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .currencyCd(currencyCd)
                .paymentAmt(NumberUtils.createBigDecimal(amt))
                .build();
        return paymentDTO;
    }
}
