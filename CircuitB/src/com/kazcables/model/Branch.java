package com.kazcables.model;

import static com.kazcables.util.Db.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static javax.swing.JOptionPane.showMessageDialog;

public final class Branch {

    // Every branch has a name and an id, according to Criterion A
    private int branch_id;
    private String name;
    private Organization organization;// back reference
    private HashMap<HierarchyLevel, Department> departments;

    public Branch(int branch_id, String name, Organization organization) {
        this.branch_id = branch_id;
        this.name = name;
        this.organization = organization;
        this.departments = new HashMap<>();
        boolean initialized = initDepartments();
        if (initialized) {
            for (Department d : this.getDepartments().values()) {
                if (d != null) {
                    d.initEmployees();
                }
            }
        }
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public HashMap<HierarchyLevel, Department> getDepartments() {
        return departments;
    }

    public void setDepartments(HashMap<HierarchyLevel, Department> departments) {
        this.departments = departments;
    }

    public void addDepartment(Department department) {
        departments.put(department.getLevel(), department);
    }
    public String[] getDepartmentNames() {
        String[] departmentNames = new String[this.getDepartments().size() + 1];
        int i = 1;
        departmentNames[0] = "--Not Specified--";
        for (Department d : this.getDepartments().values()) {
            departmentNames[i++] = d.getLevel().toString();
        }
        return departmentNames;
    }

    public String[] getDepartmentPrefixes() {
        String[] departmentNames = new String[this.getDepartments().size() + 1];
        int i = 1;
        departmentNames[0] = "--Not Specified--";
        for (Map.Entry<HierarchyLevel, Department> entry : this.getDepartments().entrySet()) {
            departmentNames[i++] = entry.getValue().getPrefix_id();
        }
        return departmentNames;
    }

    public boolean initDepartments() {
        String query = "SELECT * FROM department ORDER BY level DESC";
        if (this.getBranch_id() >= 2) {
            query = "SELECT * FROM department WHERE level <> 1 ORDER BY level DESC";
        }
        // Use try-with-resources to ensure automatic resource management
        try (Connection connection = getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query); ResultSet resultSet = prepStatement.executeQuery()) {

            while (resultSet.next()) {
                String prefix_id = resultSet.getString("prefix_id");
                String departmentName = resultSet.getString("name");
                System.out.println("Extracted department name " + departmentName);
                int level = resultSet.getInt("level");
                Department dep = new Department(prefix_id, departmentName, level, this);
                if (dep != null) {
                    addDepartment(dep);
                } else {
                    System.out.println("null Department" + departmentName + level);
                }
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            showMessageDialog(null, "Could not initialize departments: \n SQL Error code: " + e.getMessage(), "Database error", 0);
        }
        return false;
    }

    public void initClients() {
        String query = "SELECT * FROM clients WHERE branch_id = ?";
        try (Connection connection = getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setInt(1, this.getBranch_id());

            try (ResultSet resultSet = prepStatement.executeQuery()) {
                while (resultSet.next()) {
                    String client_id = resultSet.getString("client_id");
                    String client_name = resultSet.getString("client_name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    Client client = new Client(
                            client_id,
                            this.getBranch_id(),
                            client_name,
                            phone,
                            email
                    );
                    this.getOrganization().addClient(client);
                }
                connection.close();
            }
        } catch (SQLException e) {
            showMessageDialog(null, "Connection to client table was unsuccessful: " + e.getMessage(), "Database error", 0);
        }
    }

    public void setDepartmentManager(Employee emp) {
        if (emp == null || emp.getRole() == null) {
            return; // Early exit if employee or role is null
        }

        HierarchyLevel managesLevel = HierarchyLevel.valueOf(emp.getRole().getManages_level());

        if (managesLevel == null) {
            return;
        }
        if (managesLevel==HierarchyLevel.MANAGEMENT){
            for (Department dep: this.getDepartments().values()){
                dep.setManager(emp);
            }
            return;
        }
        // extracts enum for STAFF(one of ENGINEERING, MARKETING, SALES) or EXECUTIVE department
        System.out.println("trying to set manager for" + managesLevel + " " + emp.getFullName() + " for branch number "+ emp.getBranch_id());
        Department dep = this.getDepartments().get(managesLevel);
        if (dep == null) {
            System.out.println("Department is null, check your isSupervisor condition!!!");
            // If the department exists and doesn't have a manager, assign this employee as the manager
        }
        else{
            dep.setManager(emp);
        }
    }
}
