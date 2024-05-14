import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num = Integer.parseInt(request.getParameter("num"));
		int amt = Integer.parseInt(request.getParameter("amt"));
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, "system","venom");

	        String query2 = "select balance from account where num=?";
	        PreparedStatement pst2 = con.prepareStatement(query2);
	        pst2.setInt(1, num);
	        ResultSet rs = pst2.executeQuery();
	        rs.next();
	        int bal = rs.getInt(1);
	        if(bal<amt){
	            out.println("<h2>Insufficient Balance</h2>");
	            return;
	        }

			String query = "update account set balance=balance-? where num=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, amt);
			ps.setInt(2, num);
	        int x = ps.executeUpdate();
	        if(x==0){
	            out.println("<h2>Data could not be updated !</h2>");
	        } else {
	        	out.println("<h2>Amount Withdrawn !</h2>");
				out.println("Back to <a href='Success.html'>Home</a>");
	        }
		}catch(Exception e) {
			out.println("<h2>Exception:"+e.getMessage()+"</h2>");
		}
	}
}
