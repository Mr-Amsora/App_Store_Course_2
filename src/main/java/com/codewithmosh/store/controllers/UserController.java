package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.RegisterUserRequset;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false ,defaultValue = "", name = "sort") String sortBy)
    {
        if (!Set.of("id", "name", "email").contains(sortBy))
            sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy)).stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        var user= userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(userMapper.toDto(user));
            // return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }
    @PostMapping
    public ResponseEntity<UserDto> CreateUser(@RequestBody RegisterUserRequset requset,
                                              UriComponentsBuilder uriBuilder) {
        var user = userMapper.toEntity(requset);
        userRepository.save(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        if (userRepository.findById(user.getId()).isPresent()) {
            return ResponseEntity.created(uri).body(userMapper.toDto(user));
        } else {
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") long id, @RequestBody UpdateUserRequest request) {
        var user =userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.update(request,user);
        return ResponseEntity.ok(userMapper.toDto(userRepository.save(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
}
}
