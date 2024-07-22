package com.xptoinfo.workorders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {

    private int number;
    private String date;
    private int idcli;
    private String device;
    private String issue;
    private String service;
    private String technician;
    private double price;

    public Order (int number, String date, int idcli, String device, String issue, String service, String technician, double price) {
        this.number = number;
        this.date = date;
        this.idcli = idcli;
        this.device = device;
        this.issue = issue;
        this.service = service;
        this.technician = technician;
        this.price = price;

    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public int getIdcli() {
        return idcli;
    }

    public String getDevice() {
        return device;
    }

    public String getIssue() {
        return issue;
    }

    public String getService() {
        return service;
    }

    public String getTechnician() {
        return technician;
    }

    public double getPrice() {
        return price;
    }

    public boolean createOrder() throws SQLException {

        String query = "INSERT INTO tbos (equipamento, defeito, servico, tecnico, valor, idcli) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseUtil.getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, device);
            pstm.setString(2, issue);
            pstm.setString(3, service);
            pstm.setString(4, technician);
            pstm.setDouble(5, price);
            pstm.setInt(6, idcli);

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0; // Checks whether if affects at least one row
        }
    }


}
