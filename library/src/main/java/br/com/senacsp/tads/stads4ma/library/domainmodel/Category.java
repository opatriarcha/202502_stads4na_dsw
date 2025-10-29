package br.com.senacsp.tads.stads4ma.library.domainmodel;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {

    private @Setter @Getter  @EqualsAndHashCode.Include Long id;
    private @Setter @Getter String name;

}
