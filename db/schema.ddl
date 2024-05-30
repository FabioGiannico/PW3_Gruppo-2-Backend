DROP DATABASE IF EXISTS itsincom;
create database itsincom;
use itsincom;
CREATE TABLE Indirizzi
(
    id_indirizzo        INT PRIMARY KEY AUTO_INCREMENT,
    indirizzo_residenza VARCHAR(255),
    citta               VARCHAR(255),
    provincia           VARCHAR(255),
    cap                 VARCHAR(5)
);
CREATE TABLE Utenti
(
    id_utente          INT PRIMARY KEY AUTO_INCREMENT,
    nome               VARCHAR(255)                                    NOT NULL,
    cognome            VARCHAR(255)                                    NOT NULL,
    email              VARCHAR(255)                                    NOT NULL UNIQUE,
    password_hash      VARCHAR(255)                                    NOT NULL,
    ruolo              ENUM ('utente', 'insegnante', 'amministratore') NOT NULL DEFAULT 'utente',
    telefono           VARCHAR(10),
    data_nascita       DATE,
    id_indirizzo       INT,
    data_registrazione TIMESTAMP                                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_indirizzo) REFERENCES Indirizzi (id_indirizzo)
);
CREATE TABLE Corsi
(
    id_corso          INT PRIMARY KEY AUTO_INCREMENT,
    nome_corso        VARCHAR(255) NOT NULL,
    descrizione       TEXT,
    categoria         VARCHAR(255) NOT NULL,
    durata            INT,
    programma         TEXT,
    requisiti         TEXT,
    posti_disponibili INT,
    data_inizio       DATE,
    data_fine         DATE,
    immagine VARCHAR(255)
);
CREATE TABLE Candidature
(
    id_candidatura    INT PRIMARY KEY AUTO_INCREMENT,
    id_utente         INT       NOT NULL,
    id_corso          INT       NOT NULL,
    data_candidatura  TIMESTAMP NOT NULL                             DEFAULT CURRENT_TIMESTAMP,
    stato_candidatura ENUM ('in_attesa', 'selezionato', 'rifiutato') DEFAULT 'in_attesa',
    risultato_test    INT,
    note              TEXT,
    FOREIGN KEY (id_utente) REFERENCES Utenti (id_utente),
    FOREIGN KEY (id_corso) REFERENCES Corsi (id_corso)
);
CREATE TABLE Colloqui
(
    id_colloquio    INT PRIMARY KEY AUTO_INCREMENT,
    id_candidatura  INT NOT NULL,
    id_insegnante   INT NOT NULL,
    data_colloquio  DATE,
    ora_colloquio   TIME,
    luogo_colloquio VARCHAR(255),
    esito_colloquio ENUM ('positivo', 'negativo') DEFAULT NULL,
    note_colloquio  TEXT,
    FOREIGN KEY (id_candidatura) REFERENCES Candidature (id_candidatura),
    FOREIGN KEY (id_insegnante) REFERENCES Utenti (id_utente)
);
CREATE TABLE Sessioni
(
    id_sessione     INT AUTO_INCREMENT PRIMARY KEY,
    id_utente       INT                                 NOT NULL,
    data_creazione  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_id_utente
        FOREIGN KEY (id_utente) REFERENCES utenti (id_utente)
);
CREATE TRIGGER prevenire_amministratore_insegnante_candidatura
    BEFORE INSERT
    ON Candidature
    FOR EACH ROW
BEGIN
    DECLARE user_role ENUM ('utente', 'insegnante', 'amministratore');
    SELECT ruolo INTO user_role FROM Utenti WHERE id_utente = NEW.id_utente;
    IF user_role IN ('insegnante', 'amministratore') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insegnanti e amministratori non possono candidarsi ai corsi.';
END IF;
END;
CREATE TRIGGER controllare_insegnante_in_colloquio
    BEFORE INSERT
    ON Colloqui
    FOR EACH ROW
BEGIN
    DECLARE user_role ENUM ('utente', 'insegnante', 'amministratore');
    SELECT ruolo INTO user_role FROM Utenti WHERE id_utente = NEW.id_insegnante;
    IF user_role != 'insegnante' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo gli insegnanti possono essere referenziati nei colloqui.';
END IF;
END;
CREATE TRIGGER solo_amministratori_possono_aggiungere_corsi
    BEFORE INSERT ON Corsi
    FOR EACH ROW
BEGIN
    DECLARE user_role ENUM('utente', 'insegnante', 'amministratore');

    -- Assuming that the user ID who is trying to add the course is passed as a session variable
    SELECT ruolo INTO user_role FROM Utenti WHERE id_utente = @current_user_id;

    IF user_role != 'amministratore' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Solo gli amministratori possono aggiungere nuovi corsi.';
    END IF;
