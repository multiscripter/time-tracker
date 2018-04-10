package timetracker.models;

import java.util.GregorianCalendar;
import java.util.Objects;
/**
 * Класс Token реализует сущность Токен.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-10
 * @since 2018-04-10
 */
public class Token {
    /**
	 * Токен.
	 */
    private String token;
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
	public Token() {
	}
    /**
	 * Конструктор.
     * @param userId идентификатор пользователя.
     * @param token токен.
     * @param wday рабочий день.
	 */
	public Token(final int userId, final String token, GregorianCalendar wday) {
        this.userId = userId;
        this.token = token;
        this.wday = wday;
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
        Token other = (Token) obj;
        return !(this.userId != other.getUserId() || !this.token.equals(other.getToken()));
    }
    /**
	 * Получает токен.
	 * @return токен.
	 */
    public String getToken() {
        return this.token;
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
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", this.wday);
	}
    /**
     * Возвращает хэш-код.
     * @return хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.token);
    }
    /**
	 * Устанавливает токен.
	 * @param token метка времени.
	 */
    public void setToken(final String token) {
        this.token = token;
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
		return String.format("token[userId: %d, token: %s, wday: %s]", this.userId, this.token, this.getWdayStr());
	}
}