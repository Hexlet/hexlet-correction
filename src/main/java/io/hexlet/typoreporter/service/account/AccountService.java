package io.hexlet.typoreporter.service.account;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    Account registerAccount(final AccountRegistration accountRegistration);

}
