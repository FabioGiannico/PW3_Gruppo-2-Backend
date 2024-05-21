package it.itsincom.webdev2023.persistence.model;

import java.util.Date;

public class Corso {

    private int id;
    private String nome;
    private String descrizione;
    private String categoria;
    private int durata;
    private String programma;
    private String requisiti;
    private int postiDisponibili;
    private Date dataInizio;
    private Date dataFine;


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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getProgramma() {
        return programma;
    }

    public void setProgramma(String programma) {
        this.programma = programma;
    }

    public String getRequisiti() {
        return requisiti;
    }

    public void setRequisiti(String requisiti) {
        this.requisiti = requisiti;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
}