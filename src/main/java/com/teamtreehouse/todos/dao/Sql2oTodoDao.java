package com.teamtreehouse.todos.dao;

import com.teamtreehouse.todos.exc.DaoException;
import com.teamtreehouse.todos.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void add(Todo todo) throws DaoException {
        String sql = "INSERT INTO todos(name, completed) VALUES(:name, :completed)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        } catch(Sql2oException ex){
            throw new DaoException(ex, "Problem adding todo");
        }
    }

    @Override
    public void update(int id, String newName, Boolean newCompleted) throws DaoException {
        String sql = "UPDATE todos SET (name, completed) = (:name, :completed) WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", newName)
                    .addParameter("completed", newCompleted)
                    .executeUpdate();
        }catch(Sql2oException ex){
            throw new DaoException(ex, "Problem updating todo");
        }
    }

    @Override
    public List<Todo> findAll() {
        try (Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos")
                    .executeAndFetch(Todo.class);
        }
    }

    @Override
    public void deleteById(int id) throws DaoException {
        String sql = "DELETE from todos WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            throw new DaoException(ex, "Problem deleting todo");
        }
    }
}
