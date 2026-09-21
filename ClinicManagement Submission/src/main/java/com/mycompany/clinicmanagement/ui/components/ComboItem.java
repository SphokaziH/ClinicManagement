package com.mycompany.clinicmanagement.ui.components;

public class ComboItem {
    private final int id;
    private final String name;

    public ComboItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        // JComboBox uses this method to decide what to display to the user
        return name; 
    }
}