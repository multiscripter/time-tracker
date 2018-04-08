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
 * Класс UserTest тестирует класс User.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-08
 * @since 2018-04-08
 */
public class UserTest {
    /**
     * Кодировка.
     */
	private final String enc = Charset.defaultCharset().toString();
    /**
     * Пользователь.
     */
    private User user;
    /**
     * Действия перед тестом.
     */
    @Before
    public void beforeTest() {
        String login = "Тестовой логин";
        String pass = this.getHash("Тестовый пароль");
        GregorianCalendar wday = new GregorianCalendar();
        wday.set(Calendar.MILLISECOND, 0);
        String token = this.getHash(login + wday.toString());
        wday.set(2018, 3, 8, 0, 0, 0);
        this.user = new User(1, login, pass, token, wday);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * Объекты равны.
     */
    @Test
    public void testEqualsObjectAreEqual() {
        String ologin = "Тестовой логин";
        String opass = this.getHash("Тестовый пароль");
        GregorianCalendar owday = new GregorianCalendar();
        owday.set(Calendar.MILLISECOND, 0);
        String otoken = this.getHash(ologin + owday.toString());
        owday.set(2018, 3, 8, 0, 0, 0);
        User other = new User();
        other.setId(1);
        other.setLogin(ologin);
        other.setPass(opass);
        other.setToken(otoken);
        other.setWday(owday);
        assertEquals(this.user, other);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this == obj.
     */
    @Test
    public void testEquals2refsOfSameObject() {
        User actual = this.user;
        assertEquals(this.user, actual);
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * obj == null.
     */
    @Test
    public void testEqualsObjectIsNull() {
        User nullUser = null;
        assertFalse(this.user.equals(nullUser));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.getClass() != obj.getClass().
     */
    @Test
    public void testEqualsDifferentClasses() {
        assertFalse(this.user.equals(new String()));
    }
    /**
     * Тестирует public boolean equals(Object obj).
     * this.id != user.getId().
     */
    @Test
    public void testEqualsDifferentId() {
        String tedfilogin = "Тестовой логин";
        String tedfipass = this.getHash("Тестовый пароль");
        GregorianCalendar tedfiwday = new GregorianCalendar();
        tedfiwday.set(Calendar.MILLISECOND, 0);
        String tedfitoken = this.getHash(tedfilogin + tedfiwday.toString());
        tedfiwday.set(2018, 3, 8, 0, 0, 0);
        User tedfi = new User(0, tedfilogin, tedfipass, tedfitoken, tedfiwday);
        assertFalse(this.user.equals(tedfi));
    }
    /**
     * Тестирует public int getId().
     */
    @Test
    public void testGetId() {
        assertEquals(1, this.user.getId());
    }
    /**
     * Тестирует public int hashCode().
     */
    @Test
    public void testHashCode() {
        String hlogin = "Тестовой логин";
        String hpass = this.getHash("Тестовый пароль");
        GregorianCalendar hwday = new GregorianCalendar();
        hwday.set(Calendar.MILLISECOND, 0);
        String htoken = this.getHash(hlogin + hwday.toString());
        hwday.set(2018, 3, 8, 0, 0, 0);
        int expected = Objects.hash(1, hlogin, hpass, htoken, hwday);
        assertEquals(expected, this.user.hashCode());
    }
    /**
     * Тестирует public String toString().
     */
    @Test
    public void testToString() {
        String expected = "user[id: 1, login: Тестовой логин, wday: 2018-04-08 00:00:00]";
        assertEquals(expected, this.user.toString());
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