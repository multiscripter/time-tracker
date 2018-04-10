package timetracker.services;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TimeZone;
import timetracker.models.Mark;
import timetracker.utils.DBDriver;

/**
 * Класс MarkDAO реализует слой DAO для сущности Метка времени.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-09
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
     * @return true если запись с меткой времени добавлена в бд. Иначе false.
     * @throws SQLException исключение SQL.
     */
    public boolean create(Mark mark) throws SQLException {
        boolean result = false;
        String query = String.format("insert into %s (user_id, token, wday, mark, state) values (%d, '%s', '%s', '%s', %s)", this.db.getProperty("tbl_marks"), mark.getUserId(), mark.getToken(), mark.getWdayStr(), mark.getMarkStr(), mark.getState());
        HashMap<String, Integer> entries = this.db.insert(query);
        if (entries.get("affected") > 0) {
            result = true;
        }
        return result;
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
    public LinkedList<Mark> read(final String token) throws SQLException, ParseException {
        String query = String.format("select * from %s where token = '%s' order by mark", this.db.getProperty("tbl_marks"), token);
        LinkedList<HashMap<String, String>> rl = this.db.select(query);
        return this.processResult(rl);
    }
    /**
     * Обрабатывает результат запроса select.
     * @param result результат запроса select.
     * @return коллекция записей.
     * @throws SQLException исключение SQL.
     * @throws ParseException исключение парсинга.
     */
    private LinkedList<Mark> processResult(LinkedList<HashMap<String, String>> result) throws SQLException, ParseException {
        LinkedList<Mark> marks = new LinkedList<>();
        if (!result.isEmpty()) {
            int userId = Integer.parseInt(result.getFirst().get("user_id"));
            TimeZone tz = this.getTimeZone(userId);
            String token = result.getFirst().get("token");
        	for (HashMap<String, String> entry : result) {
                GregorianCalendar calWday = new GregorianCalendar();
        		SimpleDateFormat sdfWday = new SimpleDateFormat("yyyy-MM-dd");
        		String strWday = entry.get("wday");
        		Date dateWday = sdfWday.parse(strWday);
        		calWday.setTime(dateWday);
                GregorianCalendar calMark = new GregorianCalendar(tz);
        		SimpleDateFormat sdfMark = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String strMark = entry.get("mark");
        		Date dateMark = sdfMark.parse(strMark);
        		calMark.setTime(dateMark);
                boolean state = entry.get("state").equals("1");
                marks.add(new Mark(userId, token, calWday, calMark, state));
            }
        }
        return marks;
    }
    /**
     * Получает часовой пояс.
     * @param userId .
     * @return часовой пояс.
     * @throws SQLException исключение SQL.
     */
    public TimeZone getTimeZone(int userId) throws SQLException {
        LinkedList<HashMap<String, String>> rl = this.db.select(String.format("select gmt from %s where id = %d", this.db.getProperty("tbl_users"), userId));
        String gmt = rl.getFirst().get("gmt");
        String tzStr = String.format("GMT%s", gmt);
        return TimeZone.getTimeZone(tzStr);
    }
    /**
     * Обновляет метку времени.
     * @param mark запись с меткой времени.
     * @return true если запись с меткой времени обновлена в бд. Иначе false.
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