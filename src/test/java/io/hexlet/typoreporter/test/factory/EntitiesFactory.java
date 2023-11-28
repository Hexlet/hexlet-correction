package io.hexlet.typoreporter.test.factory;

import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.time.Instant.now;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class EntitiesFactory {
    public static final String WORKSPACE_101_TOKEN = "abb4f3ab-1994-4264-bc6f-7cd1aa13dc0a";

    public static final String ACCOUNT_101_EMAIL = "test101@gmail.com";

    public static final String ACCOUNT_102_EMAIL = "test102@gmail.com";

    public static final String ACCOUNT_103_EMAIL = "test103@gmail.com";

    public static final Long ACCOUNT_101_ID = 101L;

    public static final Long ACCOUNT_102_ID = 102L;

    public static final Long ACCOUNT_103_ID = 103L;

    public static final Long WORKSPACE_101_ID = 101L;

    public static final Long WORKSPACE_102_ID = 102L;

    public static final Long WORKSPACE_103_ID = 103L;

    public static Stream<Long> getWorkspaceIdsExist() {
        return Stream.of(WORKSPACE_101_ID, WORKSPACE_102_ID, WORKSPACE_103_ID);
    }

    public static Stream<Arguments> getWorkspacesAndUsersRelated() {
        return Stream.of(
            Arguments.of(WORKSPACE_101_ID, ACCOUNT_101_EMAIL),
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_102_EMAIL),
            Arguments.of(WORKSPACE_103_ID, ACCOUNT_103_EMAIL)
        );
    }

    public static Stream<Arguments> getWorkspaceAndAdminRelated() {
        return Stream.of(
            Arguments.of(WORKSPACE_103_ID, ACCOUNT_103_EMAIL)
        );
    }

    public static Stream<Arguments> getWorkspaceAndNotAdminRelated() {
        return Stream.of(
            Arguments.of(WORKSPACE_101_ID, ACCOUNT_101_EMAIL)
        );
    }

    public static Stream<Arguments> getWorkspacesAndUsersAndTypoStatusRelated() {
        return Stream.of(
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_102_EMAIL, "REPORTED"),
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_102_EMAIL, "IN_PROGRESS"),
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_102_EMAIL, "RESOLVED"),
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_102_EMAIL, "CANCELED")
        );
    }

    public static Stream<Arguments> getWorkspacesAndUsersNotRelated() {
        return Stream.of(
            Arguments.of(WORKSPACE_101_ID, ACCOUNT_103_EMAIL),
            Arguments.of(WORKSPACE_102_ID, ACCOUNT_101_EMAIL),
            Arguments.of(WORKSPACE_103_ID, ACCOUNT_102_EMAIL)
        );
    }

    public static Stream<Workspace> getWorkspaces() {
        final var workspace1 = new Workspace()
            .setId(1L)
            .setName("test")
            .setDescription("description")
            .addTypo(new Typo().setId(1L));
        setField(workspace1, "createdBy", "system");
        setField(workspace1, "createdDate", now());
        setField(workspace1, "modifiedBy", "system");
        setField(workspace1, "modifiedDate", now());

        final var workspace2 = new Workspace()
            .setId(2L)
            .setName("new-name")
            .setDescription("description")
            .addTypo(new Typo().setId(2L));
        setField(workspace2, "createdBy", "system");
        setField(workspace2, "createdDate", now());
        setField(workspace2, "modifiedBy", "system");
        setField(workspace2, "modifiedDate", now());

        return Stream.of(workspace1, workspace2);
    }

    public static Stream<CreateWorkspace> getCreateWorkspaces() {
        final var wks1 = new CreateWorkspace(
            "wks-name",
            "wks desc",
            "https://mysite.com"
        );
        final var wks2 = new CreateWorkspace(
            "wks-name",
            null,
            "https://mysite.com"
        );
        return Stream.of(wks1, wks2);
    }

    public static Stream<Typo> getTypos() {
        Typo typo1 = new Typo()
            .setId(1L)
            .setPageUrl("http://site.com")
            .setReporterName("ReporterName")
            .setReporterComment("ReporterComment")
            .setTextBeforeTypo("TextBeforeTypo")
            .setTextTypo("TextTypo")
            .setTextAfterTypo("TextAfterTypo");
        new Workspace().setId(101L).addTypo(typo1);
        setField(typo1, "createdBy", "system");
        setField(typo1, "createdDate", now());
        setField(typo1, "modifiedBy", "system");
        setField(typo1, "modifiedDate", now());

        Typo typo2 = new Typo()
            .setId(2L)
            .setPageUrl("http://site.com")
            .setReporterName("ReporterName")
            .setTextTypo("");
        new Workspace().setId(102L).addTypo(typo2);
        setField(typo2, "createdBy", "system");
        setField(typo2, "createdDate", now());
        setField(typo2, "modifiedBy", "system");
        setField(typo2, "modifiedDate", now());

        return Stream.of(typo1, typo2);
    }

    public static Stream<Long> getTypoIdsExist() {
        return LongStream.rangeClosed(201, 220).boxed();
    }

    public static Stream<Long> getTypoIdsNotExist() {
        return Stream.of(11L, 12L, 13L);
    }

    public static Stream<TypoReport> getTypoReport() {
        final var typoReport = new TypoReport(
            "http://site.com",
            "ReporterName",
            "ReporterComment",
            "TextBeforeTypo",
            "TextTypo",
            "TextAfterTypo"
        );
        final var typoReportEmpty = new TypoReport(
            "http://site.com",
            "ReporterName",
            "ReporterComment",
            "",
            "TextTypo",
            ""
        );
        final var typoReportNull = new TypoReport(
            "http://site.com",
            "ReporterName",
            null,
            null,
            "TextTypo",
            null
        );
        return Stream.of(typoReport, typoReportEmpty, typoReportNull);
    }

    public static Stream<String> getAccountEmailExist() {
        return Stream.of(ACCOUNT_101_EMAIL, ACCOUNT_102_EMAIL, ACCOUNT_103_EMAIL);
    }

    public static Stream<? extends Identifiable<Long>> getEntities() {
        return Stream.concat(getTypos(), getWorkspaces());
    }

    public static Stream<Long> getAccountsIdExist() {
        return Stream.of(ACCOUNT_101_ID, ACCOUNT_102_ID, ACCOUNT_103_ID);
    }

    public static Stream<Long> getWorkspacesIdExist() {
        return Stream.of(WORKSPACE_101_ID, WORKSPACE_102_ID, WORKSPACE_103_ID);
    }
}
