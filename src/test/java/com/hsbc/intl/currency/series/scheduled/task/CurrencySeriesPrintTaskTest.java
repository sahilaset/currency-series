package com.hsbc.intl.currency.series.scheduled.task;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.service.CurrencyService;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(MockitoJUnitRunner.class)
public class CurrencySeriesPrintTaskTest {
    @Mock
    private CurrencyService currencyService;

    private CurrencySeriesPrintTask currencySeriesPrintTask;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUp(){
        currencySeriesPrintTask = new CurrencySeriesPrintTask(currencyService);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }
    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
    }

    @Test
    public void testCurrencySeriesPrint(){
        //given
        Map<String, CurrencyBalanceDTO> dummyCurrencyMap = new ConcurrentHashMap<>();
        this.addValueToDummyMap(dummyCurrencyMap, "HKD", "111");
        this.addValueToDummyMap(dummyCurrencyMap, "USD", "111");
        this.addValueToDummyMap(dummyCurrencyMap, "GBP", "3000");
        Map<String,BigDecimal> dummyCurrencyConvMap = new ConcurrentHashMap<>();
        this.addValueToDummyMapConv(dummyCurrencyConvMap, "HKD", "10");
        this.addValueToDummyMapConv(dummyCurrencyConvMap, "USD", "1");
        Mockito.when(currencyService.getAllCurrencyBalances()).thenReturn(dummyCurrencyMap);
        Mockito.when(currencyService.getAllCurrencyConversionRate()).thenReturn(dummyCurrencyConvMap);
        //execute
        currencySeriesPrintTask.run();
        //verify
        String output= getOutput();
        output=output.trim().replaceAll(System.lineSeparator(),"");
        String expected = "--------------------------Printing Data-----------------------------------------"+
                "Currency Code: HKD Amount:111 USD Value 1110\n" +
                "Currency Code: GBP Amount:3000 USD Value doesn't exit\n" +
                "Currency Code: USD Amount:111 USD Value 111\n";
        expected=expected.trim().replaceAll("\n","");
        Assert.assertEquals(expected, output);
    }

    private String getOutput() {
        return testOut.toString();
    }


    private void addValueToDummyMap(Map<String,CurrencyBalanceDTO> dummyCurrencyMap, String key, String value){
        dummyCurrencyMap.put(key, CurrencyBalanceDTO.builder()
                        .currencyCd(key)
                        .balanceAmt( NumberUtils.createBigDecimal(value)).build());
    }

    private void addValueToDummyMapConv(Map<String,BigDecimal> dummyCurrencyMap, String key, String value){
        dummyCurrencyMap.put(key, NumberUtils.createBigDecimal(value));
    }
}
