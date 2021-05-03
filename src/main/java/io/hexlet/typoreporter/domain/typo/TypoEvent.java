package io.hexlet.typoreporter.domain.typo;

public enum TypoEvent {
    OPEN,
    RESOLVE,
    REOPEN,
    CANCEL;

    public String getStyle() {
        return switch (this) {
            case OPEN, REOPEN -> "danger";
            case RESOLVE -> "success";
            case CANCEL -> "secondary";
        };
    }
}
