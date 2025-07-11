package gr.aueb.cf.managementapp.dto;

import gr.aueb.cf.managementapp.core.enums.ChangeType;

import java.time.LocalDate;

public record RenovationReadOnlyDTO(Long id, String description, String dateStarted, String dateEnded, Long costForWork, Long costForMaterials, ChangeType changeType) {
}
