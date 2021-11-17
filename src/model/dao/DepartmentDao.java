package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj); //responsável por inserir no BD este obj que eu enviar como parametro de entrada
	void update(Department obj);
	void deleteById(Integer id);
	
	Department findById(Integer id); //responsavel por pegar essa Id e consultar no banco de dados um obj com esse Id
	List<Department> findAll();	
}
