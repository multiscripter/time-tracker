package timetracker.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 * Класс DBDriver реализует сущность Драйвер бд.
 * Использован apache tomcat database connection pool.
 * Для конфигурирования пула соединений с бд не используется JNDI.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
 * @since 2018-04-07
 */
public class DBDriver {
    /**
     * Класс Синглетона.
     */
    public static final class SingletonHolder {
        /**
         * Синглетон.
         */
        public static final DBDriver INSTANCE = new DBDriver();
    }
    /**
     * Соединение с бд.
     */
    private Connection con;
    /**
     * Логгер.
     */
    private final Logger logger;
	/**
     * DataSource.
     */
	private DataSource ds;
	/**
     * Путь до файла.
     */
    private String path;
    /**
     * Свойства с настройками бд.
     */
    private Properties props;
	/**
	 * Конструктор.
	 */
	private DBDriver() {
        this.logger = LogManager.getLogger(this.getClass().getSimpleName());
        this.props = new Properties();
        try {
            PropertyLoader pl = new PropertyLoader(this.getClass().getSimpleName() + ".properties");
    		this.props = pl.getProperties();
            Class.forName(this.props.getProperty("class")).newInstance(); //load driver
            this.path = new File(DBDriver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "/";
    		this.path = this.path.replaceFirst("^/(.:/)", "$1");
            this.ds = (DataSource) Class.forName(this.props.getProperty("server")).newInstance();
    		this.executeSqlScript(this.props.getProperty("db_struct"));
    		this.setConnection();
        } catch (URISyntaxException | IOException | NullPointerException | SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            this.logger.error("ERROR", ex);
        }
	}
    /**
     * Закрывает соединение с СУБД.
     * @throws SQLException ошибка SQL.
     */
    public void close() throws SQLException {
        if (this.con != null && !this.con.isClosed()) {
            this.con.close();
        }
    }
	/**
     * Выполняет вудуеу sql-запрос.
     * @param query sql-запрос.
     * @return количество записей, затронутых запросом.
     * @throws SQLException исключение SQL.
     */
	public int delete(String query) throws SQLException {
        if (this.con == null) {
			this.setConnection();
    	}
    	int affected = 0;
		try (Statement stmt = this.con.createStatement()) {
			affected = stmt.executeUpdate(query);
		} catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return affected;
	}
    /**
     * Выполняет sql-запрос.
     * @param query sql-запрос.
     * @throws SQLException исключение SQL.
     */
    public void executeSql(String query) throws SQLException {
    	if (this.con == null) {
			this.setConnection();
    	}
    	try (Statement stmt = this.con.createStatement()) {
			stmt.execute(query);
    	} catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }
    /**
     * Выполняет sql-скрипт.
     * @param localName локальное имя sql-файла.
     * @throws IOException исключение ввода-вывода.
     * @throws SQLException исключение SQL.
     */
    private void executeSqlScript(String localName) throws IOException, SQLException {
        if (this.con == null) {
			this.setConnection();
    	}
        byte[] bytes = Files.readAllBytes(Paths.get(path + localName));
        String query = new String(bytes, "UTF-8");
        try (Statement stmt = this.con.createStatement()) {
            stmt.execute(query);
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }
    /**
     * Получает псенглетон.
     * @return сенглетон.
     */
    public static DBDriver getInstance() {
        return SingletonHolder.INSTANCE;
    }
    /**
     * Получает свойства с настройками бд.
     * @param name имя свойства.
     * @return значение указанного свойства.
     */
    public String getProperty(String name) {
        return this.props.getProperty(name);
    }
    /**
     * Получает свойства с настройками бд.
     * @return объект свойств с настройками бд.
     */
    public Properties getProperties() {
        return this.props;
    }
    /**
     * Выполняет insert sql-запрос.
     * @param query sql-запрос.
     * @return карту с результатом запроса к бд.
     * @throws SQLException исключение SQL.
     */
    public HashMap<String, String> insert(String query) throws SQLException {
    	if (this.con == null) {
			this.setConnection();
    	}
    	HashMap<String, String> entry = new HashMap<>();
    	try (Statement stmt = this.con.createStatement()) {
			if (stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
                if (rs != null) {
                    rs.next();
                    ResultSetMetaData rsmd = rs.getMetaData();
                	entry.put(rsmd.getColumnName(1), Integer.toString(rs.getInt(rsmd.getColumnName(1))));
                }
			}
    	} catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return entry;
	}
    /**
     * Выполняет select sql-запрос.
     * @param query sql-запрос.
     * @return карту с результатом запроса к бд.
     * @throws SQLException исключение SQL.
     */
    public LinkedList<HashMap<String, String>> select(String query) throws SQLException {
    	if (this.con == null) {
			this.setConnection();
    	}
    	LinkedList<HashMap<String, String>> rl = new LinkedList<>();
    	try (PreparedStatement pstmt = this.con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
        	if (rs != null) {
            	ResultSetMetaData rsmd = rs.getMetaData();
            	while (rs.next()) {
            		HashMap<String, String> entry = new HashMap<>();
            		for (int a = 1; a < rsmd.getColumnCount() + 1; a++) {
	            		entry.put(rsmd.getColumnName(a), rs.getString(a));
	            	}
            		rl.add(entry);
            	}
        	}
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return rl;
    }
    /**
     * Устанавливает соединение с бд.
     * @throws SQLException исключение SQL.
     */
    private void setConnection() throws SQLException {
    	try {
    		this.con = this.ds.getConnection();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }
    /**
     * Выполняет update sql-запрос.
     * @param query sql-запрос.
     * @return количество записей, затронутых запросом.
     * @throws SQLException исключение SQL.
     */
    public int update(String query) throws SQLException {
    	return this.delete(query);
    }
}