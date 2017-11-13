package com.company;

import parameters.Settings;
import parameters.Texts;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String password;
    private static String login;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printMsg(Texts.WELCOME_MSG);
        while (password == null || login == null) {
            if (login == null) {
                printMsg(Texts.INPUT_LOGIN_MSG);
                login = checkInputData(scanner.nextLine(), Settings.loginRegEx);
            } else {
                printMsg(Texts.INPUT_PASSWORD_MSG);
                password = checkInputData(scanner.nextLine(), Settings.passRegEx);
            }
        }
    }

    private static String checkInputData(String inputData, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(inputData.trim());
        if (matcher.find()) {
            return inputData;
        }
        printMsg(Texts.INVALID_INPUT_DATA);
        return null;
    }

    private static void printMsg(String message) {
        System.out.println(message);
    }
}
