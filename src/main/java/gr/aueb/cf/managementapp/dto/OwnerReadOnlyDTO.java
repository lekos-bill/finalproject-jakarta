package gr.aueb.cf.managementapp.dto;

import gr.aueb.cf.managementapp.model.Property;

import java.time.LocalDate;

public record OwnerReadOnlyDTO(Long id, String firstname, String lastname, String cell, String prof, String nationality, boolean isActive, Long yearOfContract, Long yearOfBirth) {
}
