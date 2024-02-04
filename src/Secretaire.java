import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

////////////////////////////////////////////////////////////////////////////////-----------Class Secretaire------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Secretaire extends Personne {
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private String User_Name;//Nom d'utilisateur
	private String Password;//mot de passe

	
    //constructeur
	public Secretaire(String user_name,String password,String nom, String prenom, String dateN, String sex, String adr,String adrmail, String num ) {
		super(nom, prenom, dateN, sex, adr, adrmail,num);
		User_Name=user_name;
		Password=password;
		
	}
	 //constructeur vide
	public Secretaire() {
		
	}

	
	public void Ajouter(Secretaire sec) {//ajouter une secretaire
		// etablire la connection:
				cnx = ConnexionDB.CnxDB();
				//commande sql pour insererla secretaire
				String sql = "insert into Secretaire values (ID_Sec.nextval,'"+sec.User_Name+"','"+sec.Password+"','"+sec.getNom()+"','"+sec.getPrenom()+"','"+sec.getDateN()+"','"+sec.getSex()+"','"+sec.getADR()+"','"+sec.getADRmail()+"','"+sec.getNum()+"') ";
				try {
					prepared=cnx.prepareStatement(sql);
					prepared.executeQuery();//executer la commande
					//afficher message
					JOptionPane.showMessageDialog(null, "Medecin ajouté avec succès!" ,"Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "this User Name is Already taken (pls try another one :D)!\n  Please write the birth date  correctly!");
				}

	}


	public void Suprimer(String ID) {//supprimer une secretaire
		cnx = ConnexionDB.CnxDB();
		String sql ="delete from Secretaire where id_Sec="+ID;//commande sql
		
		try {
			
			prepared=cnx.prepareStatement(sql);	
			prepared.execute();//executer la commande
			//message
			JOptionPane.showMessageDialog(null, "Secretaire supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	
		
	}

	
	public void MetreAjour(String id,JTextField usernameF,JTextField passF,JTextField nomF,JTextField PreF,String DNF,String sexe,JTextArea AdrA,String Adrmail,JTextField NumTF) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		//la commande sql
		String sql = "Update Secretaire set Ueser_Name =?,password  =?, Nom =?,Prenom =?,DateN =? ,sex =?,ADR =?,ADRmail =?,Numtel =? where ID_sec="+id+"";
		try {
			
			prepared=cnx.prepareStatement(sql);
			//remplacer les ? 
			prepared.setString(1, usernameF.getText().toString());
			prepared.setString(2, passF.getText().toString());
			prepared.setString(3, nomF.getText().toString());
			prepared.setString(4, PreF.getText().toString());
			prepared.setString(5, DNF);
			prepared.setString(6, sexe);
			prepared.setString(7, AdrA.getText().toString());
			prepared.setString(8, Adrmail);
			prepared.setString(9, NumTF.getText().toString());

			prepared.execute();//executer la cimmande
			//message
			JOptionPane.showMessageDialog(null, "Secretaire  Modifié avec succès!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}

}

}
