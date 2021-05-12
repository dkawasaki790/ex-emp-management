package jp.co.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.domain.Employee;
import jp.co.sample.form.UpdateEmployeeForm;
import jp.co.sample.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	/**
	 * 
	 * @return 「employee/list.html」にフォワード
	 */
	@RequestMapping("/showList")
	public String showList(Model model){
		List<Employee>employeeList = employeeService.showList();
		model.addAttribute("employeeList",employeeList);
		return "employee/list";
	}
	
	@ModelAttribute
	public UpdateEmployeeForm setUpUpdateEmployeeForm() {
		UpdateEmployeeForm updateEmployeeForm = new UpdateEmployeeForm();
		return updateEmployeeForm;
	}
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model){
		//リクエストパラメータで送られてくる従業員 ID を引数に(int型に変換してから)渡し、
		int i = Integer.parseInt(id);
		//employeeService の showDetail()メソッドを呼ぶ。
		Employee employee = employeeService.showDetail(i);
		model.addAttribute("employee",employee);
		return "employee/detail";	
	}
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult bindingResult) {
		//送られてきたリクエストパラメータの id を使用して
		//EmployeeドメインをshowDetail()メソッドで主キー検索する
		int id = Integer.parseInt(form.getId());
		Employee employee = employeeService.showDetail(id);
		
		//バリデーションエラー発生時には上書きしない
		if(bindingResult.hasErrors()){
			return "redirect:/employee/showList";
		}
		//送られてきたリクエストパラメータを、検索してきた Employee ドメインにセットし上書きする
		
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
		employee.setName(form.getName());
		employee.setGender(form.getGender());
		employee.setMailAddress(form.getMailAddress());
		employee.setZipCode(form.getZipCode());
		employee.setAddress(form.getAddress());
		employee.setTelephone(form.getTelephone());
		employee.setSalary(form.getSalary());
		employee.setCharacteristics(form.getCharacteristics());
		employee.setHireDate(form.getHireDate());
		//date型確認用
		System.out.println(form.getHireDate());

		//employeeService の update()メソッドを呼ぶ
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}
}
