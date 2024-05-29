package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.repository.SessionRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateCandidaturaResponse;
import it.itsincom.webdev2023.rest.model.CreateColloquioResponse;
import it.itsincom.webdev2023.rest.model.CreateModifyRequest;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.AuthenticationService;
import it.itsincom.webdev2023.service.UtenteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/utenti")
public class UtenteResource {

    private final UtenteService utenteService;
    private final UtenteRepository utenteRepository;
    private final SessionRepository sessionRepository;
    private final AuthenticationService authenticationService;

    public UtenteResource(UtenteService utenteService, UtenteRepository utenteRepository, SessionRepository sessionRepository, AuthenticationService authenticationService) {
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
        this.sessionRepository = sessionRepository;
        this.authenticationService = authenticationService;
    }

    // OTTIENE LA LISTA DEGLI UTENTI
    // TODO: SOLO AMMINISTRATORE
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateUtenteResponse> getAllUtenti(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId) {
        return utenteService.getAllUtenti();
    }

    //SOLO L'AMMINISTRATORE ELIMINA DALLA TABELLE UTENTI GLI INSEGNANTI E GLI UTENTI
    @DELETE
    @Path("/{id}")
    public Response deleteUtente(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, @PathParam("id") int id) {
        CreateUtenteResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a cancellare un corso");
        }
        utenteRepository.deleteUtente(id);
        return Response.ok().build();
    }

    // OTTIENE L'UTENTE
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse getUtenteById(@PathParam("id") int id) {
        return utenteService.getUtenteById(id);
    }

    // TROVA UN UTENTE PER NOME
    @GET
    @Path("/find/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse getUtenteByNome(@PathParam("nome") String nome) {
        return utenteService.getUtenteByNome(nome);
    }


    //OTTIENE LE CANDIDATURE DI UN UTENTE SPECIFICO
    @GET
    @Path("/{id}/candidature")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateCandidaturaResponse> getCandidatureUtenteById(@PathParam("id") int id) throws SQLException {
        return utenteService.getCandidatureUtenteById(id);
    }

    //OTTIENE I COLLOQUI DI UN UTENTE SPECIFICO
    @GET
    @Path("/{id}/colloqui")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateColloquioResponse> getColloquiUtenteById(@PathParam("id") int id) throws SQLException {
        return utenteService.getColloquiUtenteById(id);
    }


// MODIFICA LE INFO DEL PROFILO CONTROLLANDO L'ID DELL'UTENTE

    /*
{
    "nome": "xxxx",
    "cognome": "xxxx",
    "email": "xx@xxxxx.it",
    "telefono": "xxxxxxxxxx",
    "indirizzo": "xxx xxxxx",
    "citta": "xxxxxxx",
    "provincia": "XX",
    "cap": "xxxxx",
    "dataNascita" : "xxxx-xx-xx"
}
     */
    @PUT
    @Path("/profile/modify")
    @Consumes(MediaType.APPLICATION_JSON)
    public void modificaInfo(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, CreateModifyRequest modify) throws SQLException {
        int idUtente = sessionRepository.getIdBySession(sessionId);
        utenteRepository.modificaInfo(idUtente, modify);
    }

}