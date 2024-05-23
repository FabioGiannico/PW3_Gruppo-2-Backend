package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.Ruolo;

import java.sql.Date;
import java.sql.Timestamp;

public class CreateUtenteResponse {

    private int id;
    private String nome;
    private String cognome;

    private String email;
    private Ruolo ruolo;
    private String telefono;
    private Date dataNascita;
    private int indirizzo;
    private Timestamp dataRegistrazione;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public int getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(int indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Timestamp getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Timestamp dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}
