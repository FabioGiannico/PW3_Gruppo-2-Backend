package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.Ruolo;

import java.sql.Date;
import java.sql.Timestamp;

public class CreateUtenteRequest {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Timestamp registrazione;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegistrazione() {
        return registrazione;
    }

    public void setRegistrazione(Timestamp registrazione) {
        this.registrazione = registrazione;
    }
}
