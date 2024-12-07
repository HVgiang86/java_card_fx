package co.sun.auto.fluter.demofx.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public static String validateFieldNotEmpty(String field, String fieldName) {
        if (field == null || field.isEmpty()) {
            return "Không được bỏ trống " + fieldName;
        }
        return null;
    }

    public static String validateBirthDate(String birthDate) {
        System.out.println("birthDate: " + birthDate);
        if (birthDate == null || birthDate.isEmpty()) {
            return "Không được bỏ trống ngày sinh";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate date = LocalDate.parse(birthDate, formatter);
            if (date.isAfter(LocalDate.now())) {
                return "Ngày sinh không hợp lệ";
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "Invalid birth date";
        }

        return null;
    }
}
