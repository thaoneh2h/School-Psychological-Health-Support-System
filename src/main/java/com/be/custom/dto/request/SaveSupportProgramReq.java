package com.be.custom.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class SaveSupportProgramReq {
    private Long id;
    private String title;
    private String description;
    private String category;
}
