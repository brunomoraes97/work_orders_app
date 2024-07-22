package com.xptoinfo.workorders;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/work_orders";
    private static final String USER = "YOURUSER";
    private static final String PASSWORD = "YOURPASSWORD";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static int checkUserCredentials(Connection conn, String username, String password) {
        String query = "SELECT iduser FROM tbusuarios WHERE login = ? AND senha= ? ";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return rs.getInt("iduser");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public static List<Integer> getOrdersFromClient(Connection conn, String nomecli) {

        String query = "select O.os " +
                "from tbos as O " +
                "inner join tbclientes as C on (O.idcli= C.idcli) " +
                "where nomecli = ?;";

        List<Integer> orders = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(query)){
            pstm.setString(1, nomecli);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int os = rs.getInt("os");
                orders.add(os);
            }
            return orders;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Client> checkClients(Connection conn) {
        String query = "SELECT idcli, nomecli FROM tbclientes";
        List<Client> clients = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int idcli = rs.getInt("idcli");
                String nomecli = rs.getString("nomecli");
                clients.add(new Client(idcli, nomecli));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clients;
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();
        System.out.println(conn);
    }

}
