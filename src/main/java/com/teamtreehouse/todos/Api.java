package com.teamtreehouse.todos;

import com.google.gson.Gson;
import com.teamtreehouse.todos.dao.Sql2oTodoDao;
import com.teamtreehouse.todos.dao.TodoDao;
import com.teamtreehouse.todos.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Api {
    public static final String APIVERSION = "/api/v1";

    public static void main(String[] args) {

        staticFileLocation("/public");

        String datasource = "jdbc:h2:~/reviews.db";
        if(args.length > 0){
            if(args.length != 2){
                System.out.println("java Api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0]));
            datasource = args[1];
        }

        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource),
                "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        get(APIVERSION + "/todos", "application/json",
                (req, res) -> todoDao.findAll(), gson::toJson);

        post(APIVERSION + "/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        put(APIVERSION + "/todos/:id", (req, res) -> {
            Todo todo = new Gson().fromJson(req.body(), Todo.class);
            todoDao.update(todo.getId(), todo.getName(), todo.isCompleted());
            res.status(200);
            return todo;
        }, gson::toJson);

        delete(APIVERSION + "/todos/:id", (req, res) -> {
            Todo todo = new Gson().fromJson(req.body(), Todo.class);
            todoDao.deleteById(Integer.parseInt(req.params(":id")));
            res.status(204);
            return null;
        }, gson::toJson);


        after((req, res) -> {
            res.type("application/json");
        });
    }
}
