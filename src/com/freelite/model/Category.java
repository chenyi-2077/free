package com.freelite.model;

/**
 * 分类实体类
 * B负责
 */
public class Category {
    private int id;
    private String name;

    public Category() {}
    public Category(int id, String name) { this.id = id; this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
