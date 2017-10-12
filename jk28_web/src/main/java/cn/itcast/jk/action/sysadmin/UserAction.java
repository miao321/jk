package cn.itcast.jk.action.sysadmin;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.itcast.jk.action.BaseAction;
import cn.itcast.jk.domain.Dept;
import cn.itcast.jk.domain.Role;
import cn.itcast.jk.domain.User;
import cn.itcast.jk.service.DeptService;
import cn.itcast.jk.service.RoleService;
import cn.itcast.jk.service.UserService;
import cn.itcast.jk.utils.Page;

import com.opensymphony.xwork2.ModelDriven;
/**
 * 部门管理的Action
 * @author Miaohh
 *
 */
/**
 * @author Miaohh
 *
 */
public class UserAction extends BaseAction implements ModelDriven<User> {
	private User model = new User();
	public User getModel() {
		return model;
	}	
	//分页查询
	private Page page = new Page();
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}	
	//注入UserServivce\
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	//注入DeptService
	private DeptService deptService;
	
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	//注入RoleService
	private RoleService roleService;
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	/**
	 * 分页查询
	 */	
	public String list() throws Exception {
		page = userService.findPage("from User", page, User.class, null);		
		//设置分页的URL地址
		page.setUrl("userAction_list");		
		//将page对象压入栈顶
		super.push(page);
		return "list";
	}	
	/**
	 * 查看
	 * id=
	 * model对象
	 *   id属性：results
	 */	
	public String toview() throws Exception {
		//1.调用业务方法，根据id得到对象
		User dept = userService.get(User.class, model.getId());
		 //放入栈顶
		super.push(dept);		
		//3.跳页面		
		return "toview";
	}	
	/**
	 * 新增
	 */	
	public String tocreate() throws Exception {
		//调用业务方法
		 List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);		
		super.put("deptList", deptList);
		
		List<User> userList = userService.find("from User where state=1", User.class,null);
		 //将查询的结果放入值栈中,它放在context区域中
		super.put("userList", userList);
		return "tocreate";
	}	
	/**
	 * 保存
	 * <s:select name="parent.id"
	 * <input type="text" name="deptName" value=""/>
	 * model对象能接收
	 * 	parent
	 * 		id
	 * deptName
	 */
	public String insert() throws Exception {
		//调用业务方法，实现保存
		userService.saveOrUpdate(model);
		//跳页面
		return "alist";
	}	
	/**
	 * 进入修改页面
	 */	
	public String toupdate() throws Exception {
		//1.根据id得到一个对象
		User dept = userService.get(User.class, model.getId());		
		//2.将对象
		super.push(dept);
		//3.查询父部门
		List<Dept> deptList = deptService.find("from Dept where state = 1", Dept.class, null);		
	
		//4.将查询的结果放入值栈中,它放在context区域中
		super.put("deptList", deptList);		
		//5.跳页面
		return "toupdate";
	}	
	/**
	 * 修改
	 */	
	public String update() throws Exception {		
		//调用业务
		User obj = userService.get(User.class, model.getId());  //根据id 得到一个数据库中保存的对象		
		//2.设置修改的属性
		obj.setDept(model.getDept());
		obj.setUserName(model.getUserName());
		obj.setState(model.getState());
		
		userService.saveOrUpdate(obj);
		return "alist";
	}
	/**
	 * 删除
	 */
	public String delete() throws Exception {
		if(model.getId() != null){
			String ids[] = model.getId().split(", ");
			//调用业务方法，实现批量删除
			userService.delete(User.class, ids);
		}		
		return "alist";
	}
	
	/**
	 * 进入角色分配页面
	 * @return
	 * @throws Exception
	 */
	public String torole() throws Exception {
		//1.根据id  得到对象
		User obj =userService.get(User.class, model.getId());
		//2.将对象保存到值栈中
		super.push(obj);
		//3.调用角色业务方法，得到角色列表
		List<Role> roleList = roleService.find("from Role", Role.class, null);
		
		//4.将roleList放入值栈
		super.put("roleList", roleList);
		
		//5.得到当前用户的角色列表
		Set<Role> roleSet = obj.getRoles();
		StringBuilder sb = new StringBuilder();
		for(Role role : roleSet){
			sb.append(role.getName()).append(",");  //管理员， 船运经理
		}
		
		//6.当前用户的角色字符串放入值栈中
		super.put("roleStr", sb.toString());
		return "torole";
	}
	
	/**
	 * 实现角色分配
	 * <input type="hidden" name="id" value="002108e2-9a10-4510-9683-8d8fd1d374ef">
	 * 
	 * <input type="checkbox" name="roleIds" value="4028a1c34ec2e5c8014ec2ebf8430001" class="input">
	 * 
	 * <input type="checkbox" name="roleIds" value="4028a1c34ec2e5c8014ec2ebf8430001" class="input">
	 * @return
	 * @throws Exception
	 */
	public String role() throws Exception {
		//1.根据用户的id，得到对象
		User obj = userService.get(User.class, model.getId());
		
		//2.有哪些角色？只要遍历roleIds,就知道了
		Set<Role> roles = new HashSet<Role>();
		for(String id : roleIds){
			Role role = roleService.get(Role.class, id);
			roles.add(role); //向角色列表中添加一个新的 角色
		}
		
		//3.设置角色与角色列表之间的关系
		obj.setRoles(roles);
		
		//4.保存到数据库表中
		userService.saveOrUpdate(obj);//影响的是用户角色的中间表
		//5.跳页面
		return "alist";
	}
	private String []roleIds; //保存角色的列表
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	
}
