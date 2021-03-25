package com.folleach.daintegrate;

public class Requirement {
    public enum RequirementType {
        String
    }

    public RequirementType type;
    public String name;

    public Requirement(String name, RequirementType type) {
        this.name = name;
        this.type = type;
    }
}
