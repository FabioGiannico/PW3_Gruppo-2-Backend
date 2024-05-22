package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.service.exception.WrongUsernameOrPasswordException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WrongUsernameOrPasswordExceptionMapper implements ExceptionMapper<WrongUsernameOrPasswordException> {
    @Override
    public Response toResponse(WrongUsernameOrPasswordException exception) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
