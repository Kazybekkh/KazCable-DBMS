package com.kazcabels.model;

public class Role 
{
    private String name;
    private String description;
    private String manager_of;
    
    public Role(String name, String description)
    {
        this.name=name;
        this.description=description;
        this.manager_of = "";
    }
    // overloaded constructor
    public Role(String name, String description, String manager_of)
    {
        this.name=name;
        this.description=description;
        this.manager_of = manager_of;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager_of() {
        return manager_of;
    }

    public void setManager_of(String manager_of) {
        this.manager_of = manager_of;
    }
}
