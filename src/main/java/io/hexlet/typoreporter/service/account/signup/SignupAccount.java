package io.hexlet.typoreporter.service.account.signup;

public record SignupAccount(
    String username,
    String email,
    String password,
    String firstName,
    String lastName,
    String authProvider,
    String yandexId
) {

    public SignupAccount(String authProvider, String lastName, String firstName, String password, String email, String username) {
        this(username, email, password, firstName, lastName, authProvider, null);
    }

    @Override
    public String toString() {
        return "SignupAccount(username=%s, email=%s, firstName=%s, lastName=%s)"
            .formatted(username, email, firstName, lastName);
    }

    public void setYandexId(String yandexId) {
        yandexId = this.yandexId;
    }

}
