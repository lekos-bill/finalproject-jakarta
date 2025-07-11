package gr.aueb.cf.managementapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;


@Getter
@Setter
@Entity
@Builder
@Table(name = "owners")
@AllArgsConstructor
@NoArgsConstructor
public class Owner extends AbstractEntity implements IdentifiableEntity{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone_num")
    private String cell;

    @Column(name = "profession")
    private String prof;

    @Column(name = "nationality")
    private String nationality;

    @Column(nullable = false, name = "is_active")
    private boolean isActive;

    @Column(nullable = false, name = "year_of_contract")
    private Long yearOfContract;

    @Column(nullable = false, name = "date_of_birth")
    private Long yearOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    public void addProperty(Property property) {
        this.property = property;
        property.addOwner(this);
    }

    public void removeProperty(Property property) {
        this.property = null;
        property.removeOwner(null);
    }

    public Owner(Long id, String firstname, String lastname, String cell, String prof, String nationality, boolean isActive, Long yearOfContract, Long yearOfBirth) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.cell = cell;
        this.prof = prof;
        this.nationality = nationality;
        this.isActive = isActive;
        this.yearOfContract = yearOfContract;
        this.yearOfBirth = yearOfBirth;
    }

//    public Owner(Long id, String firstname, String lastname, String cell, String prof, String nationality, boolean isActive, Long yearOfContract, Long yearOfBirth, Property property) {
//        this.id = id;
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.cell = cell;
//        this.prof = prof;
//        this.nationality = nationality;
//        this.isActive = isActive;
//        this.yearOfContract = yearOfContract;
//        this.yearOfBirth = yearOfBirth;
//        this.property = property;
//    }
}
