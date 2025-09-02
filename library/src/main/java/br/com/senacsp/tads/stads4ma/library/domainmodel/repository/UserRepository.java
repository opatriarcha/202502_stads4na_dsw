package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository<T, ID> {
    public List<User> findAll();

    public User findById(UUID id);
}
