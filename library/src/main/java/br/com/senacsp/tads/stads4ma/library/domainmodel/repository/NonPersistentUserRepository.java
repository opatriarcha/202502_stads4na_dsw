package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NonPersistentUserRepository implements UserRepository<User, UUID>{

    private final List<User> interalData = new ArrayList<>();

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
        for(User u : this.interalData){
            if(u.getId().equals(id)){
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean removeById(UUID id) {
        for(User u : this.interalData){
            if(u.getId().equals(id)){
                this.interalData.remove(u);
                return true;
            }
        }
        return false;
    }

    @Override
    public User create(User user) {
        this.interalData.add(user);
        return user;
    }
}
