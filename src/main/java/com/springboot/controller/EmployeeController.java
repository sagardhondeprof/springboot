package com.springboot.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.springboot.entity.EmployeeEntity;
import com.springboot.entity.EmployeePersonalDetails;
import com.springboot.pojo.EmployeePOJO;
import com.springboot.service.EmployeePersonalDetailsService;
import com.springboot.service.EmployeeService;
import com.springboot.util.EmployeePdfExporterUtil;
import com.springboot.util.ExcelHelper;

@RestController
@RequestMapping()
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeePersonalDetailsService employeePersonalDetailsService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/addemployee")
	public ResponseEntity<?> addEmployee(@RequestBody EmployeePOJO employee) {
		return new ResponseEntity<>(employeeService.adduser(employee), HttpStatus.CREATED);
	}

	@GetMapping("/listemployee")
	public ResponseEntity<?> getDataList() {
		return new ResponseEntity<>(employeeService.getUsers(), HttpStatus.OK);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		employeeService.deleteById(id);
		return new ResponseEntity<>("Employee Deleted successfully", HttpStatus.ACCEPTED);

	}

	@PutMapping("update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody EmployeePOJO emp) {
		return new ResponseEntity<>(employeeService.updateEmployee(id, emp), HttpStatus.OK);
	}

	@GetMapping("pagablelist/{page}")
	public ResponseEntity<?> pagablelist(@PathVariable("page") int page) {
		Pageable of = PageRequest.of(page, 6);
		Page<EmployeePOJO> empPageList = employeeService.getpagelist(of);
		return new ResponseEntity<>(empPageList, HttpStatus.OK);

	}

	@GetMapping("search/{searchterm}")
	public ResponseEntity<?> searchQuery(@PathVariable("searchterm") String searchterm) {
		System.out.println(searchterm);
		List<EmployeeEntity> searchList = employeeService.searchEmployee(searchterm);
		if (searchList.size() > 0)
			return new ResponseEntity<>(searchList, HttpStatus.ACCEPTED);
		else {

			return new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("likesearch/{searchterm}")
	public ResponseEntity<?> searchLike(@PathVariable("searchterm") String searchTerm) {
		List<EmployeePOJO> searchList = employeeService.searchEmployeeLike(searchTerm);
		if (searchList.size() > 0) {
			return new ResponseEntity<>(searchList, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Employee Not Found", HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("exportpdf/{page}")
	public void exportToPDF(@PathVariable("page") int page, HttpServletResponse response)
			throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Employees_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		Pageable of = PageRequest.of(page, 6);
		Page<EmployeePOJO> empPageList = employeeService.getpagelist(of);
		List<EmployeePOJO> result = empPageList.getContent();
		EmployeePdfExporterUtil exporter = new EmployeePdfExporterUtil(result);
		exporter.export(response);

	}
	
	
	@GetMapping("searchbyid/{id}")
	public ResponseEntity<?> searchById(@PathVariable("id") long id) {
		EmployeeEntity searchList = employeeService.searchEmployeeById(id);
		return new ResponseEntity<>(searchList, HttpStatus.ACCEPTED);
	}
	
	
	@GetMapping("getallemployees")
	public ResponseEntity<?> getAllEmployees() {
		List<EmployeeEntity> empList = employeeService.getAll();
		return new ResponseEntity<>(empList, HttpStatus.ACCEPTED);
	}
	
	@PostMapping(value = {"basicdetails"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> employeeBasicDetails(@RequestPart("employeedata") String employee,
			@RequestPart("profile") MultipartFile file) {
		EmployeePersonalDetails emp = new EmployeePersonalDetails();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			emp= objectMapper.readValue(employee, EmployeePersonalDetails.class);
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			byte[] image = file.getBytes();
			
			emp.setProfile(image);
			System.out.println("gjhg "+emp);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return new ResponseEntity<>(employeePersonalDetailsService.addBBasicDetails(emp), HttpStatus.CREATED);
	}
	
	
	@GetMapping("getbasicdetails/{id}")
	public ResponseEntity<?> getBasicDetails(@PathVariable("id") long id){
		EmployeePersonalDetails emp = employeePersonalDetailsService.getBasicDetails(id);
		if(emp != null)
			return new ResponseEntity<>(emp, HttpStatus.OK);
		else
			return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/importemployees")
	public ResponseEntity<?> excelImportEmployees(@RequestParam("empExcel") MultipartFile empExcel) {
		
		if(ExcelHelper.checkExcelFormat(empExcel))
		{
			try {
				employeeService.excelImportEmployees(empExcel.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>("importeddd", HttpStatus.OK);
		}
		else return new ResponseEntity<>("Not an excel file", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		
	}
	
	
	
}
