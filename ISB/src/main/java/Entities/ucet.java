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
 * @author Bindex
 */
@Entity
public class ucet {
    /**
     * The ucet entity. Has attributes:
     * id - the Id.
     * clients - the list of clients that own this account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ucetgen")
    @SequenceGenerator(name = "ucetgen", sequenceName = "ucetseq", allocationSize = 1)
    @Column(name = "id")
    private Integer id;
    @ManyToMany
    @JoinTable(
            name = "klient_ucet",
            joinColumns = @JoinColumn(name = "idu", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "idk", referencedColumnName = "id"))
    private List<klient> clients = new ArrayList<>();

    public ucet() {
        this.id = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<klient> getClients() {
        return clients;
    }

    public void setClients(List<klient> clients) {
        this.clients = clients;
    }

    public void addClient(klient k) {
        this.clients.add(k);
    }

    public void removeClient(klient k) {
        this.clients.remove(k);
    }
}
