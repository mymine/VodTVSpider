package com.github.catvod.bean;

import java.util.List;

public class Rule {
    private String name;
    private List<String> hosts;
    private List<String> regex;

    public static Rule empty() {
        return new Rule("");
    }

    public Rule() {
    }

    public Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<String> getRegex() {
        return regex;
    }

    public void setRegex(List<String> regex) {
        this.regex = regex;
    }
}
