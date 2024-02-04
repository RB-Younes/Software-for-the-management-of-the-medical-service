import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

////////////////////////////////////////////////////////////////////////////////-----------Class paiement------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Paiment {
	
	// les composants de ma connection connection a la BD:
		Connection cnx=null;
		PreparedStatement prepared = null;
		ResultSet resultat =null; 
		PreparedStatement prepared1 = null;
		ResultSet resultat1 =null; 
		PreparedStatement prepared2 = null;
		ResultSet resultat2 =null; 
		
	
	
	private String DateP;//date du paiement 
	private String DateCons;//Date de la consultation
	private String ID_Cons;//ID consultation
	private String ID_pat;//ID patient
	private String prix_a_payer;//prix a payer (le prix de la consultation)
	private String prix_payé;//(pri payé par le patient)
	private String etat;//etat du paiement 
	private String Commentaire;//Commentaire
	
	//Constructeur
	public Paiment(String dateP, String dateCons, String iD_Cons, String iD_pat, String prix_a_payer,
			String prix_payé, String etat, String commentaire) {
		super();
		DateP = dateP;
		DateCons = dateCons;
		ID_Cons = iD_Cons;
		ID_pat = iD_pat;
		this.prix_a_payer = prix_a_payer;
		this.prix_payé = prix_payé;
		this.etat = etat;
		Commentaire = commentaire;
	}

	
	//constructeur vide
	public Paiment() {
	}



	public void Ajouter(Paiment P) {	
				//Ajouter le paiement
				// etablire la connection:
				cnx = ConnexionDB.CnxDB();
				//verifier si cette consultation a deja un paiement
				String sql="Select * from paiement where ID_Cons='"+P.ID_Cons+"'";
				try {
				prepared1=cnx.prepareStatement(sql);
				resultat1=prepared1.executeQuery();
				if (resultat1.next()) {
					JOptionPane.showMessageDialog(null, "Cette consultation a deja un paiment dans la table veuillez le modifier!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else {
				 sql = "insert into paiement values (Num_Paiement.nextval ,'"+P.ID_Cons+"','"+  P.ID_pat+ "','"+P.DateP+"','"+P.DateCons+ "','"+P.Commentaire+"','"+P.prix_a_payer+"','"+P.prix_payé+"','"+P.etat+"')";
				
					prepared=cnx.prepareStatement(sql);
					prepared.executeQuery();//executer la commande
					//messagea
					JOptionPane.showMessageDialog(null, "Paiement  ajouté avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}

	}
	

	public void Suprimer(String ID) {
		cnx = ConnexionDB.CnxDB();
		String sql ="delete from paiement where Num_Paiement="+ID;//supprimer la ligne ou ce trouve l'id du paiment
		try {
			prepared=cnx.prepareStatement(sql);	
			prepared.execute();//executer
			//message
			JOptionPane.showMessageDialog(null, "Paiement supprimé avec succés!","Suppression" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}	
	}
	
	public void MetreAjour(String Commentaire,String prix_payé,String etat,String Num_Paiement) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		// modifier les info du paiement
		String sql = "Update paiement set Commentaire  =?,prix_payé =?,etat =? where Num_Paiement="+Num_Paiement+"";
		try {
			
			prepared=cnx.prepareStatement(sql);
			// remplire les ? avec ces infos
			prepared.setString(1, Commentaire);		
			prepared.setString(2, prix_payé);	
			prepared.setString(3, etat);	
			//executer
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Paiement modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public void Imprimer(String ID_Pat,String Num_Paiement) {//imprimer la liste de tout les patients
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		Document doc =new Document(PageSize.A7, 25, 25, 10, 10);
			Date actuelle = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String datactuelle = dateFormat.format(actuelle);
			
			String sql = "Select Name , FirstName  from Patient where ID_Patient ='"+ID_Pat +"'";//selectionner les infos du patient la consultation
			
			String sql2 = "Select prix_a_payer,prix_payé,ID_Cons  from paiement where Num_Paiement ='"+Num_Paiement +"'";//selectionner les infos de la consultation
				try {
					prepared = cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();//resultat de la commande sql
					
					prepared2 = cnx.prepareStatement(sql2);
					resultat2=prepared2.executeQuery();//resultat de la commande sql2
					
					
			if (resultat.next() && resultat2.next()) 	{
				PdfWriter.getInstance(doc,new FileOutputStream(new File("PDF's\\Ticket.pdf")));//creer un fichier pdf
				doc.open();
				//Date
				Paragraph Date = new Paragraph();
				Date.add(new Chunk("                                            Date:"+datactuelle, new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));
				Date.setAlignment(Element.ALIGN_LEFT);
                doc.add(Date);
                //le top de la feuille
                Paragraph Head = new Paragraph();
                Head.add(new Chunk("#RE&BE logiciel de gestion de Clinique ", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                Head.setAlignment(Element.ALIGN_CENTER);
                doc.add(Head);
               doc.add(new Paragraph(" \n"));
               doc.add(new Paragraph(" \n"));
               doc.add(new Paragraph(" \n"));
                Paragraph separation = new Paragraph();
                separation.add(new Chunk("------------------------------------------------------------ ", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
                separation.setAlignment(Element.ALIGN_LEFT);
                doc.add(separation);
               
                //Nom du patient
                Paragraph Nom = new Paragraph();
                Nom.add(new Chunk("Nom: "+resultat.getString("Name"), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                Nom.setAlignment(Element.ALIGN_LEFT);
                doc.add(Nom);
                //Prenom du patient
                Paragraph Prenom = new Paragraph();
                Prenom.add(new Chunk("Prenom: "+resultat.getString("FirstName"), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                Prenom.setAlignment(Element.ALIGN_LEFT);
                doc.add(Prenom);
                
                Paragraph avantbon = new Paragraph();
                avantbon.add(new Chunk("Le rendu du paiement de la consultation de la date: ", new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL)));
                avantbon.setAlignment(Element.ALIGN_LEFT);
                doc.add(avantbon);
                String sql1 = "Select Date_consult  from Consult where ID_Cons ='"+resultat2.getString("ID_Cons") +"'";
              //la date de la consultation
				prepared1 = cnx.prepareStatement(sql1);
				resultat1=prepared1.executeQuery();
				if (resultat1.next()) {
					Paragraph bon = new Paragraph();
                bon.add(new Chunk(resultat1.getString("Date_consult").substring(0, 10), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                bon.setAlignment(Element.ALIGN_LEFT);
                doc.add(bon);
				}
                
                //prix a payer
                Paragraph prix = new Paragraph();
                prix.add(new Chunk("Le prix a payer: "+resultat2.getString("prix_a_payer")+"DA", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                prix.setAlignment(Element.ALIGN_LEFT);
                doc.add(prix);
                //prix payé
                Paragraph montant = new Paragraph();
                montant.add(new Chunk("Le montant que  vous avez payé : "+resultat2.getString("prix_payé")+"DA", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                montant.setAlignment(Element.ALIGN_LEFT);
                doc.add(montant);
                
                int R=Integer.parseInt(resultat2.getString("prix_a_payer"))-Integer.parseInt(resultat2.getString("prix_payé"));
                //le reste
                Paragraph reste = new Paragraph();
                reste.add(new Chunk("Le montant restant: "+String.valueOf(R)+"DA", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
                reste.setAlignment(Element.ALIGN_LEFT);
                doc.add(reste);
                //fin
                Paragraph separation2 = new Paragraph();
                separation2.add(new Chunk("------------------------------------------------------------ ", new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL)));
                separation2.setAlignment(Element.ALIGN_LEFT);
                doc.add(separation2);
                
                doc.close();
                
				Desktop.getDesktop( ).open(new File("PDF's\\Ticket.pdf"));//ouverture du pdf a la fin 
            
			}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}
				
				
		
	}
	
	
	
	
}
