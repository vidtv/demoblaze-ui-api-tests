package utils;

/**
 * A class that holds constant values used throughout the project.
 */
public class Constants {

    public final static String BASE_URL = "https://www.demoblaze.com";
    public static final String BASE_API_URL = "https://api.demoblaze.com";

    public final static String USERNAME = System.getenv("USERNAME") != null ?
            System.getenv("USERNAME") : "defaultUser";

    public final static String PASSWORD = System.getenv("PASSWORD") != null ?
            System.getenv("PASSWORD") : "defaultPassword";
}
