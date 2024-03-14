package com.kazcabels.model;

import com.kazcables.util.Db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class BranchSupplier extends Person
{
    // <key = branch_id, List of Supply objects>
    private Map <Integer, List <Supply>> suppliesToBranches;

    ResultSet resultSet = null;
    Connection connection = null;
    PreparedStatement prepStatement = null;
    
    public BranchSupplier (String name, String phone_number, String email)
    {
        super(name, phone_number, email);
        this.suppliesToBranches = new HashMap<>();
    }

    public Map<Integer, List<Supply>> getSuppliesToBranches()
    {
        return suppliesToBranches;
    }

    public void setSuppliesToBranches(Map<Integer, List<Supply>> suppliesToBranches)
    {
        this.suppliesToBranches = suppliesToBranches;
    }
    
    public void addSupplyToBranch(int id, Supply supply)
    {
        List<Supply> supplies = suppliesToBranches.getOrDefault(id, new ArrayList<>());
        supplies.add(supply);
        // Gets the list of Supply objects and adds Supply object to the next pointer
        // Last it should update the list in the HashMap
        
        suppliesToBranches.put(id, supplies);
    }
    
    public void removeSupplyFromBranch(int id, Supply supply)
    {
        List<Supply> supplies = suppliesToBranches.get(id);
        if (supplies != null)
        {
            supplies.remove(supply);
            // Checks whether the list is Empty, if Supplier supplies nothing to specific branch, that branch is removed.
            
            if (supplies.isEmpty()) {suppliesToBranches.remove(id);}
        }
        
    }
    
    public void putSupplies(int branch_id)
    {
        // Selects products from supply table where their supplier_name is ? input parameter
        try
        {
            String q = "select * from supply where supplier_name = ?";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            prepStatement = connection.prepareStatement(q);
            prepStatement.setString(1, this.name);
            resultSet = prepStatement.executeQuery();
            
            while (resultSet.next())
            {
                String supplier_name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String supply_type = resultSet.getString("supply_type");
                
                Supply supply = new Supply(supplier_name, price, quantity, supply_type);
                
                addSupplyToBranch(branch_id, supply);
            }
            connection.close();
        }
        catch (SQLException e) {JOptionPane.showMessageDialog(null, "Connection to supply table was unsuccessful!!");}
    }
    
    public String[] getRow()
    {
        return new String[] {
            this.getName(),           // Assuming getName() is accessible via the Person parent class
            this.getPhone_number(),    // Assuming getPhoneNumber() method exists or directly access `phone_number`
            this.getEmail()           // Direct access to email, assuming getEmail() method exists
        };
    }
}

