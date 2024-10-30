package com.kazcables.model;

public class Role 
{
    private String name;
    private String description;
    private int manages_level;
    
    public Role(String name, String description)
    {
        this.name=name;
        this.description=description;
    }
    // overloaded constructor
    public Role(String name, String description, int m_level)
    {
        this.name=name;
        this.description=description;
        this.manages_level = m_level;
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

    public int getManages_level() {
        return manages_level;
    }

    public void setManages_level(int manages_level) {
        this.manages_level = manages_level;
    }

}
