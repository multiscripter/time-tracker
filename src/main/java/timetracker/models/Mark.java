package timetracker.models;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
/**
 * Класс Mark реализует сущность Метка времени.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-11
 * @since 2018-04-07
 */
public class Mark {
    /**
	 * Метка времени.
	 */
    private GregorianCalendar mark;
    /**
	 * Состояние.
	 */
	private boolean state;
    /**
	 * Идентификатор пользователя.
	 */
	private int userId;
    /**
	 * Рабочий день.
	 */
    private GregorianCalendar wday;
	/**
	 * Конструктор без параметров.
	 */
	public Mark() {
	}
    /**
	 * Конструктор.
     * @param userId идентификатор пользователя.
     * @param wday рабочий день.
     * @param mark метка времени.
     * @param state состояние.
	 */
	public Mark(final int userId, GregorianCalendar wday, GregorianCalendar mark, final boolean state) {
        this.userId = userId;
        this.wday = wday;
        this.mark = mark;
        this.state = state;
	}
    /**
     * Сравнивает объекты.
     * @param obj целевой объект, с которым сравнивается текущий объект.
     * @return true если объекты равны. Иначе false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Mark other = (Mark) obj;
        return !(this.userId != other.getUserId() || !this.wday.equals(other.getWday()) || !this.mark.equals(other.getMark()) || this.state != other.getState());
    }
    /**
	 * Получает метка времени.
	 * @return метка времени.
	 */
    public GregorianCalendar getMark() {
        return this.mark;
    }
    /**
	 * Получает строковое представление метки времени.
	 * @return строковое представление метки времени.
	 */
	public String getMarkStr() {
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", this.mark);
	}
    /**
	 * Получает состояние.
	 * @return состояние.
	 */
    public boolean getState() {
        return this.state;
    }
    /**
	 * Получает идентификатор пользователя.
	 * @return идентификатор пользователя.
	 */
    public int getUserId() {
        return this.userId;
    }
    /**
	 * Получает рабочий день.
	 * @return рабочий день.
	 */
    public GregorianCalendar getWday() {
        return this.wday;
    }
    /**
	 * Получает строковое представление рабочего дня.
	 * @return строковое представление рабочего дня.
	 */
	public String getWdayStr() {
        return String.format("%1$tY-%1$tm-%1$td", this.wday);
	}
    /**
     * Возвращает хэш-код.
     * @return хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.wday, this.mark, this.state);
    }
    /**
	 * Устанавливает метку времени.
	 * @param mark метка времени.
	 */
    public void setMark(GregorianCalendar mark) {
        mark.set(Calendar.MILLISECOND, 0);
        this.mark = mark;
    }
    /**
	 * Устанавливает состояние.
	 * @param state состояние.
	 */
    public void setState(final boolean state) {
        this.state = state;
    }
    /**
	 * Устанавливает идентификатор пользователя.
	 * @param userId идентификатор пользователя.
	 */
    public void setUserId(final int userId) {
        this.userId = userId;
    }
    /**
	 * Устанавливает рабочий день.
	 * @param wday рабочий день.
	 */
    public void setWday(GregorianCalendar wday) {
        this.wday = wday;
    }
    /**
	 * Возвращает строковое представление.
	 * @return строковое представление.
	 */
	@Override
	public String toString() {
		return String.format("mark[userId: %d, wday: %s, mark: %s, state: %b]", this.userId, this.getWdayStr(), this.getMarkStr(), this.state);
	}
}