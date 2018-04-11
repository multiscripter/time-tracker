package timetracker.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import timetracker.models.Mark;
import timetracker.models.Token;
import timetracker.services.MarkDAO;
import timetracker.services.TokenDAO;
import timetracker.utils.Utils;
/**
 * Класс TimeTracker контроллер приложения Трэкер времени.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-11
 * @since 2018-04-06
 */
public class Index extends AbstractServlet {
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
		try {
            resp.setContentType("text/html");
            req.setAttribute("indexRef", String.format("%s://%s:%s%s%s/", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), req.getServletPath()));
            req.setAttribute("loginRef", String.format("%s://%s:%s%s%s/login/", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), req.getServletPath()));
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/index.jsp").include(req, resp);
        } catch (Exception ex) {
			this.logger.error("ERROR", ex);
		}
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
            if (req.getParameterMap().containsKey("action") && req.getParameterMap().containsKey("token")) {
                String tokenStr = req.getParameter("token").trim();
                String action = req.getParameter("action").trim();
                if (!action.isEmpty() && !tokenStr.isEmpty()) {
                    Token token = this.tdao.read("token", tokenStr);
                    if (token != null) {
                        LinkedList<Mark> marks = this.mdao.read(token.getToken());
                        if (action.equals("wdaynew")) {
                            if (!this.tdao.update(token)) {
                                jsonb.add("status", "error");
                                errors.add("token", "notupdated");
                            } else {
                                jsonb.add("status", "ok");
                            }
                        } else if (action.equals("wdayold")) {
                            this.utils.addMarksToJson(marks, jsonb);
                            jsonb.add("status", "ok");
                        } else if (action.equals("resume")) {
                            boolean updated = true;
                            if (token.getWday() == null) {
                                updated = this.tdao.update(token);
                            }
                            if (updated) {
                                if (!marks.isEmpty() && marks.getLast().getState()) {
                                    jsonb.add("status", "error");
                                    errors.add("mark", "badseq");
                                } else {
                                    this.utils.process(token, true, "create", jsonb, errors);
                                }
                            } else {
                                jsonb.add("status", "error");
                                errors.add("token", "notupdated");
                            }
                        } else if (action.equals("wait")) {
                            if (marks.isEmpty() || !marks.getLast().getState()) {
                                jsonb.add("status", "error");
                                errors.add("mark", "badseq");
                            } else {
                                this.utils.process(token, false, "create", jsonb, errors);
                            }
                        } else if (action.equals("done")) {
                            if (!marks.isEmpty() && marks.size() > 1) {
                                long[] times = this.utils.getWorkTime(marks);
                                JsonArrayBuilder jsonTimes = Json.createArrayBuilder();
                                for (long item : times) {
                                    jsonTimes.add(item);
                                }
                                jsonb.add("status", "ok");
                                jsonb.add("worktime", jsonTimes);
                            } else {
                                jsonb.add("status", "error");
                                errors.add("done", "notime");
                            }
                        } else {
                            errors.add("action", "param-bad");
                        }
                    } else {
                        jsonb.add("status", "error");
                        errors.add("token", "nonexists");
                    }
                } else {
                    jsonb.add("status", "error");
                    if (action.isEmpty()) {
                        errors.add("action", "param-empty");
                    }
                    if (tokenStr.isEmpty()) {
                        errors.add("token", "param-empty");
                    }
                }
            } else {
                jsonb.add("status", "error");
                if (!req.getParameterMap().containsKey("action")) {
                    errors.add("action", "param-nonexists");
                }
                if (!req.getParameterMap().containsKey("token")) {
                    errors.add("token", "param-nonexists");
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