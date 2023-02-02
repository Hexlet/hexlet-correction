package io.hexlet.typoreporter.test.asserts;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import org.assertj.core.api.ObjectAssert;

public class TypoInfoAssert extends ObjectAssert<TypoInfo> {

    public TypoInfoAssert(TypoInfo typoInfo) {
        super(typoInfo);
    }

    public static TypoInfoAssert assertThat(TypoInfo actual) {
        return new TypoInfoAssert(actual);
    }

    public TypoInfoAssert isEqualsToTypoInfo(Typo expected) {
        isNotNull();
        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringFields("typoStatusStr", "createdBy", "createdDateAgo", "createdDate", "modifiedBy", "modifiedDateAgo", "modifiedDate", "account")
            .isEqualTo(expected);
        return this;
    }
}
