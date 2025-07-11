package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.Renovation;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RenovationDAOImpl extends AbstractDAO<Renovation> implements IRenovationDAO{

    public RenovationDAOImpl() {
        setPersistenceClass(Renovation.class);
    }
}
