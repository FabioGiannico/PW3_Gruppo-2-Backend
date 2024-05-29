package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.Esito;

import java.sql.Time;
import java.util.Date;

public class CreateColloquioResponse {
    private int idCandidatura;
    private int idInsegnante;
    private Date data;
    private Time orario;
    private String luogo;
    private Esito esito;

    public int getIdCandidatura() {
        return idCandidatura;
    }

    public void setIdCandidatura(int idCandidatura) {
        this.idCandidatura = idCandidatura;
    }

    public int getIdInsegnante() {
        return idInsegnante;
    }

    public void setIdInsegnante(int idInsegnante) {
        this.idInsegnante = idInsegnante;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getOrario() {
        return orario;
    }

    public void setOrario(Time orario) {
        this.orario = orario;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Esito getEsito() {
        return esito;
    }

    public void setEsito(Esito esito) {
        this.esito = esito;
    }
}
