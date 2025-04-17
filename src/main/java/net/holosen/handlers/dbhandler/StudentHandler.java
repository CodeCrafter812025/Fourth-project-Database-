package net.holosen.handlers.dbhandler;

import net.holosen.entities.Student;
import net.holosen.exceptions.StudentNotFoundException;
import net.holosen.handlers.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static net.holosen.utils.IOUtils.getInput;
import static net.holosen.utils.IOUtils.print;

public class StudentHandler {

    public static void showStudentInformation() throws StudentNotFoundException, SQLException, ClassNotFoundException {
        Student student = getStudentByInputNationalCode();
        print(student.toString());
    }

    public static void cancelRegistration() throws StudentNotFoundException, SQLException, ClassNotFoundException {
        Student student = getStudentByInputNationalCode();
        student.setDeleted(true);
        updateData(student);
        print("Student registration canceled.");
    }

    public static void updateStudentData() throws StudentNotFoundException, SQLException, ClassNotFoundException {
        Student student = getStudentByInputNationalCode();
        if (student == null) return;
        print("For editing information please enter new data or enter to ignore.");
        print("First Name : " + student.getFirstName());
        String firstName = getInput();
        if (!firstName.equals(""))
            student.setFirstName(firstName);
        print("Last Name : " + student.getLastName());
        String lastName = getInput();
        if (!lastName.equals(""))
            student.setLastName(lastName);
        print("Address : " + student.getAddress());
        String address = getInput();
        if (!address.equals(""))
            student.setAddress(address);
        updateData(student);
        print("Information updated successfully!");
    }

    public static Student getStudentByInputNationalCode() throws StudentNotFoundException, SQLException, ClassNotFoundException {
        print("Please enter student's national code : ");
        Student student = loadStudentByNationalCode(getInput());
        if (student == null) {
            print("Not found any student with entered national code");
            throw new StudentNotFoundException();
        }
        return student;
    }

    public static void updateData(Student student) throws SQLException, ClassNotFoundException {
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        String command = "UPDATE public.students SET first_name = '" + student.getFirstName() + "', " +
                "last_name = '" + student.getLastName() + "'," +
                "score = " + student.getScore() + "," +
                "address = '" + student.getAddress() + "'," +
                "national_code = '" + student.getNationalCode() + "'," +
                "is_deleted = " + student.getDeleted() +
                " where id = " + student.getId();
        statement.executeUpdate(command);
        connection.commit();
        statement.close();
        connection.close();
    }

    public static Student loadStudentByNationalCode(String input) throws SQLException, ClassNotFoundException {
        Student student = new Student();
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        String command = "SELECT * FROM public.students where national_code = '" + input + "';";
        ResultSet resultSet = statement.executeQuery(command);
        if (resultSet.next()) {
            student.setId(resultSet.getInt("id"));
            student.setFirstName(resultSet.getString("first_name"));
            student.setLastName(resultSet.getString("last_name"));
            student.setScore(resultSet.getFloat("score"));
            student.setStudentNumber(resultSet.getInt("student_number"));
            student.setAddress(resultSet.getString("address"));
            student.setNationalCode(resultSet.getString("national_code"));
            student.setDeleted(resultSet.getBoolean("is_deleted"));
            student.setYear(resultSet.getInt("year"));
        }
        statement.close();
        connection.close();
        return student;
    }

    public static Integer getCountOfNotDeletedStudentsByYear(int year, int capacity) throws SQLException, ClassNotFoundException {
        Integer count = 0;
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        String command = "SELECT count(*) FROM public.students where year = " + year +
                " and is_deleted = false;";
        ResultSet resultSet = statement.executeQuery(command);
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        statement.close();
        connection.close();
        return capacity - count;
    }

    public static void registerStudent() throws SQLException, ClassNotFoundException {
        Student student = new Student();
        print("Please enter first name : ");
        student.setFirstName(getInput());
        print("Please enter last name : ");
        student.setLastName(getInput());
        print("Please enter last year score : ");
        student.setScore(Float.parseFloat(getInput()));
        print("Please enter student number : ");
        student.setStudentNumber(Integer.parseInt(getInput()));
        print("Please enter address : ");
        student.setAddress(getInput());
        print("Please enter national code : ");
        student.setNationalCode(getInput());
        student.setDeleted(false);
        LocalDate date = LocalDate.now();
        student.setYear(date.getYear());
        insertToDB(student);
        print("Student registration succeed!");
    }

    public static void insertToDB(Student student) throws SQLException, ClassNotFoundException {
        Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        String command = "INSERT INTO public.students(" +
                "first_name, last_name, score, student_number, address, national_code, is_deleted, year)" +
                " VALUES ('" + student.getFirstName() + "'," +
                " '" + student.getLastName() + "', " + student.getScore() + ", " +
                student.getStudentNumber() + ", '" + student.getAddress() + "'," +
                " '" + student.getNationalCode() + "', false, " + student.getYear() + ");";
        statement.executeUpdate(command);
        connection.commit();
        statement.close();
        connection.close();
    }

}
