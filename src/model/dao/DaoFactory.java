package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //classe auxiliar respons�vel por instanciar meus DAOs

	public static SellerDao createSellerDao() {
		
		return new SellerDaoJDBC(DB.getConnection());
	}
	
}
// Classe respons�vel por fazer a inje��o de depend�ncia e se caso 
// mudar alguma coisa � nele que alteramos a implementa��o que ele retorna