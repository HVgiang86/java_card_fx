package co.sun.auto.fluter.demofx.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String convertDate(String date) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate.format(outputFormatter);
        } catch (Exception e) {
            return "";
        }
    }

    // Function to convert from String dd/MM/yyyy to yyyy-MM-dd
    public static String convertDate2(String date) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate.format(outputFormatter);
        } catch (Exception e) {
            return "";
        }
    }

    //Function to convert from String yyyy-MM-dd to LocalDate
    public static LocalDate convertStringToLocalDate(String date) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, inputFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    //Function to convert from String dd/MM/yyyy to LocalDate
    public static LocalDate convertStringToLocalDate2(String date) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, inputFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    //convert from LocalDate to String dd/MM/yyyy
    public static String convertLocalDateToString(LocalDate date) {
        try {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(outputFormatter);
        } catch (Exception e) {
            return "";
        }
    }
}
