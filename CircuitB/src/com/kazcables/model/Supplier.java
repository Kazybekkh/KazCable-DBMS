package com.kazcables.model;

import com.kazcables.util.Db;
import static com.kazcables.util.Db.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class Supplier extends Person {

    // <key = branch_id, List of Supply objects>
    private Map<Integer, List<Supply>> suppliesToBranches;
    private final boolean isActive;
    public Supplier(String name, String phone_number, String email) {
        super(name, phone_number, email);
        this.suppliesToBranches = new HashMap<>();
        this.isActive = true;
        putSupplies();
    }

    public Map<Integer, List<Supply>> getSuppliesToBranches() {
        return suppliesToBranches;
    }

    public void setSuppliesToBranches(Map<Integer, List<Supply>> suppliesToBranches) {
        this.suppliesToBranches = suppliesToBranches;
    }

    public void addSupplyToBranch(int id, Supply supply) {
        List<Supply> supplies = suppliesToBranches.getOrDefault(id, new ArrayList<>());
        supplies.add(supply);
        // Gets the list of Supply objects and adds Supply object to the next pointer
        // Last it should update the list in the HashMap

        suppliesToBranches.put(id, supplies);
    }

    public void removeSupplyFromBranch(int id, Supply supply) {
        List<Supply> supplies = suppliesToBranches.get(id);
        if (supplies != null) {
            supplies.remove(supply);
            // Checks whether the list is Empty, if Supplier supplies nothing to specific branch, that branch is removed.

            if (supplies.isEmpty()) {
                suppliesToBranches.remove(id);
            }
        }

    }

    public final void putSupplies() {
        String query = "SELECT * FROM supply WHERE supplier_name = ?";
        // Use try-with-resources to manage database resources
        try (Connection connection = getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1, this.getName());

            try (ResultSet resultSet = prepStatement.executeQuery()) {
                while (resultSet.next()) {
                    String supplier_name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int branch_id = resultSet.getInt("branch_id");
                    int quantity = resultSet.getInt("quantity");
                    String supply_type = resultSet.getString("supply_type");

                    Supply supply = new Supply(supplier_name, price, quantity, supply_type);
                    addSupplyToBranch(branch_id, supply); // Assumes this method is defined elsewhere
                }
            }
        } catch (SQLException e) {
            showMessageDialog(null, "Connection to supply table was unsuccessful: " + e.getMessage());
        }
    }

    public String[] getRow() {
        return new String[]{
            this.getName(), // Assuming getName() is accessible via the Person parent class
            this.getPhone_number(), // Assuming getPhoneNumber() method exists or directly access `phone_number`
            this.getEmail() // Direct access to email, assuming getEmail() method exists
        };
    }

    public String getFormattedSuppliesInfo() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, List<Supply>> entry : suppliesToBranches.entrySet()) {
            int branchId = entry.getKey();
            List<Supply> suppliesList = entry.getValue();

            sb.append("Branch ID: ").append(branchId).append("\n");
            for (Supply supply : suppliesList) {
                sb.append("\tSupply Name: ").append(supply.getName());
                sb.append(", Price: ").append(supply.getPrice());
                sb.append(", Quantity: ").append(supply.getQuantity_supplied());
                sb.append(", Type: ").append(supply.getSupply_type()).append("\n");
            }
            sb.append("\n"); // Extra newline for separation between branches
        }
        return sb.toString();
    }

}
