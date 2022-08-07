package com.hsbc.intl.currency.series.task;

import com.hsbc.intl.currency.series.constants.Constants;
import com.hsbc.intl.currency.series.mapper.PaymentMapper;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.service.CurrencyService;

import java.util.Scanner;
/*
 * Task to read the payment transaction and make update to the currency balance.
 * */
public class PaymentInputReaderTask implements Runnable {

    private CurrencyService currencyService;

    public PaymentInputReaderTask(CurrencyService currencyServiceArg){
        this.currencyService = currencyServiceArg;
    }

    @Override
    public void run() {
        //Standard input.
        Scanner userInput = new Scanner(System.in);
        try{
            System.out.println("Please enter your data below: (send 'quit' to exit) ");
            // Take input until quit is entered.
            while (Boolean.TRUE){
                // Space separated payment data .
                String currencySeries = userInput.nextLine();
                if(Constants.QUIT_VALUE.equalsIgnoreCase(currencySeries)){
                    break;
                }
                // Map to the payment object.
                PaymentDTO paymentDTO = PaymentMapper.mapInputToPaymentObj(currencySeries);
                // Process the payment transaction to update balance.
                currencyService.processPaymentTransaction(paymentDTO);
            }
        }finally {
            userInput.close();
        }
    }
}
