package com.doctor_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSearchRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String city;
    private String area;
    private String state;
    private String specialization;
}