package com.company;

import git.objects.GitProcessor;
import git.objects.User;
import parameters.Settings;
import parameters.Texts;
import utils.Helper;

import java.util.Scanner;

public class Main {
    private static String password;
    private static String login;
    private static String repoName;
    private static String collabaratorName;

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

        User currentUser = GitProcessor.getCurrentUser(login, password);
        currentUser.setRepositories(GitProcessor.getUsersRepositories(currentUser));
        currentUser.printRepositoriesNames();

        while (repoName == null) {
            Helper.printMsg(Texts.INPUT_REPO_NAME_MSG);
            repoName = scanner.nextLine();
            if (!currentUser.isRepositoryOfUser(repoName)) {
                repoName = null;
                Helper.printMsg(Texts.INVALID_INPUT_DATA);
            }
        }

        GitProcessor.getCollaboratorsOfUsersRepo(currentUser, repoName, true);

        while (collabaratorName == null) {
            Helper.printMsg(Texts.INPUT_LOGIN_MSG);
            collabaratorName = Helper.checkInputData(scanner.nextLine(), Settings.LOGIN_REG_EX);
        }

        GitProcessor.addUserAsCollaboratorToRepo(currentUser, repoName, collabaratorName);

    }
}
