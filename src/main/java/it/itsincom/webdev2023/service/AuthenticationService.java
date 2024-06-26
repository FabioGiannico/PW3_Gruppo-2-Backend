package it.itsincom.webdev2023.service;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.model.Sessione;
import it.itsincom.webdev2023.persistence.model.Utente;
import it.itsincom.webdev2023.persistence.repository.SessionRepository;
import it.itsincom.webdev2023.persistence.repository.UtenteRepository;
import it.itsincom.webdev2023.rest.model.CreateProfileResponse;
import it.itsincom.webdev2023.rest.model.CreateUtenteResponse;
import it.itsincom.webdev2023.service.exception.SessionCreationException;
import it.itsincom.webdev2023.service.exception.WrongUsernameOrPasswordException;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.SQLException;
import java.util.Optional;

@ApplicationScoped
public class AuthenticationService {

    private final UtenteRepository utenteRepository;
    private final HashCalculator hashCalculator;
    private final SessionRepository sessionRepository;
    private final UtenteService utenteService;

    public AuthenticationService(
            UtenteRepository utenteRepository,
            HashCalculator hashCalculator,
            SessionRepository sessionRepository, UtenteService utenteService

    ) {
        this.utenteRepository = utenteRepository;
        this.hashCalculator = hashCalculator;
        this.sessionRepository = sessionRepository;
        this.utenteService = utenteService;
    }

    public int login(String email, String password) throws SessionCreationException, WrongUsernameOrPasswordException {
        // 0. Calcolo dell'hash della password
        String hash = hashCalculator.calculateHash(password);
        // 1. Controllare che esista un utente con gli stessi nome, cognome, hashpassword
        Optional<Utente> maybeUtente = utenteRepository.findUtenteByEmailPasswordHash(email, hash);
        if (maybeUtente.isPresent()) {
            // 2a. Se combaciano, inserire una nuova sessione per quell'utente
            // 3a. Ritornare il codice sessione
            Utente u = maybeUtente.get();
            try {
                int sessione = sessionRepository.insertSession(u.getId());
                return sessione;
            } catch (SQLException e) {
                throw new SessionCreationException(e);
            }
        } else {
            // 2b. Se non combaciano throw exception(wrong username or password)
            throw new WrongUsernameOrPasswordException();
        }
    }

    public void logout(int sessionId){
        sessionRepository.delete(sessionId);
    }

    public CreateProfileResponse getProfile(int sessionId){
        //1. Recuperare la sessione dal database
        Sessione s = sessionRepository.getSessionById(sessionId);
        //2. Recuperare l'id partecipante della sessione
        int utenteId= s.getPartecipanteId();
        //3. Recupero il partecipante dal database
        return utenteRepository.getUtenteById(utenteId);
    }

}