package gr.aueb.cf.managementapp.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PropertyInsertDTO(String atak, Long rent, Long area, String road, Long addressNum, Long floor, Long numOfBaths, Long NumOfBeds, Long yearBuild, String energyClass, String typeOfHeating, String typeOfCooling) {
}
