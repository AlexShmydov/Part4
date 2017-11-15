package git.objects;

import parameters.Requests;
import parameters.Settings;
import parameters.Texts;
import parameters.responses.ReposResponse;
import parameters.responses.UsersResponse;
import utils.Helper;
import utils.HttpHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitProcessor {

    public void getUser(String userName) {

    }

    public static User getCurrentUser(String password, String login) {
        User currentUser = new User(login, password);
        Map<String, String> headers = new HashMap<>();
        headers.put(Settings.HEADER_AUTHORIZATION,
                String.format(Settings.HEADER_AUTHORIZATION_VALUE, currentUser.getBase64Authorization()));
        try {
            String responseBody = HttpHelper.executeGetRequest(
                    Settings.GIT_API_URL + Requests.getApiUsers().replace(Requests.login, login),
                    headers)
                    .toString();
            return setBasicUserInformationFromGitResponse(responseBody, currentUser);
        } catch (IOException e) {
            Helper.printMsg(e.getMessage());
        }
        return null;
    }

    private static User setBasicUserInformationFromGitResponse(String response, User user) {
        if (user.getLogin() == null) {
            user.setLogin(HttpHelper.getJSONParameterByKey(response, UsersResponse.LOGIN));
        }
        user.setId(Integer.parseInt(HttpHelper.getJSONParameterByKey(response, UsersResponse.ID)));
        user.setAvatar_url(HttpHelper.getJSONParameterByKey(response, UsersResponse.AVATAR_URL));
        user.setGravatar_id(HttpHelper.getJSONParameterByKey(response, UsersResponse.GRAVATAR_URL));
        user.setType(HttpHelper.getJSONParameterByKey(response, UsersResponse.TYPE));
        user.setUrl(HttpHelper.getJSONParameterByKey(response, UsersResponse.URL));
        user.setHtml_url(HttpHelper.getJSONParameterByKey(response, UsersResponse.HTML_URL));
        user.setCollaborators(Integer.parseInt(HttpHelper.getJSONParameterByKey(response, UsersResponse.COLLABORATORS)));
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

    public static List<Repository> getUsersRepositories(User user) {
        try {
            String responseBody = HttpHelper.executeGetRequest(
                    Settings.GIT_API_URL + Requests.getApiUsersRepos().replace(Requests.login, user.getLogin()))
                    .toString();
            user.setRepositories(new ArrayList<>());
            List<String> repos = HttpHelper.getJSONObjectsFromArray(responseBody);
            for (String repo : repos) {
                user.addRepo(setBasicRepoInformationFromGitResponse(responseBody, new Repository()));
            }
            if (repos.size() == 0) {
                Helper.printMsg(Texts.REPOSITORIES_IS_EMPTY_MSG);
            }
            return user.getRepositories();
        } catch (IOException e) {
            Helper.printMsg(e.getMessage());
        }
        return null;
    }

    public static List<User> getCollaboratorsOfUsersRepo(User user, String repoName, boolean needPrint) {
        List<User> collaborators = new ArrayList<>();
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(Settings.HEADER_AUTHORIZATION,
                    String.format(Settings.HEADER_AUTHORIZATION_VALUE, user.getBase64Authorization()));
            String responseBody = HttpHelper.executeGetRequest(
                    Settings.GIT_API_URL + Requests.getApiGetColloborators()
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
                }
            } else {
                Helper.printMsg(Texts.COLLABORATORS_IS_NULL_MSG);
            }
            return collaborators;
        } catch (IOException e) {
            Helper.printMsg(e.getMessage());
        }
        return null;
    }

    public static String addUserAsCollaboratorToRepo(User user, String repoName, String collaboratorName) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(Settings.HEADER_AUTHORIZATION,
                    String.format(Settings.HEADER_AUTHORIZATION_VALUE, user.getBase64Authorization()));

            String responseBody = HttpHelper.executePutRequest(
                    Settings.GIT_API_URL + Requests.getApiAddColloboratorToRepo()
                            .replace(Requests.login, user.getLogin()
                                    .replace(Requests.repo, repoName)
                                    .replace(Requests.userName, collaboratorName)),
                    headers)
                    .toString();
            //----
            return responseBody;
        } catch (IOException e) {
            Helper.printMsg(e.getMessage());
        }
        return null;
    }
}
