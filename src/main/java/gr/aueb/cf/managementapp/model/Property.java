package gr.aueb.cf.managementapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "properties")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property extends AbstractEntity implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rent")
    private Long rent;

    @Column(name = "area")
    private Long area;

    @Column(name = "road")
    private String road;

    @Column(name = "atak")
    private String atak;

    @Column(name = "address")
    private Long addressNum;

    @Column(name = "floor", nullable = false)
    private Long floor;

    @Column(name = "num_of_baths", nullable = false)
    private Long numOfBaths;

    @Column(name = "num_of_beds", nullable = false)
    private Long numOfBeds;

    @Column(name = "year_build", nullable = false)
    private Long yearBuild;

    @Column(name = "energy_class", nullable = false)
    private String energyClass;

    @Column(name = "type_of_heating", nullable = false)
    private String typeOfHeating;

    @Column(name = "type_of_cooling", nullable = false)
    private String typeOfCooling;

    @OneToMany(mappedBy = "property")
    private Set<Owner> owners = new HashSet<>();

    public void addOwner(Owner owner) {
        owners.add(owner);
        owner.setProperty(this);
    }

    public void removeOwner(Owner owner) {
        owners.remove(owner);
        owner.setActive(false);
        owner.setProperty(null);
    }

    @OneToMany(mappedBy = "property")
    @Basic(fetch = FetchType.EAGER)
    private Set<Damage> damages = new HashSet<>();

    @OneToMany(mappedBy = "property")
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
        renovation.setProperty(this);
    }

    public void removeRenovation(Renovation renovation) {
        if (renovations == null) return;
        renovations.remove(renovation);
        renovation.setProperty(null);
    }

    public void addDamage(Damage damage) {
        if (damages == null) damages = new HashSet<>();
        damages.add(damage);
        damage.setProperty(this);
    }

    public void removeDamage(Damage damage) {
        if (damages == null) return;
        damages.remove(damage);
        damage.setProperty(null);
    }



    public Property(String atak, Long rent, Long area, String road, Long addressNum, Long floor, Long numOfBaths, Long numOfBeds, Long yearBuild, String energyClass, String typeOfHeating, String typeOfCooling) {
        this.rent = rent;
        this.area = area;
        this.road = road;
        this.atak = atak;
        this.addressNum = addressNum;
        this.floor = floor;
        this.numOfBaths = numOfBaths;
        this.numOfBeds = numOfBeds;
        this.yearBuild = yearBuild;
        this.energyClass = energyClass;
        this.typeOfHeating = typeOfHeating;
        this.typeOfCooling = typeOfCooling;
    }
}
