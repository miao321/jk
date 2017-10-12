package cn.itcast.jk.action.sysadmin;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.jk.action.BaseAction;
import cn.itcast.jk.domain.Module;
import cn.itcast.jk.domain.Role;
import cn.itcast.jk.exception.SysException;
import cn.itcast.jk.service.DeptService;
import cn.itcast.jk.service.ModuleService;
import cn.itcast.jk.service.RoleService;
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
public class RoleAction extends BaseAction implements ModelDriven<Role> {
	private Role model = new Role();
	public Role getModel() {
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
	//注入RoleServivce\
	private RoleService roleService;
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	//注入DeptService
	private DeptService deptService;
	
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	//注入ModuleService
	private ModuleService moduleService;
	
	
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	/**
	 * 分页查询
	 */	
	public String list() throws Exception {
		page = roleService.findPage("from Role", page, Role.class, null);		
		//设置分页的URL地址
		page.setUrl("roleAction_list");		
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
		try {
			//1.调用业务方法，根据id得到对象
			Role dept = roleService.get(Role.class, model.getId());
			 //放入栈顶
			super.push(dept);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SysException("对不起，你有病，请先勾选在操作");
		}		
		//3.跳页面		
		return "toview";
	}	
	/**
	 * 新增
	 */	
	public String tocreate() throws Exception {
		
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
		roleService.saveOrUpdate(model);
		//跳页面
		return "alist";
	}	
	/**
	 * 进入修改页面
	 */	
	public String toupdate() throws Exception {
		//1.根据id得到一个对象
		Role dept = roleService.get(Role.class, model.getId());		
		//2.将对象
		super.push(dept);
		//3.查询父部门
			
		//5.跳页面
		return "toupdate";
	}	
	/**
	 * 修改
	 */	
	public String update() throws Exception {		
		//调用业务
		Role obj = roleService.get(Role.class, model.getId());  //根据id 得到一个数据库中保存的对象		
		//2.设置修改的属性
		obj.setName(model.getName());
		obj.setRemark(model.getRemark());
		
		roleService.saveOrUpdate(obj);
		return "alist";
	}
	/**
	 * 删除
	 */
	public String delete() throws Exception {
		if(model.getId() != null){
			String ids[] = model.getId().split(", ");
			//调用业务方法，实现批量删除
			roleService.delete(Role.class, ids);
		}		
		return "alist";
	}
	
	/**
	 * 进入模块分配页面
	 * 
	 */
	
	public String tomodule() throws Exception {
		//1.根据角色id，得到角色对象
		Role obj = roleService.get(Role.class, model.getId());
		
		//2.保存到值栈中
		super.push(obj);
		
		//3.跳页面
		return "tomodule";
	}
	
	/**
	 * 为了使用zTree树，就要组织好zTree树所使用的json数据
	 * json数据结构如下：
	 * [{id:"模块的id",pId:"父模块id",name:"模块名",checked:"true|false"},{id:"模块的id",pId:"父模块id",name:"模块名",checked:"true|false"}]
	 * 
	 * 常用的json插件有哪些？
	 * json-lib    fastjson   struts-json-plugin-xxx-jar ,手动拼接
	 * 
	 * 如何输出
	 * 借助于response对象输出数据
	 */
	
	public String roleModuleJsonStr() throws Exception {
		//1.根据角色id，得到角色对象
		Role role = roleService.get(Role.class, model.getId());
		
		//2.通过对象导航方式，加载出当前角色的模块列表
		Set<Module> moduleSet = role.getModules();
		
		//3.加载出所有发模块列表
		List<Module> moduleList = moduleService.find("from Module", Module.class, null);
		int size = moduleList.size();
		//4.组织json串
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		for(Module module : moduleList){
			size--;
			sb.append("{\"id\":\"").append(module.getId());
			sb.append("\",\"pId\":\"").append(module.getParentId());
			sb.append("\",\"name\":\"").append(module.getName());
			sb.append("\",\"checked\":\"");
			if(moduleSet.contains(module)){
				sb.append("true");
			}else{
				sb.append("false");
			}
			sb.append("\"}");
			
			if(size>0){
				sb.append(",");
			}
			
		}
		
		sb.append("]");
		
		//5.得到response对象
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		
		//6.使用response对象输出json串
		response.getWriter().write(sb.toString());
		
		//返回NONE
		return NONE;
	}
	
	/**
	 * 保存当前角色的模块列表
	 */
	
	public String module() throws Exception {
		//1.哪个角色
		Role role = roleService.get(Role.class, model.getId());
		//2.选中的模块有哪些
		String ids[] = moduleIds.split(",");
		
		//加载出这些模块列表
		Set<Module> moduleSet = new HashSet<Module>();
		if (ids !=null && ids.length>0) {
			for(String id : ids){
				moduleSet.add(moduleService.get(Module.class, id));//添加选中的模块   放到模块列表中
			}
		}
		
		//3.实现角色分配新的模块
		role.setModules(moduleSet);
		
		//4.保存结果
		roleService.saveOrUpdate(role);
		
		//5.跳页面
		return "alist";
	}
	private String moduleIds;
	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}
	
}
