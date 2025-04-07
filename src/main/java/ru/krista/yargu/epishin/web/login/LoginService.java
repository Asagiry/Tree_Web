package ru.krista.yargu.epishin.web.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.krista.yargu.epishin.exceptions.login.AuthDBLoginException;
import ru.krista.yargu.epishin.exceptions.login.InitDBLoginException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Сервис входа: проверяет логин и пароль, на соответствие записанным в базу данных.
 */
public class LoginService {

    private static final Logger logger = LogManager.getLogger();

    public LoginService() throws InitDBLoginException {
        createTable();
    }
    private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    public boolean login(String username, String password) throws AuthDBLoginException {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new AuthDBLoginException("Ошибка аутентификации", e);
        }
    }

    private static void createTable() throws InitDBLoginException {
        String sql = "CREATE TABLE IF NOT EXISTS users (ID int primary key, username varchar(50), password varchar(50))";
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
            Statement statement = connection.createStatement()){
            statement.execute(sql);
            statement.executeUpdate("Insert into users (ID, username, password) values (1, 'alice', 'alice')");
            statement.executeUpdate("Insert into users (ID, username, password) values (2, 'bob', 'bob')");
        } catch (SQLException e) {
            logger.error("Не удалось создать БД");
            throw new InitDBLoginException("Не удалось создать БД",e);
        }
    }


}
