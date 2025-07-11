package gr.aueb.cf.managementapp.dto;

import gr.aueb.cf.managementapp.core.enums.ChangeType;

import java.time.LocalDate;

public record DamageReadOnlyDTO(Long id, String description, String dateStarted, String dateEnded, String dateHappened, Long costForWork, Long costForMaterials, ChangeType changeType) {
}
