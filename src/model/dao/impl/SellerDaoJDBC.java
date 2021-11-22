package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller; 
 
public class SellerDaoJDBC implements SellerDao{  
	
	//Inje��o de depend�ncia com a conex�o
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) { //for�ando inje��o de dep�ndencia
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {

		
	}

	@Override
	public void update(Seller obj) {

		
	}

	@Override
	public void deleteById(Integer id) {

		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(                                //mostra no banco de dados uma tabela com as buscas definidas (Id,Name,Email,BirthDate,BaseSalary,DepartmentId,DepName)
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ? "					
					);
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			//quando estamos programando um sistema orientado a objetos, mesmo eu
			//buscando os dados na forma de tabela, na mem�rica do computador, precisamos
			//ter os objetos associados instanciados em mem�ria.
			
			if (rs.next()) { //testa para ver se veio algum resultado / se a consulta retorna algum registro
				
				Department dep = instantiateDepartment(rs);
				
				Seller obj = instantiateSeller(rs, dep);
				
				return obj;
				
			}
			return null; //retorna null pq nao existe nenhum vendedor com o Id escolhido (WHERER seller.Id = ?)
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
			//n�o fecha a conex�o pois vamos usar em outras fun�oes dentro desta Classe
		}		
	}	

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

		Seller obj = new Seller(); //criar obj Seller associando para departament
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); //departamento associado a este Seller
		
		return obj;
	}


	@Override
	public List<Seller> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(    
					"SELECT seller.*, department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name"					
					);

			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); //foi criado um Map vazio.
			
			while (rs.next()) { 			
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				
				list.add(obj);				
			}
			return list; 
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(    
					"SELECT seller.*, department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name"					
					);
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); //foi criado um Map vazio.
			
			while (rs.next()) { 	 //percorrer o resultset enquanto tiver um proximo.			
				
				Department dep = map.get(rs.getInt("DepartmentId")); //guardando dentro do map qualquer departamento q for instanciado
				
				if(dep == null) { //testando se o departamento ja existe, se for nulo eu vou instanciar um novo departamento e salvar dentro do Map
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs, dep);
				
				list.add(obj);				
			}
			return list; 
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs); 
			//n�o fecha a conex�o pois vamos usar em outras fun�oes dentro desta Classe
		}
	}
}

/*
SELECT seller.*, department.Name as DepName
FROM seller INNER JOIN department
ON seller.DepartmentId = department.Id
WHERE seller.Id = ?


SELECT seller.* - busca todos os campos do vendedor
department.Name - + o nome do Departamento 
as DepName      - apelido dado ao Departamento

FROM seller INNER JOIN department
ON seller.DepartmentId = department.Id - Busca os dados das duas tabelas

WHERE seller.Id = ? - restri��o onde o id do vendedor
 seja igual a ?(valor que eu quiser)
*/