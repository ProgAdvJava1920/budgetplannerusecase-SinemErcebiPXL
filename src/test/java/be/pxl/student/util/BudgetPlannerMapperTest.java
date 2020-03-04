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

public class BudgetPlannerMapperTest {

    List<String> accountLines;
    Path testCsvFile = Paths.get("src/test/resources/account_payments_test.csv");
    BudgetPlannerMapper mapper = new BudgetPlannerMapper();
    String testDataLine = "Jos,BE69771770897312,BE93287762060534,Thu Feb 20 03:28:49 CET 2020,4274.76,EUR,Nostrum ducimus error dolore amet.";

    @BeforeEach
    void setUp() throws BudgetPlannerException {
        accountLines = BudgetPlannerImporter.readCsvFile(testCsvFile);
    }

    @Test
    void it_should_return_non_empty_list() {
        assertFalse(mapper.mapAccounts(accountLines).isEmpty());
    }

    @Test
    void it_should_map_to_account_list_with_1_account() {
        List<Account> accountList = mapper.mapAccounts(accountLines);
        assertEquals(1, accountList.size(), "it should have 1 account");
    }

    @Test
    void it_should_map_to_account_list_with_1_account_with_2_payments() {
        List<Account> accountList = mapper.mapAccounts(accountLines);
        assertEquals(2, accountList.get(0).getPayments().size(), "account should have 2 payments");

    }

    @Test
    void it_should_map_line_to_account_object() throws BudgetPlannerException, ParseException {
        Account expectedAccount = new Account("Jos", "BE69771770897312");
        Account lineToAccount = mapper.mapDataLineToAccount(testDataLine);
        assertEquals(expectedAccount, lineToAccount);
    }

    @Test
    void it_should_map_line_to_payment() throws ParseException {
        String testDataLine = "Jos,BE69771770897312,BE93287762060534,Thu Feb 20 03:28:49 CET 2020,4274.76,EUR,Nostrum ducimus error dolore amet.";

        Payment expectedPayment = new Payment(
                "BE93287762060534",
                mapper.convertToDate("Thu Feb 20 03:28:49 CET 2020"),
                4274.76f,
                "EUR",
                "Nostrum ducimus error dolore amet."
        );

        //handig nadat je pauze hebt genomen
        //throw new Exception("not yet completed");
        //fail("still on it")

        Payment actualPayment = mapper.mapItemsToPayment(testDataLine.split(","));
        assertEquals(expectedPayment, actualPayment);
    }

    @Test
    void it_should_convert_date_to_string_and_back_again() throws ParseException {
        String testDate = "Thu Feb 20 03:28:49 CET 2020";
        Date date = mapper.convertToDate(testDate);
        String dateToString = mapper.convertDateToString(date);
        assertEquals(testDate, dateToString);
    }
}
