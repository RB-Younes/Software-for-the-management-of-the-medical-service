import java.sql.*;

import javax.swing.JOptionPane;  

public class ConnexionDB 
{
	Connection  conn =null;


		
			
	public static Connection CnxDB() {
		// TODO Auto-generated method stub	
		try 
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");  
				//on a pas trouver un serveur gratuit pour host notre BDD oracle 
				//step2 create  the connection object                         
				Connection conn=DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:GestionM","system","123");  
				return conn;												//----^		----^	------^	 ---^    ----^
																		// le Host //le port  //SID   //Nom   //le mot de passe
			}																				//de la bbd	 //d'utilisateur			
			catch(Exception e)
				{ 
					JOptionPane.showMessageDialog(null, e);
					return null; 
				}  
			 
		
	}  



}  
	