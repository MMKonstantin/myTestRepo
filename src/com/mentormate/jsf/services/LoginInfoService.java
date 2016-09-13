package com.mentormate.jsf.services;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import com.mentormate.jsf.dao.LoginInfoDao;
import com.mentormate.jsf.entities.LoginInfo;

@Default
@Named
@Stateless
public class LoginInfoService {

	@Inject
	protected LoginInfoDao loginInfoDao;

	public List<LoginInfo> getAllUserLogs() {
		return loginInfoDao.getAll(LoginInfo.class);
	}
	
	public void save(LoginInfo currentRecord) {
		loginInfoDao.save(currentRecord);
	}
} // end class