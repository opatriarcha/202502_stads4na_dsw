package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
