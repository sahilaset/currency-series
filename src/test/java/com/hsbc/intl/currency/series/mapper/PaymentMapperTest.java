package com.hsbc.intl.currency.series.mapper;

import com.hsbc.intl.currency.series.model.PaymentDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PaymentMapperTest {

    @Test
    public void testMapInputToPaymentObj(){
        //given
        String inputData = "HKD 100.3";
        //execute
        PaymentDTO paymentDTO = PaymentMapper.mapInputToPaymentObj(inputData);
        //verify
        Assert.assertEquals("HKD",paymentDTO.getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("100.3"),paymentDTO.getPaymentAmt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapInputToPaymentObjInvalidCurrency(){
        //given
        String inputData = "HKD1 100.3";
        //execute
        PaymentDTO paymentDTO = PaymentMapper.mapInputToPaymentObj(inputData);
        //verify
        Assert.assertEquals("HKD",paymentDTO.getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("100.3"),paymentDTO.getPaymentAmt());
    }

    @Test
    public void testMapInputToPaymentObjMultipleSpacesInBtw(){
        //given
        String inputData = "HKD         100.3";
        //execute
        PaymentDTO paymentDTO = PaymentMapper.mapInputToPaymentObj(inputData);
        //verify
        Assert.assertEquals("HKD",paymentDTO.getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("100.3"),paymentDTO.getPaymentAmt());
    }

    @Test
    public void testMapFileToListOfPaymentObj(){
        //given
        List<String> stringList = new ArrayList<>();
        stringList.add("HKD         100.3");
        stringList.add("HKD         0.7");
        stringList.add("GBP         38982222222222");
        //execute
        List<PaymentDTO> paymentDTOList = PaymentMapper.mapFileToListOfPaymentObj(stringList);
        //verify
        Assert.assertEquals("HKD",paymentDTOList.get(0).getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("100.3"),paymentDTOList.get(0).getPaymentAmt());
        Assert.assertEquals("HKD",paymentDTOList.get(1).getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("0.7"),paymentDTOList.get(1).getPaymentAmt());
        Assert.assertEquals("GBP",paymentDTOList.get(2).getCurrencyCd());
        Assert.assertEquals(NumberUtils.createBigDecimal("38982222222222"),paymentDTOList.get(2).getPaymentAmt());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testMapFileToListOfPaymentObjInvalidCurrency(){
        //given
        List<String> stringList = new ArrayList<>();
        stringList.add("HKD         100.3");
        stringList.add("HKD         0.7");
        stringList.add("GBP1         38982222222222");
        //execute
        List<PaymentDTO> paymentDTOList = PaymentMapper.mapFileToListOfPaymentObj(stringList);
        //verify
    }
}
