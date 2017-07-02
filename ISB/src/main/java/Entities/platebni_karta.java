/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Date;
import javax.persistence.*;

/**
 *
 * @author Bindex
 */
@Entity
public class platebni_karta {
        /**
     * The platebni_karta entity. Has attributes:
     * id - ID.
     * owner - the klient entity that owns the card.
     * cislo - the number.
     * cvv - CVV.
     * datum_vydani - the issue date, in the form of a java.sql.Date object.
     * datum_vyprseni - the expiration date, in the form of a java.sql.Date object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kartagen")
    @SequenceGenerator(name = "kartagen", sequenceName = "kartaseq", allocationSize = 1)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private klient owner;
    private Long cislo;
    private Integer cvv;
    private java.sql.Date datum_vydani;
    private java.sql.Date datum_vyprseni;

    public platebni_karta() {
        this.id = null;
        this.cislo = null;
        this.cvv = null;
        this.datum_vydani = null;
        this.datum_vyprseni = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public klient getOwner() {
        return owner;
    }

    public void setOwner(klient owner) {
        this.owner = owner;
    }

    public Long getCislo() {
        return cislo;
    }

    public void setCislo(Long cislo) {
        this.cislo = cislo;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Date getDatum_vydani() {
        return datum_vydani;
    }

    public void setDatum_vydani(Date datum_vydani) {
        this.datum_vydani = datum_vydani;
    }

    public Date getDatum_vyprseni() {
        return datum_vyprseni;
    }

    public void setDatum_vyprseni(Date datum_vyprseni) {
        this.datum_vyprseni = datum_vyprseni;
    }

}
