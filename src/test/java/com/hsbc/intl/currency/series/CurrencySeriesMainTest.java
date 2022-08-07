package com.hsbc.intl.currency.series;

import org.junit.*;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

public class CurrencySeriesMainTest {
    @Rule
    public TextFromStandardInputStream systemInMock = emptyStandardInputStream();
    private ByteArrayOutputStream testOut;
    private PrintStream systemOut = System.out;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setOut(systemOut);
    }

    @Test
    public void testPaymentLoadAndPaymentTransactionWithPaymentFileAndCurrency() throws URISyntaxException, InterruptedException {
        //given
        final String filePathCurrency = new File("src/test/resources/PaymentFile.txt").getAbsolutePath();
        final String filePathConversionRate = new File("src/test/resources/PaymentConversion.txt").getAbsolutePath();
        systemInMock.provideLines(filePathCurrency,filePathConversionRate,"HKD 0.7", "CNY 39999.90","quit");
        //execute
        CurrencySeriesMain.main(new String[0]);
        //verify
        String consoleOutput = this.getOutput();
        Assert.assertTrue(consoleOutput.contains("Currency Code: HKD Amount:800.7 USD Value 7206.3"));
        Assert.assertTrue(consoleOutput.contains("Currency Code: GBP Amount:1000 USD Value 2000"));
        Assert.assertTrue(consoleOutput.contains("Currency Code: CNY Amount:39999.90 USD Value doesn't exit"));
    }
    private String getOutput() {
        return testOut.toString();
    }

}
