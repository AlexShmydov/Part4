package parameters;

public class Requests {
    private static String apiUsers = "/users/%s";
    private static String apiUsersRepos = "/users/%s/repos";
    private static String apiGetColloborators = "/repos/%s/%s/collaborators";
    private static String apiAddColloboratorToRepo = "/repos/%s/%s/collaborators/%s";

    public static final String login = "{login}";
    public static final String repo = "{repo}";
    public static final String userName = "{userName}";

    public static String getApiUsers() {
        return String.format(apiUsers, login);
    }

    public static String getApiUsersRepos() {
        return String.format(apiUsersRepos, repo);
    }

    public static String getApiGetColloborators() {
        return String.format(apiGetColloborators, login, repo);
    }

    public static String getApiAddColloboratorToRepo() {
        return String.format(apiAddColloboratorToRepo, login, repo, userName);
    }
}
