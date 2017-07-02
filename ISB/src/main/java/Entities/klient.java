/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author zeman
 */
@Entity
public class klient {
    /**
     * The klient entity. Has attributes:
     * id_klient - the Id of the generalization.
     * id - ID.
     * accounts - the list of ucet entities that are tied to this client.
     * cards - the list of platebni_karta entities that are tied to this client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "klientgen")
    @SequenceGenerator(name = "klientgen", sequenceName = "klientseq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;
    @ManyToMany(mappedBy = "clients")
    private List<ucet> accounts = new ArrayList<>();
    @OneToMany(mappedBy = "owner")
    private List<platebni_karta> cards = new ArrayList<>();

    public klient() {
        this.id = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ucet> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<ucet> accounts) {
        this.accounts = accounts;
    }

    public List<platebni_karta> getCards() {
        return cards;
    }

    public void setCards(List<platebni_karta> cards) {
        this.cards = cards;
    }

    public void addCard(platebni_karta pk) {
        this.cards.add(pk);
    }

    public void removeCard(platebni_karta pk) {
        this.cards.remove(pk);
    }

    public void addAccount(ucet u) {
        this.accounts.add(u);
    }

    public void removeAccount(ucet u) {
        this.accounts.remove(u);
    }
}
