package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO class for deserializing the response from the /viewcart endpoint
 * of the Shopping Cart API.
 */
public class ViewCartResponseBody {
    @JsonProperty("Items")
    public List<ViewCartResponseBody.Item> items;

    public static class Item {
        public String cookie;
        public String id;
        public int prod_id;
    }
}
