package io.hexlet.typoreporter.domain.typo;


import java.util.Collection;
import java.util.List;

import static io.hexlet.typoreporter.domain.typo.TypoEvent.CANCEL;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.START;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.RESTART;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.RESOLVE;


public enum TypoStatus {

    REPORTED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case START -> IN_PROGRESS;
                case RESOLVE, RESTART -> throw new InvalidTypoEventException(this, event);
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(START, CANCEL);
        }
    },
    IN_PROGRESS {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case START, RESTART -> throw new InvalidTypoEventException(this, event);
                case RESOLVE -> RESOLVED;
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(RESOLVE, CANCEL);
        }

        @Override
        public String toString() {
            return "IN PROGRESS";
        }
    },
    RESOLVED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case RESTART -> IN_PROGRESS;
                case RESOLVE, START -> throw new InvalidTypoEventException(this, event);
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(RESTART, CANCEL);
        }
    },
    CANCELED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case RESTART -> IN_PROGRESS;
                case RESOLVE, START, CANCEL -> throw new InvalidTypoEventException(this, event);
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(RESTART);
        }
    };

    public String getStyle() {
        return switch (this) {
            case REPORTED -> "danger";
            case IN_PROGRESS -> "warning";
            case RESOLVED -> "success";
            case CANCELED -> "secondary";
        };
    }

    public abstract TypoStatus next(final TypoEvent event);

    public abstract Collection<TypoEvent> getValidEvents();
}
