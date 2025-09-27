package dto;

/**
 * DTO class for /viewcart API endpoint request body.
 */
public class ViewCartRequestBody {
    public String cookie;
    public boolean flag;

    public ViewCartRequestBody(String cookie) {
        this.cookie = cookie;
        this.flag = true;
    }
}
