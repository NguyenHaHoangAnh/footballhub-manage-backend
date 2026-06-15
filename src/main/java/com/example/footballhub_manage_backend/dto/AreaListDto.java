package com.example.footballhub_manage_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaListDto {
    private Integer count;

    private List<AreaDto> areas;
}
