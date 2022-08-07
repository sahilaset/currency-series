package com.hsbc.intl.currency.series.task;

import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.service.CurrencyService;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

@RunWith(MockitoJUnitRunner.class)
public class PaymentInputReaderTaskTest {
    @Mock
    private CurrencyService currencyService;

    private PaymentInputReaderTask inputReaderTask;
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;


    @Before
    public void setUp(){
        inputReaderTask = new PaymentInputReaderTask(currencyService);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testPaymentInputTask(){
        //given
        provideInput("HKD 1000"+ System.lineSeparator()+ "GBP 1000"+ System.lineSeparator()+"quit");
        //execute
        inputReaderTask.run();
        ArgumentCaptor<PaymentDTO> paymentDTOArgumentCaptor = ArgumentCaptor.forClass(PaymentDTO.class);
        //verify
        Mockito.verify(currencyService, Mockito.times(2)).processPaymentTransaction(paymentDTOArgumentCaptor.capture());
        Assert.assertEquals("HKD",paymentDTOArgumentCaptor.getAllValues().get(0).getCurrencyCd());
        Assert.assertEquals("GBP",paymentDTOArgumentCaptor.getAllValues().get(1).getCurrencyCd());
    }

    @Test
    public void testPaymentInputTaskWithDuplicateCurrency(){
        //given
        provideInput("HKD 100.3"+ System.lineSeparator()+ "GBP 1000"+ System.lineSeparator()+"HKD 0.7"+ System.lineSeparator()+"quit");
        //execute
        inputReaderTask.run();
        ArgumentCaptor<PaymentDTO> paymentDTOArgumentCaptor = ArgumentCaptor.forClass(PaymentDTO.class);
        //verify
        Mockito.verify(currencyService, Mockito.times(3)).processPaymentTransaction(paymentDTOArgumentCaptor.capture());
        Assert.assertEquals("HKD",paymentDTOArgumentCaptor.getAllValues().get(0).getCurrencyCd());
        Assert.assertEquals("GBP",paymentDTOArgumentCaptor.getAllValues().get(1).getCurrencyCd());
        Assert.assertEquals("HKD",paymentDTOArgumentCaptor.getAllValues().get(2).getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("0.7"),paymentDTOArgumentCaptor.getAllValues().get(2).getPaymentAmt());
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }


    }
