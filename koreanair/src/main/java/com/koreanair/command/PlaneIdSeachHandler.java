package com.koreanair.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreanair.model.service.AirCraftIdService;
import com.koreanair.model.service.FlightSeatSaveService;

public class PlaneIdSeachHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("PlaneIdSeachHandler 도착!");
		String craftid = request.getParameter("searchword");
		System.out.println("searchword = " + craftid);
		
		AirCraftIdService getcraftid = new AirCraftIdService();
		
		
		int check = getcraftid.searchaircraftid(craftid);
		
		
        // 1. 클라이언트(브라우저)에게 지금부터 보내는 데이터가 JSON 형식임을 알려줍니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. 응답 데이터를 쓸 수 있는 PrintWriter 객체를 얻어옵니다.
        PrintWriter out = response.getWriter();

        // 3. 보낼 JSON 데이터를 문자열로 만듭니다. (예: {"check": 1} 또는 {"check": 0})
        //    String.format을 사용하면 간단하게 만들 수 있습니다.
        String jsonResponse = String.format("{\"check\": %d}", check);
        
        // 4. 생성한 JSON 문자열을 클라이언트로 보냅니다.
        out.print(jsonResponse);
        out.flush(); // 버퍼에 남아있는 데이터를 모두 전송
        
		return null;
	}

}
