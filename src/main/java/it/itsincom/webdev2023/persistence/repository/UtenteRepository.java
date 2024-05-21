package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import it.itsincom.webdev2023.persistence.model.Utente;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UtenteRepository {

    private final DataSource dataSource;

    public UtenteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Utente createUtente(Utente utente) {
        if (checkDouble(utente.getNome(), utente.getCognome(), utente.getPasswordHash())) {
            throw new BadRequestException("Un utente con lo stesso nome, cognome e passwordHash esiste già");
        }
        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO utenti (id_utente, nome, cognome, email, password_hash, ruolo, telefono, data_nascita, id_indirizzo, data_registrazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setInt(1, utente.getId());
                    statement.setString(2, utente.getNome());
                    statement.setString(3, utente.getCognome());
                    statement.setString(4, utente.getEmail());
                    statement.setString(5, utente.getPasswordHash());
                    statement.setString(6, utente.getRuolo().name());
                    statement.setString(7, utente.getTelefono());
                    statement.setDate(8, utente.getDataNascita());
                    statement.setInt(9, utente.getIndirizzo());
                    statement.setTimestamp(10, utente.getRegistrazione());
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

    public boolean checkDouble(String nome, String cognome, String passwordHash) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM utenti WHERE nome = ? AND cognome = ? AND password_hash = ?")) {
                statement.setString(1, nome);
                statement.setString(2, cognome);
                statement.setString(3, passwordHash);
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

    public List<Utente> getAllUtente() {
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

}