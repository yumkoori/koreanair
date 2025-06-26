package com.koreanair.model.service;

import java.util.List;

import com.koreanair.model.dao.ProjectDao;
import com.koreanair.model.dao.ProjectDaoimpl;
import com.koreanair.model.dto.SearchUserDTO;

public class SearchUsersService {
	
	public List<SearchUserDTO> searchList( String username) {
		
		ProjectDao dao = new ProjectDaoimpl();
		List<SearchUserDTO> searchList = null;
		try {
			searchList = dao.searchUsers(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchList;
	}
	
}
