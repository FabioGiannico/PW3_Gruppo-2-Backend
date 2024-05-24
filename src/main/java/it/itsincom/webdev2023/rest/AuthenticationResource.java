package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteRequest;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.AuthenticationService;

import it.itsincom.webdev2023.service.UtenteService;
import it.itsincom.webdev2023.service.exception.SessionCreationException;
import it.itsincom.webdev2023.service.exception.WrongUsernameOrPasswordException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.sql.Timestamp;

@Path("/api/auth")
public class AuthenticationResource {

    private final AuthenticationService authenticationService;
    private final UtenteService utenteService;

    public AuthenticationResource(AuthenticationService authenticationService, UtenteService utenteService) {
        this.authenticationService = authenticationService;
        this.utenteService = utenteService;
    }

    // REGISTRA UN NUOVO UTENTE
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse register(@BeanParam CreateUtenteRequest request) {
        return utenteService.createUtente(request);
    }

    // FA IL LOGIN
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("email") String email,
                          @FormParam("password") String password) throws WrongUsernameOrPasswordException, SessionCreationException {
        int sessione = authenticationService.login(email, password);
        NewCookie sessionCookie = new NewCookie.Builder("SESSION_COOKIE").path("/").value(String.valueOf(sessione)).build();
        return Response.ok()
                .cookie(sessionCookie)
                .build();
    }

    // FA IL LOGOUT
    @DELETE
    @Path("/logout")
    public Response logout(@CookieParam("SESSION_COOKIE") int sessionId) {
        authenticationService.logout(sessionId);
        NewCookie sessionCookie = new NewCookie.Builder("SESSION_COOKIE").path("/").build();
        return Response.ok()
                .cookie(sessionCookie)
                .build();
    }

    // OTTIENE LE INFO DELL'UTENTE
    @GET
    @Path("/profile")
    public CreateUtenteResponse getProfile(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId) throws WrongUsernameOrPasswordException {
        if (sessionId == -1) {
            throw new WrongUsernameOrPasswordException();
        }
        return authenticationService.getProfile(sessionId);
    }
}
