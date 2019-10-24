package io.hexlet.hexletcorrection.database;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.repository.AccountRepository;
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@DBRider
public class CorrectionTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Test
    @DataSet("account.yml")
    @ExpectedDataSet("expectedAccount.yml")
    public void shouldCreateCorrection(){
        var account = accountRepository.findById(1L).get();
        var correction = Correction.builder()
                .comment("comment")
                .highlightText("text")
                .account(account)
                .pageURL("url")
                .build();

        correctionRepository.save(correction);
    }
}
