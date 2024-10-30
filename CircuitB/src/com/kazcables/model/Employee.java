package com.kazcables.model;

import java.sql.Date;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofPattern;
import java.util.UUID;
import static java.util.UUID.randomUUID;

public class Employee extends Person {

    private Department department;
    private String emp_id;
    private String super_id;
    private String lastName;
    private String birthDate;
    private int branch_id;
    private double salary;
    private Role role;

    public Employee(String emp_id,
            String first_name,
            String lastName,
            String gender,
            java.sql.Date birthDate,
            String roleName,
            double salary,
            String phone_number,
            String email,
            int branch_id,
            String super_id,
            Department department) {
        super(first_name, phone_number, email, gender);
        this.emp_id = emp_id;
        this.super_id = super_id;
        this.lastName = lastName;
        this.birthDate = convertSqlDateToString(birthDate);
        this.department = department;
        this.role = this.department.getRoles().get(roleName);
        this.salary = salary;
        this.branch_id = branch_id;
    }

    public Employee(String first_name,
            String lastName,
            String gender,
            String birthDate,
            String roleName,
            double salary,
            String phone_number,
            String email,
            int branch_id,
            Department department) {
        super(first_name, phone_number, email, gender);
        // Overloaded Constructor
        this.department = department;
        this.emp_id = generateEmployee_ID();
        if (this.getDepartment().getManager()!=null){this.super_id = this.getDepartment().getManager().getEmp_Id();}
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
        return super_id;
    }

    public void setSupervisor_id(String super_id) {
        if (this.getEmp_Id().equals(super_id)) {
            this.super_id = "NULL";
        } else {
            this.super_id = super_id;
            System.out.println("Set supervisor of "+ this.getFullName() + " as " + super_id);
        }
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

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return this.getName() + " " + this.getLastName();
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

    public final Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public java.sql.Date getFormattedBirthDate(String b) {
        if (b == null) {
            return valueOf(now());
        }
        LocalDate localDate = parse(b);
        Date sqlDate = valueOf(localDate);
        return sqlDate;
    }

    public static String convertSqlDateToString(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        LocalDate localDate = sqlDate.toLocalDate();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public static boolean is_leap_year(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        // returns true if the Employee's birth year is a leap year, false if not for Birthday selection purposes
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

    public String[] getRow() {
        return new String[]{
            this.getEmp_Id(),
            this.getDepartment().getName(),
            this.getName(),
            this.getLastName(),
            "" + this.getSalary(),
            this.getRole().getName(),
            this.getGender(),
            this.getEmail(),
            this.getPhone_number(),
            this.getBirthDate(),
            Integer.toString(this.getBranch_id()),
            this.getSupervisor_id()
        };
    }

    public final String generateEmployee_ID() {
        Department dep = this.getDepartment();
        String prefix = "XXX";
        if (dep!=null){prefix = dep.getPrefix_id();}
        UUID uuid = randomUUID();
        String fullUUID = uuid.toString();
        String finalID = fullUUID.split("-")[0];
        return prefix + "-" + finalID;
    }

    public boolean isSupervisor() {
        return this.getRole().getManages_level() > 0;
    }
}
