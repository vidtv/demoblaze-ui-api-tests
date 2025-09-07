package dto;

/**
 * DTO class for user login request body.
 */
public class LoginBody {
    public String username;
    public String password;

    /**
     * Constructor to initialize username and password.
     *
     * @param username the username
     * @param password the password
     */
    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
