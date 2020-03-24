package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BudgetPlannerMapperTest {
    BudgetPlannerMapper mapper;
    Path path = Paths.get("src/test/resources/account_payments_test.csv");
    List<String> accountLines;
    String testDataLine = "Jos,BE69771770897312,BE17795215960626,Thu Feb 13 05:47:35 CET 2020,265.8,EUR,Ut ut necessitatibus itaque ullam.";

    @BeforeEach
    void setUp() {
        mapper = new BudgetPlannerMapper();

        try {
            accountLines = BudgetPlannerImporter.readCsvFile(path);
        } catch (BudgetPlannerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void it_should_return_non_empty_list() {
        List<Account> accountList = mapper.mapAccounts(accountLines);

        assertFalse(accountList.isEmpty());
    }

    @Test
    void it_should_map_to_account_list_with_one_account() {
        List<Account> accountList = mapper.mapAccounts(accountLines);

        assertEquals(1, accountList.size(), "It should have one account");
    }

    @Test
    void it_should_map_to_account_list_with_one_account_with_two_payments() {
        List<Account> accountList = mapper.mapAccounts(accountLines);

        assertEquals(2, accountList.get(0).getPayments().size(), "Account should have two payments");
    }

    @Test
    void it_should_map_line_to_account_object() throws ParseException, BudgetPlannerException {
        Account expectedAccount = new Account("Jos", "BE69771770897312");
        Account lineToAccount = mapper.mapDataLineToAccount(testDataLine);

        assertEquals(expectedAccount, lineToAccount);
    }

    @Test
    void it_should_map_line_to_payment() throws ParseException {
        Payment expectedPayment = new Payment(
                "BE17795215960626",
                mapper.convertToDate("Thu Feb 13 05:47:35 CET 2020"),
                265.8f,
                "EUR",
                "Ut ut necessitatibus itaque ullam."
        );

        //handig nadat je pauze hebt genomen
        //throw new Exception("not yet completed");
        //fail("still on it")

        Payment actualPayment = mapper.mapItemsToPayment(testDataLine.split(","));

        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void it_should_convert_date_and_back_again() throws ParseException {
        String testDate = "Thu Feb 13 05:47:35 CET 2020";
        Date date = mapper.convertToDate(testDate);
        String dateToString = mapper.convertDateToString(date);

        assertEquals(testDate, dateToString);
    }
}