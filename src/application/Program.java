package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {		
		
		Scanner sc =  new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); //criado para caso o programa precisa fazer uma manutenção, seja feita direto no createSellerDao()
		
		System.out.println("========== TEST 1: seller findById ==========");
		System.out.println();
		
		Seller seller = sellerDao.findById(3); //3 - Id do vendedor
		
		System.out.println(seller);
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 2: seller findByDepartment ==========");
		System.out.println();
		
		Department department = new Department(2, null); //2 id do departamento
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
		
			System.out.println(obj);
		}
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 3: seller findAll ==========");
		System.out.println();
		
		list = sellerDao.findAll(); //List<Seller>
		for (Seller obj : list) {
		
			System.out.println(obj);
		}
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 4: seller insert ==========");
		System.out.println();
		
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 5: seller update ==========");
		System.out.println();
		
		seller = sellerDao.findById(1); //procurando o vendedor de Id 1
		seller.setName("Martha Waine");
		seller.setEmail("marthaWaine@gmail.com");
		sellerDao.update(seller);
		System.out.println("Update completed!");
		System.out.println(); System.out.println();
		
		System.out.println("========== TEST 6: seller delete ==========");
		System.out.println();
		
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed!");	
		
		sc.close();		
	}
}
