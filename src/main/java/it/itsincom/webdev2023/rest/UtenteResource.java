package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.UtenteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/utenti")
public class UtenteResource {

    private final UtenteService utenteService;

    public UtenteResource(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // OTTIENE LA LISTA DEGLI UTENTI
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateUtenteResponse> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse getUtenteById(@PathParam("id") int id) {
        return utenteService.getUtenteById(id);
    }
}