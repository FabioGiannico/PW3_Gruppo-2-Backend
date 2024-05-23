package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.Ruolo;
import jakarta.ws.rs.FormParam;

import java.sql.Timestamp;

public class CreateUtenteRequest {

    @FormParam("nome")
    private String nome;
    @FormParam("cognome")
    private String cognome;
    @FormParam("email")
    private String email;
    @FormParam("password")
    private String password;
    private Timestamp registrazione = new Timestamp(System.currentTimeMillis());
    @FormParam("ruolo")
    private Ruolo ruolo;

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

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
