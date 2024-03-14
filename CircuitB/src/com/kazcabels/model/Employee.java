package com.kazcabels.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

public class Employee extends Person {
    private Department department;
    private String emp_id;
    private int branch_id;
    
    private String supervisor_id;
    private String lastName;
    private String birthDate;
    private Role role;
    private double salary;
    
    public Employee(String emp_id, String first_name, String lastName,String gender, String birthDate, String roleName, double salary, String phone_number, String email, int branch_id, Department department) {
        // Basic properties of a Person 
        super(first_name, phone_number, email, gender);
        // Other properties of an Employee
        this.emp_id = emp_id;
        this.lastName = lastName; // e.g 'Bertram'
        this.birthDate = birthDate; // e.g '11/08/2003'
        this.department = department;
        this.role = this.department.getRoles().get(roleName); // e.g new Role("Network Engineer", 'description')
        this.salary = salary; // e.g 90000.0
        this.branch_id = branch_id; // e.g 1
        
    }
    
    public Employee(String first_name, String lastName,String gender, String birthDate, String roleName, double salary, String phone_number, String email, int branch_id, Department department) {
        super(first_name, phone_number, email, gender);
        // Overloaded Constructor
        this.department = department;
        this.emp_id = generateEmployee_ID(department.getPrefix_id());
        this.lastName = lastName; // e.g 'Bertram'
        this.birthDate = birthDate; // e.g '11/08/2003'
        this.role = this.department.getRoles().get(roleName); // e.g new Role("Network Engineer", 'description')
        this.salary = salary; // e.g 90000.0
        this.branch_id = branch_id; // e.g 1
        
    }
    public String getEmp_Id() {
        return emp_id;
    }

    public void setEmp_Id(String id) {
        this.emp_id = id;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id() {
        Employee emp =this.department.getManager();
        if (emp!=null & emp!=this){
            
            this.supervisor_id = emp.getEmp_Id(); //Supervisor of a CEO is null
        }
        else if (emp!=null && emp==this){
            this.supervisor_id="NULL";
        }
        
    }
    public void setSupervisor_id(String super_id){
        this.supervisor_id = super_id;
    }

    public int getBranch_id() {
        return branch_id;
    }
    
    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    public java.sql.Date getFormattedBirthDate() {
        LocalDate localDate = LocalDate.parse(this.birthDate);
        Date sqlDate =Date.valueOf(localDate);
        return sqlDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getFullName(){
        return name +" "+ lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }


    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public String[] getRow(){
        return new String[]{
            this.emp_id,
            this.department.getName(),
            this.name,
            this.lastName,
            ""+this.salary,
            this.role.getName(),
            this.getGender(),
            this.getEmail(),
            this.phone_number,
            this.birthDate,
            Integer.toString(this.branch_id),
            this.supervisor_id
        };
    }
    
    public static boolean is_leap_year(int year){
        // returns true if the Employee's birth year is a leap year, false if not for Birthday selection purposes
        return (year % 4 == 0 && year % 100 !=0) || (year % 400 == 0);
    }
    
    public static int getDaysInMonth(int month, boolean isLeapYear) {
        // returns the number of days in a month, based on a given month number and whether year is leap or not
        switch (month) {
            case 2:
                return isLeapYear ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }
    
    
    
    public static String generateEmployee_ID(String prefix){
        UUID uuid = UUID.randomUUID();
        String fullUUID = uuid.toString();
        String finalID = fullUUID.split("-")[0];
        return prefix + "-"+finalID;
    }
}
