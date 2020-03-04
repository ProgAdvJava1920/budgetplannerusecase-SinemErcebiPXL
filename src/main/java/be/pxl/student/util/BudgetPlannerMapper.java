package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BudgetPlannerMapper {
    public static final String DATE_PATTERN = "EEE MMM d HH:mm:ss z yyyy";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.US);
    public static final int CSV_ITEM_COUNT = 7;
    private Map<String, Account> accountMap = new HashMap<>();

    public List<Account> mapAccounts(List<String> accountLines) {
        for (String accountLine : accountLines) {
            try {
                Account account = mapDataLineToAccount(accountLine);
                accountMap.putIfAbsent(account.getIBAN(), account);
            } catch (ParseException | BudgetPlannerException e) {
                System.err.printf("Could not parse line [%s]", accountLine);
            }
        }
        return new ArrayList<>(accountMap.values());
    }

    public Account mapDataLineToAccount(String line) throws BudgetPlannerException, ParseException {
        String[] items = line.split(",");

        if (items.length != CSV_ITEM_COUNT) {
            throw new BudgetPlannerException(String.format("Invalid line. Expected %d items", CSV_ITEM_COUNT));
        }

        String name = items[0];
        String iban = items[1];

        // origineel: maak een account aan, daarna if statement met conditie (account == null) maak dan
        // een nieuw account aan
        Account account = accountMap.getOrDefault(iban, new Account(name, iban));
        Payment payment = mapItemsToPayment(items);
        account.getPayments().add(payment);

        return account;
    }

    private Payment createPayment(String[] data) throws ParseException {
        String iban = data[2];
        Date date = convertToDate(data[3]);
        float amount = Float.parseFloat(data[4]);
        String currency = data[5];
        String details = data[6];
        Payment payment = new Payment(iban, date, amount, currency, details);

        return payment;
    }

    public Date convertToDate(String dateString) throws ParseException {
        /*String datePattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern(datePattern, Locale.US);
        ZonedDateTime zonedDate = ZonedDateTime.parse(dateString, myFormatter);
        return Date.from(zonedDate.toInstant());*/
        return DATE_FORMAT.parse(dateString);
    }

    public String convertDateToString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public Payment mapItemsToPayment(String[] items) throws ParseException {
        return new Payment(
                items[2],                   // IBAN
                convertToDate(items[3]),    // Transaction date
                Float.parseFloat(items[4]), // ammount
                items[5],                   // currency
                items[6]                    // detail
        );

        //throw new RuntimeException("not yet implemented");
        //we hebben gekozen voor runtime exception omdat we in onze signature geen "throws" willen hebben
        // + het is beter dan return null ofzo
    }
}
