package timetracker.utils;

import java.util.Properties;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
/**
 * Класс Tomcat реализует сущность Источник данных (DataSource).
 * Использован apache tomcat database connection pool.
 *
 * @author Gureyev Ilya (mailto:ill-jah@yandex.ru)
 * @version 2018-04-07
 * @since 2018-04-07
 */
public class Tomcat extends BasicDataSource {
    /**
	 * Получает источник данных.
     * @param props свойства бд.
	 */
    public Tomcat(Properties props) {
        super();
        StringBuilder str =  new StringBuilder();
        str.append("jdbc:");
        str.append(props.getProperty("protocol"));
        str.append("://");
        str.append(props.getProperty("src"));
        if (!props.getProperty("port").equals("0")) {
            str.append(":");
            str.append(props.getProperty("port"));
        }
        str.append("/");
        if (props.getProperty("db") != null) {
            str.append(props.getProperty("db"));
        }
        this.setUrl(str.toString());
        this.setUsername(props.getProperty("user"));
        this.setPassword(props.getProperty("pass"));
        this.setMinIdle(Integer.parseInt(props.getProperty("minIdle")));
        this.setMaxIdle(Integer.parseInt(props.getProperty("maxIdle")));
        this.setMaxOpenPreparedStatements(Integer.parseInt(props.getProperty("maxOpenPreparedStatements")));
    }
}