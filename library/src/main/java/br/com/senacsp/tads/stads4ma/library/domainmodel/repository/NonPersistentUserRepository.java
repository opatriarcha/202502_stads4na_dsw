package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NonPersistentUserRepository implements UserRepositoryOld<User, UUID> {

    private final Set<User> interalData = new HashSet<>();

    public NonPersistentUserRepository(){
        Faker faker = new Faker();
        for( int i = 0; i < 100; i++ ){
            User user = new User(
                    UUID.randomUUID(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.internet().password()
            );
            this.interalData.add(user);
        }
    }

    @Override
    public List<User> findAll() {
        return this.interalData.stream().toList();
    }

    @Override
    public User findById(UUID id) {
        if(this.interalData.contains(id)){
            for(User u : this.interalData){
                if(u.getId().equals(id)){
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public boolean removeById(UUID id) {
//        return false;
        if(this.existsById(id)){
            User u = this.findById(id);
            this.interalData.remove(u);
        }
        return false;
    }

    @Override
    public User create(User user) {
        this.interalData.add(user);
        return user;
    }

    @Override
    public User update(User databaseUser) {
        this.interalData.add(databaseUser);
        return databaseUser;
    }

    public boolean existsById(UUID id) {
        return this.interalData.contains(id);
    }
}
