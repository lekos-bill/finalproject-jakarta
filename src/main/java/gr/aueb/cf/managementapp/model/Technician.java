package gr.aueb.cf.managementapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "technicians")
@AllArgsConstructor
@NoArgsConstructor
public class Technician extends AbstractEntity implements IdentifiableEntity {

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

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private Set<Damage> damages = new HashSet<>();

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    private Set<Renovation> renovations = new HashSet<>();



    public Set<Damage> getAllDamages() {
        return Collections.unmodifiableSet(damages);
    }

    public Set<Renovation> getAllRenovations() {
        return Collections.unmodifiableSet(renovations);
    }

    public void addRenovation(Renovation renovation) {
        if (renovations == null) renovations = new HashSet<>();
        renovations.add(renovation);
        renovation.setTechnician(this);
    }

    public void removeRenovation(Renovation renovation) {
        if (renovations == null) return;
        renovations.remove(renovation);
        renovation.setTechnician(null);
    }

    public void addDamage(Damage damage) {
        if (damages == null) damages = new HashSet<>();
        damages.add(damage);
        damage.setTechnician(this);
    }

    public void removeDamage(Damage damage) {
        if (damages == null) return;
        damages.remove(damage);
        damage.setTechnician(null);
    }


    public Technician(Long id, String firstname, String lastname, String cell, String prof) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.cell = cell;
        this.prof = prof;
    }
}
