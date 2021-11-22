package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //classe auxiliar responsável por instanciar meus DAOs

	public static SellerDao createSellerDao() {
		
		return new SellerDaoJDBC(DB.getConnection());
	}
	
}
// Classe responsável por fazer a injeção de dependência e se caso 
// mudar alguma coisa é nele que alteramos a implementação que ele retorna