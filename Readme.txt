1) To Run the code:
java -jar currency-series-1.0-SNAPSHOT.jar

2) To provide file path for the Payment file/Conversion rate, either provide file or enter skip.

3) To repackage the jar file 
mvn package spring-boot:repackage

4) To install the package
mvn clean install

5) Runs on JDK 11

6) Assume the conversion rate and the initial payment data is optional to load. Can be skipped as well.

7) Assuming the payment are basically transactions which needs to be added to the balance of the currency. 

Sample Files assuming the data are attached as well.

Sample Execution
Please enter your files location to load the initial Payment below: (send 'skip' to move ahead)
C://Users//sahil//PaymentFile.txt
Please enter your files location to load the currency conversion below: (send 'skip' to move ahead)
C://Users//sahil//PaymentConversion.txt
Please enter your data below: (send 'quit' to exit)
Currency Code: HKD Amount:800 USD Value 7200
Currency Code: GBP Amount:1000 USD Value 2000
HKD 0.7
Currency Code: HKD Amount:800.7 USD Value 7206.3
Currency Code: GBP Amount:1000 USD Value 2000
quit

