

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		try {
			String uname = request.getParameter("uname");
			String pwd = request.getParameter("pwd");
			
			String driver = "oracle.jdbc.driver.OracleDriver";
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,"system","venom");
			String query = "select * from register where uname=? and pwd=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, uname);
			ps.setString(2, pwd);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				out.println("<h4 style='color:blue'>Login Success</h4>");
			}else {
				throw new FailedLoginException("Login Failed");
			}
		} catch(InputMismatchException e) {
			out.println("<h4 style='color:red'>"+e.getMessage()+"</h4>");
			RequestDispatcher rd = request.getRequestDispatcher("Login.html");
			rd.include(request, response);
		} catch(FailedLoginException e) { 
			out.println("<h4 style='color:red'>"+e.getMessage()+"</h4>");
			RequestDispatcher rd = request.getRequestDispatcher("Login.html");
			rd.include(request, response);
		} catch(Exception e) {
			out.println("<h4 style='color:red'>Error:"+e.getMessage()+"</h4>");
			RequestDispatcher rd = request.getRequestDispatcher("Login.html");
			rd.include(request, response);
		}
	}
}
