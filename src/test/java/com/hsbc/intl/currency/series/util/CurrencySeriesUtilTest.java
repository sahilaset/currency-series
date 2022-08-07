package com.hsbc.intl.currency.series.util;

import org.junit.Assert;
import org.junit.Test;

public class CurrencySeriesUtilTest {

    @Test
    public void testValidCurrencyCdLengthMatchesRequirement(){
        //given
        //execute
        boolean isCurrencyValid=CurrencySeriesUtil.validCurrencyCdLengthMatchesRequirement("HKD");
        //verify
        Assert.assertTrue(isCurrencyValid);
    }

    @Test
    public void testValidCurrencyCdLengthMatchesRequirementMoreThan3(){
        //given
        //execute
        boolean isCurrencyValid=CurrencySeriesUtil.validCurrencyCdLengthMatchesRequirement("HKD1");
        //verify
        Assert.assertFalse(isCurrencyValid);
    }
    @Test
    public void testValidCurrencyCdLengthMatchesRequirementInputIsNull(){
        //given
        //execute
        boolean isCurrencyValid = CurrencySeriesUtil.validCurrencyCdLengthMatchesRequirement(null);
        //verify
        Assert.assertFalse(isCurrencyValid);
    }

}
