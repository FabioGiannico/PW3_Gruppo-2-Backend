package it.itsincom.webdev2023.rest;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.repository.SessionRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.UtenteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Path("/api/utenti")
public class UtenteResource {

    private final UtenteService utenteService;
    private final UtenteRepository utenteRepository;
    private final SessionRepository sessionRepository;

    public UtenteResource(UtenteService utenteService, UtenteRepository utenteRepository, SessionRepository sessionRepository) {
        this.utenteService = utenteService;
        this.utenteRepository = utenteRepository;
        this.sessionRepository = sessionRepository;
    }

    // OTTIENE LA LISTA DEGLI UTENTI
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreateUtenteResponse> getAllUtenti() {
        return utenteService.getAllUtenti();
    }

    // OTTIENE L'UTENTE
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CreateUtenteResponse getUtenteById(@PathParam("id") int id) {
        return utenteService.getUtenteById(id);
    }

    // MODIFICA LE INFO DI UN UTENTE SOLO SE E' IL SUO PROFILO
    @PUT
    @Path("/profile/modify")
    @Produces(MediaType.APPLICATION_JSON)
    public void modificaInfo(@CookieParam("SESSION_COOKIE") @DefaultValue("-1") int sessionId,
                               String nome,
                               String cognome,
                               String email,
                               String password,
                               String telefono,
                               String indirizzo,
                               String dataNascita
                            ) throws SQLException {

        int idUtente = sessionRepository.getIdBySession(sessionId);

        // NOME
        utenteRepository.setNome(idUtente, nome);
        // COGNOME
        utenteRepository.setCognome(idUtente, cognome);
        // EMAIL
        utenteRepository.setEmail(idUtente, email);
        // PSW
        utenteRepository.setPassword(idUtente, password);
        // TELEFONO
        utenteRepository.setTelefono(idUtente, telefono);
        // INDIRIZZO
        // DATA DI NASCITA
    }
}