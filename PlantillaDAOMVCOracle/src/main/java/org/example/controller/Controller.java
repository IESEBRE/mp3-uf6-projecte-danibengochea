package org.example.controller;

import org.example.model.daos.DAO;
import org.example.model.entities.F1pojo;
import org.example.model.exceptions.DAOException;
import org.example.view.F1;
import org.example.model.impls.PilotDAOJDBCOracleImpl;
import org.example.view.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private final F1 view;
    private Model model = new Model();
    private PilotDAOJDBCOracleImpl dadesPilot;
    private JComboBox<F1pojo.Equips.Escuderia> comboBox1;



    public Controller(PilotDAOJDBCOracleImpl pilot, F1 view) {
        this.dadesPilot = pilot;
        this.view = view;

        //Mètode per lligar la vista i el model
        lligaVistaModel();

        //Assigno el codi dels listeners
        assignarCodiListeners();

    }

    private void lligaVistaModel() {
        try {
            dadesPilot.createTable();
            setModel(model.getModel(), dadesPilot.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }
        //Obtinc els objectes de la vista
        JTable taula = view.getTaula();
        //Obtinc els objectes del model
        DefaultTableModel model = this.model.getModel();
        taula.setModel(model);
        comboBox1 = view.getComboBox1();
        comboBox1.setModel(this.model.getComboBoxModel());
        //Forcem a que només se pugue seleccionar una fila de la taula
        taula.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        canvis.addPropertyChangeListener(this);
    }

    private void setModel(DefaultTableModel modelTaulaAlumne, List<F1pojo> all) {

        // Fill the table model with data from the collection
        for (F1pojo f1pojo : all) {
            modelTaulaAlumne.addRow(new Object[]{f1pojo.getNumero(), f1pojo.getPilot(), f1pojo.getAltura(), f1pojo.isCampeoMundial(), f1pojo.getNumVictories(), f1pojo.getEscuderies()});
        }
    }

    private void assignarCodiListeners() {

        DefaultTableModel model = this.model.getModel();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField pilot = view.getCampPilot();
        JTextField altura = view.getCampAltura();
        JCheckBox campeoMundial = view.getCampCampeoMundial();
        JTextField numVictories = view.getCampNumVictories();
        JTable taula = view.getTaula();

        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    /**
                     * Invoked when an action occurs.
                     *
                     * @param e the event to be processed
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pilot.getText().isBlank() || altura.getText().isBlank() || numVictories.getText().isBlank()) {
                            setExcepcio(new DAOException(1));
                            return;
                        } else {

                            try {
                                double alt = 0;
                                NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault());

                                String alturaText = altura.getText().trim();

                                alturaText = alturaText.replaceAll(",", ".");
                                if (!altura.getText().isBlank()) {
                                    alt = Double.parseDouble(alturaText);
                                }
                                if (alt < 1.50 || alt > 2.10) {
                                    setExcepcio(new DAOException(2));
                                    altura.requestFocus();
                                    return;
                                }
                                if (!numVictories.getText().isBlank()) {
                                    Integer.valueOf(numVictories.getText());
                                } else throw new NumberFormatException();
                                // Verifiquem si ja existeix una entrada amb les mateixes dades, si una sola columna es igual no dixara insertar
                                boolean duplicat = false;

                                //Creem una expressio regular per posar nom i cognom i que pugue portar caracters especials
                                String expresion = "^[a-zA-ZÀ-ÿ]+(?: [a-zA-ZÀ-ÿ]+)+$";
                                if (!pilot.getText().matches(expresion)) {
                                    setExcepcio(new DAOException(7));
                                    pilot.requestFocus();
                                    return;
                                }

                                for (int i = 0; i < model.getRowCount(); i++) {
                                    if (pilot.getText().equals(model.getValueAt(i, 1)) && (altura.getText().replaceAll(",", ".").equals(model.getValueAt(i, 2).toString()) || altura.getText().replaceAll("\\.", ",").equals(model.getValueAt(i, 2).toString())) &&
                                            numVictories.getText().equals(model.getValueAt(i, 4))) {
                                        duplicat = true;
                                        break;
                                    }
                                }

                                if (duplicat) {
                                    setExcepcio(new DAOException(3));
                                } else {
                                    int maxNumero = 0;
                                    int nouNumero = 0;
                                    try {
                                        maxNumero = dadesPilot.obtenirNumeroMaxim();
                                        nouNumero = maxNumero + 1;
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (DAOException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    F1pojo.Equips.Escuderia selectedTeam = (F1pojo.Equips.Escuderia) comboBox1.getSelectedItem();
                                    //Ho insertem a la bd
                                    model.addRow(new Object[]{ nouNumero, pilot.getText(), alt, campeoMundial.isSelected(), numVictories.getText(), selectedTeam});
                                    dadesPilot.insertar(new F1pojo(pilot.getText(), alt, campeoMundial.isSelected(), Integer.parseInt(numVictories.getText()), Objects.requireNonNull(comboBox1.getSelectedItem().toString()), 1));
                                    pilot.setText("");
                                    altura.setText("");
                                    campeoMundial.setSelected(false);
                                    numVictories.setText("");
                                    comboBox1.setSelectedIndex(0);
                                    pilot.requestFocus();
                                }

                            } catch (NumberFormatException e1) {
                                setExcepcio(new DAOException(4));
                                altura.setSelectionStart(0);
                                altura.setSelectionEnd(altura.getText().length());
                                altura.requestFocus();
                            }
                            catch (DAOException e1) {
                                setExcepcio(new DAOException(1));
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });


        taula.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Al seleccionar la taula omplim els camps de text amb les dades de la fila seleccionada
                int filaSel = taula.getSelectedRow();
                if (filaSel != -1) { //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    pilot.setText(model.getValueAt(filaSel, 1).toString());
                    altura.setText(model.getValueAt(filaSel, 2).toString());
                    campeoMundial.setSelected((Boolean) model.getValueAt(filaSel, 3));
                    numVictories.setText(model.getValueAt(filaSel, 4).toString());

                } else {                              //No tenim cap fila seleccionada
                    //Posem els camps de text en blanc
                    pilot.setText("");
                    altura.setText("");
                    campeoMundial.setText("");
                    numVictories.setText("");

                }
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mirem si tenim una fila seleccionada
                int filaSel = taula.getSelectedRow();

                NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault());

                String alturaText = altura.getText().trim();
                alturaText = alturaText.replaceAll(",", ".");
                double alt = 0;
                if (!altura.getText().isBlank()) {
                    alt = Double.parseDouble(alturaText);
                }

                if (filaSel != -1) { // Tenim una fila seleccionada
                    // Comprovem que totes les caselles continguin informació
                    if (pilot.getText().isBlank() || altura.getText().isBlank() || numVictories.getText().isBlank()) {
                        setExcepcio(new DAOException(1));
                    } else {
                        // Verifiquem si les dades modificades ja existeixen a la taula si una sola dada no existeix no dixara modificar
                        boolean duplicat = false;
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (pilot.getText().equals(model.getValueAt(i, 1)) && (altura.getText().replaceAll(",", ".").equals(model.getValueAt(i, 1).toString()) || altura.getText().replaceAll("\\.", ",").equals(model.getValueAt(i, 2).toString())) ||
                                    numVictories.getText().equals(model.getValueAt(i, 4)) || comboBox1.getSelectedItem().equals(model.getValueAt(i, 5))) {
                                duplicat = true;
                                break;
                            }
                            String expresion = "^[a-zA-ZÀ-ÿ]+(?: [a-zA-ZÀ-ÿ]+)+$";
                            if (!pilot.getText().matches(expresion)) {
                                setExcepcio(new DAOException(7));
                                pilot.requestFocus();
                                return;
                            }
                            //Si el nom ja existeix no dixem insertar
                        }

                        if (duplicat) {
                            setExcepcio(new DAOException(3));
                        } else {
                            //Guardem el numero
                            int Numero = (int) model.getValueAt(filaSel, 0);
                            int maxNumero = 0;
                            int nouNumero = 0;
                            try {
                                maxNumero = dadesPilot.obtenirNumeroMaxim();
                                nouNumero = maxNumero + 1;
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            } catch (DAOException ex) {
                                throw new RuntimeException(ex);
                            }

                            // Si no és un duplicat, eliminem la fila antiga i afegim la nova fila amb les dades modificades
                            model.removeRow(filaSel);
                            //Ara el borrem de la base de dades
                            try {
                                dadesPilot.borrar(Numero);
                                dadesPilot.insertar(new F1pojo(pilot.getText(), alt, campeoMundial.isSelected(), Integer.parseInt(numVictories.getText()), Objects.requireNonNull(comboBox1.getSelectedItem()).toString(),1));
                            } catch (DAOException ex) {
                                throw new RuntimeException(ex);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            model.addRow(new Object[]{nouNumero,pilot.getText(), alt, campeoMundial.isSelected(), numVictories.getText(), comboBox1.getSelectedItem()});
                            pilot.setText("");
                            altura.setText("");
                            campeoMundial.setSelected(false);
                            numVictories.setText("");

                            pilot.requestFocus();

                        }
                    }
                } else { // No tenim cap fila seleccionada
                    // Mostrem un missatge d'error
                    setExcepcio(new DAOException(5));
                }
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Mirem si tenim una fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) { //Tenim una fila seleccionada
                    //Guardem el valor de la  columna del numero
                    int Numero = (int) model.getValueAt(filaSel, 0);
                    //Borrem la fila seleccionada
                    try {
                        dadesPilot.borrar(Numero);
                    } catch (DAOException ex) {
                        throw new RuntimeException(ex);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    model.removeRow(filaSel);
                    //Posem els camps de text en blanc
                    pilot.setText("");
                    altura.setText("");
                    campeoMundial.setText("");
                    numVictories.setText("");
                } else {                              //No tenim cap fila seleccionada
                    //Mostrem un missatge d'error
                    setExcepcio(new DAOException(5));
                }
            }
        });
        campeoMundial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (campeoMundial.isSelected()) {
                    campeoMundial.setText("Si");
                } else {
                    campeoMundial.setText("No");
                }
            }
        });
    }
    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO = "excepcio";
    private DAOException excepcio;

    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell = this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell, excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis = new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DAOException rebuda = (DAOException) evt.getNewValue();
        JTextField altura = view.getCampAltura();
        JTextField numVictories = view.getCampNumVictories();
        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            if (evt.getPropertyName().equals(PROP_EXCEPCIO)) {
                int codi = rebuda.getCodi();
                String missatge = rebuda.retornaMissatge();
                switch (codi) {
                    case 1, 3, 5, 7, 8:
                        JOptionPane.showMessageDialog(null, rebuda.retornaMissatge());
                        break;
                    case 2, 4:
                        JOptionPane.showMessageDialog(null, rebuda.retornaMissatge());
                        altura.setSelectionStart(0);
                        altura.setText("");
                        altura.requestFocus();
                        break;
                    case 6:
                        JOptionPane.showMessageDialog(null, rebuda.retornaMissatge());
                        numVictories.setSelectionStart(0);
                        numVictories.setText("");
                        numVictories.requestFocus();
                        break;
                }
            }
        }
    }
}
