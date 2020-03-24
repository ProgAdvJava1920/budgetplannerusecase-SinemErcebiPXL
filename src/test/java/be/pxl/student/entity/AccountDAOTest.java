package be.pxl.student.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOTest {
    // jdbc: jdbc gebruiken
    // h2: H2 database
    // mem: in geheugen opslaan
    // MODE: voor MySQL commando's te gebruiken
    // INIT: run script voor te beginnen
    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:BudgetPlannerTest.sql'";

    private DAOManager manager;
    private AccountDAO accountDAO;

    @BeforeEach
    void setUp() {
        manager = new DAOManager(DB_URL);
        accountDAO = new AccountDAO(manager);
    }

    @AfterEach
    void tearDown() {
        manager.close();
    }

    @Test
    void it_should_create_a_new_account() throws AccountException {
        Account expectedAccount = new Account("nieuwAccount", "nieuweIBAN");

        Account actualAccount = accountDAO.create(expectedAccount);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    void it_should_set_id_of_new_account() throws AccountException {
        Account expectedAccount = new Account("nieuwAccount", "nieuweIBAN");

        Account actualAccount = accountDAO.create(expectedAccount);

        assertNotEquals(0, actualAccount.getId());
    }

    @Test
    void it_should_return_3_items() throws AccountException {
        List<Account> accounts = accountDAO.getAll();

        assertEquals(3, accounts.size());
    }

    @Test
    void it_should_return_account_with_id_1() throws AccountException {
        Account account = accountDAO.getById(1);

        Account expectedAccount = new Account(1, "dummyIBAN", "dummyName");

        assertEquals(expectedAccount, account);
    }

    @Test
    void it_should_update_account_with_id_1() throws AccountException {
        Account expectedAccount = new Account(1, "geenDummyIBAN", "dummyName");

        Account actualAccount = accountDAO.update(expectedAccount);

        assertEquals(expectedAccount, actualAccount);
    }

    @Test
    void it_should_delete_account_with_id_1() throws AccountException {
        Account expectedAccount = new Account(1, "dummyIBAN", "dummyName");

        accountDAO.delete(expectedAccount);

        assertThrows(AccountNotFoundException.class, () -> {
            Account actualAccount = accountDAO.getById(1);
        });
    }
}