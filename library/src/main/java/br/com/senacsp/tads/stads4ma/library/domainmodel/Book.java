package br.com.senac.sp.stads4ma.dsw.Blog.domainmodel;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {

    private @Setter @Getter  @EqualsAndHashCode.Include Long id;
    private @Setter @Getter String title;
    private @Setter @Getter String isbn;
    private @Setter @Getter LocalDate publishedDate;
    private @Setter @Getter Author author;
    private @Setter @Getter Category category;
    private @Setter @Getter String summary;
    private @Setter @Getter String coverFileName;
}
