/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUITadeas;

import Entities.platebni_karta;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import System.DB;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 *
 * @author Bindex
 */
public class GUIHomeScreen extends JFrame {

    private JPanel panelAdd;
    private JButton addClient;
    private JPanel cards;
    private JLabel IntroLabel;
    private JFrame frame;
    private JLabel ACLabel;
    private JButton ACFOButton;
    private JButton ACPOButton;
    private JButton exitProgram;
    private JButton homeButton;
    private JLabel fillInLabel;
    private JTextField FOAddressTF;
    private JTextField FONameTF;
    private JTextField FOSurnameTF;
    private JTextField FOAgeTF;
    private JTextField PONameTF;
    private JTextField POICOTF;
    private JTextField POHQTF;
    private JButton ADDPOsubmit;
    private JButton ADDFOsubmit;
    private JLabel OKLabel;
    private JList cardlist;
    private JButton displaycard;
    private JScrollPane scrolling;
    private JButton homeButtonPO;
    private JButton homeButtonFO;
    private JLabel fillInLabelFO;
    public GUIHomeScreen() {
        frame = new JFrame("Bank Information System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);

        //Main Card Components
        IntroLabel = new JLabel("Bank Information System");
        IntroLabel.setVerticalTextPosition(JLabel.BOTTOM);
        IntroLabel.setHorizontalTextPosition(JLabel.CENTER);
        IntroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClient = new JButton("Add client");
        addClient.setAlignmentX(Component.CENTER_ALIGNMENT);
        addClient.addActionListener((ActionEvent evt) -> {
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Add Client Card");
        });
        exitProgram = new JButton("Exit");
        exitProgram.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitProgram.addActionListener((ActionEvent evt) -> {
            System.exit(0);
        });
        displaycard = new JButton("Display all cards");
        displaycard.setAlignmentX(Component.CENTER_ALIGNMENT);
        displaycard.addActionListener((ActionEvent evt) -> {
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Display Cards Card");
        });

        //Add Client Card Components
        ACLabel = new JLabel("Which client would you like to create?");
        ACLabel.setVerticalTextPosition(JLabel.BOTTOM);
        ACLabel.setHorizontalTextPosition(JLabel.CENTER);
        ACLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ACFOButton = new JButton("Physical Person");
        ACFOButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ACPOButton = new JButton("Judicial Person");
        ACPOButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButton = new JButton("Back");
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButton.addActionListener((ActionEvent evt) -> {

            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Main Card");
        });
        ACFOButton.addActionListener((ActionEvent evt) -> {
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Add FO Card");
        });
        ACPOButton.addActionListener((ActionEvent evt) -> {
            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Add PO Card");
        });
        

        //AddFO Card Components
        fillInLabelFO = new JLabel("Please fill in all the fields below.");
        fillInLabelFO.setVerticalTextPosition(JLabel.BOTTOM);
        fillInLabelFO.setHorizontalTextPosition(JLabel.CENTER);
        fillInLabelFO.setAlignmentX(Component.CENTER_ALIGNMENT);
        FOAddressTF = new JTextField("Address", 20);
        FOAddressTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        FOAddressTF.setMaximumSize(FOAddressTF.getPreferredSize());
        FONameTF = new JTextField("Name", 20);
        FONameTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        FONameTF.setMaximumSize(FONameTF.getPreferredSize());
        FOSurnameTF = new JTextField("Surname", 20);
        FOSurnameTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        FOSurnameTF.setMaximumSize(FOSurnameTF.getPreferredSize());
        FOAgeTF = new JTextField("Age", 20);
        FOAgeTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        FOAgeTF.setMaximumSize(FOAgeTF.getPreferredSize());
        ADDFOsubmit = new JButton("Submit");
        ADDFOsubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        ADDFOsubmit.addActionListener((ActionEvent evt) -> {
            try {
                Integer age = Integer.parseInt(FOAgeTF.getText());

                DB db = new DB();
                if (db.insert_fyzicka_osoba(FOAddressTF.getText(), FONameTF.getText(), FOSurnameTF.getText(), age)) {
                    CardLayout cardLayout = (CardLayout) cards.getLayout();
                    cardLayout.show(cards, "OK Card");
                } else {
                    fillInLabelFO.setText("Unknown error, please check the output.");
                }
            } catch (NumberFormatException e) {
                fillInLabelFO.setText(e.getMessage());
            }
        });
        homeButtonFO = new JButton("Back");
        homeButtonFO.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButtonFO.addActionListener((ActionEvent evt) -> {

            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Main Card");
        });

        //AddPO Components
        fillInLabel = new JLabel("Please fill in all the fields below.");
        fillInLabel.setVerticalTextPosition(JLabel.BOTTOM);
        fillInLabel.setHorizontalTextPosition(JLabel.CENTER);
        fillInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        PONameTF = new JTextField("Name", 20);
        PONameTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        PONameTF.setMaximumSize(PONameTF.getPreferredSize());
        POICOTF = new JTextField("ICO", 20);
        POICOTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        POICOTF.setMaximumSize(POICOTF.getPreferredSize());
        POHQTF = new JTextField("Headquarters", 20);
        POHQTF.setAlignmentX(Component.CENTER_ALIGNMENT);
        POHQTF.setMaximumSize(POHQTF.getPreferredSize());
        ADDPOsubmit = new JButton("Submit");
        ADDPOsubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        ADDPOsubmit.addActionListener((ActionEvent evt) -> {
            try {
                Integer ico = Integer.parseInt(POICOTF.getText());

                DB db = new DB();
                if (db.insert_pravnicka_osoba(ico, PONameTF.getText(), POHQTF.getText())) {
                    CardLayout cardLayout = (CardLayout) cards.getLayout();
                    cardLayout.show(cards, "OK Card");
                } else {
                    fillInLabel.setText("Unknown error, please check the output.");
                }
            } catch (NumberFormatException e) {
                fillInLabel.setText(e.getMessage());
            }
        });
        homeButtonPO = new JButton("Back");
        homeButtonPO.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButtonPO.addActionListener((ActionEvent evt) -> {

            CardLayout cardLayout = (CardLayout) cards.getLayout();
            cardLayout.show(cards, "Main Card");
        });

        //OKCard Components
        OKLabel = new JLabel("Operation successful!");
        OKLabel.setVerticalTextPosition(JLabel.BOTTOM);
        OKLabel.setHorizontalTextPosition(JLabel.CENTER);
        OKLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Display Card Components
        DB db = new DB();
        List<platebni_karta> x = db.display_platebni_karta();
        DefaultListModel listModel = new DefaultListModel();
        for (platebni_karta object : x) {
            listModel.addElement(object.getCislo());
        }
        scrolling = new JScrollPane();
        scrolling.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        cardlist = new JList(listModel);
        scrolling.setViewportView(cardlist);
        cardlist.setVisibleRowCount(10);
        cardlist.setFixedCellWidth(390);
        cardlist.setBounds(5, 5, 385, 250);
        //Main Card
        JPanel cardMain = new JPanel();
        BoxLayout layoutcm = new BoxLayout(cardMain, BoxLayout.Y_AXIS);
        cardMain.setLayout(layoutcm);
        cardMain.add(Box.createRigidArea(new Dimension(0, 10)));
        cardMain.add(IntroLabel);
        cardMain.add(Box.createRigidArea(new Dimension(0, 10)));
        cardMain.add(addClient);
        cardMain.add(Box.createRigidArea(new Dimension(0, 10)));
        cardMain.add(displaycard);
        cardMain.add(Box.createRigidArea(new Dimension(0, 100)));
        cardMain.add(exitProgram);

        //Add Client Card
        JPanel addClientCard = new JPanel();
        BoxLayout layoutacc = new BoxLayout(addClientCard, BoxLayout.Y_AXIS);
        addClientCard.setLayout(layoutacc);
        addClientCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addClientCard.add(ACLabel);
        addClientCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addClientCard.add(ACFOButton);
        addClientCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addClientCard.add(ACPOButton);
        addClientCard.add(Box.createRigidArea(new Dimension(0, 100)));
        addClientCard.add(homeButton);

        //Add FO Card
        JPanel addFOCard = new JPanel();
        BoxLayout layoutafo = new BoxLayout(addFOCard, BoxLayout.Y_AXIS);
        addFOCard.setLayout(layoutafo);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(fillInLabelFO);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(FONameTF);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(FOSurnameTF);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(FOAgeTF);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(FOAddressTF);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(ADDFOsubmit);
        addFOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addFOCard.add(homeButtonFO);

        //Add PO Card
        JPanel addPOCard = new JPanel();
        BoxLayout layoutapo = new BoxLayout(addPOCard, BoxLayout.Y_AXIS);
        addPOCard.setLayout(layoutapo);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(fillInLabel);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(PONameTF);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(POICOTF);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(POHQTF);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(ADDPOsubmit);
        addPOCard.add(Box.createRigidArea(new Dimension(0, 10)));
        addPOCard.add(homeButtonPO);

        //OKCard
        JPanel OKCard = new JPanel();
        BoxLayout layoutok = new BoxLayout(OKCard, BoxLayout.Y_AXIS);
        OKCard.setLayout(layoutok);
        OKCard.add(Box.createRigidArea(new Dimension(0, 50)));
        OKCard.add(OKLabel);
        OKCard.add(Box.createRigidArea(new Dimension(0, 50)));
        OKCard.add(homeButton);

        //Display Cards Card
        JPanel DisplayCCard = new JPanel();
        BoxLayout layoutdcc = new BoxLayout(DisplayCCard, BoxLayout.Y_AXIS);
        DisplayCCard.setLayout(layoutdcc);
        DisplayCCard.add(scrolling);
        DisplayCCard.add(homeButton);
        

        //Cards
        cards = new JPanel(new CardLayout());
        cards.add(cardMain, "Main Card");
        cards.add(addClientCard, "Add Client Card");
        cards.add(OKCard, "OK Card");
        cards.add(addFOCard, "Add FO Card");
        cards.add(addPOCard, "Add PO Card");
        cards.add(DisplayCCard, "Display Cards Card");

        //Draw
        getContentPane().add(cards);
        frame.pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIHomeScreen();
            }
        });
    }

}
