package com.project.shopapp.responses;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
