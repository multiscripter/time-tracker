package timetracker.models;

import java.util.Objects;
/**
 * Класс User реализует сущность Пользоваетель.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
 * @since 2018-04-07
 */
public class User {
	/**
	 * Идентификатор.
	 */
	private int id = 0;
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
	 * Конструктор.
	 */
	public User() {
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
        if (this.id != user.getId() || !this.login.equals(user.getLogin())) {
            return false;
        }
        return true;
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
     * Возвращает хэш-код.
     * @return хэш-код.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.login);
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
	 * Возвращает строковое представление.
	 * @return строковое представление.
	 */
	@Override
	public String toString() {
		return String.format("user[id: %d, login: %s]", this.id, this.login);
	}
}