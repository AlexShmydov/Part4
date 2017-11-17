package git.objects;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import parameters.Requests;
import parameters.Settings;
import parameters.Texts;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import usersExceptions.HttpsExceptions;
import utils.Helper;
import utils.HttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitProcessor {

    public static User getUser(String userName) throws IOException, HttpsExceptions {
        User user = new User(userName);
        String responseBody = HttpHelper.executeRequest(
                HttpGet.METHOD_NAME,
                Settings.GIT_API_URL + Requests.getApiUsers().replace(Requests.login, userName),
                null)
                .toString();
        return getUserFromJSON(responseBody);
    }

    public static User getCurrentUser(String login, String password) throws IOException, HttpsExceptions {
        User currentUser = new User(login, password);
        Map<String, String> headers = new HashMap<>();
        headers.put(Settings.HEADER_AUTHORIZATION,
                String.format(Settings.HEADER_AUTHORIZATION_VALUE, currentUser.getBase64Authorization()));
        String responseBody = HttpHelper.executeRequest(
                HttpGet.METHOD_NAME,
                Settings.GIT_API_URL + Requests.getApiUsers().replace(Requests.login, login),
                headers)
                .toString();
        User user = getUserFromJSON(responseBody);
        user.setPassword(password);
        return user;
    }

    public static List<Repository> getUsersRepositories(User user) throws IOException, HttpsExceptions {
        String responseBody = HttpHelper.executeRequest(
                HttpGet.METHOD_NAME,
                Settings.GIT_API_URL + Requests.getApiUsersRepos().replace(Requests.login, user.getLogin()),
                null)
                .toString();
        user.setRepositories(new ArrayList<>());
        user.setRepositories(getAllRepositoriesFromJSON(responseBody));
        if (user.getRepositories().size() == 0) {
            Helper.printMsg(Texts.REPOSITORIES_IS_EMPTY_MSG);
        }
        return user.getRepositories();
    }

    public static List<User> getCollaboratorsOfUsersRepo(User user, String repoName, boolean needPrint) throws IOException, HttpsExceptions {
        Map<String, String> headers = new HashMap<>();
        headers.put(Settings.HEADER_AUTHORIZATION,
                String.format(Settings.HEADER_AUTHORIZATION_VALUE, user.getBase64Authorization()));
        String responseBody = HttpHelper.executeRequest(
                HttpGet.METHOD_NAME,
                Settings.GIT_API_URL + Requests.getApiGetCollaborators()
                        .replace(Requests.login, user.getLogin())
                        .replace(Requests.repo, repoName),
                headers)
                .toString();
        List<User> collaborators = getAllUsersFromJSON(responseBody);
        if (needPrint) {
            if (collaborators.size() != 0) {
                Helper.printMsg(Texts.COLLABORATORS_OF_YOUR_REPO_MSG);
                collaborators.stream().forEach(collaborator -> Helper.printMsg(
                        String.format(Texts.COLLABORATORS_TEMPLATE_MSG, collaborator.getLogin(), collaborator.getId())));
            } else {
                Helper.printMsg(Texts.COLLABORATORS_IS_NULL_MSG);
            }
        }
        return collaborators;
    }

    public static void addUserAsCollaboratorToRepo(User user, String repoName, String collaboratorName) throws IOException {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(Settings.HEADER_AUTHORIZATION,
                    String.format(Settings.HEADER_AUTHORIZATION_VALUE, user.getBase64Authorization()));

            HttpHelper.executeRequest(
                    HttpPut.METHOD_NAME,
                    Settings.GIT_API_URL + Requests.getApiAddCollaboratorToRepo()
                            .replace(Requests.login, user.getLogin())
                            .replace(Requests.repo, repoName)
                            .replace(Requests.userName, collaboratorName),
                    headers)
                    .toString();
            Helper.printMsg(String.format(Texts.INVITE_RESULT_SUCCESSFUL_MSG, collaboratorName));
        } catch (HttpsExceptions e) {
            Helper.printMsg(String.format(Texts.INVITE_RESULT_FAILED_MSG, collaboratorName));
        }
    }

    public static void assertIsCollaboratorForRepo(User currentUser, User collaborator, String repoName) throws IOException, HttpsExceptions {
        boolean isCollaboratorExist = false;
        List<User> collaborators = getCollaboratorsOfUsersRepo(currentUser, repoName, false);
        for (User user : collaborators) {
            if (user.getId() == collaborator.getId()) {
                isCollaboratorExist = true;
            }
        }
        if (isCollaboratorExist) {
            Helper.printMsg(String.format(Texts.COLLABORATOR_IS_TRUE_FOR_REPO_MSG, collaborator.getLogin(), repoName));
        } else {
            Helper.printMsg(String.format(Texts.COLLABORATOR_IS_FALSE_FOR_REPO_MSG, collaborator.getLogin(), repoName));
        }
    }

    private static User getUserFromJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, User.class);
    }

    public static List<User> getAllUsersFromJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User> repositories = mapper.readValue(json, new TypeReference<List<User>>() {
        });
        return repositories;
    }

    public static List<Repository> getAllRepositoriesFromJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Repository> repositories = mapper.readValue(json, new TypeReference<List<Repository>>() {
        });
        return repositories;
    }
}
