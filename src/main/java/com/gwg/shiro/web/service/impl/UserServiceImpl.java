package com.gwg.shiro.web.service.impl;

import com.github.pagehelper.PageInfo;
import com.gwg.shiro.web.dao.AccountDao;
import com.gwg.shiro.web.dao.UserDao;
import com.gwg.shiro.web.dao.UserRoleDao;
import com.gwg.shiro.web.dto.UserDto;
import com.gwg.shiro.web.exception.BusinessException;
import com.gwg.shiro.web.model.Account;
import com.gwg.shiro.web.model.User;
import com.gwg.shiro.web.service.UserService;
import com.gwg.shiro.web.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private AccountDao accountDao;

	@Autowired
	private UserRoleDao userRoleDao;


	/**
	 * 基于Spring Aop的事物管理在自调用时 @Transactional注解无效
	 * 测试AspectJ在自调用时候，@Transactional是否有效
	 * @param dto
	 */
	public void testAspectJ(UserDto dto){
		addUserInfo(dto);

	}

    /**
     * 如果在事物上配置readOnly=true,则只能进行查询操作
     *
     */
	@Transactional(readOnly = true)
	public void addUserInfo(UserDto dto) throws BusinessException{
		//校验
       //1.账户表
		userDao.addUser(dto);

		//2.用户表
        accountDao.addAccount(dto);

		//3.用户角色关联表
		userRoleDao.addUserRole(dto);

	}

	@Transactional
	public void updateUserInfo(UserDto dto) throws BusinessException{
		//0.校验数据的合法性
		//判断该记录是否存在
		//1.账户表
		userDao.updateUserByUserId(dto);

		//2.用户表
		accountDao.updateAccountByUserId(dto);

		//3.用户角色关联表
		userRoleDao.updateUserRoleByUserId(dto);

	}


}
