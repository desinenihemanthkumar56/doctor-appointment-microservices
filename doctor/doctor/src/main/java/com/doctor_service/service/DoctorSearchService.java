package com.doctor_service.service;

import com.doctor_service.dto.DoctorSearchRequest;
import com.doctor_service.dto.DoctorSearchResponse;
import com.doctor_service.entity.Doctor;
import com.doctor_service.repository.DoctorRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorSearchService {

    private final DoctorRepository doctorRepository;
    @Cacheable(
            value = "doctorSearch",
            key = "#request.city + '-' + #request.area + '-' + #request.state + '-' + #request.specialization"
    )
    public List<DoctorSearchResponse> searchDoctors(DoctorSearchRequest request) {

        Specification<Doctor> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getCity() != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("city")),
                        request.getCity().toLowerCase()
                ));
            }

            if (request.getArea() != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("area")),
                        request.getArea().toLowerCase()
                ));
            }

            if (request.getState() != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("state")),
                        request.getState().toLowerCase()
                ));
            }

            if (request.getSpecialization() != null) {
                predicates.add(cb.equal(
                        cb.lower(root.get("specialization")),
                        request.getSpecialization().toLowerCase()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Doctor> doctors = doctorRepository.findAll(specification);

        return doctors.stream()
                .map(d -> new DoctorSearchResponse(
                        d.getId(),
                        d.getSpecialization(),
                        d.getHospitalName(),
                        d.getArea(),
                        d.getCity(),
                        d.getState(),
                        d.getExperienceYears(),
                        d.getConsultationFee()
                ))
                .toList();
    }
}