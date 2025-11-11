package br.com.senacsp.tads.stads4ma.library.presentation.dtos;

import br.com.senacsp.tads.stads4ma.library.domainmodel.Post;
import br.com.senacsp.tads.stads4ma.library.domainmodel.Profile;
import br.com.senacsp.tads.stads4ma.library.domainmodel.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
public class UserDTO {

    private @Setter @Getter UUID id;

    @NotBlank(message="Nome é obrigatório")
    @Size( max=100, message="O nome deve ter no máximo 100 caracteres")
    private @Setter @Getter String name;

    @NotBlank(message="Email é obrigatório")
    @Size( max=30, message="O email deve ter no máximo 100 caracteres")
    @Email(message="O Email deve ser válido")
    private @Setter @Getter String email;

    @NotBlank(message="O Password é obrigatório")
    @Size(min=6, max=12, message="O Passwrod deve ter entre 6 e 12 caracteres.")
    private @Setter @Getter String password;

    public static UserDTO fromEntity(User user){
        if( user == null ) return null;
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static User fromDTO(UserDTO user){
        if( user == null ) return null;
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
