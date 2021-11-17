package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //classe auxiliar respons�vel por instanciar meus DAOs

	public static SellerDao createSellerDao() {
		
		return new SellerDaoJDBC();
	}
	
}
