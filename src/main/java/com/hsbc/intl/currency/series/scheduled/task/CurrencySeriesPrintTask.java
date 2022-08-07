package com.hsbc.intl.currency.series.scheduled.task;

import com.hsbc.intl.currency.series.model.CurrencyBalanceDTO;
import com.hsbc.intl.currency.series.service.CurrencyService;

import java.math.BigDecimal;
import java.util.Map;
/*
 * Scheduled task to print the latest currency and its balances.
 * */
public class CurrencySeriesPrintTask implements Runnable{

    private  CurrencyService currencyService;

    public CurrencySeriesPrintTask( CurrencyService currencyServiceArg){
        this.currencyService = currencyServiceArg;
    }
    /*
     * Task to print the latest currency and its balances along with USD rate.
     * */
    @Override
    public void run() {
        Map<String, CurrencyBalanceDTO> currencyData = currencyService.getAllCurrencyBalances();
        Map<String, BigDecimal> currencyConversion = currencyService.getAllCurrencyConversionRate();
        System.out.println("--------------------------Printing Data-----------------------------------------");
        currencyData.forEach( (currencyCd, currencyBalanceDTO)->{
            BigDecimal currencyConversionFactor = currencyConversion.getOrDefault(currencyCd, BigDecimal.ZERO);
            if(BigDecimal.ZERO.equals(currencyConversionFactor)){
                System.out.println("Currency Code: "+ currencyCd + " Amount:"+ currencyBalanceDTO.getBalanceAmt().toEngineeringString() +" USD Value doesn't exit");
            }else{
                System.out.println("Currency Code: "+ currencyCd + " Amount:"+ currencyBalanceDTO.getBalanceAmt().toEngineeringString() +" USD Value "+
                        currencyConversion.getOrDefault(currencyCd, BigDecimal.ONE).multiply(currencyBalanceDTO.getBalanceAmt()).toEngineeringString());
            }
        });
    }
}
