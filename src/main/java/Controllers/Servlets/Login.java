package Controllers.Servlets;
import Controllers.DAO.MySQLDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to index.jsp with potential error message
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MySQLDAO dao = new MySQLDAO();
        // Get login credentials
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (dao.validateLogin(email, password)) {
            // Create a session and store user data
            HttpSession session = req.getSession();
            session.setAttribute("email", email);

            // Redirect to DashboardServlet (which forwards to dashboard.jsp)
            resp.sendRedirect("dashboard");
        } else {
            req.setAttribute("error", "Invalid email or password. Please try again.");
            // Redirect back to login page with an error message
            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
