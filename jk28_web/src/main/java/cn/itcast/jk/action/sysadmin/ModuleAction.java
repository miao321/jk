package cn.itcast.jk.action.sysadmin;


import cn.itcast.jk.action.BaseAction;
import cn.itcast.jk.domain.Module;
import cn.itcast.jk.service.DeptService;
import cn.itcast.jk.service.ModuleService;
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
public class ModuleAction extends BaseAction implements ModelDriven<Module> {
	private Module model = new Module();
	public Module getModel() {
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
	//注入ModuleServivce\
	private ModuleService moduleService;
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	//注入DeptService
	private DeptService deptService;
	
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}
	
	/**
	 * 分页查询
	 */	
	public String list() throws Exception {
		page = moduleService.findPage("from Module", page, Module.class, null);		
		//设置分页的URL地址
		page.setUrl("moduleAction_list");		
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
		Module dept = moduleService.get(Module.class, model.getId());
		 //放入栈顶
		super.push(dept);		
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
		moduleService.saveOrUpdate(model);
		//跳页面
		return "alist";
	}	
	/**
	 * 进入修改页面
	 */	
	public String toupdate() throws Exception {
		//1.根据id得到一个对象
		Module dept = moduleService.get(Module.class, model.getId());		
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
		Module obj = moduleService.get(Module.class, model.getId());  //根据id 得到一个数据库中保存的对象		
		//2.设置修改的属性
		obj.setName(model.getName());
		obj.setLayerNum(model.getLayerNum());
		obj.setCpermission(model.getCpermission());
		obj.setCurl(model.getCurl());
		obj.setCtype(model.getCtype());
		obj.setState(model.getState());
		obj.setBelong(model.getBelong());
		obj.setCwhich(model.getCwhich());
		obj.setRemark(model.getRemark());
		obj.setOrderNo(model.getOrderNo());
		
		
		moduleService.saveOrUpdate(obj);
		return "alist";
	}
	/**
	 * 删除
	 */
	public String delete() throws Exception {
		if(model.getId() != null){
			String ids[] = model.getId().split(", ");
			//调用业务方法，实现批量删除
			moduleService.delete(Module.class, ids);
		}		
		return "alist";
	}
}
