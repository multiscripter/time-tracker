package timetracker.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 * Класс TimeTracker контроллер приложения Трэкер времени.
 *
 * @author Goureev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
 * @since 2018-04-06
 */
public class Index extends AbstractServlet {
    /**
     * Логгер.
     */
    private Logger logger;
    /**
	 * Инициализатор.
     * @throws javax.servlet.ServletException исключение сервлета.
	 */
	@Override
    public void init() throws ServletException {
    	try {
            super.init();
            this.logger = LogManager.getLogger(this.getClass().getSimpleName());
            //this.ir = new ItemRepository();
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
            //List<Item> items = this.ir.getItems(new HashMap<>());
            //req.setAttribute("items", items);
            //req.setAttribute("action", String.format("%s://%s:%s%s%s/create/", req.getScheme(), req.getServerName(), req.getServerPort(), req.getContextPath(), req.getServletPath()));
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
        try {
            String enc = (String) req.getAttribute("encoding");
            String name;
            String descr;
            if (req.getParameter("type").equals("ajax")) {
                name = req.getParameter("item");
                descr = req.getParameter("descr");
            } else {
                name = new String(req.getParameter("item").getBytes("ISO-8859-1"), enc);
                descr = new String(req.getParameter("descr").getBytes("ISO-8859-1"), enc);
            }
            /*
            String createdStr = req.getParameter("created");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            long created = sdf.parse(createdStr).getTime();
            boolean done = Boolean.parseBoolean(req.getParameter("done"));
            Item item = new Item(0, name, descr, created, done);
            int id = this.idao.create(item);
            JsonObjectBuilder jsonb = Json.createObjectBuilder();
            jsonb.add("errors", Json.createArrayBuilder());
            if (id != 0) {
                item.setId(id);
                jsonb.add("status", "ok");
                jsonb.add("id", id);
            } else {
                jsonb.add("status", "error");
            }
            JsonObject json = jsonb.build();
            StringWriter strWriter = new StringWriter();
            try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
               jsonWriter.writeObject(json);
            }
            String jsonData = strWriter.toString();
            resp.setContentType("application/json");
            PrintWriter writer = new PrintWriter(resp.getOutputStream());
            writer.append(jsonData);
            writer.flush();*/
        } catch (Exception ex) {
            this.logger.error("ERROR", ex);
        }
    }
    /**
	 * Вызывается при уничтожении сервлета.
	 *
    @Override
    public void destroy() {
        try {
            this.idao.close();
        } catch (Exception ex) {
			this.logger.error("ERROR", ex);
		}
    }*/
}