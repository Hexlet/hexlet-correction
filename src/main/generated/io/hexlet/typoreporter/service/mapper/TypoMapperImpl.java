package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T23:33:48+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class TypoMapperImpl implements TypoMapper {

    @Override
    public Typo toTypo(TypoReport source) {
        if ( source == null ) {
            return null;
        }

        Typo typo = new Typo();

        typo.setPageUrl( source.pageUrl() );
        typo.setReporterName( source.reporterName() );
        typo.setReporterComment( source.reporterComment() );
        typo.setTextBeforeTypo( source.textBeforeTypo() );
        typo.setTextTypo( source.textTypo() );
        typo.setTextAfterTypo( source.textAfterTypo() );

        return typo;
    }

    @Override
    public TypoInfo toTypoInfo(Typo source) {
        if ( source == null ) {
            return null;
        }

        String modifiedDateAgo = null;
        String createdDateAgo = null;
        String typoStatusStr = null;
        Long id = null;
        String pageUrl = null;
        String reporterName = null;
        String reporterComment = null;
        String textBeforeTypo = null;
        String textTypo = null;
        String textAfterTypo = null;
        TypoStatus typoStatus = null;
        String createdBy = null;
        Instant createdDate = null;
        String modifiedBy = null;
        Instant modifiedDate = null;

        modifiedDateAgo = getDateAgoAsString( source.getModifiedDate() );
        createdDateAgo = getDateAgoAsString( source.getCreatedDate() );
        if ( source.getTypoStatus() != null ) {
            typoStatusStr = source.getTypoStatus().name();
        }
        id = source.getId();
        pageUrl = source.getPageUrl();
        reporterName = source.getReporterName();
        reporterComment = source.getReporterComment();
        textBeforeTypo = source.getTextBeforeTypo();
        textTypo = source.getTextTypo();
        textAfterTypo = source.getTextAfterTypo();
        typoStatus = source.getTypoStatus();
        createdBy = source.getCreatedBy();
        createdDate = source.getCreatedDate();
        modifiedBy = source.getModifiedBy();
        modifiedDate = source.getModifiedDate();

        TypoInfo typoInfo = new TypoInfo( id, pageUrl, reporterName, reporterComment, textBeforeTypo, textTypo, textAfterTypo, typoStatusStr, typoStatus, createdBy, createdDateAgo, createdDate, modifiedBy, modifiedDateAgo, modifiedDate );

        return typoInfo;
    }

    @Override
    public ReportedTypo toReportedTypo(Typo source) {
        if ( source == null ) {
            return null;
        }

        Long id = null;
        String pageUrl = null;
        String reporterName = null;
        String reporterComment = null;
        String textBeforeTypo = null;
        String textTypo = null;
        String textAfterTypo = null;
        String createdBy = null;
        Instant createdDate = null;

        id = source.getId();
        pageUrl = source.getPageUrl();
        reporterName = source.getReporterName();
        reporterComment = source.getReporterComment();
        textBeforeTypo = source.getTextBeforeTypo();
        textTypo = source.getTextTypo();
        textAfterTypo = source.getTextAfterTypo();
        createdBy = source.getCreatedBy();
        createdDate = source.getCreatedDate();

        ReportedTypo reportedTypo = new ReportedTypo( id, pageUrl, reporterName, reporterComment, textBeforeTypo, textTypo, textAfterTypo, createdBy, createdDate );

        return reportedTypo;
    }
}
