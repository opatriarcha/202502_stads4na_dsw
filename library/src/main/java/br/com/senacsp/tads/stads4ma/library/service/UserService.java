package br.com.senacsp.tads.stads4ma.library.service;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public List<User> findAll();

    public User findById(UUID id);
}
