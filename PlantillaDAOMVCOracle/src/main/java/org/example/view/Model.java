package org.example.view;
import org.example.model.entities.F1pojo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Model {

    private final DefaultTableModel model;
    private final ComboBoxModel<F1pojo.Equips.Escuderia> ComboBoxModel;

    public ComboBoxModel<F1pojo.Equips.Escuderia> getComboBoxModel() {
        return ComboBoxModel;
    }

    public DefaultTableModel getModel() {
        return model;
    }
    //Getters dels objectes del model
    public Model() {


        //Anem a definir l'estructura de la taula dels alumnes
        model = new DefaultTableModel(new Object[]{"Numero","Nom", "Altura", "Campeo Mundial", "Numero Victories", "Escuderia" }, 0) {
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                if (column == 1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Integer.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Double.class;
                    case 3:
                        return Boolean.class;
                    case 4:
                        return Integer.class;
                    case 5:
                        return String.class;

                    default:
                        return String.class;
                }
            }
        };
    ComboBoxModel = new DefaultComboBoxModel<>(F1pojo.Equips.Escuderia.values());
    }
}
