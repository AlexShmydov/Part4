package git.objects;

import parameters.Requests;
import parameters.Settings;
import utils.Helper;
import utils.HttpHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GitProcessor {
    public void getUser(String userName) {

    }

    public void getCurrentUser(String password, String login) {
        CurrentUser currentUser = new CurrentUser(login, password);
        Map<String, String> headers = new HashMap<>();
        headers.put(Settings.HEADER_AUTHORIZATION,
                String.format(Settings.HEADER_AUTHORIZATION_VALUE, currentUser.getBase64Authorization()));
        try {
            HttpHelper.executeGetRequest(Settings.GIT_API_URL + Requests.getApiUsers().replace(Requests.login, login), headers);
        } catch (IOException e) {
            Helper.printMsg(e.getMessage());
        }
    }

    public void printUsersReposList(User user) {
        user.printRepositoriesNames();
    }
}
