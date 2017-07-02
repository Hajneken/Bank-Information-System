package Controller;

import Entities.fyzicka_osoba;
import Entities.pravnicka_osoba;
import GUI.ErrorGUI;
import GUI.SuccessGUI;
import System.DB;
import static java.lang.Math.toIntExact;
import java.util.List;

/**
 *
 * @author zeman
 */
public class AlterController {

    /**
     * Can alter card number in SearchClientGUI
     *
     * @param altered_card_number
     * @param unaltered
     * @return boolean
     */
    public boolean alter_card_number(Long altered_card_number, Long unaltered) {
        DB db = new DB();
        // zmeni cislo karty +
        if (db.alter_platebni_karta(db.search_platebni_karta(null, null, unaltered, null, null, null).get(0), altered_card_number, null, null, null, null)) {
            new SuccessGUI().setVisible(true);
        } else {
            new ErrorGUI().setVisible(true);
        }
        return true;
    }

    /**
     * Can alter issue date in SearchClientGUI
     *
     * @param altered_issue_date
     * @param unaltered
     * @return boolean
     */
    public boolean alter_issue_date(long altered_issue_date, Long unaltered) {
        // zmeni datum issue_date
        DB db = new DB();
        if (db.alter_platebni_karta(db.search_platebni_karta(null, null, unaltered, null, null, null).get(0), null, null, new java.sql.Date(altered_issue_date), null, null)) {
            new SuccessGUI().setVisible(true);
        } else {
            new ErrorGUI().setVisible(true);
        }
        return true;
    }

    /**
     * Can alter expiery date in SearchClientGUI
     *
     * @param altered_expiery_date
     * @param unaltered
     * @return boolean
     */
    public boolean alter_expiery_date(long altered_expiery_date, Long unaltered) {
        DB db = new DB();
        // zmeni datum expirace
        //public boolean alter_platebni_karta(platebni_karta pk, Long Number, Integer CVV, Date Issue_date, Date Expiration_date, klient k)

        if (db.alter_platebni_karta(db.search_platebni_karta(null, null, unaltered, null, null, null).get(0), null, null, null, new java.sql.Date(altered_expiery_date), null)) {
            new SuccessGUI().setVisible(true);
            return true;
        } else {
            new ErrorGUI().setVisible(true);
            return true;
        }
    }

    /**
     * Can alter CVV number in SearchClientGUI
     *
     * @param altered_CVV
     * @param unaltered
     * @return boolean
     */
    public boolean alter_CVV(int altered_CVV, Long unaltered) {
        DB db = new DB();
        // zmeni CVV 
        if (db.alter_platebni_karta(db.search_platebni_karta(null, null, unaltered, null, null, null).get(0), null, altered_CVV, null, null, null)) {
            new SuccessGUI().setVisible(true);
        } else {
            new ErrorGUI().setVisible(true);
        }
        return true;
    }

    /**
     * Can alter name in SearchClientGUI
     *
     * @param altered_Full_name
     * @param ID_client
     * @return boolean
     */
    public boolean alter_Full_name(String altered_Full_name, Long ID_client) {
        DB db = new DB();
        String[] x = altered_Full_name.split(" ", 5);

        // creating lists which size will be compared later 
        List<fyzicka_osoba> list_fo = db.search_fyzicka_osoba(toIntExact(ID_client), null, null, null, null);
        List<pravnicka_osoba> list_po = db.search_pravnicka_osoba(toIntExact(ID_client), null, null, null);

        if (list_fo.size() > list_po.size()) {

            // change name of natural person
            if (db.alter_fyzicka_osoba(db.search_fyzicka_osoba(toIntExact(ID_client), null, null, null, null).get(0), null, x[0], x[1], null)) {
                new SuccessGUI().setVisible(true);
                return true;
            } else {
                new ErrorGUI().setVisible(true);
                return true;
            }
        } else {

            // change name of juridiscial person 
            if (db.alter_pravnicka_osoba(db.search_pravnicka_osoba(toIntExact(ID_client), null, null, null).get(0), null, altered_Full_name, null)) {
                new SuccessGUI().setVisible(true);
            } else {
                new ErrorGUI().setVisible(true);
                return true;
            }
        }
        return true;
    }

    /**
     * Can alter residency in SearchClientGUI.
     *
     * @param altered_residency
     * @param ID_client
     * @return boolean
     */
    public boolean alter_Residency(String altered_residency, Long ID_client) {
        DB db = new DB();

        // zmeni  bydliste 
        List<fyzicka_osoba> list_fo = db.search_fyzicka_osoba(toIntExact(ID_client), null, null, null, null);
        List<pravnicka_osoba> list_po = db.search_pravnicka_osoba(toIntExact(ID_client), null, null, null);

        //fyzicka osoba
        if (list_fo.size() > list_po.size()) {
            if (db.alter_fyzicka_osoba(db.search_fyzicka_osoba(toIntExact(ID_client), null, null, null, null).get(0), altered_residency, null, null, null)) {
                new SuccessGUI().setVisible(true);
                return true;
            } else {
                new ErrorGUI().setVisible(true);
                return true;
            }
        } else {

            // pravnicka osoba 
            if (db.alter_pravnicka_osoba(db.search_pravnicka_osoba(toIntExact(ID_client), null, null, null).get(0), null, null, altered_residency)) {
                new SuccessGUI().setVisible(true);
                return true;
            } else {
                new ErrorGUI().setVisible(true);
                return true;
            }
        }

    }

    /**
     * Can alter monthly limit in SearchClientGUI.
     *
     * @param altered_monthly_limit
     * @param unaltered
     * @return boolean
     */
    public boolean alter_monthly_limit(int altered_monthly_limit, Long unaltered) {
        DB db = new DB();

        // zmeni mesicni limit 
        if (db.alter_kreditni_karta(db.search_kreditni_karta(toIntExact(unaltered), null).get(0), altered_monthly_limit)) {
            new SuccessGUI().setVisible(true);
            return true;
        } else {
            new ErrorGUI().setVisible(true);
            return true;
        }
    }

}
