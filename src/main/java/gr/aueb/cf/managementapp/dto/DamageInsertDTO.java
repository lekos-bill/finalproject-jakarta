package gr.aueb.cf.managementapp.dto;

import java.time.LocalDate;


public record DamageInsertDTO(String description, String changeType, String dateStarted, String dateEnded, String dateHappened, Long costForWork, Long costForMaterials, Long propertyId, Long Techid) {

}
