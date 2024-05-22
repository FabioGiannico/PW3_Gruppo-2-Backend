package it.itsincom.webdev2023.persistence.repository;


import it.itsincom.webdev2023.persistence.model.Sessione;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class SessionRepository {

    private final DataSource dataSource;

    public SessionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertSession(int idPartecipante) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO session (utente_id) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, idPartecipante);
                statement.executeUpdate();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    return id;
                }
            }
        }
        throw new SQLException("Cannot insert new session for partecipante" + idPartecipante);
    }

    public void delete(int sessionId) {

        try {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM session WHERE id = ?")) {
                    statement.setInt(1, sessionId);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Sessione getSessionById(int sessionId) {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id, utente_id, data_creazione FROM session WHERE id = ?")) {
                statement.setInt(1, sessionId);
                ;
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    var sessione = new Sessione();
                    sessione.setId(rs.getInt("id"));
                    sessione.setPartecipanteId(rs.getInt("utente_id"));
                    sessione.setDataCreazione(rs.getTimestamp("data_creazione"));
                    return sessione;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("non è presente alcuna sessione con id" + sessionId);

    }
}
