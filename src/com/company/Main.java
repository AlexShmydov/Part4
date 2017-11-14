package com.company;

import parameters.Settings;
import parameters.Texts;
import utils.Helper;

import java.util.Scanner;

public class Main {
    private static String password;
    private static String login;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Helper.printMsg(Texts.WELCOME_MSG);
        while (password == null || login == null) {
            if (login == null) {
                Helper.printMsg(Texts.INPUT_LOGIN_MSG);
                login = Helper.checkInputData(scanner.nextLine(), Settings.LOGIN_REG_EX);
            } else {
                Helper.printMsg(Texts.INPUT_PASSWORD_MSG);
                password = Helper.checkInputData(scanner.nextLine(), Settings.PASS_REG_EX);
            }
        }
    }
}
