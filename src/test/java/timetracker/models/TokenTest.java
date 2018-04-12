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
 * Класс TokenTest тестирует класс Token.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-12
 * @since 2018-04-10
 */
public class TokenTest {
    /**
     * Кодировка.
     */
	private final String enc = Charset.defaultCharset().toString();
    /**
     * Мета времени.
     */
    private Token token;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        int userId = 1;
        String login = "Тестовой логин";
        GregorianCalendar wday = new GregorianCalendar();
        wday.set(Calendar.HOUR, 0);
        wday.set(Calendar.HOUR_OF_DAY, 0);
        wday.set(Calendar.MINUTE, 0);
        wday.set(Calendar.SECOND, 0);
        wday.set(Calendar.MILLISECOND, 0);
        String token = this.getHash(login + wday.toString());
        this.token = new Token(userId, token, wday);
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
        Token other = new Token();
        other.setUserId(1);
        other.setToken(otoken);
        other.setWday(owday);
        assertEquals(this.token, other);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this == obj.
     */
    @Test
    public void testEquals2refsOfSameObject() {
        Token actual = this.token;
        assertEquals(this.token, actual);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * obj == null.
     */
    @Test
    public void testEqualsObjectIsNull() {
        Token nullToken = null;
        assertFalse(this.token.equals(nullToken));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.getClass() != obj.getClass().
     */
    @Test
    public void testEqualsDifferentClasses() {
        assertFalse(this.token.equals(new String()));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.userId != other.getUserId().
     */
    @Test
    public void testEqualsDifferentUsers() {
        Token tedu = new Token(2, "a0f88cc778f5b4582da52b18d76ed5d8", new GregorianCalendar());
        assertFalse(this.token.equals(tedu));
    }
    /**
     * Тестирует public GregorianCalendar getWday().
     */
    @Test
    public void testGetWday() {
        GregorianCalendar expected = new GregorianCalendar();
        expected.set(Calendar.HOUR, 0);
        expected.set(Calendar.HOUR_OF_DAY, 0);
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.SECOND, 0);
        expected.set(Calendar.MILLISECOND, 0);
        assertEquals(expected, this.token.getWday());
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
        assertEquals(expected, this.token.getWdayStr());
    }
    /**
     * Тестирует public int hashCode().
     */
    @Test
    public void testHashCode() {
        GregorianCalendar thc = new GregorianCalendar();
        thc.set(Calendar.HOUR, 0);
        thc.set(Calendar.HOUR_OF_DAY, 0);
        thc.set(Calendar.MINUTE, 0);
        thc.set(Calendar.SECOND, 0);
        thc.set(Calendar.MILLISECOND, 0);
        String thctoken = this.getHash("Тестовой логин" + thc.toString());
        int expected = Objects.hash(1, thctoken);
        assertEquals(expected, this.token.hashCode());
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
        String expected = String.format("token[userId: 1, token: %s, wday: %2$tY-%2$tm-%2$td %2$tH:%2$tM:%2$tS]", ttswdaytoken, ttswday);
        assertEquals(expected, this.token.toString());
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