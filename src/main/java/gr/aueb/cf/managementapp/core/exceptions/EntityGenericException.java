package gr.aueb.cf.managementapp.core.exceptions;

import jakarta.ws.rs.GET;
import lombok.Getter;

@Getter
public abstract class EntityGenericException extends Exception{

    private final String code;

    public EntityGenericException(String code, String message) {

        super(message);
        this.code = code;
    }

}
