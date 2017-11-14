package git.objects;

import parameters.Texts;
import utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String login;
    private int id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String type;
    private String site_admin;
    private boolean permissions_admin;
    private boolean permissions_push;
    private boolean permissions_pull;
    private List<Repository> repositories = new ArrayList<>();

    public User(String login) {
        this.login = login;
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

    public String getSite_admin() {
        return site_admin;
    }

    public void setSite_admin(String site_admin) {
        this.site_admin = site_admin;
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

    public boolean isPermissions_admin() {
        return permissions_admin;
    }

    public boolean isPermissions_pull() {
        return permissions_pull;
    }

    public boolean isPermissions_push() {
        return permissions_push;
    }

    public void setPermissions_admin(boolean permissions_admin) {
        this.permissions_admin = permissions_admin;
    }

    public void setPermissions_pull(boolean permissions_pull) {
        this.permissions_pull = permissions_pull;
    }

    public void setPermissions_push(boolean permissions_push) {
        this.permissions_push = permissions_push;
    }

    public void initializeData() {

    }
}
