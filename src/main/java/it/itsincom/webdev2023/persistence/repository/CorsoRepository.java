package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.Candidatura;
import it.itsincom.webdev2023.persistence.model.Corso;
import it.itsincom.webdev2023.persistence.model.StatoCandidatura;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CorsoRepository {

    private final DataSource dataSource;

    public CorsoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Corso createCorso(Corso corso) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO corsi (nome_corso, descrizione, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, corso.getNome());
                statement.setString(2, corso.getDescrizione());
                statement.setString(3, corso.getCategoria());
                statement.setInt(4, corso.getDurata());
                statement.setString(5, corso.getProgramma());
                statement.setString(6, corso.getRequisiti());
                statement.setInt(7, corso.getPostiDisponibili());
                statement.setDate(8, Date.valueOf(corso.getDataInizio()));
                statement.setDate(9, Date.valueOf(corso.getDataFine()));

                statement.executeUpdate();

                // Imposta l'ID generato nel corso (se necessario)
                try (var rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        corso.setId(rs.getInt(1));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return corso;
    }

    public List<Corso> getAllCorsi() throws SQLException {
        List<Corso> corsi = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT id_corso, nome_corso, descrizione, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine FROM corsi")) {
                var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    var corso = new Corso();

                    corso.setId(resultSet.getInt("id_corso"));
                    corso.setNome(resultSet.getString("nome_corso"));
                    corso.setDescrizione(resultSet.getString("descrizione"));
                    corso.setCategoria(resultSet.getString("categoria"));
                    corso.setDurata(resultSet.getInt("durata"));
                    corso.setProgramma(resultSet.getString("programma"));
                    corso.setRequisiti(resultSet.getString("requisiti"));
                    corso.setPostiDisponibili(resultSet.getInt("posti_disponibili"));
                    corso.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());
                    corso.setDataFine(resultSet.getDate("data_fine").toLocalDate());

                    corsi.add(corso);
                }
            }
        }
        return corsi;
    }

    public List<Corso> cercaCorsiPerCategoria(String nomeCategoria) throws SQLException {
        List<Corso> corsi = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM corsi WHERE categoria = ?")) {
                statement.setString(1, nomeCategoria);
                var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    var corso = new Corso();

                    corso.setId(resultSet.getInt("id_corso"));
                    corso.setNome(resultSet.getString("nome_corso"));
                    corso.setDescrizione(resultSet.getString("descrizione"));
                    corso.setCategoria(resultSet.getString("categoria"));
                    corso.setDurata(resultSet.getInt("durata"));
                    corso.setProgramma(resultSet.getString("programma"));
                    corso.setRequisiti(resultSet.getString("requisiti"));
                    corso.setPostiDisponibili(resultSet.getInt("posti_disponibili"));
                    corso.setDataInizio(resultSet.getDate("data_inizio").toLocalDate());
                    corso.setDataFine(resultSet.getDate("data_fine").toLocalDate());

                    corsi.add(corso);
                }
            }
        }
        if (corsi.isEmpty()) {
            throw new SQLException("Categoria non trovata: " + nomeCategoria);
        }
        return corsi;
    }

    public void candidatiPerCorso(int idUtente, int idCorso) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO candidature (id_utente, id_corso, data_candidatura, stato_candidatura) VALUES (?, ?, ?, ?)")) {
                statement.setInt(1, idUtente);
                statement.setInt(2, idCorso);
                statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                statement.setString(4, StatoCandidatura.in_attesa.name());

                statement.executeUpdate();
            }
        }
    }

    public List<Candidatura> getAllCandidature() throws SQLException {
        List<Candidatura> candidature = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM candidature")) {
                var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    var candidatura = new Candidatura();

                    candidatura.setIdUtente(resultSet.getInt("id_utente"));
                    candidatura.setIdCorso(resultSet.getInt("id_corso"));
                    candidatura.setData(Timestamp.valueOf(resultSet.getTimestamp("data_candidatura").toLocalDateTime()));
                    candidatura.setStato(StatoCandidatura.valueOf(resultSet.getString("stato_candidatura")));

                    candidature.add(candidatura);
                }
            }
        }
        return candidature;
    }
}
