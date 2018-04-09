package timetracker.services;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import timetracker.models.User;
import timetracker.utils.DBDriver;
/**
 * Класс TokenDAO реализует шаблон Репозиторий для сущности Токен.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-09
 * @since 2018-04-09
 */
public class TokenDAO {
    /**
     * Кодировка.
     */
	private String enc;
    /**
     * Драйвер бд.
     */
	private final DBDriver db = DBDriver.getInstance();
    /**
     * Добавляет токен.
     * @param user пользователь.
     * @return true если токен добавлен в бд. Иначе false.
     * @throws SQLException исключение SQL.
     * @throws java.security.NoSuchAlgorithmException исключение "Нет такого алгоритма".
     * @throws java.io.UnsupportedEncodingException исключение "Кодировка не поддерживается.
     */
    public boolean create(User user) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        boolean result = false;
        GregorianCalendar tmp = new GregorianCalendar();
        String token = this.getHash(user.getLogin() + tmp.toString());
        String query = String.format("insert into %s (user_id, token) values (%d, '%s')", this.db.getProperty("tbl_tokens"), user.getId(), token);
        HashMap<String, Integer> entries = this.db.insert(query);
        if (entries.get("affected") > 0) {
            user.setToken(token);
            result = true;
        }
        return result;
    }
    /**
	 * Получает хэш-сумму строки.
	 * @param str строка.
     * @return хэш-сумма строки.
     * @throws java.security.NoSuchAlgorithmException исключение "Нет такого алгоритма".
     * @throws java.io.UnsupportedEncodingException исключение "Кодировка не поддерживается.
	 */
    public String getHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(this.enc), 0, str.length());
        return new BigInteger(1, md.digest()).toString(16);
    }
    /**
     * Устанавливает кодировку.
     * @param enc кодировка.
     */
    public void setEncoding(final String enc) {
        this.enc = enc;
    }
}