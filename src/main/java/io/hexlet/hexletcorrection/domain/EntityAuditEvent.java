package io.hexlet.hexletcorrection.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.javers.core.metamodel.object.CdoSnapshot;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;

import static io.hexlet.hexletcorrection.config.audit.EntityAuditAction.CREATE;
import static io.hexlet.hexletcorrection.config.audit.EntityAuditAction.DELETE;
import static io.hexlet.hexletcorrection.config.audit.EntityAuditAction.UPDATE;
import static java.lang.Math.round;
import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.joining;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EntityAuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Function<CdoSnapshot, String> snapshotToString = snapshot -> snapshot
        .getState()
        .getPropertyNames()
        .stream()
        .map(property -> '"' + property + "\": \"" + snapshot.getPropertyValue(property) + '"')
        .collect(joining(",", "{", "}"));

    private final static Function<CdoSnapshot, String> snapshotToAction = snapshot -> {
        //maven-checkstyle-plugin and maven-compiler-plugin doesn't support the switch expression from java 13
        switch (snapshot.getType()) {
            case INITIAL:
                return CREATE.value();
            case UPDATE:
                return UPDATE.value();
            case TERMINAL:
                return DELETE.value();
            default:
                throw new IllegalArgumentException();
        }
    };

    public final static Function<CdoSnapshot, EntityAuditEvent> fromJaversSnapshot = snapshot -> new EntityAuditEventBuilder()
        .id(snapshot.getCommitId().toString())
        .commitVersion(round(snapshot.getVersion()))
        .entityType(snapshot.getManagedType().getName())
        .entityId(snapshot.getGlobalId().value().split("/")[1])
        .modifiedBy(snapshot.getCommitMetadata().getAuthor())
        .modifiedDate(snapshot.getCommitMetadata().getCommitDate().toInstant(UTC))
        .action(snapshotToAction.apply(snapshot))
        .entityValue(snapshotToString.apply(snapshot))
        .build();

    private String id;

    private String entityId;

    private String entityType;

    private String action;

    private String entityValue;

    private Integer commitVersion;

    private String modifiedBy;

    private Instant modifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityAuditEvent entityAuditEvent = (EntityAuditEvent) o;
        return Objects.equals(id, entityAuditEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
