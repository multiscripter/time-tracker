package timetracker.utils;

import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.TimeZone;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import timetracker.models.Mark;
import timetracker.models.Token;
import timetracker.services.MarkDAO;
/**
 * Класс Utils реализует вспомогательные утилиты.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-10
 * @since 2018-04-10
 */
public class Utils {
    /**
     * DAO метки времени.
     */
    private MarkDAO mdao;
    /**
     * Конструктор.
     */
    public Utils() {
        this.mdao = new MarkDAO();
    }
    /**
	 * Обрабатывает.
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
        mark.setToken(token.getToken());
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
}