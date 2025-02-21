package com.be.custom.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SaveAppointmentDto {
    private Long id;
    private Long studentId;
    private Long parentId;
    private Long psychologistId;
    private Date startTime;
    private Date endTime;
    private String status;
    private String notes;
}
