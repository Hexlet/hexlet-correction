package io.hexlet.typoreporter.test.asserts;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.assertj.core.api.ObjectAssert;

public class ReportedTypoAssert extends ObjectAssert<ReportedTypo> {

    ReportedTypoAssert(ReportedTypo actual) {
        super(actual);
    }

    public static ReportedTypoAssert assertThat(ReportedTypo actual) {
        return new ReportedTypoAssert(actual);
    }

    public ReportedTypoAssert isEqualsToTypo(Typo expected) {
        isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        return this;
    }

    public ReportedTypoAssert isEqualsToTypoReport(TypoReport expected) {
        isNotNull();
        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringFields("id", "createdDate", "createdBy")
            .isEqualTo(expected);
        return this;
    }
}
