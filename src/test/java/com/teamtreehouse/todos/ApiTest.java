package com.teamtreehouse.todos;

import com.google.gson.Gson;
import com.teamtreehouse.testing.ApiClient;
import com.teamtreehouse.testing.ApiResponse;
import com.teamtreehouse.todos.dao.Sql2oTodoDao;
import com.teamtreehouse.todos.model.Todo;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ApiTest {

    public static final String PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oTodoDao todoDao;

    @BeforeClass
    public static void startServer(){
        String[] args = {PORT, TEST_DATASOURCE};
        Api.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
        todoDao = new Sql2oTodoDao(sql2o);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodosReturnsCreatedStatus() throws Exception{
        Map<String, Object> values = new HashMap<>();
        values.put("name", "New Todo");
        values.put("completed", false);

        ApiResponse res = client.request("POST", Api.APIVERSION + "/todos", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void todosCanBeAccessed() throws Exception{
        todoDao.add(new Todo("Todo 1", false));
        todoDao.add(new Todo("Todo 2", false));

        ApiResponse res = client.request("GET",
                Api.APIVERSION + "/todos");
        Todo[] retrieved = gson.fromJson(res.getBody(), Todo[].class);
        assertEquals(2, retrieved.length);
    }

    @Test
    public void todosCanBeUpdated() throws Exception{
        todoDao.add(new Todo("Todo 1", false));
        List<Todo> todo = todoDao.findAll();

        Map<String, Object> values = new HashMap<>();
        values.put("name", "Updated Todo 1");
        values.put("completed", true);

        ApiResponse res = client.request("PUT",
                Api.APIVERSION + "/todos/" + todo.get(0).getId(), gson.toJson(values));
        assertEquals(200, res.getStatus());

        Todo updatedTodo = gson.fromJson(res.getBody(), Todo.class);
        assertEquals("Updated Todo 1", updatedTodo.getName());
        assertEquals(true, updatedTodo.isCompleted());
    }

    @Test
    public void todosCanBeDeleted() throws Exception{
        todoDao.add(new Todo("Todo 1", false));
        List<Todo> todo = todoDao.findAll();

        ApiResponse res = client.request("DELETE",
                Api.APIVERSION + "/todos/" + todo.get(0).getId());
        assertEquals(204, res.getStatus());
    }


}