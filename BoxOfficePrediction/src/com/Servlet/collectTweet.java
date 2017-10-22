package com.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Model.Twitter_data_collection;

@WebServlet("/collectTweet")
public class collectTweet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public collectTweet() {
        super();
        
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String title= request.getParameter("title");
		String hashtag= request.getParameter("hashtag");
		Twitter_data_collection dc = new Twitter_data_collection ();
		try {
			dc.data_collect(title,hashtag);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		
		
	}

}
