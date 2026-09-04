package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        treatmentService =
                mock(TreatmentService.class);

        request =
                mock(HttpServletRequest.class);

        response =
                mock(HttpServletResponse.class);

        session =
                mock(HttpSession.class);

        dispatcher =
                mock(RequestDispatcher.class);

        loggedInUser =
                mock(User.class);

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute(
                "loggedInUser"))
                .thenReturn(loggedInUser);

        when(session.getAttribute(
                "role"))
                .thenReturn("ADMIN");

        when(request.getRequestDispatcher(
                anyString()))
                .thenReturn(dispatcher);

        treatmentServlet =
                new TreatmentServlet(
                        treatmentService
                );
    }

    @Test
    void shouldDisplayAllTreatmentsWhenTreatmentIdIsNotProvided()
            throws Exception {

        List<Treatment> treatments =
                List.of(
                        new Treatment(
                                1,
                                "Cleaning",
                                5000.00
                        ),
                        new Treatment(
                                2,
                                "Dental Filling",
                                8000.00
                        )
                );

        when(treatmentService
                .getAllTreatments())
                .thenReturn(treatments);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(treatmentService)
                .getAllTreatments();

        verify(request)
                .setAttribute(
                        "treatments",
                        treatments
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentList.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        when(request.getSession(false))
                .thenReturn(null);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendRedirect(
                        "login.jsp"
                );

        verifyNoInteractions(
                treatmentService
        );
    }

    @Test
    void shouldOpenAddTreatmentPageForAdmin()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request)
                .getRequestDispatcher(
                        "addTreatment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldRejectStaffWhenOpeningAddTreatmentPage()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn("STAFF");

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        treatmentServlet.doGet(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Admin access required."
                );

        verifyNoInteractions(
                treatmentService
        );
    }

    @Test
    void shouldDisplayTreatmentWhenTreatmentExists()
            throws Exception {

        Treatment treatment =
                new Treatment(
                        3,
                        "Root Canal",
                        12000.00
                );

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("3");

        when(treatmentService
                .getTreatmentById(
                        3
                ))
                .thenReturn(treatment);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(treatmentService)
                .getTreatmentById(
                        3
                );

        verify(request)
                .setAttribute(
                        "treatment",
                        treatment
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenTreatmentDoesNotExist()
            throws Exception {

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("999");

        when(treatmentService
                .getTreatmentById(
                        999
                ))
                .thenReturn(null);

        when(treatmentService
                .getAllTreatments())
                .thenReturn(List.of());

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Treatment not found."
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentList.jsp"
                );
    }

    @Test
    void shouldRejectInvalidTreatmentId()
            throws Exception {

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("ABC");

        when(treatmentService
                .getAllTreatments())
                .thenReturn(List.of());

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid treatment ID."
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentList.jsp"
                );
    }

    @Test
    void shouldRejectTreatmentWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        when(request.getParameter(
                "treatmentType"))
                .thenReturn("");

        when(request.getParameter(
                "treatmentPrice"))
                .thenReturn("");

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "All treatment fields are required."
                );

        verify(request)
                .getRequestDispatcher(
                        "addTreatment.jsp"
                );

        verify(treatmentService, never())
                .addTreatment(
                        any(Treatment.class)
                );
    }

    @Test
    void shouldAddTreatmentWhenDetailsAreValid()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        when(request.getParameter(
                "treatmentType"))
                .thenReturn(
                        "Cleaning"
                );

        when(request.getParameter(
                "treatmentPrice"))
                .thenReturn(
                        "5000"
                );

        when(treatmentService
                .addTreatment(
                        any(Treatment.class)
                ))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Treatment> captor =
                ArgumentCaptor.forClass(
                        Treatment.class
                );

        verify(treatmentService)
                .addTreatment(
                        captor.capture()
                );

        Treatment treatment =
                captor.getValue();

        assertEquals(
                "Cleaning",
                treatment.getTreatmentType()
        );

        assertEquals(
                5000.00,
                treatment.getTreatmentPrice()
        );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Treatment added successfully."
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentDetails.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldShowErrorWhenAddingTreatmentFails()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        when(request.getParameter(
                "treatmentType"))
                .thenReturn(
                        "Cleaning"
                );

        when(request.getParameter(
                "treatmentPrice"))
                .thenReturn(
                        "5000"
                );

        when(treatmentService
                .addTreatment(
                        any(Treatment.class)
                ))
                .thenReturn(false);

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Unable to add treatment."
                );

        verify(request)
                .getRequestDispatcher(
                        "addTreatment.jsp"
                );
    }

    @Test
    void shouldRejectInvalidTreatmentPrice()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        when(request.getParameter(
                "treatmentType"))
                .thenReturn(
                        "Cleaning"
                );

        when(request.getParameter(
                "treatmentPrice"))
                .thenReturn(
                        "invalid"
                );

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "errorMessage",
                        "Invalid treatment price."
                );

        verify(request)
                .getRequestDispatcher(
                        "addTreatment.jsp"
                );

        verify(treatmentService, never())
                .addTreatment(
                        any(Treatment.class)
                );
    }

    @Test
    void shouldOpenEditTreatmentPageForAdmin()
            throws Exception {

        Treatment treatment =
                new Treatment(
                        3,
                        "Cleaning",
                        5000.00
                );

        when(request.getParameter(
                "action"))
                .thenReturn("edit");

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("3");

        when(treatmentService
                .getTreatmentById(
                        3
                ))
                .thenReturn(treatment);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request)
                .setAttribute(
                        "treatment",
                        treatment
                );

        verify(request)
                .getRequestDispatcher(
                        "editTreatment.jsp"
                );

        verify(dispatcher)
                .forward(
                        request,
                        response
                );
    }

    @Test
    void shouldUpdateTreatmentWhenDetailsAreValid()
            throws Exception {

        Treatment existingTreatment =
                new Treatment(
                        3,
                        "Cleaning",
                        5000.00
                );

        when(request.getParameter(
                "action"))
                .thenReturn("update");

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("3");

        when(request.getParameter(
                "treatmentType"))
                .thenReturn(
                        "Advanced Cleaning"
                );

        when(request.getParameter(
                "treatmentPrice"))
                .thenReturn(
                        "6000"
                );

        when(treatmentService
                .getTreatmentById(
                        3
                ))
                .thenReturn(
                        existingTreatment
                );

        when(treatmentService
                .updateTreatment(
                        any(Treatment.class)
                ))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Treatment> captor =
                ArgumentCaptor.forClass(
                        Treatment.class
                );

        verify(treatmentService)
                .updateTreatment(
                        captor.capture()
                );

        Treatment treatment =
                captor.getValue();

        assertEquals(
                3,
                treatment.getTreatmentId()
        );

        assertEquals(
                "Advanced Cleaning",
                treatment.getTreatmentType()
        );

        assertEquals(
                6000.00,
                treatment.getTreatmentPrice()
        );

        verify(request)
                .setAttribute(
                        "successMessage",
                        "Treatment updated successfully."
                );

        verify(request)
                .getRequestDispatcher(
                        "treatmentDetails.jsp"
                );
    }

    @Test
    void shouldDeleteTreatmentWhenTreatmentIdIsValid()
            throws Exception {

        when(request.getParameter(
                "action"))
                .thenReturn("delete");

        when(request.getParameter(
                "treatmentId"))
                .thenReturn("3");

        when(treatmentService
                .deleteTreatment(
                        3
                ))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        verify(treatmentService)
                .deleteTreatment(
                        3
                );

        verify(response)
                .sendRedirect(
                        "treatment"
                );
    }

    @Test
    void shouldRejectStaffWhenPostingTreatmentChange()
            throws Exception {

        when(session.getAttribute(
                "role"))
                .thenReturn("STAFF");

        when(request.getParameter(
                "action"))
                .thenReturn("add");

        treatmentServlet.doPost(
                request,
                response
        );

        verify(response)
                .sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Admin access required."
                );

        verifyNoInteractions(
                treatmentService
        );
    }

    @Test
    void shouldThrowServletExceptionWhenTreatmentServiceFails()
            throws Exception {

        when(treatmentService
                .getAllTreatments())
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        ServletException exception =
                assertThrows(
                        ServletException.class,
                        () ->
                                treatmentServlet
                                        .doGet(
                                                request,
                                                response
                                        )
                );

        assertEquals(
                "Unable to retrieve treatment.",
                exception.getMessage()
        );
    }
}