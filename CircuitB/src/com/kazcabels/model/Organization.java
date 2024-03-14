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
import javax.swing.JOptionPane;


public final class Organization {
    
    private Map <Integer, Branch> branches;
    
    private HashMap <String, Employee> allEmployees;
    private HashMap <String, Client> allClients;
    private HashMap <String, BranchSupplier> allSuppliers;
    
    private final HashMap <String, String> employeeNameToID;
    private final HashMap <String, String> clientIDToName;
    
    private Employee commonSupervisor;
    
    ResultSet resultSet = null;
    PreparedStatement prepStatement = null;
    Connection connection = null;
    Statement statement = null;
    
    public Organization()
    {
        this.allEmployees = new HashMap<>();
        this.allClients = new HashMap<>();
        this.allSuppliers = new HashMap<>();
        this.employeeNameToID = new HashMap<>();
        this.clientIDToName = new HashMap<>();
        this.branches = new HashMap<>();
        
        initBranches();
    }
    
    public Map<Integer, Branch> getBranches() {return branches;}

    public void setBranches(Map<Integer, Branch> branches) {this.branches = branches;}
    
    public HashMap<String, Employee> getAllEmployees() {return allEmployees;}

    public void setAllEmployees(HashMap<String, Employee> allEmployees) {this.allEmployees = allEmployees;}

    public HashMap<String, String> getEmployeeNameToID() {
        return employeeNameToID;
    }
    
    public HashMap<String, Client> getAllClients() {
        return allClients;
    }

    public void setAllClients(HashMap<String, Client> allClients) {
        this.allClients = allClients;
    }

    public HashMap<String, BranchSupplier> getAllSuppliers() {
        return allSuppliers;
    }

    public void setAllSuppliers(HashMap<String, BranchSupplier> allSuppliers) {
        this.allSuppliers = allSuppliers;
    }
    
    public boolean addEmployee(Employee emp) throws NullPointerException
    {
        /*
        1. check for employee, department and role existence of an employee
        2. remove role from roles HashMap in department if Employee role is a Manager or an Executive
        3. check if employee has a supervisor by attempting to extract manager from Department object, to set supervisor id
        4. put Employee fullname as key and id as value to the HashMap of employeeNameToID
        5. put Employee to global HashMap allEmployees in the Organization class.
        */
        
        if (emp==null || emp.getDepartment()==null || emp.getRole() == null) {return false;}
        // 1. invalid employee, either employee is non-existent, doesn't belong to any department or does not have a valid role.
        
        String fullName = emp.getFullName().trim().toLowerCase();
        
        if (fullName != null)
        {
            if (employeeNameToID.containsKey(fullName)) {
                JOptionPane.showMessageDialog(null, emp.getFullName()+" already exists within this organization");
                return false;
            }
        }
        
        Role role = emp.getRole();
        // check if Employee is COO to set commonSupervisor as manager within all Management Departments
        
        Department dep = emp.getDepartment();        
        dep.getRoles().remove(role.getName());
        // removes respective role from the department if Executive or Manager
        
        Employee manager = emp.getDepartment().getManager();
        if (manager!=null){emp.setSupervisor_id(manager.getEmp_Id());}
        // extracts manager from Department so supervisor_id can be set if exists
        
        if (role.getName().equals("COO (Chief Operations Officer)")){commonSupervisor = emp;}
        
        if (dep.getLevel()==HierarchyLevel.MANAGER){emp.setSupervisor_id(commonSupervisor.getEmp_Id());}
        // sets commonSupervisor attribute of Organization. A Person who is supervisor to employees regardless of the branch
        
        // Employee Insertion 
        employeeNameToID.put(fullName, emp.getEmp_Id());
        allEmployees.put(emp.getEmp_Id(), emp);
        
        // final return value
        return true;
    }
    
    public void addClient(Client c)
    {
        String name = c.getName().trim().toLowerCase();
        
        if (name != null)
        {
            allClients.putIfAbsent(name, c);// constant time
            
            clientIDToName.putIfAbsent(c.getClient_id(), c.getName());
        }
        
    }
    
    public boolean removeClient(String name)
    {
        Client c = allClients.remove(name);// constant time
        
        return c != null;
    }
    
