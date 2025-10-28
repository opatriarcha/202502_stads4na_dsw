package br.com.senac.sp.stads4ma.dsw.Blog.domainmodel;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private @Setter @Getter UUID id;

    @Column(name = "NAME", length = 100, nullable = false)
    private @Setter @Getter String name;

    @Column(name = "EMAIL", length = 50, nullable = false)
    private @Setter @Getter String email;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private @Setter @Getter String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private @Setter @Getter List<Post> posts;

    @OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.EAGER, mappedBy = "user")
    private @Setter @Getter Profile profile;

    public User(UUID id) {
        this.id = UUID.randomUUID();
    }

//    public User(UUID id, String name, String email, String password) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public User(UUID id, String name, String email, String password, List<Post> posts, Profile profile ) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public User(UUID id) {
//        this.id = id;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
