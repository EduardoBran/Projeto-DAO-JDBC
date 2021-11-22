package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj); //responsável por inserir no BD este obj que eu enviar como parametro de entrada
	void update(Seller obj);
	void deleteById(Integer id);
	
	Seller findById(Integer id); //responsavel por pegar essa Id e consultar no banco de dados um obj com esse Id
	List<Seller> findAll();	
	
	List<Seller> findByDepartment(Department department);  //Buscar por departamento
}
