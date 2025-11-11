package br.com.senacsp.tads.stads4ma.library.presentation;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.presentation.dtos.UserDTO;
import br.com.senacsp.tads.stads4ma.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;



    @GetMapping
    public ResponseEntity<Set<UserDTO>> fetchAllUsers() {
        System.out.println("called");
        List<User> users = this.userService.findAll();

        if(users.isEmpty())
            return ResponseEntity.notFound().build();

        Set<UserDTO> usersDto = new HashSet<>();

        for( User user : users) {
            usersDto.add(UserDTO.fromEntity(user));
        }

        return ResponseEntity.ok(usersDto);
    }



    //http://localhost:8080/api/users/{id}}
    @GetMapping("/{id}")
    public ResponseEntity<User> fetchById(@PathVariable UUID id) {
        User user = this.userService.findById(id);
        if( user == null )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        if(this.userService.deleteById(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) {
        return new ResponseEntity<UserDTO>(
                UserDTO.fromEntity(
                        this.userService.create(UserDTO.fromDTO(user)
                        )
                ),
                HttpStatus.CREATED
        );
    }
    @PutMapping
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user){
        return null;
    }

}
