package Controllers.Servlets;
import Controllers.DAO.MySQLDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to index.jsp with potential error message
        RequestDispatcher dispatcher = req.getRequestDispatcher("register.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MySQLDAO dao = new MySQLDAO();
        // Get login credentials
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Simple authentication logic (replace with database validation)
        if (dao.registerStudent(name, email, password)) {
            // Create a session and store user data
            HttpSession session = req.getSession();
            session.setAttribute("email", email);

            // Redirect to DashboardServlet (which forwards to dashboard.jsp)
            resp.sendRedirect("dashboard");
        } else {
            req.setAttribute("error", "Email already registered!");
            // Redirect back to login page with an error message
            RequestDispatcher dispatcher = req.getRequestDispatcher("register.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
