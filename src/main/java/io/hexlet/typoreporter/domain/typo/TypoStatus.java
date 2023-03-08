package io.hexlet.typoreporter.domain.typo;


import java.util.Collection;
import java.util.List;

import static io.hexlet.typoreporter.domain.typo.TypoEvent.CANCEL;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.OPEN;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.REOPEN;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.RESOLVE;


public enum TypoStatus {

    REPORTED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case OPEN -> IN_PROGRESS;
                case RESOLVE, REOPEN -> throw new InvalidTypoEventException(this, event);
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(OPEN, CANCEL);
        }
    },
    IN_PROGRESS {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case OPEN, REOPEN -> throw new InvalidTypoEventException(this, event);
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
                case REOPEN -> IN_PROGRESS;
                case RESOLVE, OPEN -> throw new InvalidTypoEventException(this, event);
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(REOPEN, CANCEL);
        }
    },
    CANCELED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case REOPEN -> IN_PROGRESS;
                case RESOLVE, OPEN, CANCEL -> throw new InvalidTypoEventException(this, event);
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(REOPEN);
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
