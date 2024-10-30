package com.kazcables.model;

import static com.kazcables.util.Db.getConnection;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import static javax.swing.JOptionPane.showMessageDialog;

public final class Organization {

    private HashMap<Integer, Branch> branches;
    private HashMap<String, Client> allClients;
    private HashMap<String, Supplier> allSuppliers;

    public Organization() {
        this.allClients = new HashMap<>();
        this.allSuppliers = new HashMap<>();
        this.branches = new HashMap<>();
    }

    public HashMap<Integer, Branch> getBranches() {
        return branches;
    }

    public void setBranches(HashMap<Integer, Branch> branches) {
        this.branches = branches;
    }

    public Employee searchEmployeeByName(int branch_id, String name) {
        Branch b = this.getBranches().get(branch_id);
        if (b != null) {
            for (Department dep : b.getDepartments().values()) {
                Employee emp = dep.searchEmployeeByFullName(name);
                if (emp != null) {
                    return emp;
                }
            }
        }
        return null;
    }

    public Employee searchEmployeeByID(int branch_id, HierarchyLevel level, String id) {
        if (level == null){return null;}
        
        Branch b = this.getBranches().get(branch_id);
        // assuming that branch_id is either 1, 2, 3, if no id, then id = 0, in which case (b==null), for loop
        if (b != null && level != null) {
            Department dep = b.getDepartments().get(level);
            if (dep != null) {
                Employee emp = dep.searchEmployeeByID(id);
                if (emp != null) {
                    return emp;
                }
            }
        } else if (b == null && level != null) {
            for (Branch b2 : this.getBranches().values()) {
                Department dep = b2.getDepartments().get(level);
                if (dep != null) {
                    Employee emp = dep.searchEmployeeByID(id);
                    if (emp != null) {
                        return emp;
                    }
                }
            }
        }
        return null;
    }

    public HashMap<String, Client> getAllClients() {
        return allClients;
    }

    public void setAllClients(HashMap<String, Client> allClients) {
        this.allClients = allClients;
    }

    public HashMap<String, Supplier> getAllSuppliers() {
        return allSuppliers;
    }

    public void setAllSuppliers(HashMap<String, Supplier> allSuppliers) {
        this.allSuppliers = allSuppliers;
    }

    public boolean addSupplier(Supplier s) {
        if (s == null) {
            return false;
        }
        String name = s.getName().trim().toLowerCase();
        this.getAllSuppliers().put(name, s);
        return true;
    }

    public boolean removeSupplier(Supplier s) {
        String name = s.getName().trim().toLowerCase();
        return this.getAllSuppliers().remove(name, s);// constant time
    }

    public boolean addClient(Client c) {
        if (c == null) {
            return false;
        }
        if (isClientNamePresent(c.getName())) {
            showMessageDialog(null, "Client with name" + c.getName() + " already exists within the database");
            return false;
        }
        this.getAllClients().put(c.getClient_id(), c);// constant time
        return true;
    }

    public boolean removeClient(Client c) {
        return this.getAllClients().remove(c.getClient_id(), c);// constant time
    }

    public Client searchClientByName(String name) {
        for (Client c : this.getAllClients().values()) {
            String client_name = c.getName();
            if (client_name.equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public boolean isClientNamePresent(String name) {
        for (Client c : this.getAllClients().values()) {
            String client_name = c.getName().trim().toLowerCase();
            if (client_name.equals(name.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public Client searchClientByID(String id) {
        Client c = this.getAllClients().get(id);
        if (c != null) {
            return c;
        }
        return null;
    }

    public Supplier searchSupplier(String name) {
        Supplier s = this.getAllSuppliers().get(name.trim().toLowerCase()); // constaint time        
        if (s != null) {
            return s;
        }
        return null;
    }

    public void initSuppliers() {
        String query = "SELECT * FROM supplier";
        // Use try-with-resources for managing database resources efficiently
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String supplier_name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                Supplier supplier = new Supplier(supplier_name, phone, email);
                addSupplier(supplier); // Assumes there's an addSupplier method to handle this
            }
            
        } catch (SQLException e) {
            showMessageDialog(null, "Connection to supplier table was unsuccessful: " + e.getMessage());
        }
    }

    public void initBranches() {
        String query = "SELECT * FROM branch ORDER BY branch_id ASC";
        // Use try-with-resources to manage database resources
        try (Connection connection = getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("branch_id");
                String name = resultSet.getString("branch_name");
                Branch branch = new Branch(id, name, this);
                if (branch != null) {
                    this.getBranches().put(id, branch);
                    out.println("Branch " + branch.getName() + " was added");
                }
            }
            connection.close();
        } catch (SQLException e) {
            showMessageDialog(null, "Connection to branch table was unsuccessful: " + e.getMessage());
        }
    }

    public String[] getBranchNames() {
        // used in addEmployees and addClient form in JComboBox of branche names (jcb_branch)

        String[] branchNames = new String[this.getBranches().size() + 1];

        int i = 1;

        branchNames[0] = "--Not Specified--";

        for (Branch branch : this.getBranches().values()) {
            if (branch != null) {
                branchNames[i++] = branch.getName();
            }
        }

        return branchNames;
    }
}
