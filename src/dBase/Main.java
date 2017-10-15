package dBase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
   static Login login=new Login();
   static MainFrame mainframe=new MainFrame();
   static ManageDB mb;
   static String url = "jdbc:sqlite:SQLite/MYDB.sqlite";
	public static void main(String[]args){
		ApplicationContext apc=new ClassPathXmlApplicationContext("DBContext.xml");
		mb=(ManageDB)apc.getBean("main");
   
	}
}