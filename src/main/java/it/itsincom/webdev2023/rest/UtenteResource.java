package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.UtenteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/utenti")
public class UtenteResource {

    private final UtenteService utenteService;

    public UtenteResource(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateUtenteResponse> getAllUtenti() {
       return utenteService.getAllUtenti();
    }
}