package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {		
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); //criado para caso o programa precisa fazer uma manutenção, seja feita direto no createSellerDao()
		
		Seller seller = sellerDao.findById(3); //3 - Id do vendedor
		
		System.out.println(seller);
	}
}
