package io.hexlet.typoreporter.domain.workspace;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceUrl;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AllowedUrl{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_id_seq")
    @SequenceGenerator(name = "url_id_seq", allocationSize = 15)
    private Long id;

    @WorkspaceUrl
    private String url;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Workspace workspace;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllowedUrl that = (AllowedUrl) o;
        return Objects.equals(url, that.url) && Objects.equals(workspace, that.workspace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, workspace);
    }
}
