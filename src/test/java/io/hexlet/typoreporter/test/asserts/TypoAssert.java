package io.hexlet.typoreporter.test.asserts;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.assertj.core.api.ObjectAssert;

public class TypoAssert extends ObjectAssert<Typo> {

    TypoAssert(Typo actual) {
        super(actual);
    }

    public static TypoAssert assertThat(Typo actual) {
        return new TypoAssert(actual);
    }

    public TypoAssert isEqualsToTypoReport(TypoReport expected) {
        isNotNull();
        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringFields("id", "typoStatus", "createdDate", "createdBy",
                "modifiedDate", "modifiedBy", "workspace", "account")
            .isEqualTo(expected);
        return this;
    }
}
