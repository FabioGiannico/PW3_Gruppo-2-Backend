package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.Corso;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                    "SELECT nome_corso, descrizione, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine FROM corsi")) {
                var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    var corso = new Corso();

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
}
