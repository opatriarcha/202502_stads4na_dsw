package br.com.senacsp.tads.stads4ma.library.domainmodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="PROFILES")
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  @Setter @Getter UUID id;

    @Column(name="BIO", nullable = false, unique = false, length = 255)
    private @Setter @Getter String bio;

    @Column(name="PROFILE_PICTURE", nullable = true, length = 255)
    private @Setter @Getter String profilePicture;

    @OneToOne
    @JoinColumn(name = "user_id")
    private @Getter @Setter User user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", bio='" + bio + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", user=" + user +
                '}';
    }
}
