package cn.itcast.jk.action.cargo;


import cn.itcast.jk.action.BaseAction;
import cn.itcast.jk.domain.Contract;
import cn.itcast.jk.domain.User;
import cn.itcast.jk.service.DeptService;
import cn.itcast.jk.service.ContractService;
import cn.itcast.jk.utils.Page;
import cn.itcast.jk.utils.SysConstant;

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
public class ContractAction extends BaseAction implements ModelDriven<Contract> {
	private Contract model = new Contract();
	public Contract getModel() {
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
	private ContractService contractService;
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
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
		String hql = "from Contract where 1=1";
		
		//如何 确定出用户的等级
		User user = super.getCurUser();
		int degree = user.getUserinfo().getDegree();
		if (degree == 4) {
			//说明是员工
			hql += " and createBy='"+user.getId()+"'";
		}else if (degree == 3) {
			//说明是部门经理，管理本部门
			hql += " and createDept='"+user.getDept().getId()+"'";
		}else if (degree == 2) {
			//说明是管理本部门及下属部门？？？
		}else if (degree == 1) {
			//说明是副总？？？
		}else if (degree == 0) {
			//说明是总经理
		}
		page = contractService.findPage(hql, page, Contract.class, null);		
		//设置分页的URL地址
		page.setUrl("contractAction_list");		
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
		Contract dept = contractService.get(Contract.class, model.getId());
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
		//1.加入细粒度权限控制的数据
		User user = super.getCurUser();
		model.setCreateBy(user.getId());//设置创建者的id
		model.setCreateDept(user.getDept().getId());//设置创建者所在部门的id
		
		//调用业务方法，实现保存
		contractService.saveOrUpdate(model);
		//跳页面
		return "alist";
	}	
	/**
	 * 进入修改页面
	 */	
	public String toupdate() throws Exception {
		//1.根据id得到一个对象
		Contract dept = contractService.get(Contract.class, model.getId());		
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
		Contract obj = contractService.get(Contract.class, model.getId());  //根据id 得到一个数据库中保存的对象		
		//2.设置修改的属性
		obj.setCustomName(model.getCustomName());
		obj.setPrintStyle(model.getPrintStyle());
		obj.setContractNo(model.getContractNo());
		obj.setOfferor(model.getOfferor());
		obj.setInputBy(model.getInputBy());
		obj.setCheckBy(model.getCheckBy());
		obj.setInspector(model.getInspector());
		obj.setSigningDate(model.getSigningDate());
		obj.setImportNum(model.getImportNum());
		obj.setShipTime(model.getShipTime());
		obj.setTradeTerms(model.getTradeTerms());
		obj.setDeliveryPeriod(model.getDeliveryPeriod());
		obj.setCrequest(model.getCrequest());
		obj.setRemark(model.getRemark());
		
		
		contractService.saveOrUpdate(obj);
		return "alist";
	}
	/**
	 * 删除
	 */
	public String delete() throws Exception {
		if(model.getId() != null){
			String ids[] = model.getId().split(", ");
			//调用业务方法，实现批量删除
			contractService.delete(Contract.class, ids);
		}		
		return "alist";
	}
	
	/**
	 * 提交
	 */
	
	public String submit() throws Exception {
		String ids[] = model.getId().split(", ");
		
		//2.遍历ids，并加载出每个购销合同对象，在修改购销合同的状态
		contractService.changeState(ids, 1);
		
		return "alist";
	}
	
	/**
	 * 取消
	 */
	
	public String cancel() throws Exception {
        String ids[] = model.getId().split(", ");
		
		//2.遍历ids，并加载出每个购销合同对象，在修改购销合同的状态
		contractService.changeState(ids, 0);
		return "alist";
	}
}
