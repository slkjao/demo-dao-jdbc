package application;

import java.util.ArrayList;
import java.util.List;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		Department dp = new Department(8, "Ecommerce");
		DepartmentDaoJDBC dpDao = new DepartmentDaoJDBC(DB.getCon());
		
		
		List<Department> lista = new ArrayList<>();
		lista = dpDao.findAll();
		for (Department department : lista) {
			System.out.println(department.toString());
		}
		
		

	}

}
