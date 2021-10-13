package com.karankumar.bookproject.template;

public class EmailTemplate {
    private EmailTemplate() {
    }

    public static String getAccountCreateEmailTemplate(String username, String from){
        return String.format("Hello %s, %n%nYour account has been created%n%nBest regards %s", username, from);
    }

    public static String getAccountDeleteEmailTemplate(String username, String from){
        return String.format("Hello %s,%n%nYour account has been deleted.%n%nBest regards %s", username, from);
    }

    public static String getChangePasswordEmailTemplate(String username, String from){
        return String.format("Hello %s,%n%nYour password has been changed.%n%nBest regards %s", username, from);
    }
}
