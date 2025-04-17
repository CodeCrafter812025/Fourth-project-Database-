package net.holosen.handlers.dbhandler;

import net.holosen.handlers.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static net.holosen.handlers.dbhandler.StudentHandler.getCountOfNotDeletedStudentsByYear;
import static net.holosen.utils.IOUtils.print;

public class CapacityHandler {

    public static Integer loadCapacity() {
        LocalDate date = LocalDate.now();
        try {
            return getYearTotalCapacity(date.getYear());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void showCapacity(int totalCapacity) throws SQLException, ClassNotFoundException {
        LocalDate date = LocalDate.now();
        Integer count = getCountOfNotDeletedStudentsByYear(date.getYear(), totalCapacity);
        print("Remain registration capacity in year " + date.getYear() + " is : " + count);
    }

    private static Integer getYearTotalCapacity(int year) throws SQLException, ClassNotFoundException {
        Integer count = 0;
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        String command = "SELECT cnt FROM public.capacity where year = " + year + ";";
        ResultSet resultSet = statement.executeQuery(command);
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        statement.close();
        connection.close();
        return count;
    }
}
