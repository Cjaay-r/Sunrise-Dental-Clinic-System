package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import sunrisedentalsystem.model.Treatment;
import sunrisedentalsystem.model.User;
import sunrisedentalsystem.service.TreatmentService;

class TreatmentServletTest {

    private TreatmentService treatmentService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    private User loggedInUser;

    private TreatmentServlet treatmentServlet;

    @BeforeEach
    void setUp() {

        treatmentService = mock(TreatmentService.class);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        loggedInUser = mock(User.class);

        treatmentServlet =
                new TreatmentServlet(treatmentService);
    }

    @Test
    void shouldRejectTreatmentWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("treatmentType"))
                .thenReturn("");

        when(request.getParameter("treatmentPrice"))
                .thenReturn("");

        when(request.getRequestDispatcher(
                "addTreatment.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "All treatment fields are required."
        );

        verify(dispatcher)
                .forward(request, response);

        verifyNoInteractions(treatmentService);
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        stubValidTreatmentParameters();

        when(request.getSession(false))
                .thenReturn(null);

        treatmentServlet.doPost(request, response);

        verify(response)
                .sendRedirect("login.jsp");

        verifyNoInteractions(treatmentService);
    }

    @Test
    void shouldAddTreatmentWhenDetailsAreValid()
            throws Exception {

        stubValidTreatmentParameters();
        stubLoggedInUser();

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenReturn(true);

        when(request.getRequestDispatcher(
                "treatmentDetails.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doPost(request, response);

        ArgumentCaptor<Treatment> captor =
                ArgumentCaptor.forClass(Treatment.class);

        verify(treatmentService)
                .addTreatment(
                        captor.capture()
                );

        Treatment treatment = captor.getValue();

        assertNotNull(treatment);

        assertEquals(
                "Root Canal",
                treatment.getTreatmentType()
        );

        assertEquals(
                15000.0,
                treatment.getTreatmentPrice()
        );

        verify(request).setAttribute(
                eq("treatment"),
                any(Treatment.class)
        );

        verify(request).setAttribute(
                "successMessage",
                "Treatment added successfully."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldShowErrorWhenAddingTreatmentFails()
            throws Exception {

        stubValidTreatmentParameters();
        stubLoggedInUser();

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenReturn(false);

        when(request.getRequestDispatcher(
                "addTreatment.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Unable to add treatment."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldRejectInvalidTreatmentPrice()
            throws Exception {

        when(request.getParameter("treatmentType"))
                .thenReturn("Root Canal");

        when(request.getParameter("treatmentPrice"))
                .thenReturn("ABC");

        stubLoggedInUser();

        when(request.getRequestDispatcher(
                "addTreatment.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Invalid treatment price."
        );

        verify(dispatcher)
                .forward(request, response);

        verify(treatmentService, never())
                .addTreatment(
                        any(Treatment.class)
                );
    }

    @Test
    void shouldDisplayTreatmentWhenTreatmentExists()
            throws Exception {

        Treatment treatment =
                mock(Treatment.class);

        when(request.getParameter("treatmentId"))
                .thenReturn("3");

        when(treatmentService
                .getTreatmentById(3))
                .thenReturn(treatment);

        when(request.getRequestDispatcher(
                "treatmentDetails.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doGet(request, response);

        verify(treatmentService)
                .getTreatmentById(3);

        verify(request).setAttribute(
                "treatment",
                treatment
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldShowErrorWhenTreatmentDoesNotExist()
            throws Exception {

        when(request.getParameter("treatmentId"))
                .thenReturn("999");

        when(treatmentService
                .getTreatmentById(999))
                .thenReturn(null);

        when(request.getRequestDispatcher(
                "searchTreatment.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Treatment not found."
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldRejectInvalidTreatmentId()
            throws Exception {

        when(request.getParameter("treatmentId"))
                .thenReturn("ABC");

        when(request.getRequestDispatcher(
                "searchTreatment.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doGet(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Invalid treatment ID."
        );

        verify(dispatcher)
                .forward(request, response);

        verify(treatmentService, never())
                .getTreatmentById(
                        org.mockito.ArgumentMatchers.anyInt()
                );
    }

    @Test
    void shouldDisplayAllTreatmentsWhenTreatmentIdIsNotProvided()
            throws Exception {

        List<Treatment> treatments =
                List.of(
                        mock(Treatment.class),
                        mock(Treatment.class)
                );

        when(request.getParameter("treatmentId"))
                .thenReturn(null);

        when(treatmentService.getAllTreatments())
                .thenReturn(treatments);

        when(request.getRequestDispatcher(
                "treatmentList.jsp"))
                .thenReturn(dispatcher);

        treatmentServlet.doGet(request, response);

        verify(treatmentService)
                .getAllTreatments();

        verify(request).setAttribute(
                "treatments",
                treatments
        );

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void shouldThrowServletExceptionWhenTreatmentServiceFails()
            throws Exception {

        stubValidTreatmentParameters();
        stubLoggedInUser();

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        assertThrows(
                ServletException.class,
                () -> treatmentServlet
                        .doPost(request, response)
        );
    }

    private void stubValidTreatmentParameters() {

        when(request.getParameter("treatmentType"))
                .thenReturn("Root Canal");

        when(request.getParameter("treatmentPrice"))
                .thenReturn("15000.00");
    }

    private void stubLoggedInUser() {

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute("loggedInUser"))
                .thenReturn(loggedInUser);
    }
}