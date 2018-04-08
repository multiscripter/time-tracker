package timetracker.models;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Класс MarkTest тестирует класс Mark.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-08
 * @since 2018-04-08
 */
public class MarkTest {
    /**
     * Кодировка.
     */
	private final String enc = Charset.defaultCharset().toString();
    /**
     * Мета времени.
     */
    private Mark mark;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        int id = 1;
        String login = "Тестовой логин";
        GregorianCalendar wday = new GregorianCalendar();
        wday.set(Calendar.HOUR, 0);
        wday.set(Calendar.HOUR_OF_DAY, 0);
        wday.set(Calendar.MINUTE, 0);
        wday.set(Calendar.SECOND, 0);
        wday.set(Calendar.MILLISECOND, 0);
        String token = this.getHash(login + wday.toString());
        GregorianCalendar timeMark = new GregorianCalendar();
        timeMark.set(Calendar.MILLISECOND, 0);
        this.mark = new Mark(id, token, wday, timeMark, false);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * Объекты равны.
     */
    @Test
    public void testEqualsObjectAreEqual() {
        GregorianCalendar owday = new GregorianCalendar();
        owday.set(Calendar.HOUR, 0);
        owday.set(Calendar.HOUR_OF_DAY, 0);
        owday.set(Calendar.MINUTE, 0);
        owday.set(Calendar.SECOND, 0);
        owday.set(Calendar.MILLISECOND, 0);
        String otoken = this.getHash("Тестовой логин" + owday.toString());
        GregorianCalendar omark = new GregorianCalendar();
        omark.set(Calendar.MILLISECOND, 0);
        Mark other = new Mark();
        other.setUserId(1);
        other.setToken(otoken);
        other.setWday(owday);
        other.setMark(omark);
        other.setState(false);
        assertEquals(this.mark, other);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this == obj.
     */
    @Test
    public void testEquals2refsOfSameObject() {
        Mark actual = this.mark;
        assertEquals(this.mark, actual);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * obj == null.
     */
    @Test
    public void testEqualsObjectIsNull() {
        Mark nullMark = null;
        assertFalse(this.mark.equals(nullMark));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.getClass() != obj.getClass().
     */
    @Test
    public void testEqualsDifferentClasses() {
        assertFalse(this.mark.equals(new String()));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.userId != other.getUserId().
     */
    @Test
    public void testEqualsDifferentUserId() {
        Mark tedui = new Mark(2, "asdasd", new GregorianCalendar(), new GregorianCalendar(), false);
        assertFalse(this.mark.equals(tedui));
    }
    /**
     * Тестирует public String getMarkStr().
     */
    @Test
    public void testGetMarkStr() {
        GregorianCalendar tgms = new GregorianCalendar();
        tgms.set(Calendar.MILLISECOND, 0);
        String expected = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", tgms);
        assertEquals(expected, this.mark.getMarkStr());
    }
    /**
     * Тестирует public String getWdayStr().
     */
    @Test
    public void testGetWdayStr() {
        GregorianCalendar tgws = new GregorianCalendar();
        tgws.set(Calendar.HOUR, 0);
        tgws.set(Calendar.HOUR_OF_DAY, 0);
        tgws.set(Calendar.MINUTE, 0);
        tgws.set(Calendar.SECOND, 0);
        tgws.set(Calendar.MILLISECOND, 0);
        String expected = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", tgws);
        assertEquals(expected, this.mark.getWdayStr());
    }
    /**
     * Тестирует public int hashCode().
     */
    @Test
    public void testHashCode() {
        GregorianCalendar thcwday = new GregorianCalendar();
        thcwday.set(Calendar.HOUR, 0);
        thcwday.set(Calendar.HOUR_OF_DAY, 0);
        thcwday.set(Calendar.MINUTE, 0);
        thcwday.set(Calendar.SECOND, 0);
        thcwday.set(Calendar.MILLISECOND, 0);
        String thcwdaytoken = this.getHash("Тестовой логин" + thcwday.toString());
        GregorianCalendar thcmark = new GregorianCalendar();
        thcmark.set(Calendar.MILLISECOND, 0);
        int expected = Objects.hash(1, thcwdaytoken, thcwday, thcmark, false);
        assertEquals(expected, this.mark.hashCode());
    }
    /**
     * Тестирует public String toString().
     */
    @Test
    public void testToString() {
        GregorianCalendar ttswday = new GregorianCalendar();
        ttswday.set(Calendar.HOUR, 0);
        ttswday.set(Calendar.HOUR_OF_DAY, 0);
        ttswday.set(Calendar.MINUTE, 0);
        ttswday.set(Calendar.SECOND, 0);
        ttswday.set(Calendar.MILLISECOND, 0);
        String ttswdaytoken = this.getHash("Тестовой логин" + ttswday.toString());
        GregorianCalendar ttsmark = new GregorianCalendar();
        ttsmark.set(Calendar.MILLISECOND, 0);
        String expected = String.format("mark[userId: 1, token: %s, wday: %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS, mark: %3$tY-%3$tm-%3$td %3$tH:%3$tM:%3$tS, state false]", ttswdaytoken, ttswday, ttsmark);
        assertEquals(expected, this.mark.toString());
    }
    /**
	 * Получает хэш-сумму строки.
	 * @param str строка.
     * @return хэш-сумма строки.
	 */
    private String getHash(String str) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(this.enc), 0, str.length());
            result = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}