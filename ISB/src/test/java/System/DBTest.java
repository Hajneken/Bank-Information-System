/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import Entities.debitni_karta;
import Entities.fyzicka_osoba;
import Entities.klient;
import Entities.konto;
import Entities.kreditni_karta;
import Entities.platebni_karta;
import Entities.pravnicka_osoba;
import Entities.transakce;
import Entities.ucet;
import Entities.uver;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bindex
 */
public class DBTest {

    public DBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    //Unit tests - happy cases
    @Test
    public void UnitTestOfConstructorAndTerminateConnection() {
        DB db = new DB();
        assertEquals(true,db.check_connection());
        db.terminate_connection();
        assertEquals(false, db.check_connection());
    }
    
    @Test
    public void UnitTestOfInsertFO(){
        DB db = new DB();
        assertEquals(true,db.insert_fyzicka_osoba("Adresa", "Jméno", "Příjmení", 456));
        assertEquals(true,db.search_fyzicka_osoba(null, "Adresa", "Jméno", "Příjmení", 456).size()>0);
    }
    
    @Test
    public void UnitTestOfAlterFO(){
        DB db = new DB();
        assertEquals(true,db.alter_fyzicka_osoba(db.search_fyzicka_osoba(null, "Adresa", "Jméno", "Příjmení", 456).get(0), "Jiná adresa", "Příjmení", "Jméno", 654));
        assertEquals(true,db.search_fyzicka_osoba(null, "Jiná adresa", "Příjmení", "Jméno", 654).size()>0);
    }
    @Test
    public void UnitTestOfDeleteFO(){
        DB db = new DB();
        assertEquals(true,db.delete_fyzicka_osoba(db.search_fyzicka_osoba(null, "Jiná adresa", "Příjmení", "Jméno", 654).get(0)));
        assertEquals(true,db.search_fyzicka_osoba(null, "Jiná adresa", "Příjmení", "Jméno", 654).size()==0);
    }
    //Unit tests - Exception cases
    @Test
    public void UnitTestOfAlterKonto() {
        DB db = new DB();
        konto k = new konto();
        k.setTyp("Oh no");
        k.setZustatek(new BigDecimal(404));
        assertEquals(false, db.alter_konto(k, "Not Found", new BigDecimal(59)));
    }

    @Test
    public void UnitTestOfSearchKonto() {
        DB db = new DB();
        assertEquals(0, db.search_konto(null, "Variabletype", new BigDecimal(4561)).size());
    }

    @Test
    public void UnitTestOfDelete_debitni_karta() {
        DB db = new DB();
        assertEquals(false, db.delete_debitni_karta(new debitni_karta()));
    }

    @Test
    public void UnitTestOfInsert_fyzicka_osoba() {
        DB db = new DB();
        db.terminate_connection();
        assertEquals(false, db.insert_fyzicka_osoba("California", "Will", "Hunting", 42));
    }

    @Test
    public void UnitTestOfGet_klient_of_fyzicka_osoba() {
        DB db = new DB();
        assertEquals(null,db.get_klient_of_fyzicka_osoba(new fyzicka_osoba()));
    }
    //Integration testsa

