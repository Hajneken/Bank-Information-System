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
public class kreditni_karta {
    /**
     * The kreditni_karta entity. Has attributes:
     * id_platebni_karta - the Id of the generalization.
     * limit_cerpani - Charge limit.
     */
    @Id
    private Integer id_platebni_karta;
    private Integer limit_cerpani;

    public kreditni_karta() {
        this.id_platebni_karta = null;
        this.limit_cerpani = null;
    }

    public Integer getId_platebni_karta() {
        return id_platebni_karta;
    }

    public void setId_platebni_karta(Integer id_platebni_karta) {
        this.id_platebni_karta = id_platebni_karta;
    }

    public Integer getLimit_cerpani() {
        return limit_cerpani;
    }

    public void setLimit_cerpani(Integer limit_cerpani) {
        this.limit_cerpani = limit_cerpani;
    }

}
