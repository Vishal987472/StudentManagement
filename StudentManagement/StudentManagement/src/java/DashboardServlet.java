import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/DashboardServlet"})
@SuppressWarnings("serial")
public class DashboardServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/java1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 🔹 Redirect to the login page (index.html)
    response.sendRedirect(request.getContextPath() + "/index.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        if (validateAdmin(email, pass)) {
            HttpSession session = request.getSession();
            session.setAttribute("adminEmail", email);
            session.setAttribute("isLoggedIn", true);
            response.sendRedirect("StudentsServlet");  // 🔹 Redirecting to StudentServlet
        } else {
            response.sendRedirect("index.html?error=Invalid email or password");
        }
    }

    private boolean validateAdmin(String email, String pass) {
        boolean isValid = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM admin WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                isValid = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DashboardServlet.class.getName()).log(Level.SEVERE, "Database error", e);
        }
        return isValid;
    }
}
