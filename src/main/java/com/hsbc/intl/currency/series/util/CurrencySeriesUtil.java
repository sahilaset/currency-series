package com.hsbc.intl.currency.series.util;

import org.apache.commons.lang3.StringUtils;

/*
 * Currency Series Utility class.
 * */
public class CurrencySeriesUtil {
    /*
     * Validate if the currency code is entered more than 3 characters.
     * */
    public static boolean validCurrencyCdLengthMatchesRequirement(String currencyCd){
        if(!StringUtils.isEmpty(currencyCd)){
            if(currencyCd.length() > 3){
                return Boolean.FALSE;
            }
        }else{
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
