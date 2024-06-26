package io.hexlet.typoreporter.test.factory;

import io.hexlet.typoreporter.web.model.SignupAccountModel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_INCORRECT_EMAIL;

@Component
@Getter
public class AccountModelGenerator {
    private Model<SignupAccountModel> correctAccountModel;
    private Model<SignupAccountModel> incorrectAccountModel;
    @Autowired
    private Faker faker;

    @PostConstruct
    public void init() {
        final String password = faker.internet().password(8, 20);
        final String incorrectPassword = faker.internet().password(1, 7);
        correctAccountModel = Instancio.of(SignupAccountModel.class)
            .supply(Select.field(SignupAccountModel::getUsername), () -> faker.name().firstName())
            .supply(Select.field(SignupAccountModel::getEmail), () -> faker.internet().emailAddress())
            .supply(Select.field(SignupAccountModel::getPassword), () -> password)
            .supply(Select.field(SignupAccountModel::getConfirmPassword), () -> password)
            .supply(Select.field(SignupAccountModel::getFirstName), () -> faker.name().firstName())
            .supply(Select.field(SignupAccountModel::getLastName), () -> faker.name().lastName())
            .toModel();
        incorrectAccountModel = Instancio.of(SignupAccountModel.class)
            .supply(Select.field(SignupAccountModel::getUsername), () -> faker.name().firstName() + ".")
            .supply(Select.field(SignupAccountModel::getEmail), () -> ACCOUNT_INCORRECT_EMAIL)
            .supply(Select.field(SignupAccountModel::getPassword), () -> incorrectPassword)
            .supply(Select.field(SignupAccountModel::getConfirmPassword), () -> incorrectPassword)
            .supply(Select.field(SignupAccountModel::getFirstName), () -> "")
            .supply(Select.field(SignupAccountModel::getLastName), () -> "")
            .toModel();
    }
}
