package io.hexlet.hexletcorrection.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

/**
 * A Preference.
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Preference extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Lob
    @Column(name = "avatar")
    @ToString.Exclude
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "correcter", cascade = ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ToString.Exclude
    private Set<Correction> correctionsInProgresses = new HashSet<>();

    @OneToMany(mappedBy = "resolver", cascade = ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ToString.Exclude
    private Set<Correction> resolvedCorrections = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    public void addComment(final Comment comment) {
        comments.add(comment);
        comment.setAuthor(this);
    }

    public void removeComment(final Comment comment) {
        comments.remove(comment);
        comment.setAuthor(null);
    }

    public void addCorrectionInProgresses(final Correction correction) {
        correctionsInProgresses.add(correction);
        correction.setCorrecter(this);
    }

    public void removeCorrectionInProgresses(final Correction correction) {
        correctionsInProgresses.remove(correction);
        correction.setCorrecter(null);
    }

    public void addResolvedCorrections(final Correction correction) {
        resolvedCorrections.add(correction);
        correction.setResolver(this);
    }

    public void removeResolvedCorrections(final Correction correction) {
        resolvedCorrections.remove(correction);
        correction.setResolver(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preference)) {
            return false;
        }
        return id != null && id.equals(((Preference) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
