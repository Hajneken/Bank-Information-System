/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import javax.persistence.*;

/**
 *
 * @author Bindex
 */
@Entity
public class pravnicka_osoba {
        /**
     * The pravnicka_osoba entity. Has attributes:
     * id_klient - the Id of the generalization.
     * ico - Company identification number.
     * nazev_firmy - Company name.
     * sidlo - the headquarters.
     */
    @Id
    private Integer id_klient;
    private Integer ico;
    private String nazev_firmy;
    private String sidlo;

    public pravnicka_osoba() {
        this.id_klient = null;
        this.ico = null;
        this.nazev_firmy = null;
        this.sidlo = null;
    }

    public Integer getId_klient() {
        return id_klient;
    }

    public void setId_klient(Integer id_klient) {
        this.id_klient = id_klient;
    }

    public Integer getIco() {
        return ico;
    }

    public void setIco(Integer ico) {
        this.ico = ico;
    }

    public String getNazev_firmy() {
        return nazev_firmy;
    }

    public void setNazev_firmy(String nazev_firmy) {
        this.nazev_firmy = nazev_firmy;
    }

    public String getSidlo() {
        return sidlo;
    }

    public void setSidlo(String sidlo) {
        this.sidlo = sidlo;
    }

}
