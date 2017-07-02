package System;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entities.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 *
 * @author bindex DB_Java controls the data transfer between the Java
 * application and PostgreSQL Database
 */
public class DB {

    private EntityManagerFactory emf;
    private static final Logger LOG = Logger.getLogger(DB.class.getName());

    /**
     * Constructor of the object. Creates an EntityManagerFactory and attempts
     * to connect to the database.
     */
    public DB() {
        try {
            //Connect to the DB
            EntityManagerFactory emfu = Persistence.createEntityManagerFactory("Informacni_system_bankyPU");
            this.emf = emfu;
        } catch (PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
        } finally {
            if (this.check_connection()) {
                System.out.println("Connection established successfully.");
                LOG.fine("Connection established successfully.");
            }
        }
    }

    /**
     * Terminates the connection to the database and frees memory.
     *
     * @return True if the termination is successful, false if it is not.
     */
    public boolean terminate_connection() {
        try {
            if (this.check_connection()) {
                //Terminate the connection
                this.emf.close();
                return true;
            } else {
                return false;
            }
        } catch (IllegalStateException | NullPointerException | PersistenceException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    /**
     * Checks whether the instance is connected to the database.
     *
     * @return True if it is connected, false if it is not.
     */
    public final boolean check_connection() {
        try {
            return this.emf.isOpen();
        } catch (IllegalStateException | NullPointerException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return false;
        }
    }

    /**
     * Attempts to insert a fyzicka_osoba entity into the database. It does so
     * by first inserting the klient general entity and then attaching a
     * fyzicka_osoba entity to it. All of the parameters must be provided.
     *
     * @param Address the address of the fyzicka_osoba row.
     * @param Name the name of the fyzicka_osoba row.
     * @param Surname the surname of the fyzicka_osoba row.
     * @param Age the age of the fyzicka_osoba row.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_fyzicka_osoba(String Address, String Name, String Surname, Integer Age) {
        LOG.fine("Inserting into fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (Address == null || Name == null || Surname == null || Age == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }

        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            klient clt = new klient();
            fyzicka_osoba fo = new fyzicka_osoba();
            fo.setBydliste(Address);
            fo.setJmeno(Name);
            fo.setPrijmeni(Surname);
            fo.setAge(Age);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(clt);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            clt = em.find(klient.class, clt.getId());
            fo.setId_klient(clt.getId());
            et.begin();
            em.persist(fo);
            et.commit();
            em.close();
            LOG.fine("Insert into fyzicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }
    }

    /**
     * Attempts to insert a pravnicka_osoba entity into the database. It does so
     * by first inserting the klient general entity and then attaching a
     * pravnicka_osoba entity to it. All of the parameters must be provided.
     *
     * @param ICO the ICO of the pravnicka_osoba row.
     * @param Name the name of the pravnicka_osoba row.
     * @param HQ the HQ of the pravnicka_osoba row.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_pravnicka_osoba(Integer ICO, String Name, String HQ) {
        LOG.fine("Inserting into pravnicka_osoba...");
        //Check if parameters have been entered correctly
        if (ICO == null || Name == null || HQ == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            klient clt = new klient();
            pravnicka_osoba po = new pravnicka_osoba();
            po.setIco(ICO);
            po.setNazev_firmy(Name);
            po.setSidlo(HQ);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(clt);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            clt = em.find(klient.class, clt.getId());
            po.setId_klient(clt.getId());
            et.begin();
            em.persist(po);
            et.commit();
            em.close();
            LOG.fine("Insert into pravnicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }
    }

    /**
     * Atempts to insert a debitni_karta entity into the database. It does so by
     * first inserting the platebni_karta general entity and then attaching a
     * debitni_karta entity to it. Then, the platebni_karta entity is added to
     * the list of klient's cards. All of the parameters must be provided. A
     * klient entity is required as the first parameter. All of its variables
     * have to be non-null, otherwise the method fails.
     *
     * @param k the klient entity owner of the card.
     * @param Number the number of the platebni_karta row.
     * @param CVV the CVV of the platebni_karta row.
     * @param Issue_date the issue date of the platebni_karta row, in the form
     * of a java.sql.Date instance.
     * @param Expiration_date the expiration date of the platebni_karta row, in
     * the form of a java.sql.Date instance.
     * @param AccountID the ID of the ucet entity - which has to belong to a
     * konto entity - that is to be linked to the debitni_karta entity.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_debitni_karta(klient k, Long Number, Integer CVV, java.sql.Date Issue_date, java.sql.Date Expiration_date, Integer AccountID) {
        LOG.fine("Inserting into debitni_karta...");
        //Check if parameters have been entered correctly
        if (k == null || Number == null || CVV == null || Issue_date == null || Expiration_date == null || AccountID == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            platebni_karta pk = new platebni_karta();
            pk.setOwner(k);
            pk.setCislo(Number);
            pk.setCvv(CVV);
            pk.setDatum_vydani(Issue_date);
            pk.setDatum_vyprseni(Expiration_date);
            debitni_karta dk = new debitni_karta();
            dk.setId_ucet(AccountID);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(pk);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            pk = em.find(platebni_karta.class, pk.getId());
            dk.setId_platebni_karta(pk.getId());
            et.begin();
            em.persist(dk);
            et.commit();
            et.begin();
            //Add the card to the list of client's cards
            k = em.merge(k);
            k.addCard(pk);
            et.commit();
            em.close();
            LOG.fine("Insert into debitni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }

    }

    /**
     * Atempts to insert a kreditni_karta entity into the database. It does so
     * by first inserting the platebni_karta general entity and then attaching a
     * kreditni_karta entity to it. Then, the platebni_karta entity is added to
     * the list of klient's cards. All of the parameters must be provided. A
     * klient entity is required as the first parameter. All of its variables
     * have to be non-null, otherwise the method fails.
     *
     * @param k the klient entity owner of the card.
     * @param Number the number of the platebni_karta row.
     * @param CVV the CVV of the platebni_karta row.
     * @param Issue_date the issue date of the platebni_karta row, in the form
     * of a java.sql.Date instance.
     * @param Expiration_date the expiration date of the platebni_karta row, in
     * the form of a java.sql.Date instance.
     * @param Charge_limit the charge limit of the kreditni_karta row.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_kreditni_karta(klient k, Long Number, Integer CVV, java.sql.Date Issue_date, java.sql.Date Expiration_date, Integer Charge_limit) {
        LOG.fine("Inserting into kreditni_karta...");
        //Check if parameters have been entered correctly
        if (k == null || Number == null || CVV == null || Issue_date == null || Expiration_date == null || Charge_limit == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            platebni_karta pk = new platebni_karta();
            pk.setOwner(k);
            pk.setCislo(Number);
            pk.setCvv(CVV);
            pk.setDatum_vydani(Issue_date);
            pk.setDatum_vyprseni(Expiration_date);
            kreditni_karta kk = new kreditni_karta();
            kk.setLimit_cerpani(Charge_limit);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(pk);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            pk = em.find(platebni_karta.class, pk.getId());
            kk.setId_platebni_karta(pk.getId());
            et.begin();
            em.persist(kk);
            et.commit();
            et.begin();
            //Add the card to the list of client's cards
            k = em.merge(k);
            k.addCard(pk);
            et.commit();
            em.close();
            LOG.fine("Insert into kreditni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }

    }

    /**
     * Atempts to insert a konto entity into the database. It does so by first
     * inserting the ucet general entity and then attaching a konto entity to
     * it. Then, the konto entity is added to the list of klient's accounts. All
     * of the parameters must be provided. A klient entity is required as the
     * first parameter. All of its variables have to be non-null, otherwise the
     * method fails.
     *
     * @param kl the klient entity owner of the account.
     * @param Type the type of the konto row.
     * @param Funds the funds od the konto row.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_konto(klient kl, String Type, BigDecimal Funds) {
        LOG.fine("Inserting into konto...");
        //Check if parameters have been entered correctly
        if (kl == null || Type == null || Funds == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            ucet u = new ucet();
            u.addClient(kl);
            konto k = new konto();
            k.setTyp(Type);
            k.setZustatek(Funds);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(u);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            u = em.find(ucet.class, u.getId());
            k.setId_ucet(u.getId());
            et.begin();
            em.persist(k);
            et.commit();
            et.begin();
            //Add the account to the list of client's accounts
            kl = em.merge(kl);
            kl.addAccount(u);
            em.persist(kl);
            et.commit();
            em.close();
            LOG.fine("Insert into konto was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }

    }

    /**
     * Atempts to insert a uver entity into the database. It does so by first
     * inserting the ucet general entity and then attaching a uver entity to it.
     * Then, the uver entity is added to the list of klient's accounts. All of
     * the parameters must be provided. A klient entity is required as the first
     * parameter. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param kl the klient entity owner of the account.
     * @param Type the type of the uver row.
     * @param Funds the funds of the uver row.
     * @param Interest the interest of the uver row.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_uver(klient kl, String Type, BigDecimal Funds, BigDecimal Interest) {
        LOG.fine("Inserting into uver...");
        //Check if parameters have been entered correctly
        if (kl == null || Type == null || Funds == null || Interest == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create entity and its generalization
            ucet u = new ucet();
            u.addClient(kl);
            uver uv = new uver();
            uv.setTyp(Type);
            uv.setHodnota(Funds);
            uv.setUrok(Interest);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(u);
            et.commit();
            //Apply generalization's ID as the specialization's ID
            u = em.find(ucet.class, u.getId());
            uv.setId_ucet(u.getId());
            et.begin();
            em.persist(uv);
            et.commit();
            et.begin();
            //Add the account to the list of client's accounts
            kl = em.merge(kl);
            kl.addAccount(u);
            em.persist(kl);
            et.commit();
            em.close();
            LOG.fine("Insert into konto was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }

    }

    /**
     * Atempts to insert a transakce entity into the database. All of the
     * parameters must be provided.
     *
     * @param Amount - the amount of the transakce row.
     * @param Issue_date the issue date of the transakce row, in the form of a
     * java.sql.Date instance.
     * @param Transfer_date the transfer date of the transakce row, in the form
     * of a java.sql.Date instance.
     * @param IssuerID the ID of the issuer ucet entity.
     * @param RecieverID the ID of the reciever ucet entity.
     * @return True if the insertion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean insert_transakce(BigDecimal Amount, java.sql.Date Issue_date, java.sql.Date Transfer_date, Integer IssuerID, Integer RecieverID) {
        LOG.fine("Inserting into transakce...");
        //Check if parameters have been entered correctly
        if (Amount == null || Issue_date == null || Transfer_date == null || IssuerID == null || RecieverID == null) {
            LOG.severe("Not all arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Create the entity
            transakce t = new transakce();
            t.setCastka(Amount);
            t.setDatum_podani(Issue_date);
            t.setDatum_prevedeni(Transfer_date);
            t.setId_platce(IssuerID);
            t.setId_prijemce(RecieverID);
            t.setProvedeno(false);
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(t);
            et.commit();
            em.close();
            LOG.fine("Insert into transakce was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }

    }

    /**
     * Attepts to update the fyzicka_osoba entity. At least the fyzicka_osoba
     * object and one of the other parameters must be provided. Replace the
     * parameter with null if you want to keep the old column. A fyzicka_osoba
     * entity is required as the first parameter. All of its variables have to
     * be non-null, otherwise the method fails.
     *
     * @param fo the fyzicka_osoba entity to be updated.
     * @param Address the new address to replace the old address.
     * @param Name the new name to replace the old name.
     * @param Surname the new surname to replace the old surname.
     * @param Age the new age to replace the old age.
     * @return True if the update is successful, false if it is not, either due
     * to all parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean alter_fyzicka_osoba(fyzicka_osoba fo, String Address, String Name, String Surname, Integer Age) {
        LOG.fine("Altering fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (fo == null && (Address == null || Name == null || Surname == null || Age == null)) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            //Delete the client from his accounts' lists of owners
            for (ucet u : this.get_klient_of_fyzicka_osoba(fo).getAccounts()) {
                this.ucet_delete_klient(u, this.get_klient_of_fyzicka_osoba(fo), false);
            }
            //Alter the row
            et.begin();
            fo = em.merge(fo);
            if (Address != null) {
                fo.setBydliste(Address);
            }
            if (Name != null) {
                fo.setJmeno(Name);
            }
            if (Surname != null) {
                fo.setPrijmeni(Surname);
            }
            if (Age != null) {
                fo.setAge(Age);
            }
            et.commit();
            //Add the client to his accounts' lists of owners
            for (ucet u : this.get_klient_of_fyzicka_osoba(fo).getAccounts()) {
                this.ucet_add_klient(u, this.get_klient_of_fyzicka_osoba(fo));
            }
            em.close();
            LOG.fine("Alter fyzicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attepts to update the pravnicka_osoba entity. At least the
     * pravnicka_osoba object and one of the other parameters must be provided.
     * Replace the parameter with null if you want to keep the old column. A
     * pravnicka_osoba entity is required as the first parameter. All of its
     * variables have to be non-null, otherwise the method fails.
     *
     * @param po the pravnicka_osoba entity to be updated.
     * @param ICO the new ICO to replace the old ICO.
     * @param Name the new name to replace the old name.
     * @param HQ the new HQ to replace the old HQ.
     * @return True if the update is successful, false if it is not, either due
     * to all parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean alter_pravnicka_osoba(pravnicka_osoba po, Integer ICO, String Name, String HQ) {
        LOG.fine("Altering pravnicka_osoba...");
        //Check if parameters have been entered correctly
        if (po == null && (ICO == null || Name == null || HQ == null)) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            //Delete the client from his accounts' lists of owners
            for (ucet u : this.get_klient_of_pravnicka_osoba(po).getAccounts()) {
                this.ucet_delete_klient(u, this.get_klient_of_pravnicka_osoba(po), false);
            }
            et.begin();
            //Alter the row
            po = em.merge(po);
            if (ICO != null) {
                po.setIco(ICO);
            }
            if (Name != null) {
                po.setNazev_firmy(Name);
            }
            if (HQ != null) {
                po.setSidlo(HQ);
            }
            et.commit();
            //Add the client to his accounts' lists of owners
            for (ucet u : this.get_klient_of_pravnicka_osoba(po).getAccounts()) {
                this.ucet_add_klient(u, this.get_klient_of_pravnicka_osoba(po));
            }
            em.close();
            LOG.fine("Alter pravnicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attepts to update the platebni_karta entity. At least the platebni_karta
     * object and one of the other parameters must be provided. Replace the
     * parameter with null if you want to keep the old column. A platebni_karta
     * entity is required as the first parameter. All of its variables have to
     * be non-null, otherwise the method fails. A klient entity is available for
     * entry as the last parameter. If it is provided, all of its variables have
     * to be non-null, otherwise the method fails.
     *
     * @param pk the platebni_karta entity to be updated.
     * @param Number the new number to replace the old number.
     * @param CVV the new CVV to replace the old CVV.
     * @param Issue_date the issue date to replace the old issue date, in the
     * form of a java.sql.Date object.
     * @param Expiration_date the expiration date to replace the old expiration
     * date, in the form of a java.sql.Date object.
     * @param k the new klient owner to replace the old owner.
     * @return True if the update is successful, false if it is not, either due
     * to all parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean alter_platebni_karta(platebni_karta pk, Long Number, Integer CVV, java.sql.Date Issue_date, java.sql.Date Expiration_date, klient k) {
        LOG.fine("Altering platebni_karta...");
        //Check if parameters have been entered correctly
        if (pk == null && (Number == null || CVV == null || Issue_date == null || Expiration_date == null || k == null)) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Alter the row
            EntityTransaction et = em.getTransaction();
            et.begin();
            //Delete the card from the client's lists of cards
            klient kl = em.merge(pk.getOwner());
            kl.removeCard(pk);
            et.commit();
            et.begin();
            pk = em.merge(pk);
            if (Number != null) {
                pk.setCislo(Number);
            }
            if (CVV != null) {
                pk.setCvv(CVV);
            }
            if (Issue_date != null) {
                pk.setDatum_vydani(Issue_date);
            }
            if (Expiration_date != null) {
                pk.setDatum_vyprseni(Expiration_date);
            }
            if (k != null) {
                pk.setOwner(k);
            }
            et.commit();
            et.begin();
            //Add the card to the client's lists of cards
            kl = em.merge(pk.getOwner());
            kl.addCard(pk);
            et.commit();
            em.close();
            LOG.fine("Alter platebni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to update the kreditni_karta entity. All of the parameters are
     * required. A kreditni_karta entity is required as the first parameter. All
     * of its variables have to be non-null, otherwise the method fails.
     *
     * @param kk the kreditni_karta entity to be updated.
     * @param Charge_limit the new charge limit to replace the old charge limit.
     * @return True if the update is successful, false if it is not, either due
     * to parameters missing, no connection to the database, or other errors.
     */
    public boolean alter_kreditni_karta(kreditni_karta kk, Integer Charge_limit) {
        LOG.fine("Altering kreditni_karta...");
        //Check if parameters have been entered correctly
        if (kk == null && Charge_limit == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Alter the row
            EntityTransaction et = em.getTransaction();
            et.begin();
            kk = em.merge(kk);
            kk.setLimit_cerpani(Charge_limit);
            et.commit();
            em.close();
            LOG.fine("Alter kreditni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to update the konto entity. At least the konto and one of the
     * other parameters are required. Replace the parameter with null if you
     * want to keep the old column. A konto entity is required as the first
     * parameter. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param k the konto entity to be updated.
     * @param Type the new type to replace the old type.
     * @param Balance the new balance to replace the old balance.
     * @return True if the update is successful, false if it is not, either due
     * to parameters missing, no connection to the database, or other errors.
     */
    public boolean alter_konto(konto k, String Type, BigDecimal Balance) {
        LOG.fine("Altering konto...");
        //Check if parameters have been entered correctly
        if (k == null && (Type == null || Balance == null)) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Alter the row
            EntityTransaction et = em.getTransaction();
            et.begin();
            k = em.merge(k);
            if (Type != null) {
                k.setTyp(Type);
            }
            if (Balance != null) {
                k.setZustatek(Balance);
            }
            et.commit();
            em.close();
            LOG.fine("Alter konto was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to update the uver entity. At least the uver and one of the
     * other parameters are required. Replace the parameter with null if you
     * want to keep the old column. A uver entity is required as the first
     * parameter. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param uv the uver entity to be updated.
     * @param Type the new type to replace the old type.
     * @param Funds the new funds to replace the old funds.
     * @param Interest the new interest to replace the old interest.
     * @return True if the update is successful, false if it is not, either due
     * to parameters missing, no connection to the database, or other errors.
     */
    public boolean alter_uver(uver uv, String Type, BigDecimal Funds, BigDecimal Interest) {
        LOG.fine("Altering uver...");
        //Check if parameters have been entered correctly
        if (uv == null && (Type == null || Funds == null || Interest == null)) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Alter the row
            EntityTransaction et = em.getTransaction();
            et.begin();
            uv = em.merge(uv);
            if (Type != null) {
                uv.setTyp(Type);
            }
            if (Funds != null) {
                uv.setHodnota(Funds);
            }
            if (Interest != null) {
                uv.setUrok(Interest);
            }
            et.commit();
            em.close();
            LOG.fine("Alter uver was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to update the transakce entity. All the parameters are required.
     * A transakce entity is required as the first parameter. All of its
     * variables have to be non-null, otherwise the method fails.
     *
     * @param t the transakce entity to be updated.
     * @param Processed the new processed to replace the old processed.
     * @return True if the update is successful, false if it is not, either due
     * to parameters missing, no connection to the database, or other errors.
     */
    public boolean alter_transakce(transakce t, Boolean Processed) {
        LOG.fine("Altering transakce...");
        //Check if parameters have been entered correctly
        if (t == null && Processed == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            //Alter the row
            et.begin();
            t = em.merge(t);
            t.setProvedeno(Processed);
            et.commit();
            em.close();
            LOG.fine("Alter transakce was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the debitni_karta entity. A debitni_karta parameter is
     * required. All of its variables have to be non-null, otherwise the method
     * fails. Also removes the connected platebni_karta entity and removes it
     * from the klient's list of owned cards.
     *
     * @param dk the debitni_karta entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_debitni_karta(debitni_karta dk) {
        LOG.fine("Deleting debitni_karta...");
        //Check if parameters have been entered correctly
        if (dk == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the row and the generalization
            EntityTransaction et = em.getTransaction();
            platebni_karta pk = em.find(platebni_karta.class, dk.getId_platebni_karta());
            et.begin();
            dk = em.merge(dk);
            em.remove(dk);
            et.commit();
            et.begin();
            pk = em.merge(pk);
            em.remove(pk);
            et.commit();
            et.begin();
            //Remove the card from the client's list of cards
            klient k = em.merge(em.find(klient.class, pk.getOwner().getId()));
            k.removeCard(pk);
            et.commit();
            em.close();
            LOG.fine("Delete debitni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the kreditni_karta entity. A kreditni_karta parameter
     * is required. All of its variables have to be non-null, otherwise the
     * method fails. Also removes the connected platebni_karta entity and
     * removes it from the klient's list of owned cards.
     *
     * @param kk the kreditni_karta entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_kreditni_karta(kreditni_karta kk) {
        LOG.fine("Deleting kreditni_karta...");
        //Check if parameters have been entered correctly
        if (kk == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }

        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the row and the generalization
            EntityTransaction et = em.getTransaction();
            platebni_karta pk = em.find(platebni_karta.class, kk.getId_platebni_karta());
            et.begin();
            kk = em.merge(kk);
            em.remove(kk);
            et.commit();
            et.begin();
            pk = em.merge(pk);
            em.remove(pk);
            et.commit();
            et.begin();
            //Remove the card from the client's list of cards
            klient k = em.merge(em.find(klient.class, pk.getOwner().getId()));
            k.removeCard(pk);
            et.commit();
            em.close();
            LOG.fine("Delete kreditni_karta was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the platebni_karta entity. Also removes the connected
     * debitni_karta or kreditni_karta entity and removes it from the klient's
     * owned cards list. A platebni_karta parameter is required. All of its
     * variables have to be non-null, otherwise the method fails.
     *
     * @param pk the platebni_karta entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_platebni_karta(platebni_karta pk) {
        LOG.fine("Deleting platebni_karta...");
        //Check if parameters have been entered correctly
        if (pk == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            debitni_karta dk = em.find(debitni_karta.class, pk.getId());
            if (dk != null) {
                this.delete_debitni_karta(dk);
            }
            kreditni_karta kk = em.find(kreditni_karta.class, pk.getId());
            if (kk != null) {
                this.delete_kreditni_karta(kk);
            }
            em.close();
            LOG.fine("Delete platebni_karta was successful.");
            return true;

        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the fyzicka_osoba entity. Also removes the connected
     * klient, ucet, and platebni_karta entities, if they exist. A fyzicka_osoba
     * parameter is required. All of its variables have to be non-null,
     * otherwise the method fails.
     *
     * @param fo the fyzicka_osoba entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_fyzicka_osoba(fyzicka_osoba fo) {
        LOG.fine("Deleting fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (fo == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the client's owned cards
            List<platebni_karta> owned_cards = this.search_platebni_karta(null, fo.getId_klient(), null, null, null, null);
            owned_cards.stream().forEach((owned_card) -> {
                this.delete_platebni_karta(owned_card);
            });
            EntityTransaction et = em.getTransaction();
            klient k = em.find(klient.class, fo.getId_klient());
            //Delete the client's owned accounts
            for (ucet object : k.getAccounts()) {
                this.ucet_delete_klient(object, k, false);
            }
            et.begin();
            //Delete the row and its generalization
            fo = em.merge(fo);
            em.remove(fo);
            et.commit();
            et.begin();
            k = em.merge(k);
            em.remove(k);
            et.commit();
            em.close();
            LOG.fine("Delete fyzicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the pravnicka_osoba entity. Also removes the connected
     * klient, ucet, and platebni_karta entities, if they exist. A
     * pravnicka_osoba parameter is required. All of its variables have to be
     * non-null, otherwise the method fails.
     *
     * @param po the pravnicka_osoba entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_pravnicka_osoba(pravnicka_osoba po) {
        LOG.fine("Deleting pravnicka_osoba...");
        //Check if parameters have been entered correctly
        if (po == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the client's owned cards
            List<platebni_karta> owned_cards = this.search_platebni_karta(null, po.getId_klient(), null, null, null, null);
            owned_cards.stream().forEach((owned_card) -> {
                this.delete_platebni_karta(owned_card);
            });
            EntityTransaction et = em.getTransaction();
            //Delete the client's owned accounts
            klient k = em.find(klient.class, po.getId_klient());
            for (ucet object : k.getAccounts()) {
                this.ucet_delete_klient(object, k, false);
            }
            //Delete the row and its generalization
            et.begin();
            po = em.merge(po);
            em.remove(po);
            et.commit();
            et.begin();
            k = em.merge(k);
            em.remove(k);
            et.commit();
            em.close();
            LOG.fine("Delete pravnicka_osoba was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the konto entity. Also removes the connected
     * platebni_karta entities, if they exist. A konto parameter is required.
     * All of its variables have to be non-null, otherwise the method fails.
     *
     * @param k the konto entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_konto(konto k) {
        LOG.fine("Deleting konto...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the card tied to the account
            for (debitni_karta obj : this.search_debitni_karta(null, k.getId_ucet())) {
                this.delete_debitni_karta(obj);
            }
            //Delete the row and its generalization
            EntityTransaction et = em.getTransaction();
            ucet u = em.find(ucet.class, k.getId_ucet());
            et.begin();
            k = em.merge(k);
            em.remove(k);
            et.commit();
            et.begin();
            u = em.merge(u);
            em.remove(u);
            et.commit();
            //Remove the account from the client's owned accounts
            for (klient kl : u.getClients()) {
                et.begin();
                kl = em.merge(kl);
                kl.removeAccount(u);
                et.commit();
            }
            em.close();
            LOG.fine("Delete konto was successful.");
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the uver entity. A uver parameter is required. All of
     * its variables have to be non-null, otherwise the method fails.
     *
     * @param uv the uver entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_uver(uver uv) {
        LOG.fine("Deleting uver...");
        //Check if parameters have been entered correctly
        if (uv == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the row and its generalization
            EntityTransaction et = em.getTransaction();
            ucet u = em.find(ucet.class, uv.getId_ucet());
            et.begin();
            uv = em.merge(uv);
            em.remove(uv);
            et.commit();
            et.begin();
            u = em.merge(u);
            em.remove(u);
            et.commit();
            //Remove the account from the client's owned accounts
            for (klient kl : u.getClients()) {
                et.begin();
                kl = em.merge(kl);
                kl.removeAccount(u);
                et.commit();
            }
            em.close();
            LOG.fine("Delete uver was successful.");
            return true;

        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the ucet entity. Also removes the corresponding uver
     * or konto entity. A ucet parameter is required. All of its variables have
     * to be non-null, otherwise the method fails.
     *
     * @param u the ucet entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_ucet(ucet u) {
        LOG.fine("Deleting ucet...");
        //Check if parameters have been entered correctly
        if (u == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Find out if ucet's specialization is konto or uver, then delete that
            EntityTransaction et = em.getTransaction();
            konto k = em.find(konto.class, u.getId());
            if (k != null) {
                this.delete_konto(k);
            }
            uver uv = em.find(uver.class, u.getId());
            if (uv != null) {
                this.delete_uver(uv);
            }
            em.close();
            LOG.fine("Delete uver was successful.");
            return true;

        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to delete the transakce entity. A transakce parameter is
     * required. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param t the transakce entity to be deleted.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean delete_transakce(transakce t) {
        LOG.fine("Deleting transakce...");
        //Check if parameters have been entered correctly
        if (t == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Delete the row
            EntityTransaction et = em.getTransaction();
            et.begin();
            t = em.merge(t);
            em.remove(t);
            et.commit();
            em.close();
            LOG.fine("Delete uver was successful.");
            return true;

        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Attempts to add a klient to the list of ucet's owners, and a ucet to the
     * list of klient's accounts. Ucet and klient parameters are required. All
     * of their variables have to be non-null, otherwise the method fails.
     *
     * @param ul the ucet entity to manipulate.
     * @param kl the klient entity to manipulate.
     * @return True if the addition is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean ucet_add_klient(ucet ul, klient kl) {
        LOG.fine("Inserting klient into ucet...");
        //Check if parameters have been entered correctly
        if (ul == null || kl == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            //Add the client to the list of account's owners
            EntityTransaction et = em.getTransaction();
            et.begin();
            ul = em.merge(ul);
            ul.addClient(kl);
            et.commit();
            et.begin();
            //Add the account to the list of client's accounts
            kl = em.merge(kl);
            kl.addAccount(ul);
            em.persist(kl);
            et.commit();
            LOG.fine("Insert klient into ucet was successful.");
            em.close();
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException | NullPointerException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }
    }

    /**
     * Attempts to add a klient to the list of ucet's owners, and a ucet to the
     * list of klient's accounts. Ucet and klient parameters are required. All
     * of their variables have to be non-null, otherwise the method fails.
     *
     * @param ul the ucet entity to manipulate.
     * @param kl the klient entity to manipulate.
     * @param deleteIfNoClients whether to delete the ucet if there are no
     * owners of it after the deletion. Should be true in most cases.
     * @return True if the deletion is successful, false if it is not, either
     * due to parameters missing, no connection to the database, or other
     * errors.
     */
    public boolean ucet_delete_klient(ucet ul, klient kl, boolean deleteIfNoClients) {
        LOG.fine("Deleting klient from ucet...");
        //Check if parameters have been entered correctly
        if (ul == null || kl == null) {
            LOG.severe("No arguments provided.");
            return false;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return false;
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            //Delete the client to the list of account's owners
            et.begin();
            ul = em.merge(ul);
            ul.removeClient(kl);
            et.commit();
            et.begin();
            //Delete the account from the list of client's accounts
            kl = em.merge(kl);
            kl.removeAccount(ul);
            em.persist(kl);
            et.commit();
            //If we wish to do so and the account has no owners, delete it
            if (deleteIfNoClients && ul.getClients().isEmpty()) {
                this.delete_ucet(ul);
            }
            LOG.fine("Delete klient from ucet was successful.");
            em.close();
            return true;
        } catch (IllegalStateException | RollbackException | TransactionRequiredException | EntityExistsException | IllegalArgumentException | NullPointerException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;

        }
    }

    /**
     * Searches through the fyzicka_osoba table with the parameters specified.
     * At least one of the parameters is required. Replace the parameter with
     * null if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param Address the address to use for the search.
     * @param Name the name to use for the search.
     * @param Surname the surname to use for the search.
     * @param Age the age to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<fyzicka_osoba> search_fyzicka_osoba(Integer ID, String Address, String Name, String Surname, Integer Age) {
        LOG.fine("Searching for fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (ID == null && Address == null && Name == null && Surname == null && Age == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT fo FROM fyzicka_osoba fo WHERE ";
            if (ID != null) {
                query += "fo.id_klient = :id AND ";
            }
            if (Address != null) {
                query += "fo.bydliste = :address AND ";
            }
            if (Name != null) {
                query += "fo.jmeno = :name AND ";
            }
            if (Surname != null) {
                query += "fo.prijmeni = :surname AND ";
            }
            if (Age != null) {
                query += "fo.age = :age AND ";
            }
            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<fyzicka_osoba> tq = em.createQuery(query, fyzicka_osoba.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (Address != null) {
                tq.setParameter("address", Address);
            }
            if (Name != null) {
                tq.setParameter("name", Name);
            }
            if (Surname != null) {
                tq.setParameter("surname", Surname);
            }
            if (Age != null) {
                tq.setParameter("age", Age);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the pravnicka_osoba table with the parameters specified.
     * At least one of the parameters is required. Replace the parameter with
     * null if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param ICO the ICO to use for the search.
     * @param Name the name to use for the search.
     * @param HQ the HQ to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<pravnicka_osoba> search_pravnicka_osoba(Integer ID, Integer ICO, String Name, String HQ) {
        LOG.fine("Searching for pravnicka_osoba...");
        //Check if parameters have been entered correctly
        if (ID == null && ICO == null && Name == null && HQ == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT po FROM pravnicka_osoba po WHERE ";
            if (ID != null) {
                query += "po.id_klient = :id AND ";
            }
            if (ICO != null) {
                query += "po.ico = :ico AND ";
            }
            if (Name != null) {
                query += "po.nazev_firmy = :name AND ";
            }
            if (HQ != null) {
                query += "po.sidlo = :hq AND ";
            }
            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<pravnicka_osoba> tq = em.createQuery(query, pravnicka_osoba.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (ICO != null) {
                tq.setParameter("ico", ICO);
            }
            if (Name != null) {
                tq.setParameter("name", Name);
            }
            if (HQ != null) {
                tq.setParameter("hq", HQ);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the platebni_karta table with the parameters specified.
     * At least one of the parameters is required. Replace the parameter with
     * null if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param ClientID the klient's id to use for the search.
     * @param Number the number to use for the search.
     * @param CVV the cvv to use for the search.
     * @param Issue_date the issue date to use for the search, in the form of a
     * java.sql.Date object.
     * @param Expiration_date the expiration date to use for the search, in the
     * form of a java.sql.Date object.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<platebni_karta> search_platebni_karta(Integer ID, Integer ClientID, Long Number, Integer CVV, java.sql.Date Issue_date, java.sql.Date Expiration_date) {
        LOG.fine("Searching for platebni_karta...");
        //Check if parameters have been entered correctly
        if (ID == null && ClientID == null && Number == null && CVV == null && Issue_date == null && Expiration_date == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT pk FROM platebni_karta pk WHERE ";
            if (ID != null) {
                query += "pk.id = :id AND ";
            }
            if (ClientID != null) {
                query += "pk.owner.id = :idu AND ";
            }
            if (Number != null) {
                query += "pk.cislo = :number AND ";
            }
            if (CVV != null) {
                query += "pk.cvv = :CVV AND ";
            }
            if (Issue_date != null) {
                query += "pk.datum_vydani = :i_d AND ";
            }
            if (Expiration_date != null) {
                query += "pk.datum_vyprseni = :e_d AND ";
            }

            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<platebni_karta> tq = em.createQuery(query, platebni_karta.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (ClientID != null) {
                tq.setParameter("idu", ClientID);
            }
            if (Number != null) {
                tq.setParameter("number", Number);
            }
            if (CVV != null) {
                tq.setParameter("CVV", CVV);
            }
            if (Issue_date != null) {
                tq.setParameter("i_d", Issue_date);
            }
            if (Expiration_date != null) {
                tq.setParameter("e_d", Expiration_date);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the debitni_karta table with the parameters specified.
     * At least one of the parameters is required. Replace the parameter with
     * null if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param AccountID the ucet's id to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<debitni_karta> search_debitni_karta(Integer ID, Integer AccountID) {
        LOG.fine("Searching for debitni_karta...");
        //Check if parameters have been entered correctly
        if (ID == null && AccountID == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT dk FROM debitni_karta dk WHERE ";
            if (ID != null) {
                query += "dk.id_platebni_karta = :id AND ";
            }
            if (AccountID != null) {
                query += "dk.id_ucet = :ida AND ";
            }

            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<debitni_karta> tq = em.createQuery(query, debitni_karta.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (AccountID != null) {
                tq.setParameter("ida", AccountID);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the kreditni_karta table with the parameters specified.
     * At least one of the parameters is required. Replace the parameter with
     * null if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param Limit the charge limit to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<kreditni_karta> search_kreditni_karta(Integer ID, Integer Limit) {
        LOG.fine("Searching for kreditni_karta...");
        //Check if parameters have been entered correctly
        if (ID == null && Limit == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT kk FROM kreditni_karta kk WHERE ";
            if (ID != null) {
                query += "kk.id_platebni_karta = :id AND ";
            }
            if (Limit != null) {
                query += "kk.limit_cerpani = :limit AND ";
            }

            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<kreditni_karta> tq = em.createQuery(query, kreditni_karta.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (Limit != null) {
                tq.setParameter("limit", Limit);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the transakce table with the parameters specified. At
     * least one of the parameters is required. Replace the parameter with null
     * if you do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param Amount the amount to use for the search.
     * @param Issue_date the issue date to use for the search, in the form of a
     * java.sql.Date object.
     * @param Transfer_date the transfer date to use for the search, in the form
     * of a java.sql.Date object.
     * @param IssuerID the id of the issuer ucet to use for the search.
     * @param RecieverID the id of the reciever ucet to use for the search.
     * @param Transferred the transferred boolean to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<transakce> search_transakce(Integer ID, BigDecimal Amount, java.sql.Date Issue_date, java.sql.Date Transfer_date, Integer IssuerID, Integer RecieverID, Boolean Transferred) {
        LOG.fine("Searching for transakce...");
        //Check if parameters have been entered correctly
        if (ID == null && Amount == null && Issue_date == null && Transfer_date == null && IssuerID == null && RecieverID == null && Transferred == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT t FROM transakce t WHERE ";
            if (ID != null) {
                query += "t.id = :id AND ";
            }
            if (Amount != null) {
                query += "t.castka = :amount AND ";
            }
            if (Issue_date != null) {
                query += "t.datum_podani = :i_d AND ";
            }
            if (Transfer_date != null) {
                query += "t.datum_prevedeni = :t_d AND ";
            }
            if (IssuerID != null) {
                query += "t.id_platce = :iid AND ";
            }
            if (RecieverID != null) {
                query += "t.id_prijemce = :pid AND ";
            }
            if (Transferred != null) {
                query += "t.provedeno = :tran AND ";
            }

            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<transakce> tq = em.createQuery(query, transakce.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (Amount != null) {
                tq.setParameter("amount", Amount);
            }
            if (Issue_date != null) {
                tq.setParameter("i_d", Issue_date);
            }
            if (Transfer_date != null) {
                tq.setParameter("t_d", Transfer_date);
            }
            if (IssuerID != null) {
                tq.setParameter("iid", IssuerID);
            }
            if (RecieverID != null) {
                tq.setParameter("pid", RecieverID);
            }
            if (Transferred != null) {
                tq.setParameter("tran", Transferred);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the konto table with the parameters specified. At least
     * one of the parameters is required. Replace the parameter with null if you
     * do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param Type the type to use for the search.
     * @param Money the money to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<konto> search_konto(Integer ID, String Type, BigDecimal Money) {
        LOG.fine("Searching for konto...");
        //Check if parameters have been entered correctly
        if (ID == null && Type == null && Money == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT k FROM konto k WHERE ";
            if (ID != null) {
                query += "k.id_ucet = :id AND ";
            }
            if (Type != null) {
                query += "k.typ = :type AND ";
            }
            if (Money != null) {
                query += "k.zustatek = :money AND ";
            }

            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<konto> tq = em.createQuery(query, konto.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (Type != null) {
                tq.setParameter("type", Type);
            }
            if (Money != null) {
                tq.setParameter("money", Money);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Searches through the uver table with the parameters specified. At least
     * one of the parameters is required. Replace the parameter with null if you
     * do not want to use it as a search criteria.
     *
     * @param ID the id to use for the search.
     * @param Type the type to use for the search.
     * @param Money the money to use for the search.
     * @param Interest the interest to use for the search.
     * @return A list of objects that fit the search criteria. If no objects fit
     * the search criteria, the list is empty. Returns null if there is an
     * error.
     */
    public List<uver> search_uver(Integer ID, String Type, BigDecimal Money, BigDecimal Interest) {
        LOG.fine("Searching for uver...");
        //Check if parameters have been entered correctly
        if (ID == null && Type == null && Money == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            //Construct the query
            String query = "SELECT u FROM uver u WHERE ";
            if (ID != null) {
                query += "u.id_ucet = :id AND ";
            }
            if (Type != null) {
                query += "u.typ = :type AND ";
            }
            if (Money != null) {
                query += "u.hodnota = :money AND ";
            }
            if (Interest != null) {
                query += "u.urok = :interest AND ";
            }
            
            query = query.substring(0, query.length() - 5);
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<uver> tq = em.createQuery(query, uver.class);
            //Set the parameters
            if (ID != null) {
                tq.setParameter("id", ID);
            }
            if (Type != null) {
                tq.setParameter("type", Type);
            }
            if (Money != null) {
                tq.setParameter("money", Money);
            }
            if (Interest != null) {
                tq.setParameter("interest", Interest);
            }
            //Perform the search
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the connected ucet entity of the konto entity. A konto parameter is
     * required. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param k the konto entity to extract the ucet from.
     * @return The corresponding ucet entity, or null if there is an error.
     */
    public ucet get_ucet_of_konto(konto k) {
        LOG.fine("Getting ucet of konto...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(ucet.class, k.getId_ucet());
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Gets the connected ucet entity of the uver entity. A uver parameter is
     * required. All of its variables have to be non-null, otherwise the method
     * fails.
     *
     * @param k the uver entity to extract the ucet from.
     * @return The corresponding ucet entity, or null if there is an error.
     */
    public ucet get_ucet_of_uver(uver k) {
        LOG.fine("Getting ucet of uver...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(ucet.class, k.getId_ucet());
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Gets the connected klient entity of the fyzicka_osoba entity. A
     * fyzicka_osoba parameter is required. All of its variables have to be
     * non-null, otherwise the method fails.
     *
     * @param k the fyzicka_osoba entity to extract the klient from.
     * @return The corresponding klient entity, or null if there is an error.
     */
    public klient get_klient_of_fyzicka_osoba(fyzicka_osoba k) {
        LOG.fine("Getting klient of fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(klient.class, k.getId_klient());
        } catch (PersistenceException | IllegalArgumentException | IllegalStateException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the connected klient entity of the pravnicka_osoba entity. A
     * pravnicka_osoba parameter is required. All of its variables have to be
     * non-null, otherwise the method fails.
     *
     * @param k the pravnicka_osoba entity to extract the klient from.
     * @return The corresponding klient entity, or null if there is an error.
     */
    public klient get_klient_of_pravnicka_osoba(pravnicka_osoba k) {
        LOG.fine("Getting klient of fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(klient.class, k.getId_klient());
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Gets the connected platebni_karta entity of the debitni_karta entity. A
     * debitni_karta parameter is required. All of its variables have to be
     * non-null, otherwise the method fails.
     *
     * @param k the debitni_karta entity to extract the klient from.
     * @return The corresponding platebni_karta entity, or null if there is an
     * error.
     */
    public platebni_karta get_platebni_karta_of_debitni_karta(debitni_karta k) {
        LOG.fine("Getting klient of fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(platebni_karta.class, k.getId_platebni_karta());
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Gets the connected platebni_karta entity of the kreditni_karta entity. A
     * kreditni_karta parameter is required. All of its variables have to be
     * non-null, otherwise the method fails.
     *
     * @param k the kreditni_karta entity to extract the klient from.
     * @return The corresponding platebni_karta entity, or null if there is an
     * error.
     */
    public platebni_karta get_platebni_karta_of_kreditni_karta(kreditni_karta k) {
        LOG.fine("Getting klient of fyzicka_osoba...");
        //Check if parameters have been entered correctly
        if (k == null) {
            LOG.severe("No arguments provided.");
            return null;
        }
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            EntityManager em = this.emf.createEntityManager();
            return em.find(platebni_karta.class, k.getId_platebni_karta());
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Gets the list of all debitni_karta entities of the database.
     *
     * @return A list of objects of all debitni_karta entities of the database.
     * If no objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<debitni_karta> display_debitni_karta() {
        LOG.fine("Displaying all entries of debitni_karta...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM debitni_karta dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<debitni_karta> tq = em.createQuery(query, debitni_karta.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all fyzicka_osoba entities of the database.
     *
     * @return A list of objects of all fyzicka_osoba entities of the database.
     * If no objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<fyzicka_osoba> display_fyzicka_osoba() {
        LOG.fine("Displaying all entries of fyzicka_osoba...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM fyzicka_osoba dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<fyzicka_osoba> tq = em.createQuery(query, fyzicka_osoba.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all klient entities of the database.
     *
     * @return A list of objects of all klient entities of the database. If no
     * objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<klient> display_klient() {
        LOG.fine("Displaying all entries of klient...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM klient dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<klient> tq = em.createQuery(query, klient.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all konto entities of the database.
     *
     * @return A list of objects of all konto entities of the database. If no
     * objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<konto> display_konto() {
        LOG.fine("Displaying all entries of konto...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM konto dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<konto> tq = em.createQuery(query, konto.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all kreditni_karta entities of the database.
     *
     * @return A list of objects of all kreditni_karta entities of the database.
     * If no objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<kreditni_karta> display_kreditni_karta() {
        LOG.fine("Displaying all entries of kreditni_karta...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM kreditni_karta dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<kreditni_karta> tq = em.createQuery(query, kreditni_karta.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all platebni_karta entities of the database.
     *
     * @return A list of objects of all platebni_karta entities of the database.
     * If no objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<platebni_karta> display_platebni_karta() {
        LOG.fine("Displaying all entries of platebni_karta...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM platebni_karta dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<platebni_karta> tq = em.createQuery(query, platebni_karta.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all pravnicka_osoba entities of the database.
     *
     * @return A list of objects of all pravnicka_osoba entities of the
     * database. If no objects are present, the list is empty. Returns null if
     * there is an error.
     */
    public List<pravnicka_osoba> display_pravnicka_osoba() {
        LOG.fine("Displaying all entries of pravnicka_osoba...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM pravnicka_osoba dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<pravnicka_osoba> tq = em.createQuery(query, pravnicka_osoba.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all transakce entities of the database.
     *
     * @return A list of objects of all transakce entities of the database. If
     * no objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<transakce> display_transakce() {
        LOG.fine("Displaying all entries of transakce...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM transakce dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<transakce> tq = em.createQuery(query, transakce.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all ucet entities of the database.
     *
     * @return A list of objects of all ucet entities of the database. If no
     * objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<ucet> display_ucet() {
        LOG.fine("Displaying all entries of ucet...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM ucet dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<ucet> tq = em.createQuery(query, ucet.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the list of all uver entities of the database.
     *
     * @return A list of objects of all uver entities of the database. If no
     * objects are present, the list is empty. Returns null if there is an
     * error.
     */
    public List<uver> display_uver() {
        LOG.fine("Displaying all entries of uver...");
        //Check if connection is established
        if (!this.check_connection()) {
            LOG.severe("Connection to database lost. Please try again.");
            return null;
        }
        try {
            String query = "SELECT dk FROM uver dk";
            EntityManager em = this.emf.createEntityManager();
            TypedQuery<uver> tq = em.createQuery(query, uver.class);
            return tq.getResultList();
        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            System.err.println(e.getMessage());
            LOG.severe(e.getMessage());
            return null;
        }
    }

}
