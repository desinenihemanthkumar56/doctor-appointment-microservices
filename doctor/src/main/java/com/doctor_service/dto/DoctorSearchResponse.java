package com.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor // Crucial for Redis Deserialization
public class DoctorSearchResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID doctorId;
    private String specialization;
    private String hospitalName;
    private String area;
    private String city;
    private String state;
    private Integer experienceYears;
    private Double consultationFee;
}