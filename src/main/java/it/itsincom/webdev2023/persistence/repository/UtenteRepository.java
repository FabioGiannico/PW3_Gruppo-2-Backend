package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.model.Utente;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UtenteRepository {

    private final DataSource dataSource;
    private final CorsoRepository corsoRepository;

    public UtenteRepository(DataSource dataSource, CorsoRepository corsoRepository) {
        this.dataSource = dataSource;
        this.corsoRepository = corsoRepository;
    }
    // TODO SISTEMARE LA TRY-CATCH IN AGGIUNTA
    public Utente createUtente(Utente utente) {
        if (checkDouble(utente.getEmail())) {
            throw new BadRequestException("Un utente con questa email è già registrato");
        }
        try {
            try (Connection connection = dataSource.getConnection()) {
                Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                utente.setRegistrazione(currentTimestamp);
                int defaultAddressId = 1;
                utente.setIndirizzo(defaultAddressId);
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO utenti (id_utente, nome, cognome, email, password_hash, ruolo) VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, utente.getId());
                    statement.setString(2, utente.getNome());
                    statement.setString(3, utente.getCognome());
                    statement.setString(4, utente.getEmail());
                    statement.setString(5, utente.getPasswordHash());
                    statement.setString(6, utente.getRuolo().name());
                    statement.executeUpdate();
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        utente.setId(id);
                    }

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    public boolean checkDouble(String email) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM utenti WHERE email = ?")) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // TODO SISTEMARE LA TRY-CATCH IN AGGIUNTA
    public List<Utente> getAllUtenti() {
        List<Utente> listaUtenti = new ArrayList<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT id_utente, nome, cognome, email, password_hash, ruolo, telefono, data_nascita, id_indirizzo, data_registrazione FROM utenti")) {
                    var resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        var utente = new Utente();
                        utente.setId(resultSet.getInt("id_utente"));
                        utente.setNome(resultSet.getString("nome"));
                        utente.setCognome(resultSet.getString("cognome"));
                        utente.setEmail(resultSet.getString("email"));
                        utente.setPasswordHash(resultSet.getString("password_hash"));
                        utente.setRuolo(Ruolo.valueOf(resultSet.getString("ruolo")));
                        utente.setTelefono(resultSet.getString("telefono"));
                        utente.setDataNascita(resultSet.getDate("data_nascita"));
                        utente.setIndirizzo(resultSet.getInt("id_indirizzo"));
                        utente.setRegistrazione(resultSet.getTimestamp("data_registrazione"));
                        listaUtenti.add(utente);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaUtenti;

    }
    // TODO SISTEMARE LA TRY-CATCH IN AGGIUNTA
    public Optional<Utente> findByEmailPasswordHash(String email, String passwordHash){
        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT id_utente, nome, cognome, email, password_hash FROM utenti WHERE email = ? AND password_hash = ?")) {
                    statement.setString(1, email);
                    statement.setString(2, passwordHash);
                    var resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        var utente = new Utente();
                        utente.setId(resultSet.getInt("id_utente"));
                        utente.setNome(resultSet.getString("nome"));
                        utente.setCognome(resultSet.getString("cognome"));
                        utente.setEmail(resultSet.getString("email"));
                        utente.setPasswordHash(resultSet.getString("password_hash"));

                        return Optional.of(utente);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    // TODO SISTEMARE LA TRY-CATCH IN AGGIUNTA
    public Utente getUtenteById(int id) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT id_utente, nome, cognome, email, password_hash, ruolo, telefono, data_nascita, id_indirizzo, data_registrazione FROM utenti WHERE id_utente = ?")) {
                    statement.setInt(1, id);
                    var resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        var utente = new Utente();
                        utente.setId(resultSet.getInt("id_utente"));
                        utente.setNome(resultSet.getString("nome"));
                        utente.setCognome(resultSet.getString("cognome"));
                        utente.setEmail(resultSet.getString("email"));
                        utente.setPasswordHash(resultSet.getString("password_hash"));
                        utente.setRuolo(Ruolo.valueOf(resultSet.getString("ruolo")));
                        utente.setTelefono(resultSet.getString("telefono"));
                        utente.setDataNascita(resultSet.getDate("data_nascita"));
                        utente.setIndirizzo(resultSet.getInt("id_indirizzo"));
                        utente.setRegistrazione(resultSet.getTimestamp("data_registrazione"));
                        return utente;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Utente getUtenteByRuolo(Ruolo ruolo) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT id_utente, nome, cognome, email, password_hash, ruolo, telefono, data_nascita, id_indirizzo, data_registrazione FROM utenti WHERE ruolo = ?")) {
                    statement.setString(1, ruolo.name());
                    var resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        var utente = new Utente();
                        utente.setId(resultSet.getInt("id_utente"));
                        utente.setNome(resultSet.getString("nome"));
                        utente.setCognome(resultSet.getString("cognome"));
                        utente.setEmail(resultSet.getString("email"));
                        utente.setPasswordHash(resultSet.getString("password_hash"));
                        utente.setRuolo(Ruolo.valueOf(resultSet.getString("ruolo")));
                        utente.setTelefono(resultSet.getString("telefono"));
                        utente.setDataNascita(resultSet.getDate("data_nascita"));
                        utente.setIndirizzo(resultSet.getInt("id_indirizzo"));
                        utente.setRegistrazione(resultSet.getTimestamp("data_registrazione"));
                        return utente;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // GRAZIE ALL'ID DEL CORSO CHIAMA IL METODO corsoRepository.getListaIdUtentiPerCorso(idCorso); E OTTIENE LA LISTA DEGLI ID DEI CANDIDATI
    // CON UN CICLO FOR OTTIENE OGNI UTENTE TRAMITE IL SUO ID
    public List<Utente> getListaUtentiById(int idCorso) throws SQLException {
        List<Utente> listaUtenti = new ArrayList<>();
        List<Integer> listaId = corsoRepository.getListaIdUtentiPerCorso(idCorso);

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT nome, cognome, email FROM utenti WHERE id_utente = ?")) {
                // CON UN CICLO FOR SI SOSTITUISCE L'id_utente PER OGNI ID NELLA LISTA
                for (int i = 1; i <= listaId.size(); i++){
                    statement.setInt(1, i);

                    var resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        var utente = new Utente();
                        utente.setNome(resultSet.getString("nome"));
                        utente.setCognome(resultSet.getString("cognome"));
                        utente.setEmail(resultSet.getString("email"));

                        listaUtenti.add(utente);
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaUtenti;
    }

    public void deleteUtente(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM utenti WHERE id_utente = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}