package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;

import java.util.List;
import java.util.UUID;

public interface UserRepositoryOld<T, ID> {
    public List<User> findAll();

    public User findById(UUID id);

    public boolean removeById(UUID id);

    public User create(User user);

    User update(User databaseUser);
}
