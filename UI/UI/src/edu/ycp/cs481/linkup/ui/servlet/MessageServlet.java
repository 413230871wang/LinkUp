package edu.ycp.cs481.linkup.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs481.linkup.controller.MatchingController;
import edu.ycp.cs481.linkup.controller.MessageController;
import edu.ycp.cs481.linkup.controller.SendMessageController;
import edu.ycp.cs481.linkup.model.Path;
import edu.ycp.cs481.linkup.persistence.PersistenceException;
import edu.ycp.cs481.linkup.persistence.SQLconnection;


public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String DB_USERNAME = "ajcummins";
	private static String DB_PASSWORD = "root";
	
	int user_id;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Path urlPath = new Path(req.getPathInfo());
		System.out.println("Path = " + req.getPathInfo());
		user_id = Integer.parseInt(urlPath.getUserIDFromPath());
		MessageController controller = new MessageController();
		
		
		String username = null;
		try {
			username = controller.MessagingUserName(user_id);
		} catch (PersistenceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		req.setAttribute("username", username);
		
		
		String tableData = null;
		try {
			tableData = controller.Messaging(user_id);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		req.setAttribute("table", tableData);
		
		//get sent messages
		String sentTableData = null;
		try {
			sentTableData = controller.sentMessaging(user_id);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		req.setAttribute("sentTable", sentTableData);
		
		req.getRequestDispatcher("/_view/matchMessages.jsp").forward(req, resp);
	}
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String buttonAction = req.getParameter("submit");
		if(buttonAction.equals("Profile")){
			System.out.print("\nthis is the user id:" + user_id);
			resp.sendRedirect("userProfile/"+ user_id);					
		}if(buttonAction.equals("Matches!")){
			System.out.print("\nthis is the user id:" + user_id);
			resp.sendRedirect("userMatch/"+ user_id);				
		}else{
			System.out.print("\nthis is the value:" + buttonAction);
			SendMessageController controller = new SendMessageController();
			int id = 0;
			try {
				id = controller.ReplyID(buttonAction);
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.sendRedirect("replyMessage/"+ user_id + "/" + id);
		}		
	}	
}

