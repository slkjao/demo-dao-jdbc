package application;

import java.util.Date;

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
		
		
		System.out.println(seller.getName());
	}

}
