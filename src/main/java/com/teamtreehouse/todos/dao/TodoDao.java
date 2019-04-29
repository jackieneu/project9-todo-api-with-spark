package com.teamtreehouse.todos.dao;

import com.teamtreehouse.todos.exc.DaoException;
import com.teamtreehouse.todos.model.Todo;

import java.util.List;

public interface TodoDao {
    void add(Todo todo) throws DaoException;

    void update(int id, String newName, Boolean newCompleted) throws DaoException;

    List<Todo> findAll();

    void deleteById(int id) throws DaoException;
}
