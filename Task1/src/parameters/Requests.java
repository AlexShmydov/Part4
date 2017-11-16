package parameters;

public class Requests {
    private static String apiUsers = "/users/%s";
    private static String apiUsersRepos = "/users/%s/repos";
    private static String apiGetCollaborators = "/repos/%s/%s/collaborators";
    private static String apiAddCollaboratorToRepo = "/repos/%s/%s/collaborators/%s";

    public static final String login = "{login}";
    public static final String repo = "{repo}";
    public static final String userName = "{userName}";

    public static String getApiUsers() {
        return String.format(apiUsers, login);
    }

    public static String getApiUsersRepos() {
        return String.format(apiUsersRepos, login);
    }

    public static String getApiGetCollaborators() {
        return String.format(apiGetCollaborators, login, repo);
    }

    public static String getApiAddCollaboratorToRepo() {
        return String.format(apiAddCollaboratorToRepo, login, repo, userName);
    }
}
