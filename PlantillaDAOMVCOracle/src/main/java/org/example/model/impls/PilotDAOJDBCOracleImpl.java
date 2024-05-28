package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.F1pojo;
import org.example.model.exceptions.DAOException;
import org.example.view.F1;
import org.example.view.Model;

import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class PilotDAOJDBCOracleImpl implements DAO<F1pojo> {
    Model model = new Model();

    @Override
    public F1pojo get(Long id) throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        F1pojo pilot = null;
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            //Accés a la BD usant l'API JDBC
            try {
                con = DriverManager.getConnection(url, user, password);
                //Comprovem si hi ha una taula creada amb el nom de pilot
                DatabaseMetaData dbmd = con.getMetaData();
                rs = dbmd.getTables(null, "C##HR", "PILOT", null);
                if (!rs.next()) {
                    st = con.createStatement();
                    st.execute("BEGIN" + "CreatePilotTaula;" + "END;");
                }
                String query = "SELECT * FROM PILOT";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, id);
                rs = pst.executeQuery();
                if (rs.next()) {
                    pilot = new F1pojo(rs.getString("Nom"), rs.getDouble("altura"), rs.getBoolean("campeoMundial"), rs.getInt("numVictories"), rs.getInt("Numero"));
                }

            } catch (SQLException throwables) {
                throw new DAOException(8);
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new DAOException(8);
                }

            }
        }
        return pilot;
    }


    //fem un metode per crear la taula
    public void createTable() throws DAOException {
        Connection con = null;
        Statement st = null;
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            try {
                con = DriverManager.getConnection(url, user, password);
                st = con.createStatement();
                st.execute("BEGIN CreatePilotTaula(); END;");
            } catch (SQLException throwables) {
                throw new DAOException(8);
            } finally {
                try {
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new DAOException(8);
                }
            }
        }
    }

    @Override
    public List<F1pojo> getAll() throws DAOException {
        //Declaració de variables del mètode
        List<F1pojo> pilot = new ArrayList<>();
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            //Accés a la BD usant l'API JDBC
            try (
                 Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT PILOT.*, ESCUDERIES.Escuderia \n" +
                         "FROM PILOT \n" +
                         "LEFT JOIN ESCUDERIES ON PILOT.Numero = ESCUDERIES.Numero");
            ) {
                while (rs.next()) {
                    int campeoMundialInt = rs.getInt("campeoMundial");
                    boolean campeoMundial = (campeoMundialInt == 1);
                    F1pojo f1pojo = new F1pojo(rs.getString("Nom"), rs.getDouble("altura"), campeoMundial, rs.getInt("numVictories"), rs.getString("Escuderia"), rs.getInt("Numero"));
                    pilot.add(f1pojo);
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
        }
        return pilot;
    }
    //Creem un metode per a insertar
    public void insertar(F1pojo f1pojo) throws DAOException, SQLException {
        Connection con = null;
        PreparedStatement st = null;
        String callSql = "{call InsertarPilot(?, ?, ?, ?, ?, ?)}";
        DefaultTableModel model = this.model.getModel();
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            try {
                con = DriverManager.getConnection(url, user, password);
                st = con.prepareStatement(callSql);
                st.setString(1, f1pojo.getPilot());
                st.setDouble(2, f1pojo.getAltura());
                st.setBoolean(3, f1pojo.isCampeoMundial());
                st.setInt(4, f1pojo.getNumVictories());
                st.setString(5, f1pojo.getEscuderies().toString());
                st.setInt(6, model.getRowCount());
                st.executeUpdate();
            } finally {
                try {
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new DAOException(8);
                }
            }
        }
    }



    @Override
    public void save(F1pojo obj) throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            //Accés a la BD usant l'API JDBC
            try {
                con = DriverManager.getConnection(url, user, password);
                st = con.prepareStatement("BEGIN InsertarPilot(); END;");
                st.setString(1, obj.getPilot());
                st.setDouble(2, obj.getAltura());
                st.setBoolean(3, obj.isCampeoMundial());
                st.setInt(4, obj.getNumVictories());
                st.setString(5, obj.getEscuderies().toString());
                st.executeUpdate();
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
            } finally {
                closeResources(null, st, con);
            }
        }
    }
    public String borrar(int Numero) throws DAOException, SQLException {
        Connection con = null;
        CallableStatement st = null;
        String callSql = "{? = call BorrarPilot(?)}";
        ResourceBundle rd = ResourceBundle.getBundle("property");
        {
            String url = rd.getString("url");
            String user = rd.getString("userName");
            String password = rd.getString("password");
            try {
                con = DriverManager.getConnection(url, user, password);
                st = con.prepareCall(callSql);
                st.registerOutParameter(1, Types.VARCHAR); // Registrar el tipus de retorn
                //Establir el nom del pilot com a paràmetre
                st.setInt(2, Numero);
                st.execute();

                return st.getString(1); // Obtenir el missatge retornat
            } finally {
                try {
                    if (st != null) st.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    throw new DAOException(8);
                }
            }
        }
    }

    private void closeResources(ResultSet rs, Statement st, Connection con) throws DAOException {
        try {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (SQLException throwables) {
            throw new DAOException(8);
        }
    }
}
