package com.hsbc.intl.currency.series.service;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    private CurrencyService service;

    @Before
    public void setUp(){
        service= new CurrencyServiceImpl();
    }

    @Test
    public void testProcessPaymentTransactionProcessSuccessfully(){
        //given
        PaymentDTO paymentDTO = this.createDummyPayment("HKD", "1000");
        //execute
        service.processPaymentTransaction(paymentDTO);
        //verify
        Map<String, CurrencyBalanceDTO> result = service.getAllCurrencyBalances();
        Assert.assertEquals(1, result.size());
        result.forEach((key, value)->{
            Assert.assertEquals("HKD", key);
            Assert.assertEquals(NumberUtils.createBigDecimal("1000"), value.getBalanceAmt());
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
        service.processBulkPaymentTransaction(paymentDTOList);
        //verify
        Map<String, CurrencyBalanceDTO> result = service.getAllCurrencyBalances();
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
        service.processBulkPaymentTransaction(paymentDTOList);
        //verify
    }

    @Test
    public void testProcessBulkConversionRate(){
        //given
        List<String> currencyConversionList = new ArrayList<>();
        currencyConversionList.add("HKD 10");
        currencyConversionList.add("GBP 2");
        //execute
        service.processBulkConversionRate(currencyConversionList);
        //verify
        Map<String, BigDecimal> result = service.getAllCurrencyConversionRate();
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
        service.processBulkConversionRate(currencyConversionList);
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