END;
INSERT INTO Indirizzi (indirizzo_residenza, citta, provincia, cap)
VALUES
    ('Via Roma 1', 'Busto Arsizio', 'VA', '21052'),
    ('Via Milano 2', 'Milano', 'MI', '20100'),
    ('Via Torino 3', 'Legnano', 'MI', '10100'),
    ('Via Napoli 4', 'Lonate Pozzolo', 'VA', '80100'),
    ('Via Firenze 5', 'Varese', 'VA', '50100'),
    ('Via Venezia 6', 'Gallarate', 'VA', '30100'),
    ('Via Palermo 7', 'Bergamo', 'BG', '90100'),
    ('Via Bologna 8', 'Legnano', 'MI', '40100'),
    ('Via Genova 9', 'Genova', 'GE', '16100'),
    ('Via Bari 10', 'Bari', 'BA', '70100');
INSERT INTO Utenti (nome, cognome, email, password_hash, ruolo, telefono, data_nascita, id_indirizzo)
VALUES
    ('Admin', 'One', 'admin.one@example.com', 'fHHAiTFtevniHnzMK68AqvIecEFT5UbGe5hjkbDwQYWhZ1yQE7urQlftZkyqQJ35/l0rHq3tjpRWkbFm0LtROg', 'amministratore', '1234567890', '1980-01-01', 1),
    ('Luigi', 'Verdi', 'luigi.verdi@example.com', 'hyty5h5yb5yyb5h5by', 'utente', '2345678901', '1975-02-02', 2),
    ('Anna', 'Bianchi', 'anna.bianchi@example.com', '5nyhby5y5tbtb5t', 'utente', '3456789012', '1985-03-03', 3),
    ('Paolo', 'Neri', 'paolo.neri@example.com', '5hj5hy5yby5v55', 'utente', '4567890123', '1990-04-04', 4),
    ('Luca', 'Gialli', 'luca.gialli@example.com', '5b5bb5byy5y5y5hy5b', 'utente', '5678901234', '1970-05-05', 5),
    ('Sara', 'Rosa', 'sara.rosa@example.com', '5by5y5yyh55yhyh55hy', 'utente', '6789012345', '1988-06-06', 6),
    ('Elena', 'Viola', 'elena.viola@example.com', 'b5yby55yby5', 'utente', '7890123456', '1992-07-07', 7),
    ('Giorgio', 'Blu', 'giorgio.blu@example.com', '5yb5byy5hhhy55', 'utente', '8901234567', '1983-08-08', 8),
    ('Francesca', 'Grigi', 'francesca.grigi@example.com', '5yh5byb5yvb5yg5t', 'utente', '9012345678', '1995-09-09', 9),
    ('Alessandro', 'Nero', 'alessandro.nero@example.com', '5byby5y5byh5jyj5y', 'utente', '0123456789', '1969-10-10', 10);
INSERT INTO Corsi (nome_corso, descrizione, categoria, durata, programma, requisiti, posti_disponibili, data_inizio, data_fine, immagine)
VALUES
    ('Web Developer', 'Corso di sviluppo web', 'Informatica', 2, 'HTML, CSS, JavaScript', 'Nessuno', 20, '2024-06-01', '2024-08-31', 'https://th.bing.com/th/id/OIP.yhcKQdAh1orG4s-1Qqe70QAAAA?rs=1&pid=ImgDetMain'),
    ('Data Science', 'Corso di data science', 'Informatica', 2, 'Python, R, Machine Learning', 'Conoscenze di base di programmazione', 15, '2024-07-01', '2024-12-31', 'https://th.bing.com/th/id/R.852c0d7428602819c4e68630549df1fd?rik=DAySL8R3NeVRUg&pid=ImgRaw&r=0'),
    ('Web Designer', 'Corso grafica digitale', 'Comunicazione', 2, 'Photoshop, Illustrator', 'Nessuno', 25, '2024-06-15', '2024-09-15', 'https://www.pngmart.com/files/7/Web-Design-Transparent-Images-PNG.png'),
    ('Digital Marketing', 'Corso di marketing digitale', 'Comunicazione', 2, 'SEO, SEM, Social Media', 'Nessuno', 30, '2024-07-15', '2024-11-15','https://th.bing.com/th/id/OIP.8bfo6rY2iWETdrC38j1P6gHaGr?rs=1&pid=ImgDetMain'),
    ('Cloud Developer', 'Corso di tecnologie cloud', 'Informatica', 2, 'Microsoft Azure', 'Nessuno', 20, '2024-06-01', '2024-09-30','https://th.bing.com/th/id/OIP.qjGHzGv29s5OAZzxDaIK0QHaHa?rs=1&pid=ImgDetMain'),
    ('IOT', 'Corso  di IOT', 'Nuove Tecnologie', 1, 'Domotica', 'Nessuno', 10, '2024-08-01', '2024-11-30','https://www.pngmart.com/files/21/Internet-Of-Things-IOT-Vector-PNG-Transparent.png'),
    ('Manufacturing Design', 'Corso di design laboratoriale', 'Nuove Tecnologie', 1, 'Tecniche laboratoriali digitali', 'Nessuno', 15, '2024-06-20', '2024-08-20','https://th.bing.com/th/id/OIP.JP0uTwV-jLqhIvV6z9gdHwAAAA?rs=1&pid=ImgDetMain');
