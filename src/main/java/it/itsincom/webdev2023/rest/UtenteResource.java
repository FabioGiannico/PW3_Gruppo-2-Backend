package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.AuthenticationService;
import it.itsincom.webdev2023.service.UtenteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/utenti")
public class UtenteResource {

    private final UtenteService utenteService;
    private final UtenteRepository utenteRepository;
    private final AuthenticationService authenticationService;

    public UtenteResource(UtenteService utenteService, UtenteRepository utenteRepository, AuthenticationService authenticationService) {
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
        this.authenticationService = authenticationService;
    }

    // OTTIENE LA LISTA DEGLI UTENTI
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateUtenteResponse> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    //SOLO L'AMMINISTRATORE ELIMINA DALLA TABELLE UTENTI GLI INSEGNANTI E GLI UTENTI
    @DELETE
    @Path("/delete/{id}")
    public Response deleteUtente(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, @PathParam("id") int id) {
        CreateUtenteResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a cancellare un corso");
        }
        utenteRepository.deleteUtente(id);
        return Response.ok().build();
    }



}