package com.kazcabels.model;

import com.kazcables.util.Db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.swing.JOptionPane;


public class Branch {
    // Every branch has a name and an id, according to Criterion A
    private int branch_id;
    private String name;
    private Organization organization;// back reference
    private HashMap<String, Department> departments;
    private Stack<Employee> employees;
    ResultSet resultSet = null;
    Connection connection = null;
    Statement statement = null;
    
    public Branch(int branch_id, String name, Organization organization){
        this.branch_id=branch_id;
        this.name=name;
        this.organization = organization;
        this.departments = new HashMap<>();
        this.employees = new Stack<>();
        initDepartments();
        loadEmployees();
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
    

    public HashMap<String, Department> getDepartments() {
        return departments;
    }

    public void setDepartments(HashMap<String, Department> departments) {
        this.departments = departments;
    }

    public Stack<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Stack<Employee> employees) {
        this.employees = employees;
    }
    
    public void addDepartment(Department department){
        departments.put(department.getName(), department);
    }
    public String[] getDepartmentNames(){
        String[] departmentNames = new String[departments.size()+1];
        int i = 1;
        departmentNames[0] = "--Not Specified--";
        for (Map.Entry<String, Department> entry: this.departments.entrySet()){
            departmentNames[i++] = entry.getValue().getName();
        }
        return departmentNames;
    }
    
    public Department getSpecificDepartment(HierarchyLevel lvl){
        for (Map.Entry<String, Department> entry: this.departments.entrySet()){
            if (lvl==entry.getValue().getLevel()){
                return entry.getValue();
            }
        }
        return null;
    }
    public String[] getDepartmentPrefixes(){
        String[] departmentNames = new String[departments.size()+1];
        int i = 1;
        departmentNames[0] = "--Not Specified--";
        for (Map.Entry<String, Department> entry: this.departments.entrySet()){
            departmentNames[i++] = entry.getValue().getPrefix_id();
        }
        return departmentNames;
    }
    public void initDepartments(){
        try
        {
            String q ="select * from department";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            PreparedStatement prepStatement = connection.prepareStatement(q);
            
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()){
                String prefix_id = resultSet.getString("prefix_id");
                String departmentName = resultSet.getString("name");
                int level = resultSet.getInt("level");
                addDepartment(new Department(prefix_id, departmentName, level, this));
            }
            
            connection.close();
            System.gc();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Connection to department table was unsuccessful!!");
        }
    }
    public void initClients(){
        // Selects clients from clients table where their branch id matches this branch id
        try
        {
            String q = "select * from clients where branch_id = ?";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            PreparedStatement prepStatement = connection.prepareStatement(q);
            prepStatement.setInt(1, this.branch_id);
            resultSet = prepStatement.executeQuery();
            
            while (resultSet.next()){
                String client_id = resultSet.getString("client_id");
                String client_name = resultSet.getString("client_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                Client client = new Client(
                        client_id,
                        this.branch_id,
                        client_name,
                            phone,
                            email
                    );
                this.getOrganization().addClient(client);
            }
            
            connection.close();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Connection to client table was unsuccessful!!");
        }
    }
    private void loadEmployees(){
        while (!this.employees.isEmpty()){
            Employee emp = this.employees.pop();
            if (emp!=null){
                this.getOrganization().addEmployee(emp);
            }
        }
    }
    
}
