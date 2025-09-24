package edu.ijse.util;

import java.util.regex.Pattern;

public class RegExChecker {

    // Email must contain @ and domain with at least 2 letters
    public static String emailReg = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Name: only letters and spaces, at least 2 characters
    public static String nameReg = "^[A-Za-z ]{2,50}$";

    // Address: allows letters, numbers, commas, dots, hyphen, slash, and spaces (5–100 chars)
    public static String addressReg = "^[A-Za-z0-9,.\\-\\/ ]{5,100}$";

    // Sri Lankan phone numbers (10 digits, starting with 07)
    public static String phoneReg = "^07[0-9]{8}$";

    // Course name: letters, numbers, spaces (3–100 chars)
    public static String courseNameRegex = "^[A-Za-z0-9 ]{3,100}$";

    // Duration: letters, numbers, spaces (1–20 chars)
    public static String durationRegex = "^[0-9A-Za-z ]{1,20}$";

    // Lesson name: letters, numbers, spaces (3–50 chars)
    public static String lessonNameRegex = "^[A-Za-z0-9 ]{3,50}$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(emailReg, email);
    }

    public static boolean isValidName(String name) {
        return name != null && Pattern.matches(nameReg, name);
    }

    public static boolean isValidAddress(String address) {
        return address != null && Pattern.matches(addressReg, address);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(phoneReg, phone);
    }

    public static boolean isValidCourseName(String courseName) {
        return courseName != null && Pattern.matches(courseNameRegex, courseName);
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && Pattern.matches(durationRegex, duration);
    }

    public static boolean isLessonNameValid(String lessonName) {
        return lessonName != null && Pattern.matches(lessonNameRegex, lessonName);
    }
}
