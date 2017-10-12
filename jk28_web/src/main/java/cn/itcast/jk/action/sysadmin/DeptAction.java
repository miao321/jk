package cn.itcast.jk.action.sysadmin;


import java.util.List;

import cn.itcast.jk.action.BaseAction;
import cn.itcast.jk.domain.Dept;
import cn.itcast.jk.service.DeptService;
import cn.itcast.jk.utils.Page;

import com.opensymphony.xwork2.ActionContext;
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
public class DeptAction extends BaseAction implements ModelDriven<Dept> {
	private Dept model = new Dept();
	public Dept getModel() {
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
	//注入DeptServivce\
	private DeptService deptService;
	public DeptService getDeptService() {
		return deptService;
	}

	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}	
	/**
	 * 分页查询
	 */	
	public String list() throws Exception {
		page = deptService.findPage("from Dept", page, Dept.class, null);		
		//设置分页的URL地址
		page.setUrl("deptAction_list");		
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
		Dept dept = deptService.get(Dept.class, model.getId());
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
		//将查询的结果放入值栈中,它放在context区域中
		super.put("deptList", deptList);
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
		deptService.saveOrUpdate(model);
		//跳页面
		return "alist";
	}	
	/**
	 * 进入修改页面
	 */	
	public String toupdate() throws Exception {
		//1.根据id得到一个对象
		Dept dept = deptService.get(Dept.class, model.getId());		
		//2.将对象
		super.push(dept);
		//3.查询父部门
		List<Dept> deptList = deptService.find("from Dept where state=1", Dept.class, null);		
		deptList.remove(dept);
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
		Dept obj = deptService.get(Dept.class, model.getId());  //根据id 得到一个数据库中保存的对象		
		//2.设置修改的属性
		obj.setParent(model.getParent());
		obj.setDeptName(model.getDeptName());
		deptService.saveOrUpdate(obj);
		return "alist";
	}
	/**
	 * 删除
	 */
	public String delete() throws Exception {
		if(model.getId() != null){
			String ids[] = model.getId().split(", ");
			//调用业务方法，实现批量删除
			deptService.delete(Dept.class, ids);
		}
			
			
		return "alist";
	}
}
