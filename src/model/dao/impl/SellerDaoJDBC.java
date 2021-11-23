package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	//Injeção de dependência com a conexão
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) { //forçando injeção de depêndencia
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(  
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS //RETORNA o Id do novo vendedor inserido
					);			
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime())); //instanciando uma Data do SQL
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); //a partir de department colocar o get id
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if(rs.next()) { //if pq está inserindo um dado
					
					int id = rs.getInt(1); //pegando o valor do Id gerado(posição 1 pq é a primeira coluna das chaves (id do novo vendedor))
					obj.setId(id); //atribuido o Id gerado dentro do objeto obj para que ja fique populado.
				}
				DB.closeResultSet(rs);
			}
			else {//caso tenha ocorrido a inserção mas nenhuma linha foi alterada
				throw new DbException("Unexpected error! No rows affected");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st); 
			//não fecha a conexão pois vamos usar em outras funçoes dentro desta Classe
		}		
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(  
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?"		
					);			
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime())); 
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId()); //id do vendedor
			
			st.executeUpdate();			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
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
			
			if (rs.next()) { //testa para ver se tem o ID escolhido retorna algum vendedor
				
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
			//não fecha a conexão pois vamos usar em outras funçoes dentro desta Classe
		}		
	}	

	private Department instantiateDepartment(ResultSet rs) throws SQLException { //throws SQLException - delegando a exceção
		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

		Seller obj = new Seller();
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
			Map<Integer, Department> map = new HashMap<>(); 
			
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
			
			List<Seller> list = new ArrayList<>(); //Criada a lista pq são vários valores e tb pq o método retorna uma lista
			Map<Integer, Department> map = new HashMap<>(); //foi criado um Map vazio cujo a chave é Integer(id do departamento) e valor de cada objeto vai ser do tipo Department.
			
			while (rs.next()) { 	 //percorrer o resultset enquanto tiver um proximo.			
				
				Department dep = map.get(rs.getInt("DepartmentId")); //Teste para ver se o Department já existe, atraves do map.get eu tento buscar em Department um departamento DepartmentId. Se nao existir DepartmentId ele retorna null
				
				if(dep == null) { //testando se o departamento ja existe, se for nulo eu vou instanciar um novo departamento e salvar dentro do Map
					
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); //salvando o departamento dentro do map para que não o instancie novamente
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
			//não fecha a conexão pois vamos usar em outras funçoes dentro desta Classe
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

WHERE seller.Id = ? - restrição onde o id do vendedor
 seja igual a ?(valor que eu quiser)
*/