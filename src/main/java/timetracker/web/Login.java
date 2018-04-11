package timetracker.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import timetracker.models.User;
import timetracker.models.Mark;
import timetracker.models.Token;
import timetracker.services.MarkDAO;
import timetracker.services.TokenDAO;
import timetracker.services.UserRepository;
import timetracker.utils.Utils;
/**
 * Класс Login контроллер авторизации приложения Трэкер времени.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-11
 * @since 2018-04-09
 */
public class Login extends AbstractServlet {
    /**
     * Логгер.
     */
    private Logger logger;
    /**
     * DAO метки времени.
     */
    private MarkDAO mdao;
    /**
     * DAO токена.
     */
    private TokenDAO tdao;
    /**
     * Репозиторий пользователей.
     */
    private UserRepository ur;
    /**
     * Утилиты.
     */
    private Utils utils;
    /**
	 * Инициализатор.
     * @throws javax.servlet.ServletException исключение сервлета.
	 */
	@Override
    public void init() throws ServletException {
    	try {
            super.init();
            this.logger = LogManager.getLogger(this.getClass().getName());
            this.mdao = new MarkDAO();
            this.tdao = new TokenDAO();
            this.ur = new UserRepository();
            this.utils = new Utils();
        } catch (Exception ex) {
			this.logger.error("ERROR", ex);
		}
    }
    /**
	 * Обрабатывает GET-запросы.
	 * http://bot.net:8080/timetracker-1.0/
     * @param req объект запроса.
     * @param resp объект ответа сервера.
     * @throws javax.servlet.ServletException исключение сервлета.
     * @throws java.io.IOException исключение ввода-вывода.
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
    /**
     * Обрабатывает POST-запросы. http://bot.net:8080/timetracker-1.0/create/
     * @param req запрос.
     * @param resp ответ.
     * @throws javax.servlet.ServletException исключение сервлета.
     * @throws java.io.IOException исключение ввода-вывода.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObjectBuilder jsonb = Json.createObjectBuilder();
        JsonObjectBuilder errors = Json.createObjectBuilder();
        try {
            String enc = (String) req.getAttribute("encoding");
            this.tdao.setEncoding(enc);
            this.ur.setEncoding(enc);
            if (req.getParameterMap().containsKey("login") && req.getParameterMap().containsKey("pass")) {
                String login = req.getParameter("login").trim();
                String pass = req.getParameter("pass").trim();
                if (!login.isEmpty() && !pass.isEmpty()) {
                    User user = this.ur.getUserByLogPass(login, pass);
                    if (user != null) {
                        Token token = this.tdao.read("user_id", Integer.toString(user.getId()));
                        if (token != null) {
                            jsonb.add("status", "ok");
                            jsonb.add("token", token.getToken());
                            if (token.getWday() != null) {
                                if (this.utils.validateWday(token)) {
                                    jsonb.add("validwday", true);
                                    LinkedList<Mark> marks = this.mdao.read(token.getToken());
                                    this.utils.addMarksToJson(marks, jsonb);
                                } else {
                                    jsonb.add("validwday", false);
                                }
                            }
                        } else {
                            if (this.tdao.create(user)) {
                                jsonb.add("status", "ok");
                                jsonb.add("token", user.getToken());
                            } else {
                                jsonb.add("status", "error");
                                errors.add("token", "notcreated");
                            }
                        }
                    } else {
                        jsonb.add("status", "error");
                        errors.add("user", "nonexists");
                    }
                } else {
                    jsonb.add("status", "error");
                    if (login.isEmpty()) {
                        errors.add("login", "param-empty");
                    }
                    if (pass.isEmpty()) {
                        errors.add("pass", "param-empty");
                    }
                }
            } else {
                jsonb.add("status", "error");
                if (!req.getParameterMap().containsKey("login")) {
                    errors.add("login", "param-nonexists");
                }
                if (!req.getParameterMap().containsKey("pass")) {
                    errors.add("pass", "param-nonexists");
                }
            }
        } catch (Exception ex) {
            this.logger.error("ERROR", ex);
            errors.add("controller", "exception");
        } finally {
            jsonb.add("errors", errors);
            JsonObject json = jsonb.build();
            StringWriter strWriter = new StringWriter();
            try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
               jsonWriter.writeObject(json);
            }
            String jsonData = strWriter.toString();
            resp.setContentType("application/json");
            PrintWriter writer = new PrintWriter(resp.getOutputStream());
            writer.append(jsonData);
            writer.flush();
        }
    }
    /**
	 * Вызывается при уничтожении сервлета.
	 */
    @Override
    public void destroy() {
        super.destroy();
    }
}