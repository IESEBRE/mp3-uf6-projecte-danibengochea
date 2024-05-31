package org.example.view;

import org.example.model.entities.F1pojo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Locale;

public class F1 extends JFrame{
    private static final long serialVersionUID = 1L;

    private int accio=0;
    private JPanel panel;
    private JTable taula;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campPilot;
    private JTextField campAltura;
    private JCheckBox campCampeoMundial;
    private JTextField campNumVictories;
    private JTabbedPane tabbedPane1;
    private JComboBox<F1pojo.Equips.Escuderia> comboBox1;


    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public static final String FITXER = "pilots.dat";

    public JPanel getPanel() {
        return panel;
    }


    public JTable getTaula() {
        return taula;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }
    public JButton getModificarButton() {
        return modificarButton;
    }
    public JButton getBorrarButton() {
        return borrarButton;
    }
    public JTextField getCampPilot() {
        return campPilot;
    }
    public JTextField getCampAltura() {
        return campAltura;
    }
    public JCheckBox getCampCampeoMundial() {
        return campCampeoMundial;
    }
    public JTextField getCampNumVictories() {
        return campNumVictories;
    }
    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public F1() {
        //Definim la cultura de la nostra aplicació
        Locale.setDefault(new Locale("ca","ES"));
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Nom", "Altura","CampeoMundial","NumVictories"}, 0){
        };
        taula.setModel(model);
        taula.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        campCampeoMundial.requestFocus();
        this.setSize(600, 400);



    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new F1();
            }
        });
    }
}

