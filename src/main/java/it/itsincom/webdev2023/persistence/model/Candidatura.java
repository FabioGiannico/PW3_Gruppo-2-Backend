package it.itsincom.webdev2023.persistence.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Candidatura {
    private int id;
    private int idUtente;
    private int idCorso;
    private Timestamp data;
    private StatoCandidatura stato;
    private int risultato;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public StatoCandidatura getStato() {
        return stato;
    }

    public void setStato(StatoCandidatura stato) {
        this.stato = stato;
    }

    public int getRisultato() {
        return risultato;
    }

    public void setRisultato(int risultato) {
        this.risultato = risultato;
    }
}
