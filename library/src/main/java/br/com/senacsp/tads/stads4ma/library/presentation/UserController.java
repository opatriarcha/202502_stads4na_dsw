package br.com.senacsp.tads.stads4ma.library.presentation;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.service.UserService;
import lombok.RequiredArgsConstructor;
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

}
