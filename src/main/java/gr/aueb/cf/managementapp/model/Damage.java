package gr.aueb.cf.managementapp.model;


import gr.aueb.cf.managementapp.core.enums.ChangeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "damages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Damage extends AbstractChange implements IdentifiableEntity{

    //1Ερώτηση: Αν έχω class που έχει κοινά attributes me άλλα τα οποία θέλω να είναι miutable
    //πειράζει τον builder, ώστε να μπορω να τα θέσω απο το το sub class?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_happened")
    private LocalDate dateHappened;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @Column(name = "change_type")
    private ChangeType changeType;

//    public Damage(Long id, String description, LocalDate dateStarted, LocalDate dateEnded, LocalDate dateHappened, Long costForWork, Long costForMaterials, Property property, ChangeType changeType) {
//        super(description, dateStarted, dateEnded, costForWork, costForMaterials);
//        this.dateHappened = dateHappened;
//        this.property = property;
//        this.changeType = changeType;
//        this.id = id;
//    }

    public Damage(Long id, String description, LocalDate dateStarted, LocalDate dateEnded, LocalDate dateHappened, Long costForWork, Long costForMaterials, ChangeType changeType) {
        super(description, dateStarted, dateEnded, costForWork, costForMaterials);
        this.dateHappened = dateHappened;
        this.changeType = changeType;
        this.id = id;
    }

}
