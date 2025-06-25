package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.PopularDTO;

public class PopularService {
	
	public List<PopularDTO> popularlist( String year ) throws Exception{
		ProjectDao dao = new ProjectDaoimpl();
		
		return dao.popularroutes(year);
	}
}
