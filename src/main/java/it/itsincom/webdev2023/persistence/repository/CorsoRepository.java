package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.Candidatura;
import it.itsincom.webdev2023.persistence.model.Corso;
import it.itsincom.webdev2023.persistence.model.Utente;
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

    // PUBBLICA UN NUOVO CORSO
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

    // OTTIENE LA LISTA DEI CORSI
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
                    Corso corso = new Corso();

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


    // OTTIENE LA LISTA DEGLI ID DEGLI UTENTI CANDIDATI AD UN DETERMINATO CORSO
    public List<Integer> getListaIdUtentiPerCorso(int idCorso) throws SQLException {
        List<Integer> listaIdUtenti = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT GROUP_CONCAT(id_utente) FROM candidature WHERE id_corso = ?")) {
                statement.setInt(1, idCorso);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String idUtente = resultSet.getString("GROUP_CONCAT(id_utente)");

                    if (idUtente != null && !idUtente.isEmpty()) {
                        String[] arrayIdUtenti = idUtente.split(",");
                        for (String userId : arrayIdUtenti) {
                            listaIdUtenti.add(Integer.parseInt(userId.trim()));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return listaIdUtenti;
    }

    public Corso getCorsoById(int id) throws SQLException {
        Corso corso = null;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM corsi WHERE id_corso = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    corso = new Corso();

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
                }
            }
        }

        return corso;
    }

    public void deleteCorso(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM corsi WHERE id_corso = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void iscriviUtente(int idCorso, int idUtente) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE candidature SET stato_candidatura = ? WHERE id_corso = ? AND id_utente = ?")) {
                statement.setString(1, StatoCandidatura.selezionato.name());
                statement.setInt(2, idCorso);
                statement.setInt(3, idUtente);

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rifiutaUtente(int idCorso, int idUtente) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE candidature SET stato_candidatura = ? WHERE id_corso = ? AND id_utente = ?")) {
                statement.setString(1, StatoCandidatura.rifiutato.name());
                statement.setInt(2, idCorso);
                statement.setInt(3, idUtente);

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
