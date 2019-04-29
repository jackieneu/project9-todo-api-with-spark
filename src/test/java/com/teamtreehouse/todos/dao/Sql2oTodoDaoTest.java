package com.teamtreehouse.todos.dao;

import com.teamtreehouse.todos.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oTodoDaoTest {

    private Sql2oTodoDao dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);
        //Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodoSetsId() throws Exception {
        Todo todo = new Todo("Test Task", false);
        int originalTodoId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void addedTodosAreReturnedFromFindAll() throws Exception {
        Todo todo = new Todo("Test Task", false);

        dao.add(todo);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void noTodosReturnsEmptyList() throws Exception{
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void todosCanBeDeleted() throws Exception {
        Todo todo = new Todo("Test Task", false);
        dao.add(todo);
        List<Todo> todos = dao.findAll();
        assertEquals(1, todos.size());

        dao.deleteById(todos.get(0).getId());
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void todosCanBeUpdated() throws Exception {
        Todo todo = new Todo("Test Task", false);
        dao.add(todo);
        List<Todo> todos = dao.findAll();
        assertEquals(1, todos.size());

        dao.update(todos.get(0).getId(), "Updated Task", true);
        List<Todo> updatedTodos = dao.findAll();
        assertEquals(1, updatedTodos.size());
        assertEquals("Updated Task", updatedTodos.get(0).getName());
        assertEquals(true, updatedTodos.get(0).isCompleted());
    }

}