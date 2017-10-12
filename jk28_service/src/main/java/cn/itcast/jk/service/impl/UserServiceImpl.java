package cn.itcast.jk.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.sun.istack.FinalArrayList;

import cn.itcast.jk.dao.BaseDao;
import cn.itcast.jk.domain.User;
import cn.itcast.jk.service.UserService;
import cn.itcast.jk.utils.Encrypt;
import cn.itcast.jk.utils.MailUtil;
import cn.itcast.jk.utils.Page;
import cn.itcast.jk.utils.SysConstant;
import cn.itcast.jk.utils.UtilFuns;

public class UserServiceImpl implements UserService {

	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	private SimpleMailMessage mailMessage;
	private JavaMailSender mailSender;
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public List<User> find(String hql, Class<User> entityClass, Object[] params) {
		return baseDao.find(hql, entityClass, params);
	}

	public User get(Class<User> entityClass, Serializable id) {
		return baseDao.get(entityClass, id);
	}

	public Page<User> findPage(String hql, Page<User> page, Class<User> entityClass, Object[] params) {
		return baseDao.findPage(hql, page, entityClass, params);
	}

	public void saveOrUpdate(final User entity) {
		if(UtilFuns.isEmpty(entity.getId())){
			//新增
			String id = UUID.randomUUID().toString();
			entity.setId(id);
			entity.getUserinfo().setId(id);
			
			//补充Shiro添加后的bug
			entity.setPassword(Encrypt.md5(SysConstant.DEFAULT_PASS, entity.getUserName()));
			
			baseDao.saveOrUpdate(entity);//记录保存
			
			//再开启一个新的线程完成邮件发送功能
			/*Thread th = new Thread(new Runnable() {
				public void run() {
					try {
						MailUtil.sendMessage(entity.getUserinfo().getEmail(), "新员工入职的系统账户通知", "欢迎您加入本集团，您的用户名："+entity.getUserName()+",您的初始密码："+SysConstant.DEFAULT_PASS);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
			});*/
			
			//spring 集成javaMail
			Thread th = new Thread(new Runnable() {
				public void run() {
					try {
						mailMessage.setTo(entity.getUserinfo().getEmail());
						mailMessage.setSubject("新员工入职的系统账户通知");
						mailMessage.setText("欢迎您加入本集团，您的用户名："+entity.getUserName()+",您的初始密码："+SysConstant.DEFAULT_PASS);
						mailSender.send(mailMessage);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
			});
			th.start();
			
		}else {
			//修改
			baseDao.saveOrUpdate(entity);
			
		}
		
	}

	public void saveOrUpdateAll(Collection<User> entitys) {
		baseDao.saveOrUpdateAll(entitys);
	}

	public void deleteById(Class<User> entityClass, Serializable id) {
		
		baseDao.deleteById(entityClass, id);//删除一个对象
	}

	public void delete(Class<User> entityClass, Serializable[] ids) {
		
		for(Serializable id :ids){
			this.deleteById(User.class,id);
		}
	}

}
