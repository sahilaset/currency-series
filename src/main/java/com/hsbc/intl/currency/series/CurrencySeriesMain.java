package com.hsbc.intl.currency.series;

import com.hsbc.intl.currency.series.constants.Constants;
import com.hsbc.intl.currency.series.mapper.PaymentMapper;
import com.hsbc.intl.currency.series.model.PaymentDTO;
import com.hsbc.intl.currency.series.scheduled.task.CurrencySeriesPrintTask;
import com.hsbc.intl.currency.series.service.CurrencyService;
import com.hsbc.intl.currency.series.service.CurrencyServiceImpl;
import com.hsbc.intl.currency.series.store.CurrencyBalanceStore;
import com.hsbc.intl.currency.series.store.InMemoryDataStore;
import com.hsbc.intl.currency.series.task.PaymentInputReaderTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CurrencySeriesMain {

    public static void main(String[] args){
        // Create the Currency Service and inject it, not optimal could be used to have spring manage dependency.
        CurrencyService currencyService = new CurrencyServiceImpl();
        // Wait for the standard input to load the Currency and its payment data.
        loadInitialPaymentValue(currencyService);
        // Wait for the standard input to load the Currency Conversion Values to USD.
        loadInitialCurrencyConversionValue(currencyService);
        // Create the thread pool for the 1 minute task to print latest balances.
        ScheduledThreadPoolExecutor threadPool
                = new ScheduledThreadPoolExecutor(1);
        // Task to print all currency latest balances.
        CurrencySeriesPrintTask printTask = new CurrencySeriesPrintTask(currencyService);
        //Schedule task to 1 minute
        threadPool.scheduleAtFixedRate(printTask,0 ,1, TimeUnit.MINUTES);
        // Start the task to take user input to update the Currency balances.
        Thread userInputThread = new Thread(new PaymentInputReaderTask(currencyService), "User Input Thread");
        userInputThread.start();
        try{
            // Wait for the user thread to complete before ending main thread.
            userInputThread.join();
        }catch (InterruptedException ex){
            System.err.println("Interrupted Exception Occurred "+ ex);
            throw new RuntimeException(ex);
        }
        // Shutdown the Schedule thread pool executor.
        threadPool.shutdown();
        try {
            // Await any pending task to complete.
            if (threadPool.awaitTermination(1, TimeUnit.MINUTES)) {
                // Force kill the thread pool.
                threadPool.shutdownNow();
            }
        }catch (InterruptedException ex){
            System.err.println("Interrupted Exception Occurred "+ ex);
            throw new RuntimeException(ex);
        }
    }

    /*
    * Load the initial payment transaction data.
    * */
    private static void loadInitialPaymentValue(CurrencyService currencyService){
        System.out.println("Please enter your files location to load the initial Payment below: (send 'skip' to move ahead) ");
        // Scanner Input.
        Scanner fileNameInput = new Scanner(System.in);
        try {
            // File Path value.
            String filePathName = fileNameInput.nextLine();
            // Terminate if Skip value is entered.
            if(Constants.SKIP_VALUE.equalsIgnoreCase(filePathName)){
                return;
            }
            // Read file data.
            List<String> paymentDataList = Files.readAllLines(Path.of(filePathName));
            // Map to the payment model.
            List<PaymentDTO> paymentDTOList = PaymentMapper.mapFileToListOfPaymentObj(paymentDataList);
            // Process bulk payment transaction.
            currencyService.processBulkPaymentTransaction(paymentDTOList);
        }catch (IOException ex){
            System.err.println("Initial Payment can't be loaded due to read error but continue to load via console");
        }
    }

    /*
     * Load the initial payment currency data.
     * */
    private static void loadInitialCurrencyConversionValue(CurrencyService currencyService){
        System.out.println("Please enter your files location to load the currency conversion below: (send 'skip' to move ahead) ");
        // Scanner Input.
        Scanner fileNameInput = new Scanner(System.in);
        try {
            // File Path value.
            String filePathName = fileNameInput.nextLine();
            // Terminate if Skip value is entered.
            if(Constants.SKIP_VALUE.equalsIgnoreCase(filePathName)){
                return;
            }
            // Read file data.
            List<String> paymentDataList = Files.readAllLines(Path.of(filePathName));
            // Process bulk currency rate.
            currencyService.processBulkConversionRate(paymentDataList);
        }catch (IOException ex){
            System.err.println("Initial Payment Conversion can't be loaded due to read error but continue to load via console");
        }
    }
}
