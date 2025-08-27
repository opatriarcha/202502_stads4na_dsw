package br.com.senacsp.tads.stads4ma.library.service;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
}
