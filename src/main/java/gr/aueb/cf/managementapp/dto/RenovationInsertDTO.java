package gr.aueb.cf.managementapp.dto;

import java.time.LocalDate;

public record RenovationInsertDTO(String description, String changeType, String dateStarted, String dateEnded, Long costForWork, Long costForMaterials, Long propertyId, Long Techid) {
}
