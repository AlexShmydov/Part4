package com.company;

import git.objects.GitProcessor;
import git.objects.User;
import parameters.Settings;
import parameters.Texts;
import usersExceptions.HttpsExceptions;
import utils.Helper;

import java.io.IOException;
import java.util.Scanner;

public class MainTask1 {
    private static String password;
    private static String login;
    private static String repoName;
    private static String collaboratorName;

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
        try {
            User currentUser = GitProcessor.getCurrentUser(login, password);
            currentUser.setRepositories(GitProcessor.getUsersRepositories(currentUser));
            Helper.printMsg(String.format(Texts.REPOSITORIES_LIST_MSG,currentUser.getLogin()));
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

            while (collaboratorName == null) {
                Helper.printMsg(Texts.INVITE_TO_MSG);
                collaboratorName = Helper.checkInputData(scanner.nextLine(), Settings.LOGIN_REG_EX);
            }
            GitProcessor.addUserAsCollaboratorToRepo(currentUser, repoName, collaboratorName);
            Helper.printMsg(Texts.WAIT_TO_ACCEPT_INCITE_MSG);
            scanner.nextLine();
            User collaborator = GitProcessor.getUser(collaboratorName);
            GitProcessor.assertIsCollaboratorForRepo(currentUser, collaborator, repoName);
        } catch (IOException | HttpsExceptions e) {
            Helper.printMsg(e.getMessage());
        }
    }
}
