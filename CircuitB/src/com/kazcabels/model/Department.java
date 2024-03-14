    package com.kazcabels.model;

import com.kazcables.util.Db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JOptionPane;

public class Department {
    private String prefix_id;
    private String name;
    private HierarchyLevel level;
    private Branch branch; 
    
    private Employee manager;

    // ex. an executive supervises management department in every branch
    
    private Map<String, Role> roles; // keep track of available roles, descriptions, etc
    private Map<String, ArrayList<String>> rolesTakenByEmployee; // keep track of who is doing what in the company
    private final Stack<Employee> employees; //Stack of Employees that are initiated and to be uploaded to Organization
    ResultSet resultSet = null;
    Connection connection = null;
    PreparedStatement prepStatement = null;
    
    public Department(String prefix_id, String name, int level, Branch branch){
        this.prefix_id = prefix_id;
        this.name = name;
        this.level = HierarchyLevel.valueOf(level);
        this.branch = branch;
        this.roles = new HashMap<>();
        this.rolesTakenByEmployee = new HashMap<>();
        this.employees = new Stack<>();
        initRoles();
        initEmployees();
        loadEmployees();
    }
    public Branch getBranch() {return branch;}

    public void setBranch(Branch branch) {this.branch = branch;}
    
    public String getPrefix_id() {return prefix_id;}

    public void setPrefix_id(String prefix_id) {this.prefix_id = prefix_id;}

    public HierarchyLevel getLevel() {
        return level;
    }

    public void setLevel(HierarchyLevel level) {
        this.level = level;
    }
    
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
    
    public Map<String, Role> getRoles() {return roles;}

    public void setRoles(Map<String, Role> roles) {this.roles = roles;}

    public Map<String, ArrayList<String>> getRolesTakenByEmployee() {
        return rolesTakenByEmployee;
    }

    public void setRolesTakenByEmployee(Map<String, ArrayList<String>> rolesTakenByEmployee) {
        this.rolesTakenByEmployee = rolesTakenByEmployee;
    }
    
    
    public void addRole(Role role){
        roles.put(role.getName(), role);
        // e.g <key = 'Network Engineer', value = new Role('Network Engineer', description)>
    }
    
    public String[] getRoleNames(){
        String[] roleNames = new String[this.roles.size()+1];
        roleNames[0] = "--Not Specified";
        int i = 1;
        for (Map.Entry<String, Role> entry: this.roles.entrySet()){
            roleNames[i++]=entry.getKey();
        }
        return roleNames;
    }
    private void initRoles(){
        Role role;
        try
        {
            String q = "select name, description, manager_of from role where level = ?";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            prepStatement = connection.prepareStatement(q);
            prepStatement.setInt(1, this.level.getLevel());
            resultSet = prepStatement.executeQuery();
            
            while (resultSet.next())
            {
                String roleName = resultSet.getString("name");
                String description = resultSet.getString("description");
                String manager_of = resultSet.getString("manager_of");
                
                if (manager_of==null){
                    role = new Role(roleName, description);
                }
                else{
                    role = new Role(roleName, description, manager_of);
                }
                if (!roles.containsKey(role.getName())){roles.put(role.getName(), role);}
                // Stores role object into HashMap using its name attribute as key
            }
            
            connection.close();
        }
        
        catch(SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Connection to role table was unsuccessful!!");
        }
    }
    private void loadEmployees(){
        while(!this.employees.isEmpty()){
            Employee emp = this.employees.pop();
            boolean added = this.branch.getOrganization().addEmployee(emp);
            if (added){
                addEmployeeToRole(emp);
            }
        }
    }
    public void addEmployeeToRole(Employee emp){
        String fullName = emp.getFullName().trim().toLowerCase();
        ArrayList<String> list = this.rolesTakenByEmployee.get(emp.getRole().getName());
        if (list==null){
            list = new ArrayList<>();
            list.add(fullName);
            rolesTakenByEmployee.put(emp.getRole().getName(), list);
        }
        list.add(fullName);
    }
    public void removeEmployeeFromRole(Employee emp){
        // Checks if the role exists within the map
        String fullName = emp.getFullName().trim().toLowerCase();
        String roleName = emp.getRole().getName();
        
        if (rolesTakenByEmployee.containsKey(roleName)){
            // List of Employees associated with that role is retrieved
            List<String> list = rolesTakenByEmployee.get(roleName);
            
            boolean removed = list.remove(fullName);
            
            // However we don't want List of employees associated with that role to be empty, so we have to delete that too
            if (removed && list.isEmpty()){
                rolesTakenByEmployee.remove(roleName); 
            }
        }
    }
    private void initEmployees(){
        // Selects employees from employee table where their foreign key department_id which is their prefix, references primary key of department table
        // e.g SELECT * employee FROM WHERE department_id = 'EXE', this Department object's department prefix
        try
        {
            String q = "select * from employee where department_id = ? and branch_id = ?";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            prepStatement = connection.prepareStatement(q);
            prepStatement.setString(1, this.prefix_id);
            prepStatement.setInt(2, this.branch.getBranch_id());
            resultSet = prepStatement.executeQuery();
            
            while (resultSet.next())
            {
                String emp_id = resultSet.getString("emp_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                double salary = resultSet.getDouble("salary");
                String roleName = resultSet.getString("role");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String gender = resultSet.getString("sex");
                String birthDate = "0001-01-01";
                java.sql.Date bDate = resultSet.getDate("birthDate");
                // used code for sql date conversion to string https://www.baeldung.com/java-convert-localdate-sql-date
                if (bDate !=null)
                {
                    LocalDate lc = bDate.toLocalDate(); 
                    DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    birthDate = lc.format(fm);
                }
                int branch_id = resultSet.getInt("branch_id");
                String super_id = resultSet.getString("super_id");
                Employee emp = new Employee(emp_id,first_name,last_name,gender,birthDate, roleName,salary,phone,email,branch_id, this);
                if (super_id!=null){emp.setSupervisor_id(super_id);}
                if (emp!=null){this.branch.getEmployees().push(emp);}
            }
            connection.close();
        }
        catch(SQLException x)
        {
            System.out.println("ERROR in initEmployee() in department class");
            JOptionPane.showMessageDialog(null,x);
        }
    }
}  

