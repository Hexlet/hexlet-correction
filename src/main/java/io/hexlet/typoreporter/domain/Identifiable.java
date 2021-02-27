package io.hexlet.typoreporter.domain;

public interface Identifiable<ID> {

    ID getId();

    <T extends Identifiable<ID>> T setId(ID id);
}
