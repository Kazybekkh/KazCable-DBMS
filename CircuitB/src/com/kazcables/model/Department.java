package com.kazcables.model;

import static com.kazcables.model.HierarchyLevel.valueOf;
import static com.kazcables.util.Db.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import static javax.swing.JOptionPane.showMessageDialog;

public class Department {

    private String prefix_id;
    private String name;
    private HierarchyLevel level;
    private Branch branch;
    private Employee manager;
    private final HashMap<String, Employee> employees;
    // ex. an executive supervises management department in every branch
    private HashMap<String, Role> roles; // keep track of available roles, descriptions, etc

    public Department(String prefix_id, String name, int level, Branch branch) {
        this.prefix_id = prefix_id;
        this.name = name;
        this.level = valueOf(level);
        this.branch = branch;
        this.roles = new HashMap<>();
        this.employees = new HashMap<>();
        initRoles(); // initializes all the roles from roles table
    }
    public HashMap<String, Employee> getEmployees() {
        return employees;
    }
    public Employee searchEmployeeByID(String id){
        Employee emp = this.getEmployees().get(id);
        if (emp!=null){return emp;}
        return null;
    }
    public Employee searchEmployeeByFullName(String fullName){
        fullName = fullName.trim().toLowerCase();
        for (Employee emp: this.getEmployees().values()){
            if (fullName.equals(emp.getFullName().trim().toLowerCase())){
                return emp;
            }
        }
        return null;
    }
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getPrefix_id() {
        return prefix_id;
    }

    public void setPrefix_id(String prefix_id) {
        this.prefix_id = prefix_id;
    }

    public HierarchyLevel getLevel() {
        return level;
    }

    public void setLevel(HierarchyLevel level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee mgr) {
        if (mgr!=null){
            this.manager = mgr;
            System.out.println(mgr.getFullName() + " has been set as MANAGER of " + this.getName());
            for (Employee emp: this.getEmployees().values()){
                if (emp.getSupervisor_id()==null){emp.setSupervisor_id(mgr.getEmp_Id());}
            }
        }
        else{
            System.out.println( " null MANAGER INPUT");
        }
    }

    public HashMap<String, Role> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<String, Role> roles) {
        this.roles = roles;
    }

    public String[] getRow() {
        return new String[]{this.getPrefix_id(), this.getName(), this.getLevel().getLevel() + ""};
    }
    public boolean addEmployee(Employee emp) {
        /*
        1. invalid employee if employee is null, stop the method
        2. check if Employee is supervisor of a department
        3. insert employee into hashmap
        */
        
        if (emp == null) {
            return false;
        }
        else{
            emp.getDepartment().getEmployees().put(emp.getEmp_Id(), emp);
            if (emp.isSupervisor()){this.getBranch().setDepartmentManager(emp);}
            return true;
        }
        //out.println("successfully inserted " + emp.getFullName() + " from " + emp.getDepartment().getName());
    }
    public void addRole(Role role) {
        this.getRoles().put(role.getName(), role);
        // e.g <key = 'Network Engineer', value = new Role('Network Engineer', description)>
    }

    public void removeRole(Role role) {
        this.getRoles().remove(role.getName());
    }

    public String[] getRoleNames() {
        String[] roleNames = new String[this.getRoles().size() + 1];
        roleNames[0] = "--Not Specified";
        int i = 1;
        for (Map.Entry<String, Role> entry : this.getRoles().entrySet()) {
            roleNames[i++] = entry.getKey();
        }
        return roleNames;
    }

    public final boolean initRoles() {
        String query = "SELECT * FROM role WHERE level = ?";

        // Initialize the role variable outside the try block
        Role role = null;

        try (Connection connection = getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setInt(1, this.getLevel().getLevel());

            try (ResultSet resultSet = prepStatement.executeQuery()) {
                while (resultSet.next()) {
                    String roleName = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int manages_level = resultSet.getInt("manages_level");
                    if (resultSet.wasNull()) {
                        manages_level = 0;
                    }
                    role = new Role(roleName, description, manages_level);
                    roles.putIfAbsent(roleName, role);
                }
                connection.close();
            }
            return true; // Successfully initialized roles message
        } catch (SQLException e) {
            showMessageDialog(null, "Connection to role table was unsuccessful: " + e.getMessage());
            return false; // Failed to initialize roles message dialog
        }
    }
    public void initEmployees(){
        String q = "select * from employee where branch_id = ? and department_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(q)) {

            prepStatement.setInt(1, this.getBranch().getBranch_id());
            prepStatement.setInt(2, this.getLevel().getLevel());
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                while (resultSet.next()) {
                    String emp_id = resultSet.getString("emp_id");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    double salary = resultSet.getDouble("salary");
                    String roleName = resultSet.getString("role");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String gender = resultSet.getString("sex");
                    java.sql.Date bDate = resultSet.getDate("birthDate");
                    String super_id = resultSet.getString("super_id");
                    Employee emp = new Employee(emp_id,first_name,last_name,gender,bDate, roleName,salary,phone,email,this.getBranch().getBranch_id(), super_id, this);
                    if (emp==null){System.out.println("Employee is null" + first_name + " "+ last_name);}
                    else{this.addEmployee(emp);}
                }
                connection.close();
            }
        }
        catch (SQLException e) {
            showMessageDialog(null, "Connection to employee table was unsuccessful: " + e.getMessage());
        }
    }
}
