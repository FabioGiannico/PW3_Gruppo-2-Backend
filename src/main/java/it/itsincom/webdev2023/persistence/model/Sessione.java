package it.itsincom.webdev2023.persistence.model;

import java.sql.Timestamp;

public class Sessione {

    private int id;
    private Timestamp dataCreazione;
    private int partecipanteId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getPartecipanteId() {
        return partecipanteId;
    }

    public void setPartecipanteId(int partecipanteId) {
        this.partecipanteId = partecipanteId;
    }

}