    public Client searchClientByName(String name)
    {
        Client c = allClients.get(name);
        
        if (c != null) {return c;}
        
        return null;
    }
    
    public Client searchClientByID(String id)
    {
        String name = clientIDToName.get(id); //constant time
        return searchClientByName(name);
    }
    
    public void addSupplier(BranchSupplier s)
    {
        String name = s.getName();
        allSuppliers.putIfAbsent(name, s);// constant time
    }
    
    public boolean removeSupplier(String name)
    {
        BranchSupplier s = allSuppliers.remove(name);// constant time
        return s != null;
    }
    
    public BranchSupplier searchSupplier(String name)
    {
        BranchSupplier s = allSuppliers.get(name); // constaint time
        
        if (s != null) {return s;}
        
        return null;
    }
    
    public boolean removeEmployee(String fullName)
    {
        // Global HashMap lookup by full name with constant time
        /*
        
        1. remove from global HashMap allEmployees<String, Employee> ~ O(1)
        2. remove from global HashMap employeeNameToID<String, String> ~ (1)
        3. remove from department's rolesTakenByEmployee HashMap<String, List<String>>, remove from List. ~ O(N)
        
        */
        Employee emp = searchEmployeeByFullName(fullName);
        
        if (emp!=null)
        {
            allEmployees.remove(emp.getEmp_Id());
            
            employeeNameToID.remove(emp.getFullName());
            
            JOptionPane.showMessageDialog(null, "Employee "+emp.getEmp_Id() + " has been deleted");
            
            Department dep = emp.getDepartment();
            
            if (dep != null) {dep.removeEmployeeFromRole(emp);}
            
            return true;
        }
        return false;
    }
    
    public Employee searchEmployeeByFullName(String fullName)
    {
        /*
        1. Extract the employee id associated with the fullName
            Since it is addEmployees dialog form checks for uniqueness of the names as well, 
                no overriding of emp_id happens in employeeNameToID
        2. Perform the same searchByFullName(String = emp_id) method down below
        */
        
        String id = employeeNameToID.get(fullName);
        
        return searchEmployeeByID(id);
    }
    
    public Employee searchEmployeeByID(String emp_id)
    {
        Employee emp = allEmployees.get(emp_id);
        
        if (emp!=null){return emp;}
        
        return null;
    }
    
    public void initSuppliers()
    {
        // Selects clients from clients table where their branch id matches this branch id
        try
        {
            String q = "select * from supplier";
            connection = DriverManager.getConnection(Db.URL, Db.USER, Db.PASSWORD);
            prepStatement = connection.prepareStatement(q);
            resultSet = prepStatement.executeQuery();
            
            while (resultSet.next())
            {
                String supplier_name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                
                BranchSupplier supplier = new BranchSupplier(supplier_name,phone,email);
                
                for (int branch_id: this.getBranches().keySet())
                {
                    supplier.putSupplies(branch_id);
                }            
                addSupplier(supplier);
            }
            connection.close();
        }
        catch (SQLException e) {JOptionPane.showMessageDialog(null, "Connection to supplier table was unsuccessful!!");}
    }
    
    public void initBranches()
    {
        try
        {
            connection = DriverManager.getConnection(Db.URL,Db.USER, Db.PASSWORD);
            
            statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from branch");
            
            while (resultSet.next())
            {
                int id = resultSet.getInt("branch_id");
                
                String name = resultSet.getString("branch_name");
                
                Branch branch = new Branch(id, name, this);
                
                branches.put(id, branch);
            }
            
            connection.close();
        }
        catch (SQLException e) {JOptionPane.showMessageDialog(null, "could not initialize branches!!");}
    }
    
    public String[] getBranchNames()
    {
        // used in addEmployees and addClient form in JComboBox of branche names (jcb_branch)
        
        String[] branchNames = new String[branches.size()+1];
        
        int i = 1;
        
        branchNames[0] = "--Not Specified--";
        
        for (Branch branch: this.getBranches().values())
        {
            branchNames[i++] = branch.getName();
        }
        
        return branchNames;
    }
    
    public Branch getSpecificBranch(String branchName)
    {
        // used in addEmployees dialog form
        for (Branch branch: this.getBranches().values())
        {
            if (branch.getName().equals(branchName))
            {
                return branch;
            }
        }
        return null;
    }
}
