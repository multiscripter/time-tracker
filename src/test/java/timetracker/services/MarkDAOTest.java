package timetracker.services;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import timetracker.models.Mark;
import timetracker.testing.DBDriver;
import timetracker.utils.PropertyLoader;
/**
 * Класс MarkDAOTest тестирует класс MarkDAO.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-09
 * @since 2018-04-09
 */
public class MarkDAOTest {
    /**
     * Драйвер бд.
     */
    private DBDriver dbDriver;
    /**
     * Метка времени.
     */
    private Mark mark;
    /**
     * MarkDAO.
     */
    private MarkDAO md;
    /**
     * Свойства бд.
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
        try {
            this.md = new MarkDAO();
            this.ur = new UserRepository();
            this.ur.setEncoding(Charset.defaultCharset().toString());
            PropertyLoader pl = new PropertyLoader("DBDriver.properties");
    		this.props = pl.getProperties();
            Class.forName(this.props.getProperty("dbdriver")).newInstance();
            String url = String.format("jdbc:%s://%s:%s/%s", this.props.getProperty("protocol"), this.props.getProperty("src"), this.props.getProperty("port"), this.props.getProperty("db"));
            this.dbDriver = new DBDriver(url, this.props.getProperty("user"), this.props.getProperty("pass"));
            String path = new File(UserRepositoryTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath() + "/";
            path = path.replaceFirst("^/(.:/)", "$1");
            this.dbDriver.executeSqlScript(path + "../../src/main/resources/" + this.props.getProperty("db_test_data"));
            String tclogin = "testCreate";
            GregorianCalendar tcwday = new GregorianCalendar();
            tcwday.set(Calendar.HOUR, 0);
            tcwday.set(Calendar.HOUR_OF_DAY, 0);
            tcwday.set(Calendar.MINUTE, 0);
            tcwday.set(Calendar.SECOND, 0);
            tcwday.set(Calendar.MILLISECOND, 0);
            String tctoken = this.ur.getHash(tclogin + tcwday.toString());
            GregorianCalendar tctimeMark = new GregorianCalendar();
            tctimeMark.set(Calendar.MILLISECOND, 0);
            this.mark = new Mark(0, tctoken, tcwday, tctimeMark, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean create(Mark mark) throws SQLException.
     */
    @Ignore@Test
    public void testCreate() {
        try {
            assertTrue(this.md.create(this.mark));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean delete(Mark mark) throws SQLException.
     */
    @Ignore@Test
    public void testDelete() {
        try {
            this.md.create(this.mark);
            assertTrue(this.md.delete(this.mark));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public List<Mark> read(final String token) throws SQLException, ParseException.
     */
    @Ignore@Test
    public void testRead() {
        try {
            String trtoken = "13fe018b338e5f605a7eb281ed3134dc";
            List<HashMap<String, String>> expected = this.dbDriver.select(String.format("select * from %s where token = '%s'", this.props.getProperty("tbl_marks"), trtoken));
            List<Mark> actual = this.md.read(trtoken);
            assertEquals(expected.size(), actual.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public List<Mark> read(final String token) throws SQLException, ParseException.
     */
    @Ignore@Test
    public void testReadNoMarks() {
        try {
            List<Mark> marks = this.md.read(this.mark.getToken());
            assertEquals(0, marks.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Тестирует public boolean update(Mark mark) throws SQLException.
     */
    @Ignore@Test
    public void testUpdate() {
        try {
            this.md.create(this.mark);
            this.mark.setMark(new GregorianCalendar());
            assertTrue(this.md.update(this.mark));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}