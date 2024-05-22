package it.itsincom.webdev2023.service;


import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateUtenteRequest;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UtenteService {

    private final HashCalculator hashCalculator;
    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository, HashCalculator hashCalculator){
        this.utenteRepository = utenteRepository;
        this.hashCalculator = hashCalculator;
    }

    public CreateUtenteResponse createUtente(CreateUtenteRequest utente){
        // 1. Estrarre la password dalla richiesta
        String password = utente.getPassword();
        // 2. Calcolare l'hash della password
        String hash = hashCalculator.calculateHash(password);
        // 3. Creare l'oggetto partecipante
        Utente u = new Utente();
        u.setNome(utente.getNome());
        u.setCognome(utente.getCognome());
        u.setEmail(utente.getEmail());
        u.setDataNascita(utente.getDataNascita());
        u.setTelefono(utente.getTelefono());
        u.setIndirizzo(utente.getIndirizzo());
        u.setRegistrazione(utente.getRegistrazione());
        u.setPasswordHash(hash);
        // 4. Salvare l'oggetto utente nel database
        Utente creato = utenteRepository.createUtente(u);
        // 5. Convertire l'oggetto utente in CreateUtenteResponse
        CreateUtenteResponse response = convertToResponse(creato);
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
        return response;
    }

    public CreateUtenteResponse getUtenteById(int utenteId){
        Utente utente = utenteRepository.getUtenteById(utenteId);
        //4. Conversione
        CreateUtenteResponse ur = convertToResponse(utente);
        //5. Return
        return ur;
    }

}
