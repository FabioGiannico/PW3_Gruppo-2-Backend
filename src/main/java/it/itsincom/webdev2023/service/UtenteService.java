package it.itsincom.webdev2023.service;


import it.itsincom.webdev2023.persistence.model.Candidatura;
import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.model.StatoCandidatura;
import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateCandidaturaResponse;
import it.itsincom.webdev2023.rest.model.CreateColloquioResponse;
import it.itsincom.webdev2023.rest.model.CreateUtenteRequest;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
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

    public UtenteService(UtenteRepository utenteRepository, HashCalculator hashCalculator, DataSource dataSource) {
        this.utenteRepository = utenteRepository;
        this.hashCalculator = hashCalculator;
        this.dataSource = dataSource;
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
        u.setRegistrazione(new Timestamp(System.currentTimeMillis()));
        u.setPasswordHash(hash);

        // 4. Salvare l'oggetto utente nel database
        Utente creato = utenteRepository.createUtente(u);

        // 5. Convertire l'oggetto utente in CreateUtenteResponse
        CreateUtenteResponse response = new CreateUtenteResponse();
        response.setId(creato.getId());
        response.setNome(creato.getNome());
        response.setCognome(creato.getCognome());

        // 6. Restituire CreateUtenteResponse
        return response;
    }

    public List<CreateUtenteResponse> getAllUtenti() {
        List<Utente> utenti = utenteRepository.getAllUtenti();
        List<CreateUtenteResponse> responses = new ArrayList<>();
        for (Utente utente : utenti) {
            CreateUtenteResponse response = convertToResponse(utente);
            responses.add(response);
        }
        return responses;
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

    public CreateUtenteResponse getUtenteById(int utenteId) {
        Utente utente = utenteRepository.getUtenteById(utenteId);
        //4. Conversione
        CreateUtenteResponse ur = convertToResponse(utente);
        //5. Return
        return ur;
    }

    public List<CreateCandidaturaResponse> getCandidatureUtenteById(int id) throws SQLException {
        return utenteRepository.getCandidatureUtenteById(id);
    }

    public List<CreateColloquioResponse> getColloquiUtenteById(int id) throws SQLException {
        return utenteRepository.getColloquiUtenteById(id);
    }

    public CreateUtenteResponse getUtenteByNome(String nome) {
        Utente utente = utenteRepository.getUtenteByNome(nome);

        CreateUtenteResponse ur = convertToResponse(utente);

        return ur;
    }
}
