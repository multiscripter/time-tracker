package timetracker.utils;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.TimeZone;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import timetracker.models.Mark;
import timetracker.models.Token;
import timetracker.services.MarkDAO;
/**
 * Класс Utils реализует вспомогательные утилиты.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-11
 * @since 2018-04-10
 */
public class Utils {
    /**
     * DAO метки времени.
     */
    private MarkDAO mdao;
    /**
     * Логгер.
     */
    private Logger logger = LogManager.getLogger(this.getClass().getName());
    /**
     * Конструктор.
     */
    public Utils() {
        this.mdao = new MarkDAO();
    }
    /**
	 * Добавляет метки из списка в json-объект.
     * @param marks список меток.
     * @param jsonb JSON-строитель.
	 */
    public void addMarksToJson(LinkedList<Mark> marks, JsonObjectBuilder jsonb) {
        JsonArrayBuilder jsonMarks = Json.createArrayBuilder();
        if (!marks.isEmpty()) {
            for (Mark mark : marks) {
                JsonObjectBuilder jsonMark = Json.createObjectBuilder();
                jsonMark.add("wday", mark.getWdayStr());
                jsonMark.add("mark", mark.getMarkStr());
                jsonMark.add("state", mark.getState());
                jsonMarks.add(jsonMark);
            }
        }
        jsonb.add("marks", jsonMarks);
    }
    /**
	 * Получает рабочее время.
     * @param marks список меток.
     * @return массив с временем.
	 */
    public long[] getWorkTime(LinkedList<Mark> marks) {
        int index = marks.getLast().getState() ? marks.size() - 1 : marks.size();
        ListIterator<Mark> iter = marks.listIterator(index);
        long period = 0L;
        while (iter.hasPrevious()) {
            Date end = iter.previous().getMark().getTime();
            Date start = iter.previous().getMark().getTime();
            period += end.getTime() - start.getTime();
        }
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long[] result = new long[3];
        result[0] = period / hoursInMilli;
        period = period % hoursInMilli;
        result[1] = period / minutesInMilli;
        period = period % minutesInMilli;
        result[2] = period / secondsInMilli;
        return result;
    }
    /**
	 * Обрабатывает.
     * @param token токен.
     * @param state состояние.
     * @param methodName имя метода DAO метки времени.
     * @param jsonb JSON-строитель.
     * @param errors массив ошибок.
     * @return true обработка прошла успешно. Иначе false.
     * @throws SQLException исключение SQL.
	 */
    public boolean process(Token token, boolean state, String methodName, JsonObjectBuilder jsonb, JsonObjectBuilder errors) throws SQLException {
        Mark mark = new Mark();
        mark.setUserId(token.getUserId());
        mark.setWday(token.getWday());
        mark.setMark(new GregorianCalendar());
        mark.setState(state);
        boolean result = false;
        if (methodName.equals("create")) {
            result = this.mdao.create(mark);
        } else if (methodName.equals("update")) {
            result = this.mdao.update(mark);
        }
        if (result) {
            TimeZone tz = this.mdao.getTimeZone(token.getUserId());
            GregorianCalendar cal = new GregorianCalendar(tz);
            cal.setTime(mark.getMark().getTime());
            mark.setMark(cal);
            jsonb.add("status", "ok");
            JsonObjectBuilder jsonMark = Json.createObjectBuilder();
            jsonMark.add("wday", mark.getWdayStr());
            jsonMark.add("mark", mark.getMarkStr());
            jsonMark.add("state", mark.getState());
            jsonb.add("mark", jsonMark);
        } else {
            jsonb.add("status", "error");
            errors.add("mark", "notcreated");
        }
        return result;
    }
    /**
	 * Проверяет валидность рабочего дня с учётом часового пояса пользователя.
     * @param token токен.
     * @return true если рабочий и текущий дни - одинаковые. Иначе false.
     * @throws SQLException исключение SQL.
	 */
    public boolean validateWday(Token token) throws SQLException {
        TimeZone tz = this.mdao.getTimeZone(token.getUserId());
        GregorianCalendar today = new GregorianCalendar(tz);
        GregorianCalendar wday = new GregorianCalendar(tz);
        wday.setTime(token.getWday().getTime());
        this.logger.error("today.toString(): " + today.toString());
        this.logger.error("wday.toString(): " + wday.toString());
        wday.setTime(token.getWday().getTime());
        return today.get(Calendar.DAY_OF_MONTH) == wday.get(Calendar.DAY_OF_MONTH);
    }
}