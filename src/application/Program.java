package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Department book = new Department(1,"book");
		Seller seller = new Seller(2, "joao", "joao@gmail", new Date(), 2000.0, book);
		SellerDao sd = DaoFactory.createSellerDao();
		
		System.out.println("teste findById ---------:>");
		Seller s = sd.findById(3);
		System.out.println(s);
		
		System.out.println("\nteste findByDepartment ---------:>");
		Department deep = new Department(2, null);
		List<Seller> lista = sd.findByDepartment(deep);
		for (Seller seller2 : lista) {
			System.out.println(seller2.toString());
		}
		
		System.out.println("\nteste findAll ---------:>");
		lista = sd.findAll();
		for (Seller seller2 : lista) {
			System.out.println(seller2.toString());
		}
		
		
		System.out.println("\nteste Delete");
		sd.deleteById(12);
		
	}

}
