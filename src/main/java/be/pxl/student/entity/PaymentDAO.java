package be.pxl.student.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements DAO<Payment, PaymentException> {
    private static final String CREATE = "INSERT INTO Payment (`accountId`, `counterAccountId`, `IBAN`, `date`, `amount`, `currency`, `detail`) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Payment";
    private static final String SELECT_BY_ID = "SELECT * FROM Payment WHERE id = ?";
    private static final String UPDATE = "UPDATE Payment SET `accountId` = ?, `counterAccountId` = ?, `IBAN` = ?, `date` = ?, `amount` = ?, `currency` = ?, `detail` = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Payment WHERE `id` = ?";

    private Logger logger = LogManager.getLogger(PaymentDAO.class);
    DAOManager manager;

    public PaymentDAO(DAOManager manager) {
        this.manager = manager;
    }

    @Override
    public Payment create(Payment payment) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(CREATE)) {
            preparedStatement.setInt(1, payment.getAccountId());
            preparedStatement.setInt(2, payment.getCounterAccountId());
            preparedStatement.setString(3, payment.getIBAN());
            preparedStatement.setDate(4, (Date) payment.getDate());
            preparedStatement.setFloat(5, payment.getAmount());
            preparedStatement.setString(6, payment.getCurrency());
            preparedStatement.setString(7, payment.getDetail());

            int result = preparedStatement.executeUpdate();

            // id opvragen uit database
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.first()) {
                int paymentId = generatedKeys.getInt(1); // 1 is start index
                payment.setId(paymentId);
            }

            // geef payment terug als het succesvol was
            if (result == 1) {
                return payment;
            }

            manager.commit();
        } catch (SQLException e) {
            throw new PaymentException(String.format("Failed to create payment [%s]", payment), e);
        }

        throw new PaymentException("Not yet implemented");
    }

    @Override
    public Payment getById(int id) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id); // indexen beginnen vanaf 1
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) { // is er één resultaat
                return new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("accountId"),
                        resultSet.getInt("counterAccountId"),
                        resultSet.getString("IBAN"),
                        resultSet.getDate("date"),
                        resultSet.getFloat("amount"),
                        resultSet.getString("currency"),
                        resultSet.getString("detail")
                );
            } else {
                throw new PaymentNotFoundException(String.format("Payment with id [%d]", id));
            }
        } catch (SQLException e) {
            // exception gooien met informatie en originele exception meegeven voor debuggen
            // stack trace blijft dan volledig
            throw new PaymentException(String.format("Exception while retrieveing payment with id [%d]", id), e);
        }
    }

    @Override
    public List<Payment> getAll() throws PaymentException {
        List<Payment> paymentList = new ArrayList<>();

        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                paymentList.add(
                        new Payment(
                                resultSet.getInt("id"),
                                resultSet.getInt("accountId"),
                                resultSet.getInt("counterAccountId"),
                                resultSet.getString("IBAN"),
                                resultSet.getDate("date"),
                                resultSet.getFloat("amount"),
                                resultSet.getString("currency"),
                                resultSet.getString("detail")
                        )
                );
            }
        } catch (SQLException e) {
            throw new PaymentException("Error retrieving payments", e);
        }

        return paymentList;
    }

    @Override
    public Payment update(Payment payment) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(UPDATE)) {
            preparedStatement.setInt(1, payment.getAccountId());
            preparedStatement.setInt(2, payment.getCounterAccountId());
            preparedStatement.setString(3, payment.getIBAN());
            preparedStatement.setDate(4, (Date) payment.getDate());
            preparedStatement.setFloat(5, payment.getAmount());
            preparedStatement.setString(6, payment.getCurrency());
            preparedStatement.setString(7, payment.getDetail());

            preparedStatement.setInt(8, payment.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return payment;
            }
        } catch (SQLException e) {
            throw new PaymentException(String.format("Exception while updating payment with id [%d]", payment.getId()), e);
        }

        throw new PaymentException("Could not update payment");
    }

    @Override
    public Payment delete(Payment payment) throws PaymentException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(DELETE)) {
            preparedStatement.setInt(1, payment.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return payment;
            }
        } catch (SQLException e) {
            throw new PaymentException(String.format("Exception while deleting payment with id [%d]", payment.getId()), e);
        }

        throw new PaymentException("Could not delete payment");
    }
}
