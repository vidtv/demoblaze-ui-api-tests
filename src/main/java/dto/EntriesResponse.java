package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO class for deserializing the response from the /entries endpoint
 * of the Product Catalog API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntriesResponse {
    @JsonProperty("Items")
    public List<Item> items;

    public static class Item {
        public String cat;
        public String desc;
        public int id;
        public String img;
        public double price;
        public String title;
    }
}
