package sims.hodaksims.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Klasa za baze podataka
 */
public class DbConUtil {
    private static final Logger log = LoggerFactory.getLogger(DbConUtil.class);

    private DbConUtil() {

    }

    /**
     * getConnection dohvati vezu
     * @return Connection
     * @throws SQLException SQLException
     */
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try(final FileReader f = new FileReader("database.properties")) {
            props.load(f);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return DriverManager.getConnection(props.getProperty("databaseUrl"), props.getProperty("username"), props.getProperty("password"));
    }
}
