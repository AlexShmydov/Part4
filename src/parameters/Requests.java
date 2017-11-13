package parameters;

public class Requests {
    public String apiUsers = "/users/"+login;
    public String apiUsersRepos = "/users/"+login+"/repos";
    public String apiGetColloborators = "/repos/"+login+"/"+repo+"/collaborators";
    public String apiAddColloboratorToRepo = "/repos/"+login+"/"+repo+"/collaborators/"+userName;

    public static final String login = "{login}";
    public static final String repo = "{repo}";
    public static final String userName = "{userName}";
}
