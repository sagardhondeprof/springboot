package com.springboot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFRow.CellIterator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.springboot.entity.EmployeeEntity;
import com.springboot.pojo.EmployeePOJO;
import com.springboot.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepo;

	@Autowired
	private ModelMapper modelMapper;

	public Object getUsers() {
		return employeeRepo.findAll();
	}

	public Page<EmployeePOJO> getpagelist(Pageable pageable) {
		Page<EmployeeEntity> entitypage = employeeRepo.findAll(pageable);
		Page<EmployeePOJO> pojopage = pageMapTOpojo(entitypage);

		return pojopage;

	}

	public void deleteById(long id) {
		employeeRepo.deleteById(id);

	}

	public EmployeePOJO adduser(EmployeePOJO employeepojo) {

		String loggeduser = userName();
		EmployeeEntity employee = new EmployeeEntity(loggeduser, LocalDateTime.now(), loggeduser, LocalDateTime.now(),
				employeepojo.getFirstName(), employeepojo.getLastName(), employeepojo.getEmail(),
				employeepojo.getDate());
		EmployeeEntity employeentity = employeeRepo.save(employee);
		EmployeePOJO responseEntity = mapToPojo(employeentity);
		return responseEntity;
	}

	public EmployeePOJO updateEmployee(long id, EmployeePOJO emppojo) {
		EmployeeEntity employee = employeeRepo.findById(id).get();
		String loggedInUser = userName();
		if (employee != null) {
			employee.setFirstName(emppojo.getFirstName());
			employee.setLastName(emppojo.getLastName());
			employee.setEmail(emppojo.getEmail());
			employee.setDate(emppojo.getDate());
			employee.setLastUpdateDate(LocalDateTime.now());
			employee.setLastUpdatedBy(loggedInUser);
		}
		EmployeeEntity employeentity = employeeRepo.save(employee);
		EmployeePOJO responseEntity = mapToPojo(employeentity);
		return responseEntity;
	}

	private EmployeePOJO mapToPojo(EmployeeEntity user) {
		return modelMapper.map(user, EmployeePOJO.class);
	}

	private Page<EmployeePOJO> pageMapTOpojo(Page<EmployeeEntity> entitypage) {
		return entitypage.map(entityObject -> modelMapper.map(entityObject, EmployeePOJO.class));
	}

	public List<EmployeeEntity> searchEmployee(String searchterm) {
		List<EmployeeEntity> searchlist = employeeRepo.findByFirstName(searchterm);
		return searchlist;

	}

	public String userName() {
		return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}

	public List<EmployeePOJO> searchEmployeeLike(String query) {
		List<EmployeeEntity> list = employeeRepo.searchEmployee(query);
		List<EmployeePOJO> pojolist = listMapTOpojo(list);
		return pojolist;
	}

	private List<EmployeePOJO> listMapTOpojo(List<EmployeeEntity> entitypage) {
		List<EmployeePOJO> pojo = new ArrayList<>();
		entitypage.forEach(emppojoobj -> pojo.add(modelMapper.map(emppojoobj, EmployeePOJO.class)));
		return pojo;
	}

	public EmployeeEntity searchEmployeeById(long id) {
		EmployeeEntity emp = employeeRepo.findById(id).get();
		return emp;

	}

	public List<EmployeeEntity> getAll() {
		List<EmployeeEntity> empList = employeeRepo.findAll();
		return empList;

	}

	public void excelImportEmployees(InputStream excelFile) {

		long ID;
		String CREATED_BY = "sagar";
		LocalDateTime CREATION_DATE = LocalDateTime.now();
		LocalDateTime LAST_UPDATED_DATE = LocalDateTime.now();
		String LAST_UPDATED_BY = "Sagar";
		String FIRST_NAME = "";
		String LAST_NAME = "";
		String EMAIL = "";
		LocalDate DATE_OF_BIRTH = null;

		List<EmployeeEntity> employeeList = new ArrayList<EmployeeEntity>();
//		String excelFilePath = "C:\\Users\\Sagar Dhonde\\Desktop\\employeesBulkData.xlsx";
//
//		InputStream inputStream;
		try {
//			inputStream = new FileInputStream(excelFilePath);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet firstSheet = workbook.getSheetAt(0);
			java.util.Iterator<Row> rowIterator = firstSheet.iterator();
			rowIterator.next();

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						FIRST_NAME = nextCell.getStringCellValue();
						break;
					case 1:
						LAST_NAME = nextCell.getStringCellValue();
						break;
					case 2:
						EMAIL = nextCell.getStringCellValue();
						break;
					case 3:
						DATE_OF_BIRTH = nextCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
						break;
					case 4:
						ID = (long) nextCell.getNumericCellValue();
						break;

					}

				}
				employeeList.add(new EmployeeEntity(CREATED_BY, CREATION_DATE, LAST_UPDATED_BY, LAST_UPDATED_DATE,
						FIRST_NAME, LAST_NAME, EMAIL, DATE_OF_BIRTH));

			}

		} catch (Exception e) {
			System.out.println(e);
		}

		employeeRepo.saveAll(employeeList);

	}
}
