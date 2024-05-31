package org.example.model.daos;


import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public interface DAO <T>{


    List<T> getAll() throws DAOException;
    void save(T obj) throws DAOException;
    //Posem el metode de insertar

    /**
     * Metode per insertar un objecte a la taula
     * @param obj
     * @throws DAOException
     * @throws SQLException
     */
    void insertar(T obj) throws DAOException, SQLException;
//Posem el metode de borrar

    /**
     * Metode per obtenir el numero maxim de la taula
     * @return
     * @throws SQLException
     * @throws DAOException
     */
    default int obtenirNumeroMaxim() throws SQLException, DAOException {
    int maxNumero = 0;
    ResourceBundle rd = ResourceBundle.getBundle("property");
    String url = rd.getString("url");
    String user = rd.getString("userName");
    String password = rd.getString("password");

    String query = "SELECT MAX(Numero) AS MaxNumero FROM PILOT";

    try (Connection con = DriverManager.getConnection(url, user, password);
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {
        if (rs.next()) {
            maxNumero = rs.getInt("MaxNumero");
        }
    } catch (SQLException throwables) {
        int tipoError = throwables.getErrorCode();
        System.out.println(tipoError + " " + throwables.getMessage());
        switch (tipoError) {
            case 17002:
                tipoError = 8;
                break;
            default:
                tipoError = 0;  // Error desconocido
        }
        throw new DAOException(tipoError);
    }

    return maxNumero;
}

    //Tots els mètodes necessaris per interactuar en la BD

}
