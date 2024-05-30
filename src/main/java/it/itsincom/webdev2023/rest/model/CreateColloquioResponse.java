package it.itsincom.webdev2023.rest.model;

import it.itsincom.webdev2023.persistence.model.Esito;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class CreateColloquioResponse {
    private int idCandidatura;
    private int idInsegnante;
    private LocalDate data;
    private LocalTime orario;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOrario() {
        return orario;
    }

    public void setOrario(LocalTime orario) {
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
