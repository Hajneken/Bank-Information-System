package Controller;

import Entities.konto;
import GUI.ErrorGUI;
import GUI.SuccessGUI;
import java.sql.Date;
import System.DB;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author zeman
 */
public class CreateController {

    private static final Logger LOG = Logger.getLogger(CreateController.class.getName());

    public boolean CreateNaturalPersonAccount(String[] InfoArray) {
//instantiating database
        DB db = new DB();
        LOG.fine("Connecting to the database...");

// instantiating klient with given parameters and attaching fyzicka_osoba to it 
        db.insert_fyzicka_osoba(InfoArray[6], InfoArray[4], InfoArray[5], 100);

// convertig Strings back to Integer
        long card_number = Long.parseLong(InfoArray[2]);
        int CVV = Integer.parseInt(InfoArray[3]);
        int mlimit = Integer.parseInt(InfoArray[7]);

// converting to java.sql.Date
        java.sql.Date issue_date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        // adding 8 years for expiration
        long add_eight_years = issue_date.getTime() + (8 * (31449600000L));
        // instantiating expiration_date
        Date expiration_date = new Date(add_eight_years);

        db.insert_konto(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, InfoArray[6], InfoArray[4], InfoArray[5], 100).get(0)), "student", new BigDecimal(0));

        // case where user chooses Credit_card 
        if ("Credit_card".equals(InfoArray[1])) {
            db.insert_kreditni_karta(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, InfoArray[6], InfoArray[4], InfoArray[5], 100).get(0)), card_number, CVV, issue_date, expiration_date, mlimit);
        }

        // case where user chooses Debit_card
        // najdeme prave pridanou fo, ziskame z ni klienta, z toho ziskame seznam vlastnenych uctu, vybereme prave pridany, z toho ID
        int IDAccount = db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, InfoArray[6], InfoArray[4], InfoArray[5], 100).get(0)).getAccounts().get(0).getId();

        if ("Debit_card".equals(InfoArray[1])) {
            db.insert_debitni_karta(db.get_klient_of_fyzicka_osoba(db.search_fyzicka_osoba(null, InfoArray[6], InfoArray[4], InfoArray[5], 100).get(0)), card_number, CVV, issue_date, expiration_date, IDAccount);
        }
        return true;

    }

    public boolean CreateJuridicalPersonAccount(String[] InfoArray) {
        //instantiating new current date
        java.sql.Date issue_date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        long add_eight_years = issue_date.getTime() + 31449600000L;
        // instantiating expiration_date
        Date expiration_date = new Date(add_eight_years);

        //generating random number for ICO
        int x = (int) Math.round(Math.random() * 100000000);

        //instantiating database
        DB db = new DB();

        //(Integer ICO, String Name, String HQ)
        db.insert_pravnicka_osoba(x, InfoArray[4], InfoArray[6]);

        // case where user chooses Credit_card 
        //  insert_kreditni_karta(klient k, Integer Number, Integer CVV, java.sql.Date Issue_date, java.sql.Date Expiration_date, Integer Charge_limit) {
        // public List<pravnicka_osoba> search_pravnicka_osoba(Integer ID, Integer ICO, String Name, String HQ)
        if ("Credit_card".equals(InfoArray[1])) {
            db.insert_kreditni_karta(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, x, InfoArray[4], InfoArray[6]).get(0)), Long.parseLong((InfoArray[2])), Integer.parseInt(InfoArray[3]), issue_date, expiration_date, Integer.parseInt(InfoArray[7]));

        }

        Integer balance = Integer.parseInt(InfoArray[8]);

        db.insert_konto(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, x, InfoArray[4], InfoArray[6]).get(0)), "firma", new BigDecimal(balance));

        //db.search_konto(...).get(0).getID_ucet();
        int account_ID = db.search_konto(null, "firma", new BigDecimal(InfoArray[8])).get(0).getId_ucet();

        if ("Debit_card".equals(InfoArray[1])) {
            db.insert_debitni_karta(db.get_klient_of_pravnicka_osoba(db.search_pravnicka_osoba(null, x, InfoArray[4], InfoArray[6]).get(0)), Long.parseLong(InfoArray[2]), Integer.parseInt(InfoArray[3]), issue_date, expiration_date, account_ID);
        }

        return true;

    }

    public boolean CreateTransaction(int InfoArray[]) {

        String transaction_type;

        //connecting to db
        DB db = new DB();
        LOG.fine("Connecting to the database...");

        // initialize current date 
        java.sql.Date issue_date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        // transfer date takes place exactly 1 day after issue_date
        long transfer_date1 = issue_date.getTime() + 86400000;
        Date transfer_date2 = new Date(transfer_date1);

        //deciding what type of transaction has been chosen 
        switch (InfoArray[0]) {
            case 0:
                transaction_type = "Payment";

                // performing transaction
                //get the ID of konto who recieves payment
                konto konto_reciever = db.search_konto(InfoArray[2], null, null).get(0);

                //get the ID of konto who pays
                konto konto_payer = db.search_konto(InfoArray[3], null, null).get(0);

                // subtract the amount from ID of konto_payer
                db.alter_konto(konto_payer, null, (konto_payer.getZustatek().subtract(new BigDecimal(InfoArray[1]))));

                // add the amount to ID of konto_reciever
                db.alter_konto(konto_reciever, null, (konto_reciever.getZustatek().add(new BigDecimal(InfoArray[1]))));

                // saving data about the transaction 
                // public boolean insert_transakce(BigDecimal Amount, Date Issue_date, Date Transfer_date, Integer IssuerID, Integer RecieverID)
                if (db.insert_transakce(new BigDecimal(InfoArray[1]), issue_date, transfer_date2, InfoArray[3], InfoArray[2])) {
                    new SuccessGUI().setVisible(true);
                    break;
                } else {
                    new ErrorGUI().setVisible(true);
                    break;
                }
            case 1:
                transaction_type = "Deposit";

                konto result = db.search_konto(InfoArray[2], null, null).get(0);

                //adding amount to the account 
                db.alter_konto(result, result.getTyp(), result.getZustatek().add(new BigDecimal(InfoArray[1])));

                // saving data about the transaction 
                if (db.insert_transakce(new BigDecimal(InfoArray[1]), issue_date, transfer_date2, InfoArray[2], InfoArray[2])) {
                    new SuccessGUI().setVisible(true);
                    break;
                } else {
                    new ErrorGUI().setVisible(true);
                    break;
                }
            default:
                new ErrorGUI().setVisible(true);
        }
        return false;
    }

}
