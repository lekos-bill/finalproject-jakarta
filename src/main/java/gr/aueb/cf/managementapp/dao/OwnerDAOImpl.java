package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.Owner;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OwnerDAOImpl extends AbstractDAO<Owner> implements IOwnerDAO{

    public OwnerDAOImpl() {
        this.setPersistenceClass(Owner.class);
    }


}
