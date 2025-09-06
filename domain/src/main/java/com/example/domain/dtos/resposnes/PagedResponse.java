package com.example.domain.dtos.resposnes;


import com.example.domain.pojos.PaginationInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponse <T> {
    @JsonProperty("page_content")
    private List<T> data;

    @JsonProperty("pagination_info")
    private PaginationInfo paginationInfo ;
}
