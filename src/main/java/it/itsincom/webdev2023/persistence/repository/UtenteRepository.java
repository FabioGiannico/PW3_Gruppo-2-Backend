package it.itsincom.webdev2023.persistence.repository;

import it.itsincom.webdev2023.persistence.model.*;
import it.itsincom.webdev2023.rest.model.CreateCandidaturaResponse;
import it.itsincom.webdev2023.rest.model.CreateColloquioResponse;
import it.itsincom.webdev2023.rest.model.CreateModifyRequest;
import it.itsincom.webdev2023.rest.model.CreateProfileResponse;
import it.itsincom.webdev2023.service.HashCalculator;
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
    private final HashCalculator hashCalculator;

    public UtenteRepository(DataSource dataSource, CorsoRepository corsoRepository, HashCalculator hashCalculator) {
        this.dataSource = dataSource;
        this.corsoRepository = corsoRepository;
        this.hashCalculator = hashCalculator;
    }

    private String getUtenteByNomeQuery = "SELECT u.id_utente, u.nome, u.cognome, u.email, u.telefono, u.data_nascita, u.data_registrazione, i.indirizzo_residenza, i.citta, i.provincia, i.cap FROM utenti u LEFT JOIN indirizzi i ON u.id_indirizzo = i.id_indirizzo WHERE u.nome = ?";
    private String getUtenteByIdQuery = "SELECT u.id_utente, u.nome, u.cognome, u.email, u.ruolo, u.telefono, u.data_nascita, u.data_registrazione, i.indirizzo_residenza, i.citta, i.provincia, i.cap FROM utenti u LEFT JOIN indirizzi i ON u.id_indirizzo = i.id_indirizzo WHERE u.id_utente = ?";


    // CREA UN UTENTE
    public Utente createUtente(Utente utente) {
        if (checkDouble(utente.getEmail())) {
            throw new BadRequestException("Un utente con questa email è già registrato");
        }
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utente;
    }

    // CONTROLLA SE E' GIA' PRESENTE UN UTENTE CON LA STESSA EMAIL
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

    // OTTIENE LA LISTA DEGLI UTENTI
    public List<Utente> getAllUtenti() {
        List<Utente> listaUtenti = new ArrayList<>();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaUtenti;
    }

    // ELIMINA UN UTENTE
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

    // TROVA UN UTENTE TRAMITE EMAIL E PSW
    public Optional<Utente> findUtenteByEmailPasswordHash(String email, String passwordHash) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    // OTTIENE L'UTENTE TRAMITE L'ID
    public CreateProfileResponse getUtenteById(int id) {
        var res = new CreateProfileResponse();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(getUtenteByIdQuery)) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setIndirizzoResidenza(resultSet.getString("indirizzo_residenza"));
                    indirizzo.setCitta(resultSet.getString("citta"));
                    indirizzo.setProvincia(resultSet.getString("provincia"));
                    indirizzo.setCap(resultSet.getString("cap"));

                    res.setId(resultSet.getInt("id_utente"));
                    res.setNome(resultSet.getString("nome"));
                    res.setCognome(resultSet.getString("cognome"));
                    res.setEmail(resultSet.getString("email"));
                    res.setRuolo(Ruolo.valueOf(resultSet.getString("ruolo")));
                    res.setTelefono(resultSet.getString("telefono"));
                    res.setDataNascita(resultSet.getDate("data_nascita"));
                    res.setIndirizzo(indirizzo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    // OTTIENE L'UTENTE TRAMITE IL NOME
    public CreateProfileResponse getUtenteByNome(String nome) {
        CreateProfileResponse res = new CreateProfileResponse();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(getUtenteByNomeQuery)) {
                statement.setString(1, nome);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    Indirizzo indirizzo = new Indirizzo();
                    indirizzo.setIndirizzoResidenza(resultSet.getString("indirizzo_residenza"));
                    indirizzo.setCitta(resultSet.getString("citta"));
                    indirizzo.setProvincia(resultSet.getString("provincia"));
                    indirizzo.setCap(resultSet.getString("cap"));

                    res.setId(resultSet.getInt("id_utente"));
                    res.setNome(resultSet.getString("nome"));
                    res.setCognome(resultSet.getString("cognome"));
                    res.setEmail(resultSet.getString("email"));
                    res.setTelefono(resultSet.getString("telefono"));
                    res.setDataNascita(resultSet.getDate("data_nascita"));
                    res.setIndirizzo(indirizzo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    // OTTIENE LE CANDIDATURE DI UN UTENTE
    public List<CreateCandidaturaResponse> getCandidatureUtenteById(int idUtente) throws SQLException {
        List<CreateCandidaturaResponse> listaCandidature = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id_corso, data_candidatura, stato_candidatura, risultato_test FROM candidature WHERE id_utente = ?")) {
                statement.setInt(1, idUtente);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        CreateCandidaturaResponse candidatura = new CreateCandidaturaResponse();
                        candidatura.setIdCorso(resultSet.getInt("id_corso"));
                        candidatura.setData(resultSet.getTimestamp("data_candidatura"));
                        candidatura.setStato(StatoCandidatura.valueOf(resultSet.getString("stato_candidatura")));
                        candidatura.setRisultato(resultSet.getInt("risultato_test"));

                        listaCandidature.add(candidatura);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCandidature;
    }

    // OTTIENE I COLLOQUI DI UN UTENTE
    public List<CreateColloquioResponse> getColloquiUtenteById(int idUtente) throws SQLException {
        int idCandidatura = getIdCandidaturaByIdUtente(idUtente);
        List<CreateColloquioResponse> listaColloqui = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id_candidatura, id_insegnante, data_colloquio, ora_colloquio, luogo_colloquio, esito_colloquio FROM colloqui WHERE id_candidatura = ?")) {
                statement.setInt(1, idCandidatura);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        CreateColloquioResponse colloquio = new CreateColloquioResponse();
                        colloquio.setIdCandidatura(resultSet.getInt("id_candidatura"));
                        colloquio.setIdInsegnante(resultSet.getInt("id_insegnante"));
                        colloquio.setData(resultSet.getDate("data_colloquio").toLocalDate());
                        colloquio.setOrario(resultSet.getTime("ora_colloquio").toLocalTime());
                        colloquio.setLuogo(resultSet.getString("luogo_colloquio"));
                        colloquio.setEsito(Esito.valueOf(resultSet.getString("esito_colloquio")));

                        listaColloqui.add(colloquio);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaColloqui;
    }

    public int getIdCandidaturaByIdUtente(int idUtente){
        int idCandidatura = 0;
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id_candidatura FROM candidature WHERE id_utente = ?")) {
                statement.setInt(1, idUtente);
                ResultSet res = statement.executeQuery();

                while (res.next()) {
                    idCandidatura = res.getInt("id_candidatura");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idCandidatura;
    }
    public void modificaInfo(int idUtente, CreateModifyRequest req) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE utenti SET ");
        List<Object> parameters = new ArrayList<>();

        if (req.getNome() != null) {
            query.append("nome = ?, ");
            parameters.add(req.getNome());
        }
        if (req.getCognome() != null) {
            query.append("cognome = ?, ");
            parameters.add(req.getCognome());
        }
        if (req.getEmail() != null) {
            query.append("email = ?, ");
            parameters.add(req.getEmail());
        }
        if (req.getTelefono() != null) {
            query.append("telefono = ?, ");
            parameters.add(req.getTelefono());
        }
        if (req.getDataNascita() != null) {
            query.append("data_nascita = ?, ");
            parameters.add(req.getDataNascita());
        }
        if (req.getIndirizzo() != null) {
            query.append("id_indirizzo = ?, ");
            parameters.add(setIndirizzo(req));
        }

        query.setLength(query.length() - 2);
        query.append(" WHERE id_utente = ?");
        parameters.add(idUtente);

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SELEZIONA L'INDIRIZZO TRAMITE L'UTENTE
    public Indirizzo getIndirizzo(Utente u){
        Indirizzo i = new Indirizzo();
        int idIndirizzo = u.getIndirizzo();

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT indirizzo_residenza, citta, provincia, cap FROM indirizzi WHERE id_indirizzo = ?")) {
                statement.setInt(1, idIndirizzo);
                ResultSet res = statement.executeQuery();
                while (res.next()){
                    i.setIndirizzoResidenza(res.getString("indirizzo_residenza"));
                    i.setCitta(res.getString("citta"));
                    i.setProvincia(res.getString("provincia"));
                    i.setCap(res.getString("cap"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return i;
    }

    // SELEZIONA L'INDIRIZZO: ESISTE => RETURN ID  /  NON ESISTE => RETURN -1
    public int getIdIndirizzo(CreateModifyRequest indirizzo) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id_indirizzo FROM indirizzi WHERE indirizzo_residenza = ? AND citta = ? AND provincia = ? AND cap = ?")) {
                statement.setString(1, indirizzo.getIndirizzo());
                statement.setString(2, indirizzo.getCitta());
                statement.setString(3, indirizzo.getProvincia());
                statement.setString(4, indirizzo.getCap());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("id_indirizzo");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // CONTROLLA SE ESISTE GIA' UN INDIRIZZO, ALTRIMENTI LO CREA
    public int setIndirizzo(CreateModifyRequest indirizzo) throws SQLException {
        int idIndirizzo = getIdIndirizzo(indirizzo);
        if (idIndirizzo == -1) {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO indirizzi (indirizzo_residenza, citta, provincia, cap) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, indirizzo.getIndirizzo());
                    statement.setString(2, indirizzo.getCitta());
                    statement.setString(3, indirizzo.getProvincia());
                    statement.setString(4, indirizzo.getCap());
                    statement.executeUpdate();

                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idIndirizzo = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Inserimento dell'indirizzo fallito");
                        }
                    }
                }
            }
        }
        return idIndirizzo;
    }
}