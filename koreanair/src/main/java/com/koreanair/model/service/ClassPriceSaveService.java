package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.ClassPriceSaveDTO;

public class ClassPriceSaveService {
	
	
	public int priceSave (List<ClassPriceSaveDTO> priceList, String flightid ) throws Exception {
		ProjectDao dao = new ProjectDaoimpl();
		return dao.priceSave(priceList, flightid);
	}
}
