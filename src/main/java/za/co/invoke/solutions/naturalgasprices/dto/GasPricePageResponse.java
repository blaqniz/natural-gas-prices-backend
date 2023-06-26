
package za.co.invoke.solutions.naturalgasprices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "totalItems",
        "totalPages",
        "pageNumber",
        "gasPriceObjects"
})
@Data
public class GasPricePageResponse {

    /**
     * Total number of items to apply pagination on.
     */
    @JsonProperty("totalItems")
    @JsonPropertyDescription("Total number of items to apply pagination on.")
    private int totalItems;

    @JsonProperty("totalPages")
    @JsonPropertyDescription("Total pages for ResultSet used for pagination.")
    private int totalPages;

    @JsonProperty("pageNumber")
    @JsonPropertyDescription("Current page number.")
    private int pageNumber;

    @JsonProperty("gas_prices")
    private List<GasPriceDto> gasPriceObjects = new ArrayList<>();

    public GasPricePageResponse withGasPriceObjects(List<GasPriceDto> dashboardData) {
        this.gasPriceObjects = dashboardData;
        return this;
    }

    public GasPricePageResponse withPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public GasPricePageResponse withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public GasPricePageResponse withTotalItems(int totalItems) {
        this.totalItems = totalItems;
        return this;
    }
}
