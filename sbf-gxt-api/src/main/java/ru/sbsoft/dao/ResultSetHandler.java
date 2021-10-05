package ru.sbsoft.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс представляет метод <code> process </code> бросающий исключение <code>InterruptedException</code>
 * при прервывании потока в котором идет загрузка {@code ResultSet}.
 * @author balandin
 * @since Jun 5, 2014 5:22:46 PM
 */
public interface ResultSetHandler {

	void process(ResultSet resultSet) throws InterruptedException, SQLException;

}
