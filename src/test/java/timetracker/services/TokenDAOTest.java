package timetracker.services;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import org.junit.Before;
//import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import timetracker.models.Token;
import timetracker.models.User;
import timetracker.testing.DBDriver;
import timetracker.utils.PropertyLoader;
/**
 * Класс TokenDAOTest тестирует класс TokenDAO.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-12
 * @since 2018-04-12
 */
public class TokenDAOTest {
    /**
     * Драйвер бд.
     */
    private DBDriver dbDriver;
    /**
     * Свойства бд.
     */
    private Properties props;
    /**
     * TokenDAO.
     */
    private TokenDAO td;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        try {
            this.td = new TokenDAO();
            this.td.setEncoding(Charset.defaultCharset().toString());
            PropertyLoader pl = new PropertyLoader("DBDriver.properties");
    		this.props = pl.getProperties();
            Class.forName(this.props.getProperty("dbdriver")).newInstance();
            String url = String.format("jdbc:%s://%s:%s/%s", this.props.getProperty("protocol"), this.props.getProperty("src"), this.props.getProperty("port"), this.props.getProperty("db"));
            this.dbDriver = new DBDriver(url, this.props.getProperty("user"), this.props.getProperty("pass"));
            String path = new File(UserRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "/";
            path = path.replaceFirst("^/(.:/)", "$1");
            this.dbDriver.executeSqlScript(path + "../../src/main/resources/" + this.props.getProperty("db_test_data"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean create(User user) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException.
     */
    @Test
    public void testCreate() {
        try {
            GregorianCalendar calWday = new GregorianCalendar();
            SimpleDateFormat sdfWday = new SimpleDateFormat("yyyy-MM-dd");
            Date dateWday = sdfWday.parse("2018-04-10");
            calWday.setTime(dateWday);
            User user = new User(3, "test_user3", "0d275885fea21ff4263bcba2fb75fffe", "test_token_user_3", calWday, "+0");
            assertTrue(this.td.create(user));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean delete(Token token) throws SQLException.
     */
    @Test
    public void testDelete() {
        try {
            GregorianCalendar tdWday = new GregorianCalendar();
            SimpleDateFormat tdSdfWday = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTdWday = tdSdfWday.parse("2018-04-10");
            tdWday.setTime(dateTdWday);
            Token token = new Token(2, "test_token_user_2", tdWday);
            assertTrue(this.td.delete(token));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public Token read(String fieldName, String fieldVal) throws SQLException, ParseException.
     */
    @Test
    public void testRead() {
        try {
            GregorianCalendar tdWday = new GregorianCalendar();
            SimpleDateFormat tdSdfWday = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTdWday = tdSdfWday.parse("2018-04-12");
            tdWday.setTime(dateTdWday);
            Token expected = new Token(2, "test_token_user_2", tdWday);
            assertEquals(expected, this.td.read("user_id", "2"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean update(Token token) throws SQLException.
     */
    @Test
    public void testUpdate() {
        try {
            GregorianCalendar tuWday = new GregorianCalendar();
            SimpleDateFormat tdSdfWday = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTdWday = tdSdfWday.parse("2018-04-13");
            tuWday.setTime(dateTdWday);
            Token tuToken = new Token(2, "test_token_user_2", tuWday);
            assertTrue(this.td.update(tuToken));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean update(Token token) throws SQLException.
     * Ни одна запись не обновлена.
     */
    @Test
    public void testUpdateNothingUpdated() {
        try {
            GregorianCalendar tuLoWday = new GregorianCalendar();
            SimpleDateFormat tdSdfWday = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTdWday = tdSdfWday.parse("2018-04-12");
            tuLoWday.setTime(dateTdWday);
            Token tuToken = new Token(3, "test_token_user_3", tuLoWday);
            assertFalse(this.td.update(tuToken));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}