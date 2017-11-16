package git.objects;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import parameters.Requests;
import parameters.Settings;
import parameters.Texts;
import parameters.responses.ReposResponse;
import parameters.responses.UsersResponse;
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
        return setBasicUserInformationFromGitResponse(responseBody, user);
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
        return setBasicUserInformationFromGitResponse(responseBody, currentUser);
    }

    public static List<Repository> getUsersRepositories(User user) throws IOException, HttpsExceptions {
        String responseBody = HttpHelper.executeRequest(
                HttpGet.METHOD_NAME,
                Settings.GIT_API_URL + Requests.getApiUsersRepos().replace(Requests.login, user.getLogin()),
                null)
                .toString();
        user.setRepositories(new ArrayList<>());
        List<String> repos = HttpHelper.getJSONObjectsFromArray(responseBody);
        for (String repo : repos) {
            user.addRepo(setBasicRepoInformationFromGitResponse(repo, new Repository()));
        }
        if (repos.size() == 0) {
            Helper.printMsg(Texts.REPOSITORIES_IS_EMPTY_MSG);
        }
        return user.getRepositories();
    }

    public static List<User> getCollaboratorsOfUsersRepo(User user, String repoName, boolean needPrint) throws IOException, HttpsExceptions {
        List<User> collaborators = new ArrayList<>();
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
        List<String> usersJsonObjects = HttpHelper.getJSONObjectsFromArray(responseBody);
        for (String userObject : usersJsonObjects) {
            collaborators.add(setBasicUserInformationFromGitResponse(userObject, new User()));
        }
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

    private static User setBasicUserInformationFromGitResponse(String response, User user) {
        user.setLogin(HttpHelper.getJSONParameterByKey(response, UsersResponse.LOGIN));
        user.setId(Integer.parseInt(HttpHelper.getJSONParameterByKey(response, UsersResponse.ID)));
        user.setAvatar_url(HttpHelper.getJSONParameterByKey(response, UsersResponse.AVATAR_URL));
        user.setGravatar_id(HttpHelper.getJSONParameterByKey(response, UsersResponse.GRAVATAR_ID));
        user.setType(HttpHelper.getJSONParameterByKey(response, UsersResponse.TYPE));
        user.setUrl(HttpHelper.getJSONParameterByKey(response, UsersResponse.URL));
        user.setHtml_url(HttpHelper.getJSONParameterByKey(response, UsersResponse.HTML_URL));
        return user;
    }

    private static Repository setBasicRepoInformationFromGitResponse(String response, Repository repository) {
        repository.setId(Integer.parseInt(HttpHelper.getJSONParameterByKey(response, ReposResponse.ID)));
        repository.setFullName(HttpHelper.getJSONParameterByKey(response, ReposResponse.FULL_NAME));
        repository.setHtmlUrl(HttpHelper.getJSONParameterByKey(response, ReposResponse.HTML_URL));
        repository.setName(HttpHelper.getJSONParameterByKey(response, ReposResponse.NAME));
        repository.setUrl(HttpHelper.getJSONParameterByKey(response, ReposResponse.URL));
        return repository;
    }
}
