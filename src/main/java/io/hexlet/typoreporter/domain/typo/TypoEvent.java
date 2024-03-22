package io.hexlet.typoreporter.domain.typo;

public enum TypoEvent {
    START,
    RESOLVE,
    RESTART,
    CANCEL;

    public String getStyle() {
        return switch (this) {
            case START, RESTART -> "danger";
            case RESOLVE -> "success";
            case CANCEL -> "secondary";
        };
    }
}
