package be.pxl.student.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentDAOTest {
    private static final String DB_URL = "jdbc:h2:mem:test;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:BudgetPlannerTest.sql'";

    private DAOManager manager;
    private PaymentDAO paymentDAO;

    @BeforeEach
    void setUp() {
        manager = new DAOManager(DB_URL);
        paymentDAO = new PaymentDAO(manager);
    }

    @AfterEach
    void tearDown() {
        manager.close();
    }

    @Test
    void it_should_create_new_payment() throws PaymentException {
        Payment expectedPayment = new Payment(1, 2, "dummyIBAN3", Date.valueOf(LocalDate.now()), 50, "EUR", "nothing");

        Payment actualPayment = paymentDAO.create(expectedPayment);

        assertEquals(expectedPayment, actualPayment);

    }

    @Test
    void it_should_set_id_of_new_payment() throws PaymentException {
        Payment expectedPayment = new Payment(1, 2, "dummyIBAN3", Date.valueOf(LocalDate.now()), 50, "EUR", "nothing");

        Payment actualPayment = paymentDAO.create(expectedPayment);

        assertNotEquals(0, actualPayment.getId());
    }

    @Test
    void it_should_return_payment_with_id_1() throws PaymentException {
        Payment expectedPayment = new Payment(1, 2, "dummyIBAN", Date.valueOf(LocalDate.now()), 50, "EUR", "nothing");

        Payment foundPayment = paymentDAO.getById(1);

        assertEquals(expectedPayment, foundPayment);
    }

    @Test
    void it_should_return_2_payments() throws PaymentException {
        List<Payment> payments = paymentDAO.getAll();

        assertEquals(2, payments.size());
    }

    @Test
    void it_should_update_existing_payment_with_id_1() throws PaymentException {
        Payment expectedPayment = new Payment(1, 1, 2, "dummyIBAN77", Date.valueOf(LocalDate.now()), 25, "USD", "");

        Payment updatedPayment = paymentDAO.update(expectedPayment);

        assertEquals(expectedPayment, updatedPayment);
    }

    @Test
    void it_should_delete_payment_with_id_1() throws PaymentException {
        Payment expectedPayment = new Payment(1, 1, 2, "dummyIBAN", Date.valueOf(LocalDate.now()), 50, "EUR", "nothing");

        paymentDAO.delete(expectedPayment);

        assertThrows(PaymentNotFoundException.class, () -> {
            Payment payment = paymentDAO.getById(1);
        });
    }

//    @Test
//    void it_should_return_account_with_id_1() throws AccountException {
//        AccountDAO dao = new AccountDAO(DB_URL);
//        Account account = dao.getById(1);
//        Account exceptedAccount = new Account(1, "dummyIBAN", "dummyName");
//        assertEquals(exceptedAccount, account);
//    }
}
