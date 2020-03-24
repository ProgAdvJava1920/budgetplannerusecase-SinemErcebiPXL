package be.pxl.student.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements DAO<Account, AccountException> {
    // SQL statements
    //public static final String CREATE = "INSERT INTO account VALUES(?, ?, ?)";
    private static final String CREATE = "INSERT INTO account (`IBAN`, `name`) VALUES(?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Account";
    private static final String SELECT_BY_ID = "SELECT * FROM Account WHERE id = ?";
    private static final String UPDATE = "UPDATE account SET `IBAN` = ?, `name` = ? WHERE `id` = ?";
    private static final String DELETE = "DELETE FROM account WHERE `id` = ?";

    private Logger logger = LogManager.getLogger(be.pxl.student.entity.AccountDAO.class);
    private DAOManager manager;

    public AccountDAO(DAOManager manager) {
        this.manager = manager;
    }

    @Override
    public Account create(Account account) throws AccountException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(CREATE)) {
            //preparedStatement.setInt(1, account.getId());       // 1 is start index, normaal gezien zelf niet id meegeven
            preparedStatement.setString(1, account.getIBAN());
            preparedStatement.setString(2, account.getName());
            int result = preparedStatement.executeUpdate();

            // id opvragen uit database
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.first()) {
                int accountId = generatedKeys.getInt(1); // 1 is start index
                account.setId(accountId);
            }

            // geef account terug als het succesvol was
            if (result == 1) {
                return account;
            }

            manager.commit();
        } catch (SQLException e) {
            manager.rollback();
            throw new AccountException(String.format("Error creating account [%s]", account), e);
        }

        // leeg account terug geven, dan op applicatie niveau kijken wat misging
        //return new Account();

        // exception die opgevangen kan worden
        throw new AccountException("Could not create account");
    }

    @Override
    public List<Account> getAll() throws AccountException {
        List<Account> accountList = new ArrayList<>();

        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accountList.add(
                        new Account(
                                resultSet.getInt("id"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (SQLException e) {
            throw new AccountException("Error retrieving accounts", e);
        }

        return accountList;
    }

    @Override
    public Account getById(int id) throws AccountException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id); // bij preparedStatements telt die vanaf 1, niet 0!
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) { // is er één resultaat
                return new Account(
                        resultSet.getInt("id"),         // lees een int van een kolom met naam "id"
                        resultSet.getString("IBAN"),
                        resultSet.getString("name")
                );
            } else {
                throw new AccountNotFoundException(String.format("Account with id [%d]", id));
            }
        } catch (SQLException e) {
            // exception gooien met informatie en originele exception meegeven voor debuggen
            // stack trace blijft dan volledig
            throw new AccountException(String.format("Exception while retrieveing account with id [%d]", id), e);
        }
    }

    @Override
    public Account update(Account account) throws AccountException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(UPDATE)) {
            preparedStatement.setString(1, account.getIBAN());
            preparedStatement.setString(2, account.getName());

            preparedStatement.setInt(3, account.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return account;
            }
        } catch (SQLException e) {
            throw new AccountException(String.format("Exception while updating account with id [%d]", account.getId()), e);
        }

        throw new AccountException("Could not update account");
    }

    @Override
    public Account delete(Account account) throws AccountException {
        try (PreparedStatement preparedStatement = manager.getConnection().prepareStatement(DELETE)) {
            preparedStatement.setInt(1, account.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return account;
            }
        } catch (SQLException e) {
            throw new AccountException(String.format("Exception while deleting account with id [%d]", account.getId()), e);
        }

        throw new AccountException("Could not delete account");
    }
}
