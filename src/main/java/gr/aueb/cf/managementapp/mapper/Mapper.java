package gr.aueb.cf.managementapp.mapper;

import gr.aueb.cf.managementapp.core.enums.ChangeType;
import gr.aueb.cf.managementapp.core.enums.RoleType;
import gr.aueb.cf.managementapp.dto.*;
import gr.aueb.cf.managementapp.model.*;
import gr.aueb.cf.managementapp.security.SecUtil;
import jakarta.ws.rs.PUT;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Mapper {

    public Mapper() {
    }

    public static Property mapPropertyInsertDTOToProperty(PropertyInsertDTO dto) {
        return Property.builder().rent(dto.rent()).energyClass(dto.energyClass())
                .numOfBeds(dto.NumOfBeds()).numOfBaths(dto.numOfBaths())
                .floor(dto.floor()).area(dto.area()).addressNum(dto.addressNum())
                .typeOfCooling(dto.typeOfCooling()).typeOfHeating(dto.typeOfHeating())
                .yearBuild(dto.yearBuild()).road(dto.road()).atak(dto.atak()).build();
    }

    public static Property mapPropertyUpdateDTOtoProperty(PropertyUpdateDTO dto) {
        return Property.builder().rent(dto.rent()).energyClass(dto.energyClass())
                .numOfBeds(dto.NumOfBeds()).numOfBaths(dto.numOfBaths())
                .floor(dto.floor()).area(dto.area()).addressNum(dto.addressNum())
                .typeOfCooling(dto.typeOfCooling()).typeOfHeating(dto.typeOfHeating())
                .yearBuild(dto.yearBuild()).road(dto.road()).id(dto.id()).atak(dto.atak()).build();
    }
    public static PropertyReadOnlyDTO mapPropertyToPropertyReadOnlyDTO(Property property) {
        return new PropertyReadOnlyDTO(property.getId(), property.getAtak(), property.getRent(), property.getArea(),
                property.getRoad(), property.getAddressNum(), property.getNumOfBaths()
                , property.getNumOfBeds(), property.getFloor(), property.getYearBuild()
                , property.getEnergyClass(), property.getTypeOfCooling(),
                property.getTypeOfHeating());
    }

    public static OwnerReadOnlyDTO mapOwnerToOwnerReadOnlyDTO(Owner owner) {
        return new OwnerReadOnlyDTO(owner.getId(), owner.getFirstname(), owner.getLastname(), owner.getCell(), owner.getProf(), owner.getNationality(), owner.isActive(), owner.getYearOfContract(), owner.getYearOfBirth());
    }

    public static Owner mapOwnerInsertDTOToOwner(OwnerInsertDTO dto) {
        return new Owner(null, dto.firstname(), dto.lastname(), dto.cell(), dto.prof(), dto.nationality(), dto.isActive(),dto.yearOfContract(), dto.yearOfBirth());
    }

    public static Owner mapOwnerUpdateDTOToOwner(OwnerUpdateDTO dto) {
        return new Owner(dto.id(), dto.firstname(), dto.lastname(), dto.cell(), dto.prof(), dto.nationality(), dto.isActive(),dto.yearOfContract(), dto.yearOfBirth());
    }

    public static List<OwnerReadOnlyDTO> mapOwnersToListOfOwnerReadOnlyDTOs(List<Owner> owners) {
        return owners.stream().map(Mapper::mapOwnerToOwnerReadOnlyDTO).collect(Collectors.toList());
    }

    public static List<PropertyReadOnlyDTO>  mapPropertiesToListOfPropertyReadOnlyDTOs(List<Property> properties) {
        return properties.stream()
                .map(Mapper::mapPropertyToPropertyReadOnlyDTO)
                .collect(Collectors.toList());
    }

    public static Damage mapDamageInsertDTOToDamage(DamageInsertDTO dto, Property property) {
        return new Damage(null, dto.description(), LocalDate.parse(dto.dateStarted(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , LocalDate.parse(dto.dateEnded(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse(dto.dateHappened(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                dto.costForMaterials(), dto.costForWork(),  property, ChangeType.valueOf(dto.changeType()));
    }
    //οταν βάζω κάποιο αντικείμενο που ειναι σε σχέση με κάποιο άλλο, πως το επιστρέφω
    public static DamageReadOnlyDTO mapDamagetoDamageReadOnlyDTO(Damage damage) {
        return new DamageReadOnlyDTO(damage.getId(), damage.getDescription(), damage.getDateStarted().toString(), damage.getDateEnded().toString()
        , damage.getDateHappened().toString(), damage.getCostForWork(), damage.getCostForMaterials(), damage.getChangeType());
    }

    public static Damage mapDamageUpdateDTOToDamage(DamageUpdateDTO dto) {
        return new Damage(dto.id(), dto.description(), LocalDate.parse(dto.dateStarted(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , LocalDate.parse(dto.dateEnded(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse(dto.dateHappened(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                dto.costForMaterials(), dto.costForWork(), ChangeType.valueOf(dto.changeType()));
    }

    public static List<CostPerChange> mapListOfObjectsToToCostPerDamagePerProperty(List<Object[]> list, Long count) {

        List<CostPerChange> listOfCostperDamage = new ArrayList<>();

        //MAKES A LIST OF THE COSTPERCHANGE
        for (int i = 1; i <= count; i++) {
            CostPerChange costPerDamage = new CostPerChange((long) i);
            listOfCostperDamage.add(costPerDamage);
        }
        //MAPS EACH ELEMENT OF COSTPERCHANGE LIST
        //TO COSTPERDAMAGE MODEL CLASS WITH SWITCH
        for (Object[] eachElementOfList : list) {
            List<Object> toList = Arrays.stream(eachElementOfList).toList();
            ChangeType stringChangeTypeFromList = (ChangeType) toList.get(2);


            switch (stringChangeTypeFromList.name()) {
                case "PLUMING":
                    listOfCostperDamage.get((((Long) toList.get(0))).intValue() - 1).setCostForPluming((long) toList.get(3) + (long) toList.get(4));
                    break;
                case "ELECTRICAL":
                    listOfCostperDamage.get(((Long) toList.get(0)).intValue() - 1).setCostForElectrical((long) toList.get(3) + (long) toList.get(4));
                    break;
                case "FLOOR":
                    listOfCostperDamage.get(((Long) toList.get(0)).intValue() - 1).setCostForFloor((long) toList.get(3) + (long) toList.get(4));
                    break;
                case "CEILING":
                    listOfCostperDamage.get(((Long) toList.get(0)).intValue() - 1).setCostForCeiling((long) toList.get(3) + (long) toList.get(4));
                    break;
                case "BALCONY":
                    listOfCostperDamage.get(((Long) toList.get(0)).intValue() - 1).setCostForBalcony((long) toList.get(3) + (long) toList.get(4));
                    break;
                case "OTHER":
                    listOfCostperDamage.get(((Long) toList.get(0)).intValue() - 1).setCostForOther((long) toList.get(3) + (long) toList.get(4));
                    break;
            }
        }

        listOfCostperDamage.forEach(entry -> entry.calculateTotal());
        return listOfCostperDamage;
    }

    public static CostPerChangeReadOnlyDTO mapCostPerChangeToCostPerChangeReadOnlyDTO(CostPerChange costperChange) {
        return new CostPerChangeReadOnlyDTO(costperChange.getId(), costperChange.getCostForPluming(), costperChange.getCostForFloor(), costperChange.getCostForCeiling(), costperChange.getCostForBalcony(), costperChange.getCostForOther());
    }

    public static List<DamageReadOnlyDTO>  mapDamagesToListOfDamageReadOnlyDTOs(List<Damage> damages) {
        return damages.stream()
                .map(Mapper::mapDamagetoDamageReadOnlyDTO)
                .collect(Collectors.toList());
    }

    public static Technician mapTechnicianInsertDTOToTechnician(TechnicianInsertDTO insertDTO) {
        return new Technician(null, insertDTO.firstname(), insertDTO.lastname(), insertDTO.cell(), insertDTO.prof());
    }

    public static Technician mapTechnicianUpdateDTOToTechnician(TechnicianUpdateDTO insertDTO) {
        return new Technician(insertDTO.id(), insertDTO.firstname(), insertDTO.lastname(), insertDTO.cell(), insertDTO.prof());
    }

    public static TechnicianReadOnlyDTO mapTechnicianToTechnicianReadOnlyDTO(Technician technician) {
        return new TechnicianReadOnlyDTO(technician.getId(), technician.getFirstname(), technician.getLastname(), technician.getCell(), technician.getProf());
    }

    public static List<TechnicianReadOnlyDTO> mapTechniciansToTechnicianReadOnlyDTOs(List<Technician> technicians) {
        return technicians.stream().map(Mapper::mapTechnicianToTechnicianReadOnlyDTO).collect(Collectors.toList());
    }

    public static Renovation mapRenovationInsertDTOToRenovation(RenovationInsertDTO dto, Property property) {
        return new Renovation(null, dto.description(), LocalDate.parse(dto.dateStarted(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , LocalDate.parse(dto.dateEnded(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), dto.costForWork(),dto.costForMaterials(), property, ChangeType.valueOf(dto.changeType()));
    }
    //οταν βάζω κάποιο αντικείμενο που ειναι σε σχέση με κάποιο άλλο, πως το επιστρέφω
    public static RenovationReadOnlyDTO mapRenovationToRenovationReadOnlyDTO(Renovation renovation) {
        return new RenovationReadOnlyDTO(renovation.getId(), renovation.getDescription(), renovation.getDateStarted().toString(), renovation.getDateEnded().toString()
                , renovation.getCostForWork(), renovation.getCostForMaterials(), renovation.getChangeType());
    }

    public static Renovation mapRenovationUpdateDTOToRenovation(RenovationUpdateDTO dto) {
        return new Renovation(null, dto.description(), LocalDate.parse(dto.dateStarted(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                , LocalDate.parse(dto.dateEnded(), DateTimeFormatter.ofPattern("yyyy-MM-dd")), dto.costForWork(),dto.costForMaterials(),  ChangeType.valueOf(dto.changeType()));
    }

    public static List<RenovationReadOnlyDTO>  mapRenovationsToListOfRenovationReadOnlyDTOs(List<Renovation> renovations) {
        return renovations.stream()
                .map(Mapper::mapRenovationToRenovationReadOnlyDTO)
                .collect(Collectors.toList());
    }

    public static Map<String, Object> mapPropertyFilfersDTOToCriteria(PropertyFiltersDTO dto) {
        Map<String , Object> criteria = new HashMap<>();
        criteria.put("rent", dto.rent() == null? Collections.emptyMap(): dto.rent());
        criteria.put("area", dto.area() == null? Collections.emptyMap(): dto.area());
        criteria.put("road", dto.road());
        criteria.put("atak", dto.atak() == null? Collections.emptyMap(): dto.atak());
        criteria.put("addressNum", dto.addressNum() == null? Collections.emptyMap(): dto.addressNum());
        criteria.put("yearBuild", dto.yearBuild() == null? Collections.emptyMap(): dto.yearBuild());
        criteria.put("floor", dto.floor() == null? Collections.emptyMap(): dto.floor());
        criteria.put("numOfBaths", dto.numOfBaths());
        criteria.put("energyClass", dto.energyClass());
        criteria.put("typeOfHeating", dto.typeOfHeating());
        criteria.put("typeOfCooling", dto.typeOfCooling());
        criteria.put("numOfBeds", dto.numOfBeds());
        return criteria;
    }

    public static User mapToUser(UserInsertDTO dto) {
        return new User(null, dto.getUsername(), SecUtil.hashPassword(dto.getPassword()),
                RoleType.valueOf(dto.getRole()));
    }

    public static UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getId(), user.getUsername(), user.getPassword(),
                user.getRoleType().name());
    }

}
