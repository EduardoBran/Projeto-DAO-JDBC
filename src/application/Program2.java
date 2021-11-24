package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {		
		
		Scanner sc =  new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("========== TEST 1: department findById ==========");
		System.out.println();
		
		Department dep = departmentDao.findById(2);
		System.out.println(dep);
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 2: seller findByDepartment ==========");
		System.out.println();
		
		List<Department> list = departmentDao.findAll();
		for (Department obj : list) {
			System.out.println(obj);
		}
		System.out.println(); System.out.println();
		
//		System.out.println("========== TEST 3: department insert ==========");
//		System.out.println();
//		
//		Department newDep = new Department(null, "Music");
//		departmentDao.insert(newDep);
//		System.out.println("Inserted! New id = " + newDep.getId());
//		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 4: department update ==========");
		System.out.println();
		
		dep = departmentDao.findById(6);
		dep.setName("Toys");
		departmentDao.update(dep);
		System.out.println("Update Complete!");
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST : seller delete ==========");
		System.out.println();
		
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		
		departmentDao.deleteById(id);
		System.out.println("Delete completed!");
		
		sc.close();			
	}
}
