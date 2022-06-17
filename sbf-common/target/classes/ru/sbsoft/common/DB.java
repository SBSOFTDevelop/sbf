package ru.sbsoft.common;

import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author balandin
 * @since Jan 20, 2014 12:11:44 PM
 */
public class DB {

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignore) {
            }
        }
    }

    public static void close(PreparedStatement prepareStatement) {
        if (prepareStatement != null) {
            try {
                prepareStatement.close();
            } catch (SQLException ignore) {
            }
        }
    }

//    public static void close(Connection connection) {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException ignore) {
//            }
//        }
//    }

    public static long getSingleLong(ResultSet resultSet) throws SQLException {
        checkNext(resultSet);
        final long value = resultSet.getLong(1);
        checkEnd(resultSet);
        return value;
    }

    public static void checkNext(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            throw new SQLException("Unexpected result");
        }
    }

    public static void checkEnd(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            throw new SQLException("Unexpected result");
        }
    }

    public static <T> T getSingleResult(List<T> items, boolean allowNull) {
        if (items == null || items.isEmpty()) {
            if (allowNull) {
                return null;
            }
			throw new IllegalStateException("Empty result");
        }

        final int count = items.size();
		if (count != 1) {
			throw new IllegalStateException("Unexpected result size " + count);
		}
        
        return items.get(0);
    }
}
