package gr.aueb.cf.managementapp.dto;

import gr.aueb.cf.managementapp.core.enums.ChangeType;

import java.time.LocalDate;

public record DamageUpdateDTO (Long id, String description, String changeType, String dateStarted, String dateEnded, String dateHappened, Long costForWork, Long costForMaterials, Long propertyId, Long Techid){
}
