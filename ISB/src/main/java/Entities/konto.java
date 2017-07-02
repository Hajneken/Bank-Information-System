/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 *
 * @author Bindex
 */
@Entity
public class konto {
    /**
     * The konto entity. Has attributes:
     * id_ucet - the Id of the generalization.
     * typ - Type.
     * zustatek - Surname.
     * bydliste - Balance.
     */
    @Id
    private Integer id_ucet;
    private String typ;
    private BigDecimal zustatek;

    public konto() {
        this.id_ucet = null;
        this.typ = null;
        this.zustatek = null;
    }

    public Integer getId_ucet() {
        return id_ucet;
    }

    public void setId_ucet(Integer id_ucet) {
        this.id_ucet = id_ucet;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public BigDecimal getZustatek() {
        return zustatek;
    }

    public void setZustatek(BigDecimal zustatek) {
        this.zustatek = zustatek;
    }

}
