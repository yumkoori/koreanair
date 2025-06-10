package com.koreanair.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.command.CommandHandler;


// @WebServlet("/ProjectServlet")
public class ProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ProjectServlet() {
        super();
        
    }
    
	public void destroy() {
		// System.out.println("> DispatcherServlet.destroy()...");
	}
	
    // Map 선언 : key=url, value=모델 객체를 생성해서
	public Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 System.out.println("> ProjectServlet.init()...");
		String mappingPath = this.getInitParameter("mappingPath");
		String realPath = this.getServletContext().getRealPath(mappingPath);
		 System.out.println("> realPath : " + realPath);
		//  > realPath : C:\Class\JSPClass\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jspPro\WEB-INF\commandHandker.properties
		
		Properties prop = new Properties();
		try(FileReader reader = new FileReader(realPath)){
		       prop.load(reader);
		}catch (Exception e) {
			throw new ServletException();
		}
		
		Set<Entry<Object, Object>> set = prop.entrySet();
		Iterator<Entry<Object, Object>> ir = set.iterator();
		while( ir.hasNext() ) {
			Entry<Object, Object> entry = ir.next();
			String url = (String)entry.getKey();  // board/list.do
			String fullName = (String)entry.getValue();  // days08.mvc.command.ListHandler
			
			Class<?> commandHandlerClass = null;
	         try {
	            commandHandlerClass = Class.forName(fullName);
	            try {
	               CommandHandler handler = (CommandHandler) commandHandlerClass.newInstance();
	               this.commandHandlerMap.put(url, handler); // 맵 추가
	            } catch (InstantiationException e) { 
	               e.printStackTrace();
	            } catch (IllegalAccessException e) { 
	               e.printStackTrace();
	            }
	         } catch (ClassNotFoundException e) { 
	            e.printStackTrace();
	         }
		}
	}





	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 2단계 (요청 URL 분석)
				//   /jspPro/board/list.do
				String requestURI = request.getRequestURI();
				// request.getRequestURL();
				
				// "/jspPro" 만큼 자른다
				int beginIndex = request.getContextPath().length();
				// /board/list.do
				requestURI = requestURI.substring(beginIndex);
				
				// 3단계 - 로직처리하는 모댈객체를 commandHandlerMap으로 부터 얻어오기
				CommandHandler handler = this.commandHandlerMap.get(requestURI);
				
				/*
				 * if(handler == null ) { handler = new NullHandler(); }
				 */
				String view = null;
				try {
					view = handler.process(request, response);
					// 4단계 : request, session 객체 결과를 저장
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				// 5단계 - 뷰 출력 ( 포워딩, 리다이렉트 ) 
				if( view != null) {
					// 포워딩
					RequestDispatcher dispatcher = request.getRequestDispatcher(view);
					dispatcher.forward(request, response);
				}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
