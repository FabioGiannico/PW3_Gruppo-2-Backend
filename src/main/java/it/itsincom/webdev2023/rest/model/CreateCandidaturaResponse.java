package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.StatoCandidatura;

import java.sql.Timestamp;

public class CreateCandidaturaResponse {

    private int idCorso;
    private Timestamp data;
    private StatoCandidatura stato;
    private int risultato;


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
