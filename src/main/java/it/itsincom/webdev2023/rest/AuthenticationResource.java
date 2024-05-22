package it.itsincom.webdev2023.rest;

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

@Path("/auth")
public class AuthenticationResource {

    private final AuthenticationService authenticationService;
    private final UtenteRepository utenteRepository;
    private final UtenteService utenteService;

    public AuthenticationResource(AuthenticationService authenticationService, UtenteRepository utenteRepository, UtenteService utenteService) {
        this.authenticationService = authenticationService;

        this.utenteRepository = utenteRepository;
        this.utenteService = utenteService;
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse createUtenteResponse(CreateUtenteRequest utente) {
        return utenteService.createUtente(utente);
    }

    @POST
    @Path("/login")
    @Produces()
    public Response login(@FormParam("nome") String nome, @FormParam("cognome") String cognome, @FormParam("password") String password) throws WrongUsernameOrPasswordException, SessionCreationException {
        int sessione = authenticationService.login(nome, cognome, password);
        NewCookie sessionCookie = new NewCookie.Builder("SESSION_COOKIE").value(String.valueOf(sessione)).build();
        return Response.ok()
                .cookie(sessionCookie)
                .build();
    }

    @DELETE
    @Path("/logout")
    public Response logout(@CookieParam("SESSION_COOKIE") int sessionId) {
        authenticationService.logout(sessionId);
        NewCookie sessionCookie = new NewCookie.Builder("SESSION_COOKIE").build();
        return Response.ok()
                .cookie(sessionCookie)
                .build();
    }

    @GET
    @Path("/profile")
    public CreateUtenteResponse getProfile(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId) throws WrongUsernameOrPasswordException {
        if (sessionId == -1) {
            throw new WrongUsernameOrPasswordException();
        }
        return authenticationService.getProfile(sessionId);
    }
}
