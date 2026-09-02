package sunrisedentalsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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
import sunrisedentalsystem.service.TreatmentService;

class TreatmentServletTest {

    private TreatmentService treatmentService;
    private TreatmentServlet treatmentServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

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

        when(request.getSession(false))
                .thenReturn(session);

        when(session.getAttribute("loggedInUser"))
                .thenReturn(new Object());

        when(request.getRequestDispatcher(anyString()))
                .thenReturn(dispatcher);

        treatmentServlet =
                new TreatmentServlet(
                        treatmentService
                );
    }

    @Test
    void shouldRejectTreatmentWhenRequiredFieldsAreEmpty()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("treatmentType"))
                .thenReturn("");

        when(request.getParameter("treatmentPrice"))
                .thenReturn("");

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "All treatment fields are required."
        );

        verify(request).getRequestDispatcher(
                "addTreatment.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldRedirectToLoginWhenUserIsNotLoggedIn()
            throws Exception {

        when(request.getSession(false))
                .thenReturn(null);

        treatmentServlet.doPost(
                request,
                response
        );

        verify(response).sendRedirect(
                "login.jsp"
        );

        verifyNoInteractions(
                treatmentService
        );
    }

    @Test
    void shouldAddTreatmentWhenDetailsAreValid()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("treatmentType"))
                .thenReturn(
                        "Dental Cleaning"
                );

        when(request.getParameter("treatmentPrice"))
                .thenReturn(
                        "5000"
                );

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Treatment> treatmentCaptor =
                ArgumentCaptor.forClass(
                        Treatment.class
                );

        verify(treatmentService)
                .addTreatment(
                        treatmentCaptor.capture()
                );

        Treatment treatment =
                treatmentCaptor.getValue();

        assertEquals(
                "Dental Cleaning",
                treatment.getTreatmentType()
        );

        assertEquals(
                5000.0,
                treatment.getTreatmentPrice()
        );

        verify(request).setAttribute(
                "treatment",
                treatment
        );

        verify(request).setAttribute(
                "successMessage",
                "Treatment added successfully."
        );

        verify(request).getRequestDispatcher(
                "treatmentDetails.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldShowErrorWhenAddingTreatmentFails()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("treatmentType"))
                .thenReturn(
                        "Dental Cleaning"
                );

        when(request.getParameter("treatmentPrice"))
                .thenReturn(
                        "5000"
                );

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenReturn(false);

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Unable to add treatment."
        );

        verify(request).getRequestDispatcher(
                "addTreatment.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldRejectInvalidTreatmentPrice()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("treatmentType"))
                .thenReturn(
                        "Dental Filling"
                );

        when(request.getParameter("treatmentPrice"))
                .thenReturn(
                        "invalid"
                );

        treatmentServlet.doPost(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Invalid treatment price."
        );

        verify(request).getRequestDispatcher(
                "addTreatment.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldDisplayTreatmentWhenTreatmentExists()
            throws Exception {

        Treatment treatment =
                new Treatment(
                        3,
                        "Root Canal",
                        25000.0
                );

        when(request.getParameter("treatmentId"))
                .thenReturn("3");

        when(treatmentService.getTreatmentById(3))
                .thenReturn(treatment);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(treatmentService)
                .getTreatmentById(3);

        verify(request).setAttribute(
                "treatment",
                treatment
        );

        verify(request).getRequestDispatcher(
                "treatmentDetails.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldShowErrorWhenTreatmentDoesNotExist()
            throws Exception {

        when(request.getParameter("treatmentId"))
                .thenReturn("99");

        when(treatmentService.getTreatmentById(99))
                .thenReturn(null);

        when(treatmentService.getAllTreatments())
                .thenReturn(List.of());

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Treatment not found."
        );

        verify(treatmentService)
                .getAllTreatments();

        verify(request).getRequestDispatcher(
                "treatmentList.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldRejectInvalidTreatmentId()
            throws Exception {

        when(request.getParameter("treatmentId"))
                .thenReturn("invalid");

        when(treatmentService.getAllTreatments())
                .thenReturn(List.of());

        treatmentServlet.doGet(
                request,
                response
        );

        verify(request).setAttribute(
                "errorMessage",
                "Invalid treatment ID."
        );

        verify(treatmentService)
                .getAllTreatments();

        verify(request).getRequestDispatcher(
                "treatmentList.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldDisplayAllTreatmentsWhenTreatmentIdIsNotProvided()
            throws Exception {

        List<Treatment> treatments =
                List.of(
                        new Treatment(
                                1,
                                "Dental Cleaning",
                                5000.0
                        ),
                        new Treatment(
                                2,
                                "Dental Filling",
                                8000.0
                        )
                );

        when(request.getParameter("treatmentId"))
                .thenReturn(null);

        when(treatmentService.getAllTreatments())
                .thenReturn(treatments);

        treatmentServlet.doGet(
                request,
                response
        );

        verify(treatmentService)
                .getAllTreatments();

        verify(request).setAttribute(
                "treatments",
                treatments
        );

        verify(request).getRequestDispatcher(
                "treatmentList.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldThrowServletExceptionWhenTreatmentServiceFails()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("add");

        when(request.getParameter("treatmentType"))
                .thenReturn(
                        "Dental Cleaning"
                );

        when(request.getParameter("treatmentPrice"))
                .thenReturn(
                        "5000"
                );

        when(treatmentService.addTreatment(
                any(Treatment.class)))
                .thenThrow(
                        new SQLException(
                                "Database error"
                        )
                );

        ServletException exception =
                assertThrows(
                        ServletException.class,
                        () ->
                                treatmentServlet.doPost(
                                        request,
                                        response
                                )
                );

        assertEquals(
                "Unable to process treatment request.",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateTreatmentWhenDetailsAreValid()
            throws Exception {

        Treatment existingTreatment =
                new Treatment(
                        1,
                        "Dental Filling",
                        8000.0
                );

        when(request.getParameter("action"))
                .thenReturn("update");

        when(request.getParameter("treatmentId"))
                .thenReturn("1");

        when(request.getParameter("treatmentType"))
                .thenReturn(
                        "Dental Filling"
                );

        when(request.getParameter("treatmentPrice"))
                .thenReturn(
                        "8500"
                );

        when(treatmentService.getTreatmentById(1))
                .thenReturn(
                        existingTreatment
                );

        when(treatmentService.updateTreatment(
                any(Treatment.class)))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        ArgumentCaptor<Treatment> treatmentCaptor =
                ArgumentCaptor.forClass(
                        Treatment.class
                );

        verify(treatmentService)
                .updateTreatment(
                        treatmentCaptor.capture()
                );

        Treatment updatedTreatment =
                treatmentCaptor.getValue();

        assertEquals(
                1,
                updatedTreatment.getTreatmentId()
        );

        assertEquals(
                "Dental Filling",
                updatedTreatment.getTreatmentType()
        );

        assertEquals(
                8500.0,
                updatedTreatment.getTreatmentPrice()
        );

        verify(request).setAttribute(
                "treatment",
                updatedTreatment
        );

        verify(request).setAttribute(
                "successMessage",
                "Treatment updated successfully."
        );

        verify(request).getRequestDispatcher(
                "treatmentDetails.jsp"
        );

        verify(dispatcher).forward(
                request,
                response
        );
    }

    @Test
    void shouldDeleteTreatmentWhenTreatmentIdIsValid()
            throws Exception {

        when(request.getParameter("action"))
                .thenReturn("delete");

        when(request.getParameter("treatmentId"))
                .thenReturn("1");

        when(treatmentService.deleteTreatment(1))
                .thenReturn(true);

        treatmentServlet.doPost(
                request,
                response
        );

        verify(treatmentService)
                .deleteTreatment(1);

        verify(response).sendRedirect(
                "treatment"
        );
    }
}