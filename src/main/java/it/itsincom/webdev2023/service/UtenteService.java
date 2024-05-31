package it.itsincom.webdev2023.service;


import it.itsincom.webdev2023.persistence.model.*;
import it.itsincom.webdev2023.persistence.repository.SessionRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.*;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UtenteService {

    private final DataSource dataSource;
    private final HashCalculator hashCalculator;
    private final UtenteRepository utenteRepository;
    private final SessionRepository sessionRepository;

    public UtenteService(UtenteRepository utenteRepository, HashCalculator hashCalculator, DataSource dataSource, SessionRepository sessionRepository) {
        this.utenteRepository = utenteRepository;
        this.hashCalculator = hashCalculator;
        this.dataSource = dataSource;
        this.sessionRepository = sessionRepository;
    }

    public CreateUtenteResponse createUtente(CreateUtenteRequest utente) {
        // 1. Estrarre la password dalla richiesta
        String password = utente.getPassword();
        // 2. Calcolare l'hash della password
        String hash = hashCalculator.calculateHash(password);
        // 3. Creare l'oggetto utente
        Utente u = new Utente();
        u.setNome(utente.getNome());
        u.setCognome(utente.getCognome());
        u.setEmail(utente.getEmail());
        u.setRuolo(Ruolo.valueOf(utente.getRuolo()));
        u.setRegistrazione(new Timestamp(System.currentTimeMillis()));

        u.setPasswordHash(hash);

        // 4. Salvare l'oggetto utente nel database
        Utente creato = utenteRepository.createUtente(u);

        // 5. Convertire l'oggetto utente in CreateUtenteResponse
        CreateUtenteResponse response = new CreateUtenteResponse();
        response.setId(creato.getId());
        response.setNome(creato.getNome());
        response.setCognome(creato.getCognome());
        response.setRuolo(creato.getRuolo());

        // 6. Restituire CreateUtenteResponse
        return response;
    }

    // OTTIENE LA LISTA DEGLI UTENTI CON RELATIVO INDIRIZZO
    public List<CreateProfileResponse> getAllUtenti() throws SQLException {
        List<CreateProfileResponse> responses = new ArrayList<>();
            List<Utente> utenti = utenteRepository.getAllUtenti();
            for (Utente utente : utenti) {
                Indirizzo indirizzo = utenteRepository.getIndirizzo(utente);
                CreateProfileResponse res = convertToProfileResponse(utente, indirizzo);
                responses.add(res);
            }
            return responses;
    }


    private CreateProfileResponse convertToProfileResponse(Utente u, Indirizzo indirizzo){
        Indirizzo i = new Indirizzo();
        i.setIndirizzoResidenza(indirizzo.getIndirizzoResidenza());
        i.setCitta(indirizzo.getCitta());
        i.setProvincia(indirizzo.getProvincia());
        i.setCap(indirizzo.getCap());

        CreateProfileResponse res = new CreateProfileResponse();
        res.setId(u.getId());
        res.setNome(u.getNome());
        res.setCognome(u.getCognome());
        res.setEmail(u.getEmail());
        res.setRuolo(u.getRuolo());
        res.setTelefono(u.getTelefono());
        res.setIndirizzo(indirizzo);
        res.setDataNascita(u.getDataNascita());

        return res;
    }

    private CreateUtenteResponse convertToResponse(Utente utente) {
        CreateUtenteResponse response = new CreateUtenteResponse();
        response.setId(utente.getId());
        response.setNome(utente.getNome());
        response.setCognome(utente.getCognome());
        response.setEmail(utente.getEmail());
        response.setRuolo(utente.getRuolo());
        response.setTelefono(utente.getTelefono());
        response.setDataNascita(utente.getDataNascita());
        response.setIndirizzo(utente.getIndirizzo());
        response.setDataRegistrazione(utente.getRegistrazione());
        return response;
    }
}
