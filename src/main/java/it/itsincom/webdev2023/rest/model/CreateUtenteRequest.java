package it.itsincom.webdev2023.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.itsincom.webdev2023.persistence.model.Ruolo;
import jakarta.ws.rs.FormParam;

import java.sql.Date;
import java.sql.Timestamp;

public class CreateUtenteRequest {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo;
    private Timestamp registrazione = new Timestamp(System.currentTimeMillis());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

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
