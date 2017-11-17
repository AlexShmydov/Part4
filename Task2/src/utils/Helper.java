package utils;

import parameters.Texts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static String checkInputData(String inputData, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(inputData.trim());
        if (matcher.find()) {
            return inputData;
        }
        printMsg(Texts.INVALID_INPUT_DATA);
        return null;
    }

    public static void printMsg(String message) {
        System.out.println(message);
    }

}
