package sunrisedentalsystem.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.dao.ReportDAO;
import sunrisedentalsystem.model.ClinicReport;

class ReportServiceTest {

    private ReportDAO reportDAO;

    private ReportService reportService;

    @BeforeEach
    void setUp() {

        reportDAO =
                mock(ReportDAO.class);

        reportService =
                new ReportServiceImpl(
                        reportDAO
                );
    }

    @Test
    void shouldGenerateClinicReport()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        3
                );

        ClinicReport expectedReport =
                new ClinicReport();

        when(reportDAO
                .getClinicReport(
                        reportDate
                ))
                .thenReturn(
                        expectedReport
                );

        ClinicReport result =
                reportService
                        .generateClinicReport(
                                reportDate
                        );

        assertSame(
                expectedReport,
                result
        );

        verify(reportDAO)
                .getClinicReport(
                        reportDate
                );
    }

    @Test
    void shouldRejectMissingReportDate() {

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        reportService
                                .generateClinicReport(
                                        null
                                )
        );
    }
}