package timetracker.services;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import timetracker.models.User;
import timetracker.utils.DBDriver;
/**
 * Класс UserRepository реализует шаблон Репозиторий для сущности Пользователь.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-09
 * @since 2018-04-07
 */
public class UserRepository {
    /**
     * Драйвер бд.
     */
	private final DBDriver db = DBDriver.getInstance();
    /**
     * Кодировка.
     */
	private String enc;
    /**
     * Логгер.
     */
    private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
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
     * Получает список элементов по заданному критерию.
     * @param login логин пользователя.
     * @param pass пароль пользователя.
     * @return список элементов.
     * @throws java.sql.SQLException исключение "SQL".
     * @throws java.security.NoSuchAlgorithmException исключение "Нет такого алгоритма".
     * @throws java.io.UnsupportedEncodingException исключение "Кодировка не поддерживается.
     */
    public User getUserByLogPass(String login, String pass) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String query = String.format("select * from %s where login = '%s' and pass = '%s'", this.db.getProperty("tbl_users"), login, this.getHash(pass));
        User user = null;
        List<HashMap<String, String>> rl = this.db.select(query);
        if (!rl.isEmpty()) {
            for (HashMap<String, String> entry : rl) {
                user = new User();
                user.setId(Integer.parseInt(entry.get("id")));
                user.setLogin(entry.get("login"));
                user.setPass(entry.get("pass"));
            }
        }
        return user;
    }
    /**
     * Устанавливает кодировку.
     * @param enc кодировка.
     */
    public void setEncoding(final String enc) {
        this.enc = enc;
    }
}