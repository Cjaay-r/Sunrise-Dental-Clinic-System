package sunrisedentalsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sunrisedentalsystem.dao.StaffDAOImpl;
import sunrisedentalsystem.model.Staff;
import sunrisedentalsystem.service.StaffService;
import sunrisedentalsystem.service.StaffServiceImpl;

@WebServlet("/staff")
public class StaffServlet
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private StaffService staffService;

    public StaffServlet() {

    }

    StaffServlet(
            StaffService staffService) {

        this.staffService =
                staffService;
    }

    @Override
    public void init()
            throws ServletException {

        if (staffService == null) {

            staffService =
                    new StaffServiceImpl(
                            new StaffDAOImpl()
                    );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (!isAdmin(session)) {

            handleUnauthorized(
                    session,
                    response
            );

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        try {

            if ("add".equals(action)) {

                request.getRequestDispatcher(
                        "addStaff.jsp"
                ).forward(
                        request,
                        response
                );

                return;
            }

            if ("edit".equals(action)) {

                showEditStaff(
                        request,
                        response
                );

                return;
            }

            String staffIdText =
                    request.getParameter(
                            "staffId"
                    );

            String staffName =
                    request.getParameter(
                            "staffName"
                    );

            if (!isEmpty(staffIdText)) {

                showStaffById(
                        staffIdText,
                        request,
                        response
                );

                return;
            }

            if (!isEmpty(staffName)) {

                showStaffByName(
                        staffName,
                        request,
                        response
                );

                return;
            }

            showStaffManagement(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to retrieve staff information.",
                    e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException {

        HttpSession session =
                request.getSession(false);

        if (!isAdmin(session)) {

            handleUnauthorized(
                    session,
                    response
            );

            return;
        }

        String action =
                request.getParameter(
                        "action"
                );

        try {

            if ("add".equals(action)) {

                addStaff(
                        request,
                        response
                );

                return;
            }

            if ("update".equals(action)) {

                updateStaff(
                        request,
                        response
                );

                return;
            }

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid staff action."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to process staff request.",
                    e
            );
        }
    }

    private void addStaff(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String staffName =
                request.getParameter(
                        "staffName"
                );

        String contactNumber =
                request.getParameter(
                        "contactNumber"
                );

        String username =
                request.getParameter(
                        "username"
                );

        String password =
                request.getParameter(
                        "password"
                );

        if (isEmpty(staffName)
                || isEmpty(contactNumber)
                || isEmpty(username)
                || isEmpty(password)) {

            request.setAttribute(
                    "errorMessage",
                    "All staff fields are required."
            );

            request.getRequestDispatcher(
                    "addStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }
        
        if (!contactNumber.trim().matches("\\d{1,10}")) {

            request.setAttribute(
                    "errorMessage",
                    "Contact number must contain a maximum of 10 digits."
            );

            request.getRequestDispatcher(
                    "addStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        if (staffService.usernameExists(
                username.trim())) {

            request.setAttribute(
                    "errorMessage",
                    "Username already exists."
            );

            request.getRequestDispatcher(
                    "addStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        Staff staff =
                new Staff(
                        0,
                        0,
                        username.trim(),
                        password,
                        staffName.trim(),
                        contactNumber.trim()
                );

        Staff savedStaff =
                staffService.addStaff(
                        staff
                );

        request.setAttribute(
                "staff",
                savedStaff
        );

        request.setAttribute(
                "successMessage",
                "Staff member added successfully."
        );

        request.getRequestDispatcher(
                "staffDetails.jsp"
        ).forward(
                request,
                response
        );
    }

    private void updateStaff(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String staffIdText =
                request.getParameter(
                        "staffId"
                );

        String staffName =
                request.getParameter(
                        "staffName"
                );

        String contactNumber =
                request.getParameter(
                        "contactNumber"
                );

        String username =
                request.getParameter(
                        "username"
                );

        if (isEmpty(staffIdText)
                || isEmpty(staffName)
                || isEmpty(contactNumber)
                || isEmpty(username)) {

            request.setAttribute(
                    "errorMessage",
                    "All staff fields are required."
            );

            request.getRequestDispatcher(
                    "editStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }
        
        if (!contactNumber.trim().matches("\\d{1,10}")) {

            request.setAttribute(
                    "errorMessage",
                    "Contact number must contain a maximum of 10 digits."
            );

            request.getRequestDispatcher(
                    "editStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        int staffId;

        try {

            staffId =
                    Integer.parseInt(
                            staffIdText
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid staff ID."
            );

            return;
        }

        if (staffId <= 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid staff ID."
            );

            return;
        }

        Staff existingStaff =
                staffService.getStaffById(
                        staffId
                );

        if (existingStaff == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Staff member not found."
            );

            return;
        }

        if (!existingStaff
                .getUsername()
                .equals(
                        username.trim()
                )
                && staffService
                        .usernameExists(
                                username.trim()
                        )) {

            request.setAttribute(
                    "staff",
                    existingStaff
            );

            request.setAttribute(
                    "errorMessage",
                    "Username already exists."
            );

            request.getRequestDispatcher(
                    "editStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        existingStaff.setUsername(
                username.trim()
        );

        existingStaff.setStaffName(
                staffName.trim()
        );

        existingStaff.setContactNumber(
                contactNumber.trim()
        );

        boolean updated =
                staffService.updateStaff(
                        existingStaff
                );

        if (!updated) {

            request.setAttribute(
                    "staff",
                    existingStaff
            );

            request.setAttribute(
                    "errorMessage",
                    "Unable to update staff member."
            );

            request.getRequestDispatcher(
                    "editStaff.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        Staff updatedStaff =
                staffService.getStaffById(
                        staffId
                );

        request.setAttribute(
                "staff",
                updatedStaff
        );

        request.setAttribute(
                "successMessage",
                "Staff member updated successfully."
        );

        request.getRequestDispatcher(
                "staffDetails.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showEditStaff(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        String staffIdText =
                request.getParameter(
                        "staffId"
                );

        if (isEmpty(staffIdText)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Staff ID is required."
            );

            return;
        }

        int staffId;

        try {

            staffId =
                    Integer.parseInt(
                            staffIdText
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid staff ID."
            );

            return;
        }

        Staff staff =
                staffService.getStaffById(
                        staffId
                );

        if (staff == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Staff member not found."
            );

            return;
        }

        request.setAttribute(
                "staff",
                staff
        );

        request.getRequestDispatcher(
                "editStaff.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showStaffById(
            String staffIdText,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        int staffId;

        try {

            staffId =
                    Integer.parseInt(
                            staffIdText
                    );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid staff ID."
            );

            showStaffManagement(
                    request,
                    response
            );

            return;
        }

        if (staffId <= 0) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid staff ID."
            );

            showStaffManagement(
                    request,
                    response
            );

            return;
        }

        Staff staff =
                staffService.getStaffById(
                        staffId
                );

        if (staff == null) {

            request.setAttribute(
                    "errorMessage",
                    "Staff member not found."
            );

            showStaffManagement(
                    request,
                    response
            );

            return;
        }

        request.setAttribute(
                "staff",
                staff
        );

        request.getRequestDispatcher(
                "staffDetails.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showStaffByName(
            String staffName,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        List<Staff> staffList =
                staffService
                        .searchStaffByName(
                                staffName.trim()
                        );

        request.setAttribute(
                "staffList",
                staffList
        );

        request.setAttribute(
                "searchName",
                staffName.trim()
        );

        request.getRequestDispatcher(
                "staffList.jsp"
        ).forward(
                request,
                response
        );
    }

    private void showStaffManagement(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
                   IOException,
                   SQLException {

        List<Staff> staffList =
                staffService
                        .getAllStaff();

        request.setAttribute(
                "staffList",
                staffList
        );

        request.getRequestDispatcher(
                "staffList.jsp"
        ).forward(
                request,
                response
        );
    }

    private boolean isAdmin(
            HttpSession session) {

        return session != null
                && session.getAttribute(
                        "loggedInUser"
                ) != null
                && "ADMIN".equals(
                        session.getAttribute(
                                "role"
                        )
                );
    }

    private void handleUnauthorized(
            HttpSession session,
            HttpServletResponse response)
            throws IOException {

        if (session == null
                || session.getAttribute(
                        "loggedInUser"
                ) == null) {

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Admin access required."
        );
    }

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}