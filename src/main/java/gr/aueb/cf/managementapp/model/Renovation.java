package gr.aueb.cf.managementapp.model;


import gr.aueb.cf.managementapp.core.enums.ChangeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "renovations")
@Getter
@Setter
@NoArgsConstructor
public class Renovation extends AbstractChange implements IdentifiableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;
    private ChangeType changeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private Technician technician;

//    public Renovation(Long id, String description, LocalDate dateStarted, LocalDate dateEnded, Long costForWork, Long costForMaterials, Property property, ChangeType changeType) {
//        super(description, dateStarted, dateEnded, costForWork, costForMaterials);
//
//        this.property = property;
//        this.changeType = changeType;
//        this.id = id;
//    }

    public Renovation(Long id, String description, LocalDate dateStarted, LocalDate dateEnded, Long costForWork, Long costForMaterials, ChangeType changeType) {
        super(description, dateStarted, dateEnded, costForWork, costForMaterials);
        this.changeType = changeType;
        this.id = id;
    }

}
