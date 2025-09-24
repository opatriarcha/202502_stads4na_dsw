package br.com.senacsp.tads.stads4ma.library.presentation;

import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import br.com.senacsp.tads.stads4ma.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("GET /api/users -> 200 com lista")
    void fetchAllUsers_ok() throws Exception {
        var u1 = new User(UUID.randomUUID(), "Ana", "ana@x.com", "p1");
        var u2 = new User(UUID.randomUUID(), "Bob", "bob@x.com", "p2");
        given(userService.findAll()).willReturn(List.of(u1, u2));

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Ana")))
                .andExpect(jsonPath("$[1].email", is("bob@x.com")));
    }

    @Test
    @DisplayName("GET /api/users/{id} -> 200 quando existe")
    void fetchById_found() throws Exception {
        var id = UUID.randomUUID();
        var u = new User(id, "Ana", "ana@x.com", "p1");
        given(userService.findById(id)).willReturn(u);

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Ana")));
    }

    @Test
    @DisplayName("GET /api/users/{id} -> 404 quando não existe")
    void fetchById_notFound() throws Exception {
        var id = UUID.randomUUID();
        given(userService.findById(id)).willReturn(null);

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} -> 204 quando remove")
    void deleteById_noContent() throws Exception {
        var id = UUID.randomUUID();
        given(userService.deleteById(id)).willReturn(true);

        mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(id);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} -> 404 quando não existe")
    void deleteById_notFound() throws Exception {
        var id = UUID.randomUUID();
        given(userService.deleteById(id)).willReturn(false);

        mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/users -> 201 created")
    void createUser_created() throws Exception {
        var input = new User(null, "Ana", "ana@x.com", "p1");
        var saved = new User(UUID.randomUUID(), "Ana", "ana@x.com", "p1");
        given(userService.create(any(User.class))).willReturn(saved);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Ana")));
    }

    @Nested
    class PutAndPatch {
        @Test
        @DisplayName("PUT /api/users/{id} -> 404 quando não existe")
        void put_notFound() throws Exception {
            var id = UUID.randomUUID();
            given(userService.existsById(id)).willReturn(false);

            mvc.perform(put("/api/users/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new User(null, "N", "e@x", "p"))))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/users/{id} -> 200 quando existe")
        void put_ok() throws Exception {
            var id = UUID.randomUUID();
            var db = new User(id, "Old", "old@x.com", "oldp");
            var body = new User(null, "New", "new@x.com", "newp");

            given(userService.existsById(id)).willReturn(true);
            given(userService.findById(id)).willReturn(db);

            mvc.perform(put("/api/users/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("New")))
                    .andExpect(jsonPath("$.email", is("new@x.com")))
                    .andExpect(jsonPath("$.password", is("newp")));

            verify(userService).deleteById(id);
            verify(userService).update(any(User.class));
        }

        @Test
        @DisplayName("PATCH /api/users/{id} -> 404 quando não existe")
        void patch_notFound() throws Exception {
            var id = UUID.randomUUID();
            given(userService.existsById(id)).willReturn(false);

            mvc.perform(patch("/api/users/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new User(null, "x", null, null))))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PATCH /api/users/{id} -> 200 com atualização parcial")
        void patch_ok() throws Exception {
            var id = UUID.randomUUID();
            var db = new User(id, "Old", "old@x.com", "oldp");
            var body = new User(null, "NewName", null, null);

            given(userService.existsById(id)).willReturn(true);
            given(userService.findById(id)).willReturn(db);

            mvc.perform(patch("/api/users/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("NewName")))
                    .andExpect(jsonPath("$.email", is("old@x.com")))
                    .andExpect(jsonPath("$.password", is("oldp")));

            verify(userService).deleteById(id);
            verify(userService).update(any(User.class));
        }
    }
}
