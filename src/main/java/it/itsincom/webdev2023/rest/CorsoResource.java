package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Corso;
import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.CorsoRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.SQLException;
import java.util.List;

@Path("/api/corso")
public class CorsoResource {

    private final CorsoRepository corsoRepository;
    private final UtenteRepository utenteRepository;

    public CorsoResource(CorsoRepository corsoRepository, UtenteRepository utenteRepository) {
        this.corsoRepository = corsoRepository;
        this.utenteRepository = utenteRepository;
    }


    // OTTIENE LA LISTA DEI CORSI
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getAllCorsi() throws SQLException {
        return corsoRepository.getAllCorsi();
    }

    // CREA UN CORSO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Corso createCorso(Corso corso) throws SQLException {
        return corsoRepository.createCorso(corso);
    }

    // VEDE CHI E' IN LISTA PER UN CERTO CORSO
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utente> getListaIdUtentiPerCorso(@PathParam("id") int id) throws SQLException {
        return utenteRepository.getListaUtentiById(id);
    }
}