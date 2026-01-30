package com.bichart.model;

public class ColumnSchema {

    private String name;
    private String type;

    public ColumnSchema(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
