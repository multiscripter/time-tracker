package timetracker.services;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import timetracker.models.Token;
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
     * Удаляет токен.
     * @param token токен.
     * @return true если токен удалён из бд. Иначе false.
     * @throws SQLException исключение SQL.
     */
    public boolean delete(Token token) throws SQLException {
        boolean result = false;
        String query = String.format("delete from %s where user_id = %d", this.db.getProperty("tbl_tokens"), token.getUserId());
        if (this.db.delete(query) > 0) {
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
	 * Читает токен из бд.
	 * @param fieldName имя поля.
     * @param fieldVal значение поля.
     * @return объект токена.
     * @throws SQLException исключение SQL.
     * @throws java.text.ParseException исключение парсинга.
	 */
    public Token read(String fieldName, String fieldVal) throws SQLException, ParseException {
        String query = String.format("select * from %s where %s = '%s'", this.db.getProperty("tbl_tokens"), fieldName, fieldVal);
        List<HashMap<String, String>> rl = this.db.select(query);
        return this.processResult(rl);
    }
    /**
     * Обрабатывает результат запроса select.
     * @param result результат запроса select.
     * @return объект токена.
     * @throws SQLException исключение SQL.
     * @throws ParseException исключение парсинга.
     */
    private Token processResult(List<HashMap<String, String>> result) throws SQLException, ParseException {
        Token token = null;
        if (!result.isEmpty()) {
        	for (HashMap<String, String> entry : result) {
                token = new Token();
                token.setUserId(Integer.parseInt(entry.get("user_id")));
                token.setToken(entry.get("token"));
                String strWday = entry.get("wday");
                if (strWday != null) {
                    GregorianCalendar calWday = new GregorianCalendar();
                    SimpleDateFormat sdfWday = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateWday = sdfWday.parse(strWday);
                    calWday.setTime(dateWday);
                    token.setWday(calWday);
                }
            }
        }
        return token;
    }
    /**
     * Устанавливает кодировку.
     * @param enc кодировка.
     */
    public void setEncoding(final String enc) {
        this.enc = enc;
    }
    /**
     * Обновляет токен.
     * @param token токен.
     * @return true если токен обновлён в бд. Иначе false.
     * @throws SQLException исключение SQL.
     */
    public boolean update(Token token) throws SQLException {
        boolean result = false;
        GregorianCalendar wday = new GregorianCalendar();
        wday.set(Calendar.HOUR, 0);
        wday.set(Calendar.HOUR_OF_DAY, 0);
        wday.set(Calendar.MINUTE, 0);
        wday.set(Calendar.SECOND, 0);
        wday.set(Calendar.MILLISECOND, 0);
        token.setWday(wday);
        String query = String.format("update %s set wday = '%s' where token = '%s'", this.db.getProperty("tbl_tokens"), token.getWdayStr(), token.getToken());
        if (this.db.update(query) > 0) {
        	result = true;
        } else {
            token.setWday(null);
        }
        return result;
    }
}