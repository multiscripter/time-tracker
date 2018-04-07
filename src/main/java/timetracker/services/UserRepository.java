package timetracker.services;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import timetracker.models.User;
import timetracker.utils.DBDriver;
/**
 * Класс UserRepository реализует шаблон Репозиторий для сущности Пользователь.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
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
	private final String enc;
    /**
     * Логгер.
     */
    private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
    /**
     * Конструктор.
     * @param enc кодировка.
     */
    public UserRepository(final String enc) {
        this.enc = enc;
    }
    /**
	 * Получает хэш-сумму пароля.
	 * @param pass пароль.
     * @return хэш-сумма пароля.
     * @throws java.security.NoSuchAlgorithmException исключение "Нет такого алгоритма".
     * @throws java.io.UnsupportedEncodingException исключение "Кодировка не поддерживается.
	 */
    public String getPassHash(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes(this.enc), 0, pass.length());
        return new BigInteger(1, md.digest()).toString(16);
    }
    /**
     * Получает список элементов по заданному критерию.
     * @param query строка запроса к БД.
     * @return список элементов.
     */
    public LinkedList<User> getUsers(String query) {
        LinkedList<User> list = new LinkedList<>();
        try {
            List<HashMap<String, String>> rl = this.db.select(query);
            if (!rl.isEmpty()) {
                for (HashMap<String, String> entry : rl) {
                    User user = new User();
                    user.setId(Integer.parseInt(entry.get("id")));
                    user.setLogin(entry.get("login"));
                    user.setPass(entry.get("pass"));
                    list.add(user);
                }
            }
        } catch (Exception ex) {
            this.logger.error("ERROR", ex);
        }
        return list;
    }
}