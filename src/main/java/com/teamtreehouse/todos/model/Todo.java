package com.teamtreehouse.todos.model;

import java.util.Objects;

public class Todo {
    private int id;
    private String name;
    private boolean completed;

    public Todo(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id &&
                completed == todo.completed &&
                Objects.equals(name, todo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, completed);
    }
}
