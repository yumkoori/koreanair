package com.koreanair.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koreanair.model.dto.SearchUserDTO;
import com.koreanair.model.service.SearchUsersService;

public class SearchusersHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {						
		String username = request.getParameter("userName");
		username = java.net.URLDecoder.decode(username, "UTF-8");
		System.out.println("SearchusersHandler 도착 >" + username);
		
		SearchUsersService service = new SearchUsersService();
		List<SearchUserDTO> searchList = service.searchList(username);
		

		 System.out.println("최종" + searchList);
		 
		 
		 Map<String, Object> responseMap = new HashMap<>();
		 
		 if (searchList != null ) {
			 responseMap.put("status", "success");
			 responseMap.put("data", searchList);
		}
		 
	     // json 으로 변환
		 Gson gson = new GsonBuilder().create();
		 String json = gson.toJson(responseMap);
		// 응답 설정
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");

	    // JSON 응답 출력
		 response.getWriter().write(json);

		return null;
	}

}
