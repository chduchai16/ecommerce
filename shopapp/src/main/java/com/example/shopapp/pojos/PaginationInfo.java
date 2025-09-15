package com.example.shopapp.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {

    @JsonProperty("current_page")
    private int currentPage ;

    @JsonProperty("page_size")
    private int pageSize ;

    @JsonProperty("total_pages")
    private int totalPages ;

    @JsonProperty("total_elements")
    private long totalElements ;
}
