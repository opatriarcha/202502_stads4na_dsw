package br.com.senacsp.tads.stads4ma.library.domainmodel;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Author implements Serializable {
    private @Setter @Getter  @EqualsAndHashCode.Include Long id;
    private @Setter @Getter String name;
    private @Setter @Getter String email;


}
