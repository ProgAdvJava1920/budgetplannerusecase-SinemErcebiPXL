package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BudgetPlannerMapper {
    // TODO: move to property file

    public static final String DATE_PATTERN = "EEE MMM d HH:mm:ss z yyyy";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.US);
    public static final int CSV_ITEM_COUNT = 7;
    private Map<String, Account> accountMap = new HashMap<>();

    public List<Account> mapAccounts(List<String> accountLines) {
        //       v---- cursor here, alt+enter, 2nd option (convert...)
        //return accountLines.stream().map(this::mapDataLineToAccount).collect(Collectors.toList());

        //List<Account> list = new ArrayList<>();
        for (String accountLine : accountLines) {
            Account account = null;
            // skip bad lines, continue parsing
            try {
                // parse account (and payment) from line
                account = mapDataLineToAccount(accountLine);

                // if account isn't in the map yet, put it in
                accountMap.putIfAbsent(account.getIBAN(), account);

                                /*
                if (!list.contains(account)) {
                    list.add(account);
                } else {
                    list.get(list.indexOf(account)).getPayments().add(account.getPayments().get(0));
                }
                */

            } catch (ParseException | BudgetPlannerException e) {
                System.err.printf("Could not parse line [%s]", accountLine);
            }
        }
        return new ArrayList<>(accountMap.values());
    }

    public Account mapDataLineToAccount(String line) throws BudgetPlannerException, ParseException {
        String[] items = line.split(",");

        if (items.length != CSV_ITEM_COUNT) {
            throw new BudgetPlannerException(String.format("Expected %d fields in line, but found %d", CSV_ITEM_COUNT, items.length));
        }

        String name = items[0];
        String iban = items[1];

        // origineel: maak een account aan, daarna if statement met conditie (account == null) maak dan
        // een nieuw account aan

        // get existing account from map, if it doesn't doesn't exist in the map create a new account
        Account account = accountMap.getOrDefault(iban, new Account(name, iban));

        //Account account = new Account(name, iban);

        Payment payment = mapItemsToPayment(items);
        account.getPayments().add(payment);

        return account;
    }

//    private Payment createPayment(String[] data) throws ParseException {
//        String iban = data[2];
//        Date date = convertToDate(data[3]);
//        float amount = Float.parseFloat(data[4]);
//        String currency = data[5];
//        String details = data[6];
//        Payment payment = new Payment(iban, date, amount, currency, details);
//
//        return payment;
//    }

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