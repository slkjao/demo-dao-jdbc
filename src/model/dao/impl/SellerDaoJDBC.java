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

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller sel) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) values (?, ?, ?, ? ,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, sel.getName());
			st.setString(2, sel.getEmail());
			st.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
			st.setDouble(4, sel.getBaseSalary());
			st.setInt(5, sel.getDepartment().getId());
			
			int rows = st.executeUpdate();
			if(rows > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					sel.setId(id);
				}
				DB.closeResult(rs);
			} else {
				throw new DbException("Erro inesperado! 0 linhas afetadas!");
			}
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller sel) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update seller set Name=?, Email=?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? where id = ?",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, sel.getName());
			st.setString(2, sel.getEmail());
			st.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
			st.setDouble(4, sel.getBaseSalary());
			st.setInt(5, sel.getDepartment().getId());
			st.setInt(6, sel.getId());
			
			st.executeUpdate();
			
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from seller where id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department on seller.DepartmentId = department.Id "
					+ "where seller.id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instanciaDepartment(rs);
				Seller sel = instanciaSeller(rs, dep);
				return sel;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResult(rs);
		}

	}

	private Seller instanciaSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setEmail(rs.getString("Email"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setDepartment(dep);
		return sel;
	}

	private Department instanciaDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department on seller.DepartmentId = department.Id" + " order by name");

			rs = st.executeQuery();

			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instanciaDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller sel = instanciaSeller(rs, dep);
				lista.add(sel);
			}

			return lista;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResult(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department on seller.DepartmentId = department.Id "
					+ "where DepartmentId = ? order by name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> lista = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instanciaDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller sel = instanciaSeller(rs, dep);
				lista.add(sel);
			}

			return lista;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResult(rs);
		}
	}

}
