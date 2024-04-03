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

    public String getButtonName() {
        return switch (this) {
            case OPEN -> "start";
            case RESOLVE -> "resolve";
            case REOPEN -> "restart";
            case CANCEL -> "cancel";
        };
    }

}
