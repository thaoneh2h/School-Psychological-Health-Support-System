package com.be.custom.dto.response_api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReportAppointmentRes {
    private Long studentId;
    private Long psychologistId;
    private Long parentId;
    private String studentName;
    private String psychologistName;
    private String parentName;
    private String report;
    private Date timeUpload;
    private Date timeAppointment;
}
