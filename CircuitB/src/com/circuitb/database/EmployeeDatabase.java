/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circuitb.database;


import com.circuitb.model.Employee;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Comparator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDatabase {
    
    private static List<Employee> employees = new ArrayList<>();

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static void setEmployees(List<Employee> employees) {
        EmployeeDatabase.employees = employees;
    }
    public void saveAsCSV(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("emp_id,first_name,last_name,role,branch_id,super_id\n");
            for (Employee emp : employees) {
                writer.write(String.format("%d,%s,%s,%s,%d,%s\n",
                    emp.getEmpId(), emp.getFirstName(), emp.getLastName(), 
                    emp.getRole(), emp.getBranchId(), emp.getSuperId()));
            }
        }
    }

    public void saveAsSQL(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Employee emp : employees) {
                String sql = String.format(
                    "INSERT INTO employees (emp_id, first_name, last_name, role, branch_id, super_id) VALUES (%d, '%s', '%s', '%s', %d, %s);\n",
                    emp.getEmpId(), emp.getFirstName(), emp.getLastName(), 
                    emp.getRole(), emp.getBranchId(), emp.getSuperId());
                writer.write(sql);
                
            }
        }
    }
    public static void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public static boolean deleteEmployee(int empId) {
        return employees.removeIf(employee -> employee.getEmpId() == empId);
    }

    public static List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee getEmployeeById(int empId) {
        return employees.stream()
                .filter(employee -> employee.getEmpId() == empId)
                .findFirst()
                .orElse(null);
    }

    public static void sortEmployeesByName() {
        employees.sort(Comparator.comparing(Employee::getFirstName)
                                  .thenComparing(Employee::getLastName));
    }

    public static void sortEmployeesById() {
        employees.sort(Comparator.comparingInt(Employee::getEmpId));
    }

    public static void sortEmployeesByRole() {
        employees.sort(Comparator.comparing(Employee::getRole));
    }
    
    
}
