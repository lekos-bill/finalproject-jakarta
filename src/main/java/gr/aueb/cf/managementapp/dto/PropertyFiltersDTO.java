package gr.aueb.cf.managementapp.dto;

import java.util.List;
import java.util.Map;

public record PropertyFiltersDTO(Map<String, Long> rent, Map<String, Long> area,
                                 List<String> road, Map<String, Object> atak, Map<String, Object> addressNum,
                                 Map<String, Long> floor, List<Long> numOfBaths, List<Long> numOfBeds,Map<String, String>
                                  yearBuild, List<String> energyClass, List<String> typeOfHeating,
                                 List<String> typeOfCooling) {
}
