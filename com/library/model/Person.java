package com.library.model;

// Parent class: common information for people in the library system.
public abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    
    public abstract String getRole();
}
