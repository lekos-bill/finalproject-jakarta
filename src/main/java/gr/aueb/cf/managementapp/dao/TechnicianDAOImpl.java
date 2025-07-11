package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.Technician;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TechnicianDAOImpl extends AbstractDAO<Technician> implements ITechnicianDAO{
    public TechnicianDAOImpl() {
        setPersistenceClass(Technician.class);
    }
}
