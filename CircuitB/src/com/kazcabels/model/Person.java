package com.kazcabels.model;


abstract class Person {
    protected String name;
    protected String phone_number;
    protected String email;
    protected String gender;

    public Person(String name, String phone_number, String email, String gender) {
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.gender = gender;
    }
    public Person(String name, String phone, String email){
        this.name = name;
        this.email = email;
        this.phone_number = phone;
        this.gender = "";
    }
    public Person(String name){
        this.name = name;
        this.email = "";
        this.phone_number = "";
        this.gender = "";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
}
