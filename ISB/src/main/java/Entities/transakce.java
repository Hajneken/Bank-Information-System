/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.*;

/**
 *
 * @author Bindex
 */
@Entity
public class transakce {
        /**
     * The transakce entity. Has attributes:
     * id - the Id.
     * castka - the amount to be transferred.
     * datum_podani - the issue date, in the form of a java.sql.Date object.
     * datum_prevedeni - the transaction date, in the form of a java.sql.Date object.
     * id_platce - the Id of the account that is to pay the amount.)
     * id_prijemce - the Id of the account that is to recieve the amount.
     * provedeno - whether the transaction has been performed.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transakcegen")
    @SequenceGenerator(name = "transakcegen", sequenceName = "transakceseq", allocationSize = 1)
    private Integer id;
    private BigDecimal castka;
    private java.sql.Date datum_podani;
    private java.sql.Date datum_prevedeni;
    private Integer id_platce;
    private Integer id_prijemce;
    private Boolean provedeno;

    public transakce() {
        this.id = null;
        this.castka = null;
        this.datum_podani = null;
        this.datum_prevedeni = null;
        this.id_platce = null;
        this.id_prijemce = null;
        this.provedeno = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCastka() {
        return castka;
    }

    public void setCastka(BigDecimal castka) {
        this.castka = castka;
    }

    public Date getDatum_podani() {
        return datum_podani;
    }

    public void setDatum_podani(Date datum_podani) {
        this.datum_podani = datum_podani;
    }

    public Date getDatum_prevedeni() {
        return datum_prevedeni;
    }

    public void setDatum_prevedeni(Date datum_prevedeni) {
        this.datum_prevedeni = datum_prevedeni;
    }

    public Integer getId_platce() {
        return id_platce;
    }

    public void setId_platce(Integer id_platce) {
        this.id_platce = id_platce;
    }

    public Integer getId_prijemce() {
        return id_prijemce;
    }

    public void setId_prijemce(Integer id_prijemce) {
        this.id_prijemce = id_prijemce;
    }

    public Boolean getProvedeno() {
        return provedeno;
    }

    public void setProvedeno(Boolean provedeno) {
        this.provedeno = provedeno;
    }

}
