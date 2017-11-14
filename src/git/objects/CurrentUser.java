package git.objects;

import java.util.Base64;

public class CurrentUser extends User {
    private String password;
    private String base64Authorization;

    public CurrentUser(String login) {
        super(login);
    }

    public CurrentUser(String login, String password) {
        super(login);
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = password;
        calculateBase64Authorization();
    }

    private void calculateBase64Authorization() {
        base64Authorization = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String getBase64Authorization() {
        return base64Authorization;
    }
}
