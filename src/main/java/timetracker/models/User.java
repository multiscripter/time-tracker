package timetracker.models;

import java.util.GregorianCalendar;
import java.util.Objects;
/**
 * Класс User реализует сущность Пользоваетель.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-10
 * @since 2018-04-07
 */
public class User {
    /**
	 * Часовой пояс.
	 */
    private String gmt;
	/**
	 * Идентификатор.
	 */
	private int id;
	/**
	 * Логин.
	 */
	private String login;
    /**
	 * Хэш пароля.
	 */
    private String pass;
    /**
	 * Токен.
	 */
    private String token;
    /**
	 * Рабочий день.
	 */
    private GregorianCalendar wday;
	/**
	 * Конструктор без параметров.
	 */
	public User() {
	}
    /**
	 * Конструктор.
     * @param id идентификатор.
     * @param login логин.
     * @param pass хэш пароля.
     * @param token токен.
     * @param wday рабочий день.
     * @param gmt часовой пояс.
	 */
	public User(int id, String login, String pass, String token, GregorianCalendar wday, String gmt) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.token = token;
        this.wday = wday;
        this.gmt = gmt;
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
        User user = (User) obj;
        if (this.id != user.getId() || !this.login.equals(user.getLogin()) || !this.pass.equals(user.getPass()) || !this.gmt.equals(user.getGmt())) {
            return false;
        }
        return true;
    }
    /**
	 * Получает часовой пояс.
	 * @return часовой пояс.
	 */
	public String getGmt() {
		return this.gmt;
	}
	/**
	 * Получает идентификатор.
	 * @return идентификатор.
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * Получает логин.
	 * @return логин.
	 */
	public String getLogin() {
		return this.login;
	}
    /**
	 * Получает хэш пароля.
	 * @return хэш пароля.
	 */
    public String getPass() {
        return this.pass;
    }
    /**
	 * Получает токен.
	 * @return токен.
	 */
    public String getToken() {
        return this.token;
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
        return Objects.hash(this.id, this.login, this.pass, this.gmt);
    }
    /**
	 * Получает часовой пояс.
	 * @param gmt часовой пояс.
	 */
	public void setGmt(String gmt) {
		this.gmt = gmt;
	}
	/**
	 * Устанавливает идентификатор.
	 * @param id идентификатор.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Устанавливает логин.
	 * @param login логин.
	 */
	public void setLogin(final String login) {
		this.login = login;
	}
    /**
	 * Устанавливает пароль.
	 * @param pass пароль.
	 */
    public void setPass(String pass) {
        this.pass = pass;
    }
    /**
	 * Устанавливает токен.
	 * @param token токен.
	 */
    public void setToken(String token) {
        this.token = token;
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
		return String.format("user[id: %d, login: %s, wday: %s, gmt: %s]", this.id, this.login, this.getWdayStr(), this.gmt);
	}
}