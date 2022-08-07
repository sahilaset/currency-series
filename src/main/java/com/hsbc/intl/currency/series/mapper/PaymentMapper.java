package com.hsbc.intl.currency.series.mapper;

import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.util.CurrencySeriesUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/*
 * Mapper to convert user input to the payment data object.
 * */
public class PaymentMapper {
    /*
     * Convert user input to the payment data object.
     * */
    public static PaymentDTO mapInputToPaymentObj(String userInputData){
        //Assuming delimiter to be space could one or more spaces.
        String[] currencyDataSplit = userInputData.split("\\s+");
        String currencyCd = currencyDataSplit[0];
        if(!CurrencySeriesUtil.validCurrencyCdLengthMatchesRequirement(currencyCd)){
            throw new IllegalArgumentException("Currency Name is incorrect");
        }
        BigDecimal paymentAmt = NumberUtils.createBigDecimal(currencyDataSplit[1]);
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .currencyCd(currencyCd)
                .paymentAmt(paymentAmt)
                .build();
        return paymentDTO;
    }
    /*
     * Convert the file input to the list of payment input object.
     * */
    public static List<PaymentDTO> mapFileToListOfPaymentObj(List<String> fileInputData){
        List<PaymentDTO> paymentDTOList = new ArrayList<>();
        fileInputData.forEach(fileData ->{
            PaymentDTO paymentDTO = mapInputToPaymentObj(fileData);
            paymentDTOList.add(paymentDTO);
        });
        return paymentDTOList;
    }
}
