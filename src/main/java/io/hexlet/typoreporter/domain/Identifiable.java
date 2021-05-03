package io.hexlet.typoreporter.domain;

public interface Identifiable<I> {

    I getId();

    <T extends Identifiable<I>> T setId(I id);
}
