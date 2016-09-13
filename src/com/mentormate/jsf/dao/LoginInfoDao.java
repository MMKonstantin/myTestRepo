package com.mentormate.jsf.dao;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import com.mentormate.jsf.entities.LoginInfo;

@Named
@Default
@Stateless
public class LoginInfoDao extends BaseDao<LoginInfo> {
	
} // end class