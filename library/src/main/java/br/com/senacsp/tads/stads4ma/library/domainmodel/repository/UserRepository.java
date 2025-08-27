package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;

import java.util.List;

public interface UserRepository<T, ID> {
    List<User> findAll();
}
