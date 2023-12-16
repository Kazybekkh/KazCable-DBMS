/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.circuitb.ui;

/**
 *
 * @author kazybekkhairulla
 */
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeApp extends javax.swing.JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> sortOptions;
    private JButton saveButton;
    private JButton saveExitButton;

    public EmployeeApp() {
        createMenuBar();
        createMainContent();
        setFrameProperties();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }

    private void createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Table for Employee Records
        tableModel = new DefaultTableModel(new Object[]{"emp_id", "first_name", "last_name", "role", "branch_id", "super_id"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Search Field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(new JButton("Search"), BorderLayout.EAST);

        // Sorting Options
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortOptions = new JComboBox<>(new String[]{"first/last name", "employee_id", "role"});
        sortPanel.add(new JLabel("Sort by"));
        sortPanel.add(sortOptions);

        // Save Buttons
        saveButton = new JButton("save");
        saveExitButton = new JButton("save/exit");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(saveExitButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(sortPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private void setFrameProperties() {
        setTitle("Employee Records");
        setSize(800, 600);
        setBackground(Color.LIGHT_GRAY);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeApp app = new EmployeeApp();
            app.setVisible(true);
        });
    }
    
}
