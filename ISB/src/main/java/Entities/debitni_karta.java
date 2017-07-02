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
public class debitni_karta {

    @Id
    private Integer id_platebni_karta;
    private Integer id_ucet;

    /**
     * The debitni_karta entity. Has attributes:
     * id_platebni_karta - the Id of the generalization to use.
     * id_ucet - the Id of the konto entity.
     */
    public debitni_karta() {
        this.id_platebni_karta = null;
        this.id_ucet = null;
    }

    public Integer getId_platebni_karta() {
        return id_platebni_karta;
    }

    public void setId_platebni_karta(Integer id_platebni_karta) {
        this.id_platebni_karta = id_platebni_karta;
    }

    public Integer getId_ucet() {
        return id_ucet;
    }

    public void setId_ucet(Integer id_ucet) {
        this.id_ucet = id_ucet;
    }

}
