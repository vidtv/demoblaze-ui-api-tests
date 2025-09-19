package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ViewCartResponseBody {
    @JsonProperty("Items")
    public List<ViewCartResponseBody.Item> items;

    public static class Item {
        public String cookie;
        public String id;
        public int prod_id;
    }
}
