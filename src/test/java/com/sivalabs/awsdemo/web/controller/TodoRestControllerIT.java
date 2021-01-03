package com.sivalabs.awsdemo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.awsdemo.common.AbstractIntegrationTest;
import com.sivalabs.awsdemo.entity.Todo;
import com.sivalabs.awsdemo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoRestControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Todo> todoList;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();

        this.todoList = new ArrayList<>();
        this.todoList.add(new Todo(1L, "First Todo"));
        this.todoList.add(new Todo(2L, "Second Todo"));
        this.todoList.add(new Todo(3L, "Third Todo"));

        this.todoList = todoRepository.saveAll(todoList);
    }

    @Test
    void shouldFetchAllTodos() throws Exception {
        this.mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(todoList.size())));
    }

    @Test
    void shouldCreateNewTodo() throws Exception {
        Todo todo = new Todo(null, "New Todo");
        this.mockMvc.perform(post("/api/todos")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(todo.getContent())))
                .andExpect(jsonPath("$.created_at", notNullValue()));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        Long todoId = todoList.get(0).getId();
        this.mockMvc.perform(delete("/api/todos/{id}", todoId))
                .andExpect(status().isNoContent());
    }
}
