import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

////////////////////////////////////////////////////////////////////////////////-----------Class Consultation------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Consultation {
	
	// les composants de ma connection connection a la BD:
		Connection cnx=null;
		PreparedStatement prepared = null;
		ResultSet resultat =null; 
	
		
	private String Datecons;//date de la consultation
	private int ID_Pat;//iD du patient
	private String Num_RDV;//numero du rdv (id_RDV)
	private String Nom_Pre_Pat;//Nom prenom Patient
	private String GRP_SNG;//groupe sanguin
	private String taille;//taille
	private String poids;//poids
	private String tension;//tension
	private String Diab_O_N;//Diabetique oui ou non
	private String T_Diab;//Taux de diabette
	private String Diag;//diagnostique
	private String M_Diag;//maladie diagnostiqué
	private String Obs;//observation
	private String Bilan_O_N;//Bilan oui ou non?(es ce que le medecin a demandé un bilan)
	private String MP;//montant
	
	//constructeur
	public Consultation(String datecons, int iD_Pat, String num_RDV, String nom_Pre_Pat, String gRP_SNG, String taille,
			String poids, String tension, String diab_O_N, String t_Diab, String diag, String m_Diag, String obs,
			String bilan_O_N, String mP) {
		super();
		Datecons = datecons;
		ID_Pat = iD_Pat;
		Num_RDV = num_RDV;
		Nom_Pre_Pat = nom_Pre_Pat;
		GRP_SNG = gRP_SNG;
		this.taille = taille;
		this.poids = poids;
		this.tension = tension;
		Diab_O_N = diab_O_N;
		T_Diab = t_Diab;
		Diag = diag;
		M_Diag = m_Diag;
		Obs = obs;
		Bilan_O_N = bilan_O_N;
		MP = mP;
	}

	//constructeur
	public Consultation() {
	
	}

	
	public void Ajouter(Consultation cns) {//ajouter uen consultation
		// etablire la connection: 
				cnx = ConnexionDB.CnxDB();
				String GRP = "" ;
				String sql="SELECT GRP_SNG from Consult where ID_Pat='"+cns.ID_Pat+"'";//selectionner le groupe sanguin du patient des consultations precedantes
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					
					if(resultat.next())
					    { 
						GRP = resultat.getString(1);//recuperer le groupe sanguin
					    }
					  
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}
				
				if (GRP.equals(cns.GRP_SNG) || GRP.equals("")) {//dans le cas ou c'est le meme groupe sanguin ou c'est la premiere consultation-----------------> inserer normalement
					int nextID_from_seq=0;               //le id qu' on va recuperer a l aide du sequenceur que on a creé
				String jours = cns.Datecons.substring(0,2 );//jours
				String mois = cns.Datecons.substring(3,5 );//mois
				String annee = cns.Datecons.substring(6,10 );//annee
				
				 sql="SELECT Num_SEQ_Consult.nextval S FROM dual";//recuperer le numero sequenciel de la bdd (avec l'aide du sequenceur que ona creé)
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
					  
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}
				
				sql = "insert into Consult values ('"+nextID_from_seq+jours+mois+annee+"','"+cns.Datecons+"','"+cns.ID_Pat+"','"+cns.Num_RDV+"','"+cns.Nom_Pre_Pat+"','"+cns.GRP_SNG+"','"+cns.taille+"','"+cns.poids+"','"+cns.tension+"','"+cns.Diab_O_N+"','"+cns.T_Diab+"','"+cns.Diag+"','"+cns.M_Diag+"','"+cns.Obs+"','"+cns.MP+"','"+cns.Bilan_O_N+"') ";                                   
				//inserer dans la bdd avec comme id -----------^(la valeur du sequenceur(creeer dans la bbd) +jours+mois+annee),aprs les informations de la consultation
				try {
					prepared=cnx.prepareStatement(sql);
					prepared.executeQuery();//executer la commande
					//message
					JOptionPane.showMessageDialog(null, "Consultation Médicale ajoutée avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}
				}
				else {//le groupe sanguin est different
					//afficher un message de confirmation (en disant que ca va modifier le groupe de tout les autres groupe sanguin pour garder le meme groupe)
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Verifiez le groupe sanguin !\n"
							+ "Vouler Vous vraiment ajouter cette consultation ? \n(!Le Goupe Sanguin est differnt de celui enegistré dans les autres lignes sela causera la modification de ce dernier pour toutes les autres lignes!)", "Confirmation", 
							JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					if(ClickedButton==JOptionPane.YES_OPTION)
					{	
						int nextID_from_seq=0;               //le id qu' on va recuperer a l'aide du sequenceur que ona creé
						String jours = cns.Datecons.substring(0,2 );
						String mois = cns.Datecons.substring(3,5 );
						String annee = cns.Datecons.substring(6,10 );
						
						 sql="SELECT Num_SEQ_Consult.nextval S FROM dual";//recuperer le numero sequenciel de la bdd (avec l'aide du sequenceur que ona creé)
						try {
							prepared=cnx.prepareStatement(sql);
							resultat=prepared.executeQuery();
							if(resultat.next())
							    {nextID_from_seq = resultat.getInt(1);}//le numero sequenciel
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
						}
						//commande de l'insertion
						sql = "insert into Consult values ('"+nextID_from_seq+jours+mois+annee+"','"+cns.Datecons+"','"+cns.ID_Pat+"','"+cns.Num_RDV+"','"+cns.Nom_Pre_Pat+"','"+cns.GRP_SNG+"','"+cns.taille+"','"+cns.poids+"','"+cns.tension+"','"+cns.Diab_O_N+"','"+cns.T_Diab+"','"+cns.Diag+"','"+cns.M_Diag+"','"+cns.Obs+"','"+cns.MP+"','"+cns.Bilan_O_N+"') ";  
						try {
							prepared=cnx.prepareStatement(sql);
							prepared.executeQuery();
							
							// update le groupe sanguin pour les autres lignes pour garder le meme groupe sanguin 
							sql = "Update Consult set  GRP_SNG =? where ID_Pat="+ID_Pat+"";
							prepared=cnx.prepareStatement(sql);
							prepared.setString(1, GRP_SNG);
							prepared.execute();//executer la commande
							//message
							JOptionPane.showMessageDialog(null, "Consultation Médicale ajoutée avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
						}
					}
					
				}
				

	}
	
	public void Supprimer(String ID) {//supprimer une consultation
		cnx = ConnexionDB.CnxDB();
		String  sql ="Select NUM_RDV from Consult where ID_Cons="+ID;//selectionner le numero du rdv de la consultation car le bilan et relier avec 
		try {
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if (resultat.next()) {
				  sql ="delete from Bilan where NUM_RDV="+resultat.getString("NUM_RDV");//supprimer les bilans
				  prepared=cnx.prepareStatement(sql);
				  prepared.executeQuery();//executer la commande
			}
			sql ="delete from paiement where ID_Cons="+ID;
			prepared=cnx.prepareStatement(sql);	
			prepared.execute();//executer la commande
			sql ="delete from Consult where ID_Cons="+ID;//supprimer la consultation
			prepared=cnx.prepareStatement(sql);	
			prepared.execute();//executer la commande
			//message
			JOptionPane.showMessageDialog(null, "Consultation Médicale supprimé avec succés!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}	
	}
	
	
	public void MetreAjour(String ID_Cons,int ID_Pat,String NUM_RDV,String Patient_Name,String GRP_SNG,String taille,String poids,String tension,String Diab_O_N,String T_Diab,String Diag,String M_Diag,String Obs,String MP,String Bilan_O_N) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		//modifier une cnsultation
		String sql = "Update Consult set ID_Pat =?,Patient_Name  =?, GRP_SNG =?,taille =?,poids =?,tension=?,Diab_O_N =?,T_Diab =?,Diag =?,M_Diag =?,Obs =? ,MP =?,Bilan_O_N =? ,NUM_RDV=? where ID_Cons="+ID_Cons+"";
		try {
			
			prepared=cnx.prepareStatement(sql);
			
			prepared.setInt(1, ID_Pat);	
			prepared.setString(2, Patient_Name);
			prepared.setString(3, GRP_SNG);	
			prepared.setString(4, taille);
			prepared.setString(5, poids);
			prepared.setString(6, tension);
			prepared.setString(7,Diab_O_N);
			prepared.setString(8, T_Diab);
			prepared.setString(9, Diag);
			prepared.setString(10, M_Diag);
			prepared.setString(11, Obs);
			prepared.setString(12, MP);
			prepared.setString(13, Bilan_O_N);
			prepared.setString(14, NUM_RDV);
			
			prepared.execute();
			//pareil pour metre a jour le groupe sanguin il suffit de modiffier q'une seule ligne (et les autres lignes aurant le meme groupe sanguin) 
			sql = "Update Consult set  GRP_SNG =? where ID_Pat="+ID_Pat+"";
			prepared=cnx.prepareStatement(sql);
			prepared.setString(1, GRP_SNG);
			prepared.execute();//executer la commande
			//message
			JOptionPane.showMessageDialog(null, "Consultation Médicale modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	
	
	
}
