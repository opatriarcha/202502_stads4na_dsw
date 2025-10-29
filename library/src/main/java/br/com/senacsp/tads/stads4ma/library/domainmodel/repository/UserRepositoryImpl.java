package br.com.senacsp.tads.stads4ma.library.domainmodel.repository;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> findByIdWithProfileAndPostsCriteria(UUID id){
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);

        Fetch<User, ?> profileFetch = root.fetch("profile", JoinType.LEFT);
        Fetch<User, ?> postFetch = root.fetch("posts", JoinType.LEFT);

        criteriaQuery.select(root)
                .distinct(true)
                .where(builder.equal(root.get("id"), id));

        TypedQuery query = entityManager.createQuery(criteriaQuery);

        List<User> resultset = query.getResultList();
        return resultset.stream().findFirst();
    }

    public List<User> findByMinPostsAndNameLikeCriteria( int minPosts, String namePart){
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(root)
                .where(
                        builder.and(
                                builder.greaterThanOrEqualTo(
                                        builder.size(root.get("posts")), minPosts),
                                (builder.like(
                                        builder.lower(root.get("name")),
                                        "%" + namePart.toLowerCase() + "%"))
                        )
                )
                .orderBy(builder.asc(root.get("name")));

        return entityManager.createQuery(criteriaQuery)
                .getResultList();
    }
}
