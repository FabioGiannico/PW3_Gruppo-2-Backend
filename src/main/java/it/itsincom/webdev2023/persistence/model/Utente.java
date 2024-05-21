package it.itsincom.webdev2023.persistence.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Utente {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash;
    private Ruolo ruolo;
    private String telefono;
    private int indirizzo;      //Riferisce alla tabella degli indirizzi di casa
    private Date dataNascita;
    private Timestamp registrazione;


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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public int getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(int indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Timestamp getRegistrazione() {
        return registrazione;
    }

    public void setRegistrazione(Timestamp registrazione) {
        this.registrazione = registrazione;
    }
}
