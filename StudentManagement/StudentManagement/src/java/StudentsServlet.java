import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/StudentsServlet")
@SuppressWarnings("serial")
public class StudentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response); // Forward GET requests to doPost
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {

            out.println("<html><head><title>Student List</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background: linear-gradient(90deg, #C7C5F4, #776BCC); text-align: center; }");
            out.println(".heading { color: white; font-size: 50px; }");
            out.println(".container { width: 90%; margin: auto; }");
            out.println("table { width: 100%; border-collapse: collapse; background: #fff; margin-top: 20px; }");
            out.println("button { padding: 10px 20px; border: none; background: #776BCC; color: white; font-weight: 600; font-size: 16px; cursor: pointer; border-radius: 5px; }");
            out.println(".addbutton{background:white; color:#776BCC;}");
            out.println("button:hover { background: #5a4bb7; color:white; }");
            out.println("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
            out.println("th { background: #776BCC; color: white; }");
            out.println("tr:nth-child(even) { background: #f2f2f2 }");
            out.println(".button-container { text-align: right; margin-bottom: 10px; }"); // Moved Add Button to Right
            out.println("</style></head><body>");

            out.println("<h2 class='heading'>Student List</h2>");
            out.println("<div class='container'>");
            
            // Add Student Button shifted to the right
            out.println("<div class='button-container'>");
            out.println("<form action='AddServlet' method='GET' style='display:inline;'>");
            out.println("<button class='addbutton'>Add Student</button>");
            out.println("</form>");
            out.println("</div>");

            // Table for students
            out.println("<table><tr><th>S.No</th><th>UID</th><th>Name</th><th>Enrolled Date</th><th>Course</th><th>Address</th><th>Action</th></tr>");

            String url = "jdbc:mysql://localhost:3306/java1";
            String username = "root";
            String password = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                try (Connection conn = DriverManager.getConnection(url, username, password);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getInt("sno") + "</td>");
                        out.println("<td>" + rs.getString("uid") + "</td>");
                        out.println("<td>" + rs.getString("name") + "</td>");
                        out.println("<td>" + rs.getString("enrolled") + "</td>");
                        out.println("<td>" + rs.getString("course") + "</td>");
                        out.println("<td>" + rs.getString("address") + "</td>");
                        out.println("<td>");
                        out.println("<form action='UpdateServlet' method='GET' style='display:inline;'>");
                        out.println("<input type='hidden' name='sno' value='" + rs.getInt("sno") + "'>");
                        out.println("<button class='button edit-button' type='submit'>Edit</button>");
                        out.println("</form>");
                        out.println("<form action='RemoveServlet' method='POST' style='display:inline;'>");
                        out.println("<input type='hidden' name='sno' value='" + rs.getInt("sno") + "'>");
                        out.println("<button class='button delete-button' type='submit'>Remove</button>");
                        out.println("</form>");
                        out.println("</td></tr>");
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            }

            out.println("</table></div></body></html>");
        }
    }
}
