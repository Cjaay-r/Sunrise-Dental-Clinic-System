package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.ClinicReport;
import sunrisedentalsystem.service.ReportService;

class ReportServletTest {

    private ReportService reportService;

    private ReportServlet reportServlet;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {

        reportService =
                mock(ReportService.class);

        reportServlet =
                new ReportServlet(
                        reportService
                );

        request =
                mock(HttpServletRequest.class);

        response =
                mock(HttpServletResponse.class);

        session =
                mock(HttpSession.class);

        dispatcher =
                mock(RequestDispatcher.class);
    }

    @Test
    void shouldDisplayReportForAdmin()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        3
                );

        ClinicReport report =
                new ClinicReport();

        when(request
                .getSession(
                        false
                ))
                .thenReturn(
                        session
                );

        when(session
                .getAttribute(
                        "loggedInUser"
                ))
                .thenReturn(
                        new Object()
                );

        when(session
                .getAttribute(
                        "role"
                ))
                .thenReturn(
                        "ADMIN"
                );

        when(request
                .getParameter(
                        "reportDate"
                ))
                .thenReturn(
                        "2026-09-03"
                );

        when(reportService
                .generateClinicReport(
                        reportDate
                ))
                .thenReturn(
                        report
                );

        when(request
                .getRequestDispatcher(
                        "report.jsp"
                ))
                .thenReturn(
                        dispatcher
                );


        reportServlet.doGet(
                request,
                response
        );


        verify(reportService)
                .generateClinicReport(
                        reportDate
                );

        verify(request)
                .setAttribute(
                        "clinicReport",
                        report
                );

        verify(request)
                .setAttribute(
                        "selectedDate",
                        reportDate
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectStaffAccess()
            throws Exception {

        when(request
                .getSession(
                        false
                ))
                .thenReturn(
                        session
                );

        when(session
                .getAttribute(
                        "loggedInUser"
                ))
                .thenReturn(
                        new Object()
                );

        when(session
                .getAttribute(
                        "role"
                ))
                .thenReturn(
                        "STAFF"
                );


        reportServlet.doGet(
                request,
                response
        );


        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Admin access required."
                );

        verify(reportService,
                never())
                .generateClinicReport(
                        org.mockito.ArgumentMatchers
                                .any()
                );
    }

    @Test
    void shouldRedirectUnauthenticatedUserToLogin()
            throws Exception {

        when(request
                .getSession(
                        false
                ))
                .thenReturn(null);


        reportServlet.doGet(
                request,
                response
        );


        verify(response)
                .sendRedirect(
                        "login.jsp"
                );

        verify(reportService,
                never())
                .generateClinicReport(
                        org.mockito.ArgumentMatchers
                                .any()
                );
    }

    @Test
    void shouldUseSelectedReportDate()
            throws Exception {

        LocalDate reportDate =
                LocalDate.of(
                        2026,
                        9,
                        10
                );

        ClinicReport report =
                new ClinicReport();

        when(request
                .getSession(
                        false
                ))
                .thenReturn(
                        session
                );

        when(session
                .getAttribute(
                        "loggedInUser"
                ))
                .thenReturn(
                        new Object()
                );

        when(session
                .getAttribute(
                        "role"
                ))
                .thenReturn(
                        "ADMIN"
                );

        when(request
                .getParameter(
                        "reportDate"
                ))
                .thenReturn(
                        "2026-09-10"
                );

        when(reportService
                .generateClinicReport(
                        reportDate
                ))
                .thenReturn(
                        report
                );

        when(request
                .getRequestDispatcher(
                        "report.jsp"
                ))
                .thenReturn(
                        dispatcher
                );


        reportServlet.doGet(
                request,
                response
        );


        verify(reportService)
                .generateClinicReport(
                        reportDate
                );

        assertEquals(
                reportDate,
                LocalDate.parse(
                        "2026-09-10"
                )
        );
    }
}