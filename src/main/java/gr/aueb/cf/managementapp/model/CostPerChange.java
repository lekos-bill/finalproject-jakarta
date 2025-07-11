package gr.aueb.cf.managementapp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostPerChange {
    private Long id;
    private Long costForPluming = 0L;
    private Long costForElectrical = 0L;
    private Long costForFloor = 0L;
    private Long costForCeiling = 0L;
    private Long costForBalcony = 0L;
    private Long costForOther = 0L;
    private Long totalCost = 0L;

    @Override
    public String toString() {
        return "CostPerDamage{" +
                "id=" + id +
                ", costForPluming=" + costForPluming +
                ", costForElectrical=" + costForElectrical +
                ", costForFloor=" + costForFloor +
                ", costForCeiling=" + costForCeiling +
                ", costForBalcony=" + costForBalcony +
                ", costForOther=" + costForOther +
                ", totalCost=" + totalCost +
                '}';
    }

    public void calculateTotal() {
        this.totalCost =  getCostForBalcony() +  getCostForCeiling() +  getCostForFloor() +  getCostForPluming()  +  getCostForElectrical() +  getCostForOther();
    }

    public CostPerChange(Long id) {
        this.id = id;
    }
}
