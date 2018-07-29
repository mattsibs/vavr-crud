package com.dm.vavr.crud.controller;

import com.dm.vavr.crud.data.Identifiable;
import com.dm.vavr.crud.data.User;
import com.dm.vavr.crud.service.UserDto;
import com.dm.vavr.crud.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements CRUDController<User, String> {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity findAll(@RequestParam(value = "ids", required = false) final List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.ok(userService.findAll());
        }

        return userService.findByIds(ids)
                .fold(this::toBadRequest, ResponseEntity::ok);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity findById(@PathVariable("id") final String id) {
        return userService.findById(id)
                .fold(this::toBadRequest, ResponseEntity::ok);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody final UserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto)
                .toIdentifiable());
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateById(@PathVariable("id") final String id, @RequestBody final UserDto userDto) {
        return userService.update(id, userDto)
                .map(Identifiable::toIdentifiable)
                .fold(this::toBadRequest, ResponseEntity::ok);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteById(@PathVariable("id") final String id) {
        return userService.delete(id)
                .map(Identifiable::toIdentifiable)
                .fold(this::toBadRequest, ResponseEntity::ok);
    }

}
