package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.Damage;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DamageDAOImpl extends AbstractDAO<Damage> implements IDamageDAO{

        public DamageDAOImpl() {
        this.setPersistenceClass(Damage.class);
    }

}
