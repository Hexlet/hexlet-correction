package io.hexlet.typoreporter.domain.workspace;

import com.fasterxml.jackson.annotation.*;
import io.hexlet.typoreporter.domain.*;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.constraint.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Workspace extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workspace_id_seq")
    @SequenceGenerator(name = "workspace_id_seq", allocationSize = 15)
    private Long id;

    @WorkspaceName
    @Column(unique = true)
    private String name;

    @WorkspaceDescription
    private String description;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Typo> typos = new HashSet<>();

    public Workspace addTypo(final Typo typo) {
        typos.add(typo);
        typo.setWorkspace(this);
        return this;
    }

    public Workspace removeTypo(final Typo typo) {
        typos.remove(typo);
        typo.setWorkspace(null);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || id != null && obj instanceof Workspace other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
