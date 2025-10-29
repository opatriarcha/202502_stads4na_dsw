package br.com.senacsp.tads.stads4ma.library.presentation;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


//    @RequestMapping(produces = "json", method = RequestMethod.GET)
    @GetMapping//http://localhost:8080/api/users
    public ResponseEntity<List<User>> fetchAllUsers() {
        return ResponseEntity.ok().body(this.userService.findAll());
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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<User>(this.userService.create(user), HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user){
        return null;
    }

}
