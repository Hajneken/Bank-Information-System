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
public class uver {
        /**
     * The uver entity. Has attributes:
     * id_ucet - the Id of the generalization.
     * hodnota - the value of the loan.
     * typ - Type.
     * urok - Interest of the loan.
     */
    @Id
    private Integer id_ucet;
    private BigDecimal hodnota;
    private String typ;
    private BigDecimal urok;

    public uver() {
        this.id_ucet = null;
        this.hodnota = null;
        this.typ = null;
        this.urok = null;
    }

    public Integer getId_ucet() {
        return id_ucet;
    }

    public void setId_ucet(Integer id_ucet) {
        this.id_ucet = id_ucet;
    }

    public BigDecimal getHodnota() {
        return hodnota;
    }

    public void setHodnota(BigDecimal hodnota) {
        this.hodnota = hodnota;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public BigDecimal getUrok() {
        return urok;
    }

    public void setUrok(BigDecimal urok) {
        this.urok = urok;
    }

}
