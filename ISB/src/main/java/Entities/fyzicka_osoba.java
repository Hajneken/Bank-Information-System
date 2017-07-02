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
public class fyzicka_osoba {

    @Id
    private Integer id_klient;
    private String jmeno;
    private String prijmeni;
    private Integer age;
    private String bydliste;

    /**
     * The fyzicka_osoba entity. Has attributes:
     * id_klient - the Id of the generalization.
     * jmeno - Name.
     * prijmeni - Surname.
     * bydliste - Address.
     */
    public fyzicka_osoba() {
        this.id_klient = null;
        this.jmeno = null;
        this.prijmeni = null;
        this.age = null;
        this.bydliste = null;
    }

    public Integer getId_klient() {
        return id_klient;
    }

    public void setId_klient(Integer id_klient) {
        this.id_klient = id_klient;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBydliste() {
        return bydliste;
    }

    public void setBydliste(String bydliste) {
        this.bydliste = bydliste;
    }

}
