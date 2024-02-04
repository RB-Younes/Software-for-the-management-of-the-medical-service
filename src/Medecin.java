import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

////////////////////////////////////////////////////////////////////////////////-----------Class Medecin------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Medecin extends Personne {
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private String User_Name;//Nom d'utilisateurs
	private String Password;//mot de passe
	private String Spe;//specialité du medecin
	
    //constructeur
	public Medecin(String user_name,String password,String nom, String prenom, String dateN, String sex, String adr,String adrmail, String num ,String spe) {
		super(nom, prenom, dateN, sex, adr,adrmail ,num);
		User_Name=user_name;
		Password=password;
		setSpe(spe);

	}
	//constructeur vide
	public Medecin() {
		super();
	}

	
	

	public void Ajouter(Medecin med) {//ajoutr un medecin
		// etablire la connection:
				cnx = ConnexionDB.CnxDB();
				// la commande sql
				String sql = "insert into Medecin values (ID_Med.nextval,'"+med.User_Name+"','"+med.Password+"','"+med.getNom()+"','"+med.getPrenom()+"','"+med.getDateN()+"','"+med.getSex()+"','"+med.getADR()+"','"+med.getADRmail()+"','"+med.getNum()+"','"+med.getSpe()+"') ";
				try {
					prepared=cnx.prepareStatement(sql);
					prepared.executeQuery();//executer la commande
					//message
					JOptionPane.showMessageDialog(null, "Medecin ajouté avec succès!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Nom d'utilisateur déjà pris  (veuillez saisir un autre)!");
				}

	}


	public void Suprimer(String ID) {//supprimer un medecin
		cnx = ConnexionDB.CnxDB();
		//verifier si le medecin a des rtv lier a lui (poerte son id)
		String sql ="Select * from RDV where ID_Med="+ID;
		try {
			prepared=cnx.prepareStatement(sql);	
			resultat=prepared.executeQuery();
					if (resultat.next()) {
						JOptionPane.showMessageDialog(null, "la suppression de ce medecin causera la perte de plusieurs RDV et consultation (Suppression impossible!)","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else {
						sql ="delete from Medecin where id_Med="+ID;
						prepared=cnx.prepareStatement(sql);	
						prepared.execute();
						JOptionPane.showMessageDialog(null, "Medecin supprimé avec succès!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
					}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	
		
	}

	
	public void MetreAjour(String id,JTextField usernameF,JTextField passF,JTextField nomF,JTextField PreF,String DNF,String sexe,JTextArea AdrA,String Adrmail,JTextField NumTF,String spe ) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		//la commande sql
		String sql = "Update Medecin set Ueser_Name =?,password  =?, Nom =?,Prenom =?,DateN =? ,sex =?,ADR =?,ADRmail =?,Numtel =?,Specilité =? where ID_Med="+id+"";
		try {
			prepared=cnx.prepareStatement(sql);
			//remplasser les ?
			prepared.setString(1, usernameF.getText().toString());
			prepared.setString(2, passF.getText().toString());
			prepared.setString(3, nomF.getText().toString());
			prepared.setString(4, PreF.getText().toString());
			prepared.setString(5, DNF);
			prepared.setString(6, sexe);
			prepared.setString(7, AdrA.getText().toString());
			prepared.setString(8, Adrmail);
			prepared.setString(9, NumTF.getText().toString());
			prepared.setString(10, spe);
			
			prepared.execute();//executer la commande
			//il faut aussi changer la specialite du medecin sur les rdv
			sql = "Update RDV set SpE =? where ID_Med="+id+"";
			prepared=cnx.prepareStatement(sql);
			prepared.setString(1,spe);
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Medecin Modifié avec succès!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}

}
	


	public String getSpe() {
		return Spe;
	}


	public void setSpe(String spe) {
		Spe = spe;
	}

}
