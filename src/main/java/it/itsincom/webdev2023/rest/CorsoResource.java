package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.*;
import it.itsincom.webdev2023.persistence.model.Candidatura;
import it.itsincom.webdev2023.persistence.repository.CorsoRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateProfileResponse;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.AuthenticationService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/api/corso")
public class CorsoResource {

    private final CorsoRepository corsoRepository;
    private final AuthenticationService authenticationService;

    public CorsoResource(CorsoRepository corsoRepository,AuthenticationService authenticationService) {
        this.corsoRepository = corsoRepository;
        this.authenticationService = authenticationService;
    }

    // OTTIENE TUTTI I CORSI
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getAllCorsi(@QueryParam("categoria") String categoria) throws SQLException {
        return corsoRepository.getAllCorsi();
    }



    // CREA UN CORSO
    /*
{
    "nome": "xxx",
    "descrizione": "xxx",
    "categoria": "xxx",
    "durata": xxx,
    "programma": "xxx",
    "requisiti":  "xxx",
    "postiDisponibili": xxx,
    "dataInizio": "xxxx-xx-xx",
    "dataFine": "xxxx-xx-xx"
}
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Corso createCorso(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, Corso corso) throws SQLException {
        CreateProfileResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a creare un corso");
        }
        return corsoRepository.createCorso(corso);
    }



    @DELETE
    @Path("/{id}")
    public Response deleteCorso(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, @PathParam("id") int id) {
        CreateProfileResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a cancellare un corso");
        }
        corsoRepository.deleteCorso(id);
        return Response.ok().build();
    }

    // TODO: FINIRE
    @GET
    @Path("/{idCorso}/candidature")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Candidatura> getAllCandidature() throws SQLException {
        return corsoRepository.getAllCandidature();
    }

    // TROVA UN CORSO TRAMITE CATEGORIA
    @GET
    @Path("/categoria/{nomeCategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getCorsiPerCategoria(@PathParam("nomeCategoria") String nomeCategoria) throws SQLException {
        return corsoRepository.cercaCorsiPerCategoria(nomeCategoria);
    }

    // TROVA UN CORSO TRAMITE ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Corso getCorsoById(@PathParam("id") int id) throws SQLException {
        return corsoRepository.getCorsoById(id);
    }

    //CAMBIARE LO STATO DELL'UTENTE DA CANDIDATO A ISCRITTO
    @PUT
    @Path("/{idCorso}/candidature/{idUtente}/accetta")
    public void accettaUtente(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, @PathParam("idCorso") int idCorso, @PathParam("idUtente") int idUtente) throws SQLException {
        CreateProfileResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a gesetire l'iscrizione di un utente ad un corso");
        }
        corsoRepository.accettaUtente(idCorso, idUtente);
    }

    //CAMBIARE LO STATO UTENTE DA CANDIDATO A RIFIUTATO
    @PUT
    @Path("/{idCorso}/candidature/{idUtente}/reindirizzare")
    public void reindirizzaUtente(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId,@PathParam("idCorso") int idCorso, @PathParam("idUtente") int idUtente) throws SQLException {
        CreateProfileResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a gesetire l'iscrizione di un utente ad un corso");
        }
        corsoRepository.reindirizzaUtente(idCorso, idUtente);
    }

    @PUT
    @Path("/{idCorso}/candidature/{idUtente}/test")
    public void risultatoTest(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, @PathParam("idCorso") int idCorso, @PathParam("idUtente") int idUtente, RisultatoTest risultatoTest) throws SQLException {
        CreateProfileResponse profile = authenticationService.getProfile(sessionId);
        if (profile == null || profile.getRuolo() != Ruolo.amministratore) {
            throw new RuntimeException("Non sei autorizzato a cambiare il risultato del test");
        }
        corsoRepository.risultatoTest(idCorso, idUtente, risultatoTest.getRisultatoTest());
    }

    // CANDIDATI AD UN CORSO
    @POST
    @Path("/{idCorso}/candidature")
    @Consumes(MediaType.APPLICATION_JSON)
    public void candidaturaCorso(@PathParam("idCorso") int idCorso, @CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId) throws SQLException {
        CreateProfileResponse utente = authenticationService.getProfile(sessionId);
        if (utente == null) {
            throw new RuntimeException("Devi essere loggato per candidarti a un corso");
        }
        corsoRepository.candidaturaCorso(utente.getId(), idCorso);
    }



}