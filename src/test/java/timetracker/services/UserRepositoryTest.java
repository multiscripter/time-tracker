package timetracker.services;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import timetracker.models.User;
import timetracker.testing.DBDriver;
import timetracker.utils.PropertyLoader;
/**
 * Класс UserRepositoryTest тестирует класс UserRepository.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-09
 * @since 2018-04-08
 */
public class UserRepositoryTest {
    /**
     * Драйвер бд.
     */
    private DBDriver dbDriver;
    /**
     * Репозиторий пользователей.
     */
    private UserRepository ur;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        Properties props;
        try {
            this.ur = new UserRepository();
            this.ur.setEncoding(Charset.defaultCharset().toString());
            PropertyLoader pl = new PropertyLoader("DBDriver.properties");
    		props = pl.getProperties();
            Class.forName(props.getProperty("dbdriver")).newInstance();
            String url = String.format("jdbc:%s://%s:%s/%s", props.getProperty("protocol"), props.getProperty("src"), props.getProperty("port"), props.getProperty("db"));
            this.dbDriver = new DBDriver(url, props.getProperty("user"), props.getProperty("pass"));
            String path = new File(UserRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "/";
            path = path.replaceFirst("^/(.:/)", "$1");
            this.dbDriver.executeSqlScript(path + "../../src/main/resources/" + props.getProperty("db_test_data"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public String getHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException.
     */
    @Ignore@Test
    public void testGetHash() {
        try {
            assertEquals("4ac1b63dca561d274c6055ebf3ed97db", this.ur.getHash("test_pass"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public User getUserByLogPass(String login, String pass) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException.
     */
    @Test
    public void testGetUserByLogPass() {
        try {
            String login = "test_user";
            String query = String.format("select pass from users where login = '%s'", login);
            LinkedList<HashMap<String, String>> result = this.dbDriver.select(query);
            String expected = result.get(0).get("pass");
            String actual = this.ur.getUserByLogPass(login, "test_pass").getPass();
            assertEquals(expected, actual);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует LinkedList<User> getUsers(String query).
     * Пустой набор.
     */
    @Test
    public void testGetUsersEmptySet() {
        try {
            User user = this.ur.getUserByLogPass("Несуществующий логин", "test_pass");
            assertNull(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}