package com.koreanair.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.command.CommandHandler;


public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public DispatcherServlet() {
		super();
	}
	
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
    @Override
	public void init() throws ServletException {
    	String configFile = getInitParameter("configFile");
    	Properties prop = new Properties();
    	String configFilePath = getServletContext().getRealPath(configFile);
    	try(FileReader fis = new FileReader(configFilePath)) {
			prop.load(fis);
		} catch (Exception e) {
			throw new ServletException();
		}
    	Iterator keyIter = prop.keySet().iterator();
    	
    	while (keyIter.hasNext()) {
    		String command = (String)keyIter.next();
    		String handlerClassName = prop.getProperty(command);
    		
    		try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler)handlerClass.newInstance();
				commandHandlerMap.put(command, handlerInstance);
				
			} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
				throw new ServletException();
			}
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getRequestURI();
		
		if(command.indexOf(request.getContextPath())==0) {
			command = command.substring(request.getContextPath().length());
		}
		
		CommandHandler handler = commandHandlerMap.get(command);
		
		if (handler == null) {
			// 매핑되지 않은 URL의 경우 홈페이지로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/index.do");
			return;
		}
		
		String viewPage = null;
		
		try {
			viewPage = handler.process(request, response);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		
		if (viewPage != null) {
		    if (viewPage.startsWith("redirect:")) {
		        String redirectPath = viewPage.substring("redirect:".length());
		        response.sendRedirect(request.getContextPath() + redirectPath);
		    } else {
		        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		        dispatcher.forward(request, response);
		    }
		}
		
		
		
	}
}
