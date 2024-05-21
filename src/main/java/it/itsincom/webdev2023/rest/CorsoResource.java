package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Corso;
import it.itsincom.webdev2023.persistence.repository.CorsoRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.SQLException;
import java.util.List;

@Path("/corso")
public class CorsoResource {

    private final CorsoRepository corsoRepository;

    public CorsoResource(CorsoRepository corsoRepository) {
        this.corsoRepository = corsoRepository;
    }




    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Corso> getAllCorsi() throws SQLException {
        return corsoRepository.getAllCorsi();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Corso createCorso(Corso corso) throws SQLException {
        return corsoRepository.createCorso(corso);
    }

}
