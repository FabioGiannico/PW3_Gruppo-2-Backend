package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Candidatura;
import it.itsincom.webdev2023.persistence.model.Corso;
import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.repository.CorsoRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteRequest;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.AuthenticationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.SQLException;
import java.util.List;

@Path("/corsi")
public class CorsoResource {

    private final CorsoRepository corsoRepository;
    private final AuthenticationService authenticationService;

    public CorsoResource(CorsoRepository corsoRepository, AuthenticationService authenticationService) {
        this.corsoRepository = corsoRepository;
        this.authenticationService = authenticationService;
    }

    // http://localhost:8080/corsi?categoria=DEV
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getAllCorsi(@QueryParam("categoria") String categoria) throws SQLException {
        return corsoRepository.getAllCorsi();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Corso createCorso(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId, Corso corso) throws SQLException {
        CreateUtenteResponse profile = authenticationService.getProfile(sessionId);
       // if (profile == null || profile.getRuolo != Ruolo.amministratore) {
            // Errore
       // }
        return corsoRepository.createCorso(corso);
    }

    @GET
    @Path("/categoria/{nomeCategoria}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getCorsiPerCategoria(@PathParam("nomeCategoria") String nomeCategoria) throws SQLException {
        return corsoRepository.cercaCorsiPerCategoria(nomeCategoria);
    }

    @POST
    @Path("/{idCorso}/candidature")
    @Consumes(MediaType.APPLICATION_JSON)
    public void candidatiPerCorso(@PathParam("idCorso") int idCorso, CreateUtenteRequest utente) throws SQLException {
        corsoRepository.candidatiPerCorso(utente.getId(), idCorso);
    }

    @GET
    @Path("/{idCorso}/candidature")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Candidatura> getAllCandidature() throws SQLException {
        return corsoRepository.getAllCandidature();
    }
}
