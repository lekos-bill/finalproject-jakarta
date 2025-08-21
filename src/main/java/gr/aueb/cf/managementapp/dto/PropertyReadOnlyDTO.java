package gr.aueb.cf.managementapp.dto;

public record PropertyReadOnlyDTO(Long id, String atak, Long rent, Long area, String road, Long addressNum, Long floor, Long numOfBaths, Long NumOfBeds, Long yearBuild, String energyClass, String typeOfHeating, String typeOfCooling) {

}
