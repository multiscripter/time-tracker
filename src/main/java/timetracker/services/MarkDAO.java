package timetracker.services;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import timetracker.models.Mark;
import timetracker.utils.DBDriver;

/**
 * Класс MarkDAO реализует слой DAO для сущности Метка времени.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
 * @since 2018-04-07
 */
public class MarkDAO {
    /**
     * Драйвер бд.
     */
	private final DBDriver db = DBDriver.getInstance();
    /**
     * Добавляет запись с меткой времени.
     * @param mark запись с меткой времени.
     * @throws SQLException исключение SQL.
     */
    public void create(Mark mark) throws SQLException {
        String query = String.format("insert into %s (user_id, token, state) values (%d, '%s', '%s', true)", this.db.getProperty("tbl_marks"), mark.getUserId(), mark.getToken(), mark.getState());
        this.db.insert(query);
    }
    /**
     * Удаляет запись с меткой времени.
     * @param mark запись с меткой времени.
     * @return true если запись с меткой времени удалёна из бд. Иначе false.
     * @throws SQLException исключение SQL.
     */
    public boolean delete(Mark mark) throws SQLException {
        boolean result = false;
        String query = String.format("delete from %s where token = '%s' and mark = '%s'", this.db.getProperty("tbl_marks"), mark.getToken(), mark.getMarkStr());
        if (this.db.delete(query) > 0) {
        	result = true;
        }
        return result;
    }
    /**
     * Получает все записи с меткой времени по токену.
     * @param token токен записей.
     * @return список записей.
     * @throws SQLException исключение SQL.
     * @throws ParseException исключение парсинга.
     */
    public List<Mark> read(final String token) throws SQLException, ParseException {
        String query = String.format("select * from %s where token = '%s' order by mark", this.db.getProperty("tbl_marks"), token);
        List<HashMap<String, String>> rl = this.db.select(query);
        return this.processResult(rl);
    }
    /**
     * Обрабатывает результат запроса select.
     * @param result результат запроса select.
     * @return коллекция записей.
     * @throws SQLException исключение SQL.
     * @throws ParseException исключение парсинга.
     */
    private List<Mark> processResult(List<HashMap<String, String>> result) throws SQLException, ParseException {
        List<Mark> marks = new LinkedList<>();
        if (!result.isEmpty()) {
        	for (HashMap<String, String> entry : result) {
                int userId = Integer.parseInt(entry.get("user_id"));
                String token = entry.get("token");
                GregorianCalendar cal = new GregorianCalendar();
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String strDate = entry.get("mark");
        		Date date = sdf.parse(strDate);
        		cal.setTime(date);
                boolean state = entry.get("token").equals("1");
                marks.add(new Mark(userId, token, cal, state));
            }
        }
        return marks;
    }
    /**
     * Обновляет метку времени.
     * @param mark запись с меткой времени.
     * @return true если запись с меткой времени обновлёна в бд. Иначе false.
     * @throws SQLException исключение SQL.
     */
    public boolean update(Mark mark) throws SQLException {
        boolean result = false;
        String query = String.format("update %s set mark = current_timestamp where token = '%s' and mark = '%s'", this.db.getProperty("tbl_marks"), mark.getToken(), mark.getMarkStr());
        if (this.db.update(query) > 0) {
        	result = true;
        }
        return result;
    }
}