    @Test
    public void UnitTestOfInsertPOandInsertKonto() {
        DB db = new DB();
        pravnicka_osoba po = new pravnicka_osoba();
        po.setIco(568999412);
        po.setNazev_firmy("EvilCorp");
        po.setSidlo("Kettnerova 34");
        for (pravnicka_osoba obj : db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo())) {
            db.delete_pravnicka_osoba(obj);
        }
        konto k = new konto();
        k.setTyp("Sporeni pro seniory");
        k.setZustatek(new BigDecimal(1000.00));
        for (konto obj : db.search_konto(null, k.getTyp(), null)) {
            db.delete_konto(obj);
        }
        db.insert_pravnicka_osoba(po.getIco(), po.getNazev_firmy(), po.getSidlo());
        pravnicka_osoba inter1 = db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0);
        klient inter2 = db.get_klient_of_pravnicka_osoba(inter1);
        db.insert_konto(inter2, k.getTyp(), k.getZustatek());
        List<klient> result = db.get_ucet_of_konto(db.search_konto(null, k.getTyp(), null).get(0)).getClients();
        assertEquals(1, result.size());
    }

    @Test
    public void UnitTestOfAlterKontoandTransakce() {
        DB db = new DB();
        fyzicka_osoba fo1 = new fyzicka_osoba();
        fo1.setAge(28);
        fo1.setBydliste("Kent");
        fo1.setJmeno("Elon");
        fo1.setPrijmeni("Musk");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        transakce t = new transakce();
        t.setCastka(new BigDecimal(521.00));
        t.setDatum_podani(new Date(48181981));
        t.setDatum_prevedeni(new Date(549498423));
        for (transakce obj : db.search_transakce(null, t.getCastka(), null, null, null, null, null)) {
            db.delete_transakce(obj);
        }
        fyzicka_osoba fo2 = new fyzicka_osoba();
        fo2.setAge(55);
        fo2.setBydliste("Michigan");
        fo2.setJmeno("Bill");
        fo2.setPrijmeni("Gates");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        konto k1 = new konto();
        k1.setTyp("Typ1");
        k1.setZustatek(new BigDecimal(551891.00));
        for (konto obj : db.search_konto(null, k1.getTyp(), k1.getZustatek())) {
            db.delete_konto(obj);
        }
        konto k2 = new konto();
        k2.setZustatek(new BigDecimal(1811.00));
        k2.setTyp("Typ2");
        for (konto obj : db.search_konto(null, k2.getTyp(), k2.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_fyzicka_osoba(fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge());
        db.insert_fyzicka_osoba(fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge());
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k1.getTyp(), k1.getZustatek());
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge()).get(0)), k2.getTyp(), k2.getZustatek());
        db.alter_konto(db.search_konto(null, "Typ1", null).get(0), "Typ1", new BigDecimal(52088.00));
        db.alter_konto(db.search_konto(null, "Typ2", null).get(0), "Typ2", new BigDecimal(151451610.00));
        t.setId_platce(db.search_konto(null, "Typ1", null).get(0).getId_ucet());
        t.setId_prijemce(db.search_konto(null, "Typ2", null).get(0).getId_ucet());
        db.insert_transakce(t.getCastka(), t.getDatum_podani(), t.getDatum_prevedeni(), t.getId_platce(), t.getId_prijemce());
        db.alter_transakce(db.search_transakce(null, t.getCastka(), null, null, null, null, null).get(0), true);
        transakce result = db.search_transakce(null, t.getCastka(), null, null, null, null, true).get(0);
        assertEquals(true, result.getProvedeno());
    }

    @Test
    public void UnitTestOfMultipleClientsToOneAccount() {
        DB db = new DB();
        fyzicka_osoba fo1 = new fyzicka_osoba();
        fo1.setAge(29);
        fo1.setBydliste("Hacker Way");
        fo1.setJmeno("Mark");
        fo1.setPrijmeni("Zuckerberg");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        fyzicka_osoba fo2 = new fyzicka_osoba();
        fo2.setAge(71);
        fo2.setBydliste("White House");
        fo2.setJmeno("Donald");
        fo2.setPrijmeni("Trump");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge());
        db.insert_fyzicka_osoba(fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge());
        konto k = new konto();
        k.setZustatek(new BigDecimal(5512.00));
        k.setTyp("Saving Premium");
        for (konto obj : db.search_konto(null, k.getTyp(), k.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k.getTyp(), k.getZustatek());
        db.ucet_add_klient(db.get_ucet_of_konto(db.search_konto(null, k.getTyp(), k.getZustatek()).get(0)), db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo2.getBydliste(), fo2.getJmeno(), fo2.getPrijmeni(), fo2.getAge()).get(0)));
        assertEquals(2, db.get_ucet_of_konto(db.search_konto(null, k.getTyp(), k.getZustatek()).get(0)).getClients().size());
    }

    @Test
    public void UnitTestOfMultipleAccountsToOneClient() {
        DB db = new DB();
        fyzicka_osoba fo1 = new fyzicka_osoba();
        fo1.setAge(45);
        fo1.setBydliste("Tipsport Arena");
        fo1.setJmeno("Alois");
        fo1.setPrijmeni("Hadamczik");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge());
        konto k1 = new konto();
        k1.setZustatek(new BigDecimal(365.00));
        k1.setTyp("Saving Premium");
        for (konto obj : db.search_konto(null, k1.getTyp(), k1.getZustatek())) {
            db.delete_konto(obj);
        }
        konto k2 = new konto();
        k2.setZustatek(new BigDecimal(8668.00));
        k2.setTyp("Saving Basic");
        for (konto obj : db.search_konto(null, k2.getTyp(), k2.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k1.getTyp(), k1.getZustatek());
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k2.getTyp(), k2.getZustatek());
        assertEquals(2, db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)).getAccounts().size());
    }

    @Test
    public void UnitTestOfPlatebniKarta() {
        DB db = new DB();
        fyzicka_osoba fo1 = new fyzicka_osoba();
        fo1.setAge(42);
        fo1.setBydliste("Vinewood");
        fo1.setJmeno("Michael");
        fo1.setPrijmeni("De Santa");
        platebni_karta pk1 = new platebni_karta();
        pk1.setCislo(512345677L);
        pk1.setCvv(722);
        pk1.setDatum_vydani(new Date(456121854));
        pk1.setDatum_vyprseni(new Date(888888888));
        platebni_karta pk2 = new platebni_karta();
        pk2.setCislo(522458477L);
        pk2.setCvv(556);
        pk2.setDatum_vydani(new Date(484841848));
        pk2.setDatum_vyprseni(new Date(888898888));
        List<platebni_karta> purge = db.search_platebni_karta(null, null, pk1.getCislo(), pk1.getCvv(), pk1.getDatum_vydani(), pk1.getDatum_vyprseni());
        List<platebni_karta> add = db.search_platebni_karta(null, null, pk2.getCislo(), pk2.getCvv(), pk2.getDatum_vydani(), pk2.getDatum_vyprseni());
        if (add != null) {
            purge.addAll(add);
        }
        for (platebni_karta object : purge) {
            db.delete_debitni_karta(db.search_debitni_karta(object.getId(), null).get(0));
        }
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge());
        konto k1 = new konto();
        k1.setZustatek(new BigDecimal(181.00));
        k1.setTyp("Master Thief");
        for (konto obj : db.search_konto(null, k1.getTyp(), k1.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k1.getTyp(), k1.getZustatek());
        debitni_karta dk1 = new debitni_karta();
        dk1.setId_ucet(db.get_ucet_of_konto(db.search_konto(null, k1.getTyp(), k1.getZustatek()).get(0)).getId());

        pk1.setOwner(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)));
        for (debitni_karta obj : db.search_debitni_karta(null, dk1.getId_ucet())) {
            db.delete_debitni_karta(obj);
        }
        db.insert_debitni_karta(pk1.getOwner(), pk1.getCislo(), pk1.getCvv(), pk1.getDatum_vydani(), pk1.getDatum_vyprseni(), dk1.getId_ucet());
        konto k2 = new konto();
        k2.setZustatek(new BigDecimal(151581.00));
        k2.setTyp("Master Banker");
        for (konto obj : db.search_konto(null, k2.getTyp(), k2.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)), k2.getTyp(), k2.getZustatek());
        debitni_karta dk2 = new debitni_karta();
        dk2.setId_ucet(db.get_ucet_of_konto(db.search_konto(null, k1.getTyp(), k1.getZustatek()).get(0)).getId());
        pk2.setOwner(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)));
        for (debitni_karta obj : db.search_debitni_karta(null, dk2.getId_ucet())) {
            db.delete_debitni_karta(obj);
        }
        db.insert_debitni_karta(pk2.getOwner(), pk2.getCislo(), pk2.getCvv(), pk2.getDatum_vydani(), pk2.getDatum_vyprseni(), dk2.getId_ucet());
        assertEquals(2, db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo1.getBydliste(), fo1.getJmeno(), fo1.getPrijmeni(), fo1.getAge()).get(0)).getCards().size());
    }

    @Test
    public void UnitTestOfKreditni_karta() {
        DB db = new DB();
        pravnicka_osoba po = new pravnicka_osoba();
        po.setIco(4567889);
        po.setNazev_firmy("Gigasoft");
        po.setSidlo("Redmond");
        for (pravnicka_osoba object : db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo())) {
            db.delete_pravnicka_osoba(object);
        }
        db.insert_pravnicka_osoba(po.getIco(), po.getNazev_firmy(), po.getSidlo());
        platebni_karta pk1 = new platebni_karta();
        pk1.setCislo(1234558941257891L);
        pk1.setCvv(710);
        pk1.setDatum_vydani(new java.sql.Date(178956481891L));
        pk1.setDatum_vyprseni(new java.sql.Date(178966481891L));
        kreditni_karta kk = new kreditni_karta();
        kk.setId_platebni_karta(Integer.MIN_VALUE);
        kk.setLimit_cerpani(500000);
        db.insert_kreditni_karta(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)), pk1.getCislo(), pk1.getCvv(), pk1.getDatum_vydani(), pk1.getDatum_vyprseni(), kk.getLimit_cerpani());
        platebni_karta pk2 = new platebni_karta();
        pk2.setCislo(1234558941257411L);
        pk2.setCvv(719);
        pk2.setDatum_vydani(new java.sql.Date(178956581891L));
        pk2.setDatum_vyprseni(new java.sql.Date(1789655581891L));

        uver uv1 = new uver();
        uv1.setHodnota(new BigDecimal(45681));
        uv1.setTyp("Hippoteka");
        uv1.setUrok(new BigDecimal(0.45));
        db.insert_uver(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)), uv1.getTyp(), uv1.getHodnota(), uv1.getUrok());
        konto k1 = new konto();
        k1.setTyp("Make it rain+");
        k1.setZustatek(new BigDecimal(454898189));
        for (konto obj : db.search_konto(null, k1.getTyp(), k1.getZustatek())) {
            db.delete_konto(obj);
        }
        db.insert_konto(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)), k1.getTyp(), k1.getZustatek());
        debitni_karta dk2 = new debitni_karta();
        dk2.setId_ucet(db.search_konto(null, k1.getTyp(), k1.getZustatek()).get(0).getId_ucet());
        db.insert_debitni_karta(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)), pk2.getCislo(), pk2.getCvv(), pk2.getDatum_vydani(), pk2.getDatum_vyprseni(), dk2.getId_ucet());
        assertEquals(2, db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)).getCards().size());
        assertEquals(2, db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, po.getIco(), po.getNazev_firmy(), po.getSidlo()).get(0)).getAccounts().size());
    }

    @Test
    public void UnitTestOfUcet_delete_client() {
        DB db = new DB();
        fyzicka_osoba fo = new fyzicka_osoba();
        fo.setAge(52);
        fo.setBydliste("Right Up My Alley");
        fo.setJmeno("Fred");
        fo.setPrijmeni("Rogers");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge());
        konto k = new konto();
        k.setTyp("Dime saver");
        k.setZustatek(new BigDecimal(55892));
        for (konto object : db.search_konto(null, k.getTyp(), k.getZustatek())) {
            db.delete_konto(object);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)), k.getTyp(), k.getZustatek());
        db.ucet_delete_klient(db.get_ucet_of_konto(db.search_konto(null, k.getTyp(), k.getZustatek()).get(0)), db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)), true);
        assertEquals(0, db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)).getAccounts().size());
    }

    @Test
    public void UnitTestOfTransakce() {
        DB db = new DB();
        fyzicka_osoba fo = new fyzicka_osoba();
        fo.setAge(19);
        fo.setBydliste("Beston Way 221");
        fo.setJmeno("Jack");
        fo.setPrijmeni("Daniels");
        for (transakce t : db.search_transakce(null, new BigDecimal(458), new java.sql.Date(7897897897L), new java.sql.Date(7898897897L), null, null, null)) {
            db.delete_transakce(t);
        }
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge());
        konto k = new konto();
        k.setTyp("Typper");
        k.setZustatek(new BigDecimal(849182));
        for (konto object : db.search_konto(null, k.getTyp(), k.getZustatek())) {
            db.delete_konto(object);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)), k.getTyp(), k.getZustatek());
        konto k1 = new konto();
        k1.setTyp("Topper");
        k1.setZustatek(new BigDecimal(45677));
        for (konto object : db.search_konto(null, k1.getTyp(), k1.getZustatek())) {
            db.delete_konto(object);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)), k1.getTyp(), k1.getZustatek());
        Integer kid1 = db.search_konto(null, k.getTyp(), k.getZustatek()).get(0).getId_ucet();
        Integer kid2 = db.search_konto(null, k1.getTyp(), k1.getZustatek()).get(0).getId_ucet();
        db.insert_transakce(new BigDecimal(458), new java.sql.Date(7897897897L), new java.sql.Date(7898897897L), kid1, kid2);
        List<transakce> result = db.search_transakce(null, null, null, null, kid1, kid2, null);
        assertEquals(1, result.size());
        db.delete_transakce(db.search_transakce(null, new BigDecimal(458), new java.sql.Date(7897897897L), new java.sql.Date(7898897897L), kid1, kid2, null).get(0));
        assertEquals(0, db.search_transakce(null, new BigDecimal(458), new java.sql.Date(7897897897L), new java.sql.Date(7898897897L), kid1, kid2, null).size());
    }
    //Advanced integration tests

    @Test
    public void UnitTestOfMultipleAccounts() {
        DB db = new DB();
        fyzicka_osoba fo = new fyzicka_osoba();
        fo.setAge(241);
        fo.setBydliste("Boston");
        fo.setJmeno("Winston");
        fo.setPrijmeni("Bishop");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge());
        konto k = new konto();
        k.setTyp("Savings+");
        k.setZustatek(new BigDecimal(841182));
        for (konto object : db.search_konto(null, k.getTyp(), k.getZustatek())) {
            db.delete_konto(object);
        }
        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0)), k.getTyp(), k.getZustatek());
        assertEquals(1,db.search_konto(null, k.getTyp(), k.getZustatek()).size());
        db.delete_konto(db.search_konto(null, k.getTyp(), k.getZustatek()).get(0));
        assertEquals(0,db.search_konto(null, k.getTyp(), k.getZustatek()).size());
    }

    @Test
    public void UnitTestOfDelete_fyzicka_osoba() {
        DB db = new DB();
        fyzicka_osoba fo = new fyzicka_osoba();
        fo.setAge(241);
        fo.setBydliste("Africa");
        fo.setJmeno("Mister");
        fo.setPrijmeni("Sandman");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge());
        assertEquals(1,db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).size());
        db.delete_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0));
        assertEquals(0,db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).size());
    }

    @Test
    public void UnitTestOfSearch_fyzicka_osoba() {
        DB db = new DB();
        fyzicka_osoba fo = new fyzicka_osoba();
        fo.setAge(241);
        fo.setBydliste("Prague");
        fo.setJmeno("Tadeas");
        fo.setPrijmeni("Binder");
        for (fyzicka_osoba obj : db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge())) {
            db.delete_fyzicka_osoba(obj);
        }
        db.insert_fyzicka_osoba(fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge());
        assertEquals(1,db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).size());
        db.alter_fyzicka_osoba(db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).get(0),fo.getBydliste(),"Patrik","Hezucky",fo.getAge());
        assertEquals(0,db.search_fyzicka_osoba(null, fo.getBydliste(), fo.getJmeno(), fo.getPrijmeni(), fo.getAge()).size());
    }

}
