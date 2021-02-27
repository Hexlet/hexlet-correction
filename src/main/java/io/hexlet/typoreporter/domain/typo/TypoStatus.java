package io.hexlet.typoreporter.domain.typo;


import java.util.*;

import static io.hexlet.typoreporter.domain.typo.TypoEvent.*;


public enum TypoStatus {

    REPORTED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case OPEN -> IN_PROGRESS;
                case RESOLVE -> throw new InvalidTypoEventException(this, event);
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
                case OPEN -> throw new InvalidTypoEventException(this, event);
                case RESOLVE -> RESOLVED;
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(RESOLVE, CANCEL);
        }
    },
    RESOLVED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case OPEN -> IN_PROGRESS;
                case RESOLVE -> throw new InvalidTypoEventException(this, event);
                case CANCEL -> CANCELED;
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(OPEN, CANCEL);
        }
    },
    CANCELED {
        @Override
        public TypoStatus next(TypoEvent event) {
            return switch (event) {
                case OPEN -> IN_PROGRESS;
                case RESOLVE, CANCEL -> throw new InvalidTypoEventException(this, event);
            };
        }

        @Override
        public Collection<TypoEvent> getValidEvents() {
            return List.of(OPEN);
        }
    };

    public abstract TypoStatus next(final TypoEvent event);

    public abstract Collection<TypoEvent> getValidEvents();
}
