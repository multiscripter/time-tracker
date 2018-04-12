package timetracker.utils;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import org.junit.Before;
//import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
/**
 * Класс PropertyLoaderTest тестирует класс PropertyLoader.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-12
 * @since 2018-01-10
 */
public class PropertyLoaderTest {
    /**
     * Загрузчик свойств.
     */
    private PropertyLoader pl;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        this.pl = new PropertyLoader();
    }
    /**
     * Тестирует public Properties getProperties().
     */
    @Test
    public void testGetPropertiesOnEmptyLoader() {
        assertNotNull(this.pl.getProperties());
    }
    /**
     * Тестирует public LinkedList<Property> getPropertiesList() throws UnsupportedEncodingException.
     */
    @Test
    public void testGetPropertiesList() {
        try {
            List<Property> expected = new LinkedList();
            expected.add(new Property("server", "timetracker.utils.Tomcat"));
            expected.add(new Property("minIdle", "5"));
            expected.add(new Property("maxIdle", "10"));
            expected.add(new Property("maxOpenPreparedStatements", "100"));
            expected.add(new Property("dbdriver", "com.mysql.cj.jdbc.Driver"));
            expected.add(new Property("protocol", "mysql"));
            expected.add(new Property("src", "localhost"));
            expected.add(new Property("port", "3306"));
            expected.add(new Property("user", "root"));
            expected.add(new Property("pass", "mysqlrootpass"));
            expected.add(new Property("db", "time_tracker"));
            expected.add(new Property("db_struct", "time_tracker_struct.sql"));
            expected.add(new Property("db_test_data", "time_tracker_test_data.sql"));
            expected.add(new Property("tbl_users", "users"));
            expected.add(new Property("tbl_tokens", "tokens"));
            expected.add(new Property("tbl_marks", "marks"));
            expected.add(new Property("gmt", "+3"));
            this.pl.load("DBDriver.properties");
            List<Property> actual = this.pl.getPropertiesList();
            boolean contains = true;
            for (Property prop : expected) {
                if (!actual.contains(prop)) {
                    contains = false;
                    break;
                }
            }
            assertTrue(contains);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public void load(String localName) throws IOException.
     */
    @Test
    public void testLoad() {
        try {
            this.pl.load("DBDriver.properties");
            assertFalse(this.pl.getProperties().isEmpty());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}