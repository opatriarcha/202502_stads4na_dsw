package br.com.senacsp.tads.stads4ma.library.presentation;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Sobe o contexto Spring inteiro e testa os endpoints reais do controlador,
 * serialização JSON e o “serviço” in-memory abaixo.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @TestConfiguration
    static class InMemoryUserServiceConfig {
        @Bean
        public UserService userService() {
            return new InMemoryUserService();
        }

        static class InMemoryUserService implements UserService {
            private final Map<UUID, User> store = new ConcurrentHashMap<>();

            @Override public List<User> findAll() { return new ArrayList<>(store.values()); }

            @Override public User findById(UUID id) { return store.get(id); }

            @Override public boolean deleteById(UUID id) { return store.remove(id) != null; }

            @Override public User create(User user) {
                UUID id = Optional.ofNullable(user.getId()).orElse(UUID.randomUUID());
                var toSave = new User(id, user.getName(), user.getEmail(), user.getPassword());
                store.put(id, toSave);
                return toSave;
            }

            @Override public boolean existsById(UUID id) { return store.containsKey(id); }

            @Override public User update(User user) {
                store.put(user.getId(), user);
                return user;
            }
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    MockMvc mvc;

    @org.springframework.beans.factory.annotation.Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Fluxo completo: POST -> GET all -> GET by id -> PUT -> PATCH -> DELETE -> GET 404")
    void fullFlow() throws Exception {
        // 1) POST create
        var bodyCreate = new User(null, "Ana", "ana@x.com", "p1");
        var postResult = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Ana")))
                .andReturn();

        var created = objectMapper.readValue(
                postResult.getResponse().getContentAsByteArray(),
                User.class
        );
        var id = created.getId();

        // 2) GET all
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // 3) GET by id
        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("ana@x.com")));

        // 4) PUT (troca tudo conforme o controller)
        var putBody = new User(null, "Bruna", "bru@x.com", "p2");
        mvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bruna")))
                .andExpect(jsonPath("$.email", is("bru@x.com")))
                .andExpect(jsonPath("$.password", is("p2")));

        // 5) PATCH (parcial: só email)
        var patchBody = new User(null, null, "nova@x.com", null);
        mvc.perform(patch("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bruna")))
                .andExpect(jsonPath("$.email", is("nova@x.com")))
                .andExpect(jsonPath("$.password", is("p2")));

        // 6) DELETE
        mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        // 7) GET by id -> 404
        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT/PATCH devem retornar 404 quando id não existe")
    void putPatchNotFound() throws Exception {
        var randomId = UUID.randomUUID();
        var body = new User(null, "X", "x@x.com", "p");

        mvc.perform(put("/api/users/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());

        mvc.perform(patch("/api/users/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }
}
