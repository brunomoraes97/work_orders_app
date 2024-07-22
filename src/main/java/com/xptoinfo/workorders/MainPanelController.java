package com.xptoinfo.workorders;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.List;

public class MainPanelController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private int userId;
    @FXML
    private ChoiceBox<String> users;
    @FXML
    private Button updateList;
    @FXML
    private ChoiceBox<String> osUser;
    @FXML
    private ChoiceBox<Integer> osId;
    @FXML
    private Button searchOrder;
    @FXML
    private TextField osNumber;
    @FXML
    private TextField osData;
    @FXML
    private TextField idcliOs;
    @FXML
    private TextField equipmentOs;
    @FXML
    private TextField defeitoOs;
    @FXML
    private TextField servicoOs;
    @FXML
    private TextField tecnicoOs;
    @FXML
    private TextField valorOs;
    @FXML
    private Button createOrderButton;
    @FXML
    private TextArea createEquipment;
    @FXML
    private TextArea createIssue;
    @FXML
    private TextArea createTechnician;
    @FXML
    private TextArea createService;
    @FXML
    private TextArea createPrice;
    @FXML
    private TextField clientName;
    @FXML
    private TextField clientPhone;
    @FXML
    private TextField clientAddress;
    @FXML
    private TextField clientEmail;

    @FXML
    private void initialize() {
        updateChoices();
        populateUsersChoice();
        this.osUser.setOnAction(event -> {
            try {
                onUserSelected();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void populateWorkOrders(int idcli) {
        // get UserID
    }

    @FXML
    private void populateUsersChoice() {
        try {
            Connection conn = DatabaseUtil.getConnection();
            List<Client> clients = DatabaseUtil.checkClients(conn);
            this.osUser.getItems().clear();

            for (Client client : clients) {
                String clientName = client.getName();
                this.osUser.getItems().add(clientName);

            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    @FXML
    private void onUserSelected() throws SQLException {
        String selectedUser = osUser.getValue();
        Connection conn = DatabaseUtil.getConnection();
        List<Integer> orders = DatabaseUtil.getOrdersFromClient(conn, selectedUser);
        this.osId.getItems().clear();

        for (Integer os : orders) {
            this.osId.getItems().add(os);
        }
    }

    @FXML
    private void clearFields() {
        this.osNumber.clear();
        this.osData.clear();
        this.idcliOs.clear();
        this.equipmentOs.clear();
        this.defeitoOs.clear();
        this.servicoOs.clear();
        this.tecnicoOs.clear();
        this.valorOs.clear();
    }
    @FXML
    public int getIdCliFromNomeCli(String nomeCli) throws SQLException {

        try {
            String query = "SELECT idcli FROM tbclientes where nomecli = ?";

            Connection conn = DatabaseUtil.getConnection();

            try (PreparedStatement pstm = conn.prepareStatement(query)) {
                pstm.setString(1, nomeCli);
                ResultSet rs = pstm.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return rs.getInt("idcli");
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void onClickCreateClient() throws SQLException {
        String clientName = this.clientName.getText();
        String clientPhone = this.clientPhone.getText();
        String clientAddress = this.clientAddress.getText();
        String clientEmail = this.clientEmail.getText();

        String query = "INSERT INTO tbclientes (nomecli, endcli, fonecli, emailcli) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = DatabaseUtil.getConnection();

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, clientName);
            pstm.setString(2, clientAddress);
            pstm.setString(3, clientPhone);
            pstm.setString(4, clientEmail);

            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Create Client");
                alert.setHeaderText("Success! Client created!");
                alert.setContentText("Client " + clientName);
                alert.showAndWait();

                this.clientName.clear();
                this.clientPhone.clear();
                this.clientAddress.clear();
                this.clientEmail.clear();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Create Client");
                alert.setHeaderText("Something is wrong. Please try again.");
                alert.setContentText("Client " + clientName);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }




    }

    @FXML
    private void onClickCreateOrder() throws SQLException {

        try {
            String nomeCli = this.users.getValue();
            int idCli = getIdCliFromNomeCli(nomeCli);
            String equipment = this.createEquipment.getText();
            String issue = this.createIssue.getText();
            String technician = this.createTechnician.getText();
            String service = this.createService.getText();
            String price = this.createPrice.getText();
            double doubleprice = Double.parseDouble(price);

            Order newOrder = new Order(0, "", idCli, equipment, issue, technician, service, doubleprice);
            boolean orderCreated = newOrder.createOrder();
            if (orderCreated) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Create Order");
                alert.setHeaderText("Success! Order created!");
                alert.setContentText("Order for " + nomeCli);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Create Order");
                alert.setHeaderText("Something is wrong. Please try again.");
                alert.setContentText("Order for " + nomeCli);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void onSearchButtonClicked() throws SQLException {

        clearFields();
        Connection conn = DatabaseUtil.getConnection();
        String nomecli = this.osUser.getValue();
        Integer os = this.osId.getValue();

        String query = "SELECT data_os, equipamento, defeito, servico, tecnico, valor, idcli " +
                "FROM tbos WHERE os = ?;";

        try (PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setInt(1, os);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String data_os = rs.getString("data_os");
                int idcli = rs.getInt("idcli");
                String equipamento = rs.getString("equipamento");
                String defeito = rs.getString("defeito");
                String servico = rs.getString("servico");
                String tecnico = rs.getString("tecnico");
                double valor = rs.getDouble("valor");

                Order order = new Order(os, data_os, idcli, equipamento, defeito, servico, tecnico, valor);

                updateFields(order);
            }
        }
    }

    @FXML
    public void updateFields(Order order) {

        this.osNumber.setText(Integer.toString(order.getNumber()));
        this.osData.setText(order.getDate());
        this.idcliOs.setText(Integer.toString(order.getIdcli()));
        this.equipmentOs.setText(order.getDevice());
        this.defeitoOs.setText(order.getIssue());
        this.servicoOs.setText(order.getService());
        this.tecnicoOs.setText(order.getTechnician());
        this.valorOs.setText(String.valueOf(order.getPrice()));
    }

    @FXML
    public void updateChoices() {

        try {
            Connection conn = DatabaseUtil.getConnection();
            List<Client> clients = DatabaseUtil.checkClients(conn);
            this.users.getItems().clear();

            for (Client client : clients) {
               String clientName = client.getName();
               this.users.getItems().add(clientName);

            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
