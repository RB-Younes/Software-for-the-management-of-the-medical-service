import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

////////////////////////////////////////////////////////////////////////////////-----------Class Rendez-Vous------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class RDV {
	
	// les composants de ma connection connection a la BD:
		Connection cnx=null;
		PreparedStatement prepared = null;
		ResultSet resultat =null; 
		PreparedStatement prepared1 = null;
		ResultSet resultat1 =null; 
	
	
	private String DateRDV;//date du rdv
	private String HRDV;//Heure du rdv
	private int ID_Med;//ID du Medecin
	private int ID_Pat;//Id du patient
	private String Nom_Pre_Med;//Nom prenom Medecin
	private String Med_Spe;//la specialité du med
	private String Nom_Pre_Pat;//Nom prenom patient
	private String Commentaire;//Commentaire
	
	//constructeur
	public RDV(String dateRDV,String hrdv, int iD_Med, int iD_Pat, String nom_Pre_Med, String med_Spe, String nom_Pre_Pat,
			String commentaire) {
		
		DateRDV = dateRDV;
		HRDV=hrdv;
		ID_Med = iD_Med;
		ID_Pat = iD_Pat;
		Nom_Pre_Med = nom_Pre_Med;
		Med_Spe = med_Spe;
		Nom_Pre_Pat = nom_Pre_Pat;
		Commentaire = commentaire;
	}
	//constructeur vide
	public  RDV() {
		
	}



	public void Ajouter(RDV rdv) {//Ajouter le rdv
		// etablire la connection: 
				cnx = ConnexionDB.CnxDB();
				
				String 	sql = "Select * from RDV where Date_RDV='"+rdv.DateRDV+"'and ID_Pat='"+rdv.ID_Pat +"'";//verifier si le patient a deja rdv pour ce jour
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();//executer la commande
					if (resultat.next()) {//si oui
						//message d'erreur
						JOptionPane.showMessageDialog(null, "Ce patient a deja un rendez-vous ce jour la!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));				
					}
					else {//sinon
							sql = "insert into RDV values (NUM_RDV.nextval,'"+rdv.DateRDV+"','"+ rdv.HRDV+ "','"+rdv.ID_Med+"','"+rdv.Nom_Pre_Med+"','"+rdv.Med_Spe+"','"+rdv.ID_Pat+"','"+rdv.Nom_Pre_Pat+"','"+rdv.Commentaire+"') ";
								
								prepared=cnx.prepareStatement(sql);
								prepared.executeQuery();//executer la commande
								//message
								JOptionPane.showMessageDialog(null, "RDV ajouté avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}

	}
	
	public void Suprimer(String ID,String indice) {
		cnx = ConnexionDB.CnxDB();
		if (indice.equals("S")) {// indice S donc authentifier en tant que secretaire
			String sql ="Select * from Consult where NUM_RDV="+ID;//verifier si le RDV est liee a une consultation 
			try {
				prepared=cnx.prepareStatement(sql);
				resultat=prepared.executeQuery();
				if (resultat.next()) {
					JOptionPane.showMessageDialog(null, "la suppression de ce RDV causera la perte d'information tres imporatnate (Suppression impossible)!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else {//donc ce rdv n'est pas relieé a une consultation
						sql ="delete from RDV where NUM_RDV="+ID;//supprimer les RDV du patient vu quil na pas de consultation
						try {
							prepared=cnx.prepareStatement(sql);	
							prepared.execute();
							//message
							JOptionPane.showMessageDialog(null, "Rendez-Vous supprimé avec succés !","Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
							} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
							}
					}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e1);
			}	
		}
		else //en tant que medecin
		{
			 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png"));
			int ClickedButton	=JOptionPane.showConfirmDialog(null,  " Cela va causer la perte de plusieurs données de consultation !! oui pour continuer.", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
			if(ClickedButton==JOptionPane.YES_OPTION)
				{
				//d'abord il faut supprimer les fils de la consultation
			String sql ="delete from Bilan where NUM_RDV="+ID;//supprimer le Bilan
			try {
				prepared=cnx.prepareStatement(sql);
				prepared.executeQuery();
				
				 sql ="Select ID_cons from Consult where NUM_RDV="+ID;
				prepared1=cnx.prepareStatement(sql);	
				resultat1=prepared1.executeQuery();
				while (resultat1.next()) {
					sql ="delete from paiement where ID_Cons="+resultat1.getString("ID_Cons");//supprimer les paiements
					prepared=cnx.prepareStatement(sql);	
					prepared.execute();
				}
					
				sql ="delete from Consult where NUM_RDV="+ID;//supprimer les consultations
				prepared=cnx.prepareStatement(sql);	
				prepared.execute();
				
				sql ="delete from RDV where NUM_RDV="+ID;//supprimmer le rdv
				prepared=cnx.prepareStatement(sql);	
				prepared.execute();
				
			//message
				JOptionPane.showMessageDialog(null, "Rendez-Vous supprimé avec succés !", "Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				} catch (SQLException e1) {
			// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1);
				}	
			
				}
		}
	}
	
	public void MetreAjour(String Date_rdv,String H_rdv,int ID_Med,String Doctor_Name,String SpE,int ID_Pat,String Patient_Name,String Commentary,String NUM_RDV) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		//avant de modifier un rdv il faut d'abord verifier si ce rdv est reli a une consultation (ou plus)
		String sql = "Select * from Consult where NUM_RDV="+NUM_RDV+"";
		try {	
			prepared=cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
			if (resultat.next()) {
				//message d'erreur
				JOptionPane.showMessageDialog(null, "Ce RDV est lieé a une consultation Modification impossible!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
			}
			else {
				// modifier les info de la consultation
			sql = "Update RDV set Date_RDV =?,H_RDV  =?, ID_Med =?,Doctor_Name =?,SpE =?,ID_Pat=?,Patient_Name =?,Commentary =? where NUM_RDV="+NUM_RDV+"";
			
			prepared=cnx.prepareStatement(sql);
			// remplire les ? avec ces infos
			prepared.setString(1, Date_rdv);		
			prepared.setString(2, H_rdv);
			prepared.setInt(3, ID_Med);
			prepared.setString(4, Doctor_Name);
			prepared.setString(5, SpE);
			prepared.setInt(6, ID_Pat);
			prepared.setString(7,Patient_Name);
			prepared.setString(8, Commentary);
			
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "RDV modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	public void Imprimer(Document doc) {//imprimer la liste de tout les RDV
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		
		String sql = "Select * from  RDV";//selectionner tout les RDV
		try {
			prepared = cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
				PdfWriter.getInstance(doc, new FileOutputStream(new File("PDF's\\Liste des RDV.pdf")));//creer un fichier pdf
				doc.open();
				Image IMG = Image.getInstance(GestionUsersFrame.class.getResource("/PDF_img/Document  TOP.png"));	// le top de la feuille
				//IMG.scaleAbsoluteHeight(95);
				//IMG.scaleAbsoluteWidth(600);
				IMG.setAlignment(Image.ALIGN_CENTER);
				doc.add(IMG);
				
				doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
				doc.add(new Paragraph(" La liste des tous les RDV:") );
				doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
				
				PdfPTable Table =new PdfPTable(8);//8 et le nombre de colone
				Table.setWidthPercentage(110);
				
				PdfPCell c;//cellule
				//les cellules de ma table (la premiere ligne qui contient les titres)
				c= new PdfPCell(new Phrase("NUM_RDV",FontFactory.getFont("Arial", 12)));//titre
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.GREEN);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Doctor_Name",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.RED);
				//new Color(153, 204, 255)
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("SpE",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.RED);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("ID_Pat",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.YELLOW);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Patient_Name",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.YELLOW);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Date_RDV",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.GRAY);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("H_RDV",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.GRAY);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Commentary",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.CYAN);
				Table.addCell(c);
				
				
				
				//Remplissage ligne par ligne------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					while(resultat.next())
					{
						if (resultat.getString("NUM_RDV")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));// le cas ou il est a null on met "-"
						}
						else {
								c= new PdfPCell(new Phrase(resultat.getString("NUM_RDV").toString(),FontFactory.getFont("Arial", 12)));//creation de la cellule
						}
					
						c.setHorizontalAlignment(Element.ALIGN_CENTER);//alignement
						c.setBackgroundColor(BaseColor.GREEN);//colorer en rouge
						Table.addCell(c);//ajouter a la table
						
						if (resultat.getString("Doctor_Name")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Doctor_Name").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						
						if (resultat.getString("SpE")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("SpE").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						if (resultat.getString("ID_Pat")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("ID_Pat").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.YELLOW);
						Table.addCell(c);

						if (resultat.getString("Patient_Name")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Patient_Name").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.YELLOW);
						Table.addCell(c);
						
						if (resultat.getString("Date_RDV")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Date_RDV").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.GRAY);
						Table.addCell(c);
						
						if (resultat.getString("H_RDV")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("H_RDV").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.GRAY);
						Table.addCell(c);
						
						if (resultat.getString("Commentary")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
						}
						else {
								c= new PdfPCell(new Phrase(resultat.getString("Commentary").toString(),FontFactory.getFont("Arial", 12)));
						}
					
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table.addCell(c);
						

						
					}
					doc.add(Table);//ajouter la table 
					

				
				doc.close();//fermer le doc
				Desktop.getDesktop( ).open(new File("PDF's\\Liste des RDV.pdf"));//ouverture du pdf a la fin 
				
		} catch (FileNotFoundException | DocumentException  |SQLException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1);
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1);
		}		
		
	}
}
