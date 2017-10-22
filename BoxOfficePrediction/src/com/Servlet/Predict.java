package com.Servlet;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Model.Initializer;

import static com.Model.Initializer.inConn1;

@WebServlet("/Predict")
public class Predict extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Predict() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String movie= request.getParameter("movie");
		Initializer in=new Initializer();
		double polarityPart=0,hypePart=0,actorPart=0,actressPart=0,holidayPart=0,genrePart=0 ,sequelPart=0, revenue=0 ;
		String url = null;
				
		try {
				
					 in.connection_open();
			
				  	 String SQL = "select * from movie where TITLE= ?";							//take values
					 PreparedStatement pstmt = inConn1.prepareStatement(SQL);
					 pstmt.setString(1, movie);
					 ResultSet rs = pstmt.executeQuery();
					 rs.next();
					 polarityPart = rs.getDouble("PolarityRatioPart");
			  	     hypePart = rs.getDouble("HypePart");
			  	     actorPart = rs.getDouble("ActorPart");
			  	     actressPart = rs.getDouble("ActressPart");
    			  	 holidayPart = rs.getDouble("HolidayPart");
			  	     genrePart = rs.getDouble("GenrePart");
			  	     sequelPart = rs.getDouble("SequelPart");
			  	     revenue = rs.getDouble("Revenue");
			  	     url = rs.getString("URL");
		  	       
	  	  
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServletContext sc = this.getServletContext();
		request.setAttribute("movie", movie);
		request.setAttribute("PolarityRatioPart", polarityPart);
		request.setAttribute("HypePart", hypePart);
		request.setAttribute("ActorPart",actorPart);
		request.setAttribute("ActressPart", actressPart);
		request.setAttribute("HolidayPart", holidayPart);
		request.setAttribute("GenrePart", genrePart);
		request.setAttribute("SequelPart", sequelPart);
		request.setAttribute("Revenue", revenue);
		request.setAttribute("url", url);
		
		RequestDispatcher rd = sc.getRequestDispatcher("/Jsp/result.jsp");
		rd.include(request, response);
	    
	}

}
