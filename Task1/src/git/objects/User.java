package git.objects;

import parameters.Texts;
import utils.Helper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String login;
    private String password;
    private String base64Authorization;
    private int id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String type;
    private List<Repository> repositories = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        setPassword(password);
    }

    public User(String login) {
        this.login = login;
    }

    public User() {

    }

    public void setPassword(String password) {
        this.password = password;
        calculateBase64Authorization();
    }

    private void calculateBase64Authorization() {
        base64Authorization = Base64.getEncoder().encodeToString(String.format("%s:%s",login,password).getBytes());
    }

    public String getBase64Authorization() {
        return base64Authorization;
    }

    public void addRepo(Repository repository) {
        repositories.add(repository);
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void printRepositoriesNames() {
        if (repositories.size() > 0) {
            for (Repository repository : repositories) {
                Helper.printMsg(String.format(Texts.REPOSITORY_NAME, repository.getName()));
            }
        } else {
            Helper.printMsg(Texts.REPOSITORIES_IS_EMPTY_MSG);
        }
    }

    public boolean isRepositoryOfUser(String repoName) {
        for (Repository repository : repositories) {
            if (repository.getName().toUpperCase().equals(repoName.trim().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getGravatar_id() {
        return gravatar_id;
    }

    public void setGravatar_id(String gravatar_id) {
        this.gravatar_id = gravatar_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
}
