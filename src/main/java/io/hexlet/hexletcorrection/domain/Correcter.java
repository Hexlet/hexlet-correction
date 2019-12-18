package io.hexlet.hexletcorrection.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.hexlet.hexletcorrection.domain.enumeration.CorrecterStatus;

/**
 * A Correcter.
 */
@Entity
@Table(name = "correcter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Correcter extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CorrecterStatus status;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8, max = 50)
    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Size(min = 8, max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "correcter")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Correction> correctionsInProgresses = new HashSet<>();

    @OneToMany(mappedBy = "resolver")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Correction> correctionsResolveds = new HashSet<>();

    @OneToMany(mappedBy = "correcter")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Correcter firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Correcter lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public CorrecterStatus getStatus() {
        return status;
    }

    public Correcter status(CorrecterStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CorrecterStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public Correcter email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Correcter password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public Correcter phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Correcter avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public Correcter avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public User getUser() {
        return user;
    }

    public Correcter user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Correction> getCorrectionsInProgresses() {
        return correctionsInProgresses;
    }

    public Correcter correctionsInProgresses(Set<Correction> corrections) {
        this.correctionsInProgresses = corrections;
        return this;
    }

    public Correcter addCorrectionsInProgress(Correction correction) {
        this.correctionsInProgresses.add(correction);
        correction.setCorrecter(this);
        return this;
    }

    public Correcter removeCorrectionsInProgress(Correction correction) {
        this.correctionsInProgresses.remove(correction);
        correction.setCorrecter(null);
        return this;
    }

    public void setCorrectionsInProgresses(Set<Correction> corrections) {
        this.correctionsInProgresses = corrections;
    }

    public Set<Correction> getCorrectionsResolveds() {
        return correctionsResolveds;
    }

    public Correcter correctionsResolveds(Set<Correction> corrections) {
        this.correctionsResolveds = corrections;
        return this;
    }

    public Correcter addCorrectionsResolved(Correction correction) {
        this.correctionsResolveds.add(correction);
        correction.setResolver(this);
        return this;
    }

    public Correcter removeCorrectionsResolved(Correction correction) {
        this.correctionsResolveds.remove(correction);
        correction.setResolver(null);
        return this;
    }

    public void setCorrectionsResolveds(Set<Correction> corrections) {
        this.correctionsResolveds = corrections;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Correcter comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Correcter addComments(Comment comment) {
        this.comments.add(comment);
        comment.setCorrecter(this);
        return this;
    }

    public Correcter removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setCorrecter(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Correcter)) {
            return false;
        }
        return id != null && id.equals(((Correcter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Correcter{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", status='" + getStatus() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", phone='" + getPhone() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            "}";
    }
}
