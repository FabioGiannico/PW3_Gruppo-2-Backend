package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/utenti")
public class UtenteResource {

    private final UtenteRepository utenteRepository;

    public UtenteResource(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Utente createUtente(Utente utente) {
        return utenteRepository.createUtente(utente);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utente> getAllPartecipanti() {
        return utenteRepository.getAllUtente();
    }
}