package com.company;

import parameters.Texts;

public class Main {
    public static void main(String[] args) {
        printMsg(Texts.WELCOME_MSG);
    }

    private static void printMsg(String message){
        System.out.println(message);
    }
}
