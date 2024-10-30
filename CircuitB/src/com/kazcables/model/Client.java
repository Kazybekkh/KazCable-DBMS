package com.kazcables.model;

import java.util.UUID;
import static java.util.UUID.randomUUID;

public class Client extends Person
{
    private String client_id;
    private int branch_id;
    private String assigned_to;
    public Client(String client_id,
            int branch_id,
            String name,
            String phone_number,
            String email)
    {
        super(name, phone_number, email);
        this.client_id = client_id;
        this.branch_id = branch_id;
    }
    public Client(String name,
            int branch_id,
            String phone_number,
            String email)
    {
        super(name, phone_number, email);
        this.client_id = generateClient_ID();
        this.branch_id = branch_id;
    }
    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }  

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    
    public Object[] getRow(){
        return new Object[]{this.getClient_id(), this.getName(), this.getBranch_id(), this.getPhone_number(), this.getEmail(), this.getAssigned_to()};
    }
    public static String generateClient_ID(){
        UUID uuid = randomUUID();
        String fullUUID = uuid.toString();
        String finalID = fullUUID.split("-")[0];
        return finalID;
    }
}