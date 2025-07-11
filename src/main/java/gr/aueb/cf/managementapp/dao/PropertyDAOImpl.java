package gr.aueb.cf.managementapp.dao;

import gr.aueb.cf.managementapp.model.Property;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PropertyDAOImpl extends AbstractDAO<Property> implements IPropertyDAO{

    public PropertyDAOImpl() {
         this.setPersistenceClass(Property.class);
    }

}
