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
                    setCorso(corso, resultSet);
                    corsi.add(corso);
                }
            }
        }
        return corsi;
    }

    // TROVA I CORSI DU UNA CERTA CATEGORIA
    public List<Corso> cercaCorsiPerCategoria(String categoria) throws SQLException {
        List<Corso> corsi = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT id_corso, nome_corso, descrizione, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine FROM corsi WHERE categoria = ?")) {
                statement.setString(1, categoria);
                var resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Corso corso = new Corso();
                    setCorso(corso, resultSet);
                    corsi.add(corso);
                }
            }
        }
        if (corsi.isEmpty()) {
            throw new SQLException("Categoria " + categoria + " non trovata");
        }
        return corsi;
    }

    // CANDIDA UN UTENTE AD UN CORSO
    public void candidaturaCorso(int idUtente, int idCorso) throws SQLException {
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

    // OTTIENE TUTTE LE CANDIDATURE PRESENTI
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

    // OTTIENE IL CORSO TRAMITE IL SUO ID
    public Corso getCorsoById(int id) throws SQLException {
        Corso corso = null;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT id_corso, nome_corso, descrizione, categoria, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine FROM corsi WHERE id_corso = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    corso = new Corso();
                    setCorso(corso, resultSet);
                }
            }
        }
        return corso;
    }

    // ELIMINA UN CORSO
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

    // IMPOSTA IL RISULTATO DEL TEST DELL'UTENTE E MODIFICA LO STATO DELLA CANDIDATURA
    public void risultatoTest(int idCorso, int idUtente, int risultatoTest) throws RuntimeException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE candidature SET risultato_test = ? WHERE id_corso = ? AND id_utente = ?")) {
                statement.setInt(1, risultatoTest);
                statement.setInt(2, idCorso);
                statement.setInt(3, idUtente);

                statement.executeUpdate();
            }

            int punteggioMinimo = 70;
            if (risultatoTest < punteggioMinimo) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE candidature SET stato_candidatura = ? WHERE id_corso = ? AND id_utente = ?")) {
                    statement.setString(1, StatoCandidatura.bocciato.name());
                    statement.setInt(2, idCorso);
                    statement.setInt(3, idUtente);

                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE candidature SET stato_candidatura = ? WHERE id_corso = ? AND id_utente = ?")) {
                    statement.setString(1, StatoCandidatura.convocatoColloquio.name());
                    statement.setInt(2, idCorso);
                    statement.setInt(3, idUtente);

                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // OTTIENE LO STATO DELLA CANDIDATURA
    private void checkStatoCandidatura(int idCorso, int idUtente, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT stato_candidatura FROM candidature WHERE id_corso = ? AND id_utente = ?")) {
            statement.setInt(1, idCorso);
            statement.setInt(2, idUtente);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String statoCandidatura = resultSet.getString("stato_candidatura");
                if (!StatoCandidatura.convocatoColloquio.name().equals(statoCandidatura)) {
                    throw new RuntimeException("L'utente non è stato convocato ad un colloquio");
                }
            }
        }
    }

    // ACCETTA LA CANDIDATURA DI UN UTENTE
    public void accettaUtente(int idCorso, int idUtente) {
        try (Connection connection = dataSource.getConnection()) {
            checkStatoCandidatura(idCorso, idUtente, connection);
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

    // REINDIRIZZA LA CANDIDATURA DI UN UTENTE
    public void reindirizzaUtente(int idCorso, int idUtente) {
        try (Connection connection = dataSource.getConnection()) {
            checkStatoCandidatura(idCorso, idUtente, connection);
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE candidature SET stato_candidatura = ? WHERE id_corso = ? AND id_utente = ?")) {
                statement.setString(1, StatoCandidatura.reindirizzato.name());
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


    private void setCorso(Corso corso, ResultSet resultSet) throws SQLException {
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
