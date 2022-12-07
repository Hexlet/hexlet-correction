package io.hexlet.typoreporter.domain.workspacesettings;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorkspaceSettings extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workspace_settings_id_seq")
    @SequenceGenerator(name = "workspace_settings_id_seq", allocationSize = 15)
    private Long id;

    @NotNull
    private UUID apiAccessToken;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Workspace workspace;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || Hibernate.getClass(obj) != Hibernate.getClass(this)) {
            return false;
        }
        WorkspaceSettings workspaceSettings = (WorkspaceSettings) obj;
        return id != null && id.equals(workspaceSettings.id);
    }
}
