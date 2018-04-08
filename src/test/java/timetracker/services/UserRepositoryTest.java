package timetracker.services;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import org.junit.Before;
//import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import timetracker.models.User;
import timetracker.testing.DBDriver;
import timetracker.utils.PropertyLoader;
/**
 * Класс UserRepositoryTest тестирует класс UserRepository.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-08
 * @since 2018-04-08
 */
public class UserRepositoryTest {
    /**
     * Драйвер бд.
     */
    private DBDriver dbDriver;
    /**
     * Кодировка.
     */
    private final String enc = Charset.defaultCharset().toString();
    /**
     * Свойства с настройками бд.
     */
    private Properties props;
    /**
     * Репозиторий пользователей.
     */
    private UserRepository ur;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        this.props = new Properties();
        try {
            this.ur = new UserRepository(this.enc);
            PropertyLoader pl = new PropertyLoader("DBDriver.properties");
    		this.props = pl.getProperties();
            Class.forName(this.props.getProperty("class")).newInstance();
            String url = String.format("jdbc:%s://%s:%s/%s", this.props.getProperty("protocol"), this.props.getProperty("src"), this.props.getProperty("port"), this.props.getProperty("db"));
            this.dbDriver = new DBDriver(url, this.props.getProperty("user"), props.getProperty("pass"));
            String path = new File(UserRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "/";
            path = path.replaceFirst("^/(.:/)", "$1");
            this.dbDriver.executeSqlScript(path + "../../src/main/resources/time_tracker_data.sql");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public String getPassHash(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException.
     */
    @Test
    public void testGetPassHash() {
        try {
            assertEquals("4ac1b63dca561d274c6055ebf3ed97db", this.ur.getPassHash("test_pass"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует LinkedList<User> getUsers(String query).
     */
    @Test
    public void testGetUsers() {
        try {
            LinkedList<HashMap<String, String>> result = this.dbDriver.select("select * from users order by id");
            String expected = result.get(0).get("pass");
            LinkedList<User> users = this.ur.getUsers("select * from users order by id");
            String actual = users.getFirst().getPass();
            assertEquals(expected, actual);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}