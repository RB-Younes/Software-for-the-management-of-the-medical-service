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

////////////////////////////////////////////////////////////////////////////////-----------Class Medicament------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Medicament {
	
	// les composants de ma connection connection a la BD:
		Connection cnx=null;
		PreparedStatement prepared = null;
		ResultSet resultat =null; 
	
	
	private String NomMedoc;//Nom du medicament
	private String Forme;//Forme du medicament
	private String Famille;//Famille du medicament
	private String obs;//Observation
	
	//constucteur 
	public Medicament(String nomMedoc, String forme, String famille, String obs) {

		NomMedoc = nomMedoc;
		Forme = forme;
		Famille = famille;
		this.obs = obs;
	}
	
	//constucteur Vide
	public Medicament() {

	}


	public void Ajouter(Medicament M) {//Ajouter le Medicament
		// etablire la connection:
				cnx = ConnexionDB.CnxDB();
				String sql = "insert into Medoc values (ID_Medoc.nextval,'"+M.NomMedoc+"','"+ M.Famille+ "','"+M.Forme+"','"+M.obs+"') ";
				try {
					prepared=cnx.prepareStatement(sql);
					prepared.executeQuery();//executer la commande
					//Message
					JOptionPane.showMessageDialog(null, "Médicaments ajouté avec succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e);
				}

	}
	
	public void Suprimer(String ID) {
		cnx = ConnexionDB.CnxDB();
		String sql ="delete from Medoc where ID_Medoc="+ID;
		
		try {
			
			prepared=cnx.prepareStatement(sql);	
			prepared.execute();//execution de la commande
			//message
			JOptionPane.showMessageDialog(null, " Médicaments supprimé avec succés!","Suppretion" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}	
	}
	
	public void MetreAjour(String Nom,String Famille,String Forme,String Obs,String ID_Medoc) {
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		// modifier les info du Medicament
		String sql = "Update Medoc set Nom =?,Famille  =?,Forme =?,Obs =? where ID_Medoc="+ID_Medoc+"";
		try {
			
			prepared=cnx.prepareStatement(sql);
			
			prepared.setString(1, Nom);		
			prepared.setString(2, Famille);
			prepared.setString(3, Forme);
			prepared.setString(4, Obs);
			
			
			prepared.execute();
			//message
			JOptionPane.showMessageDialog(null, "Médicaments modifié avec succés!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	public void Imprimer(Document doc) {//imprimer la liste de tout les Medicaments
		// etablire la connection:
		cnx = ConnexionDB.CnxDB();
		
		String sql = "Select * from  Medoc";//selectionner tout les Medicaments
		try {
			prepared = cnx.prepareStatement(sql);
			resultat=prepared.executeQuery();
				PdfWriter.getInstance(doc,new FileOutputStream(new File("PDF's\\Liste Medicaments.pdf")));//creer le pdf
				doc.open();
				Image IMG = Image.getInstance(GestionUsersFrame.class.getResource("/PDF_img/Document  TOP.png"));	// le top de la feuille
				IMG.scaleAbsoluteHeight(95);
				IMG.scaleAbsoluteWidth(600);
				IMG.setAlignment(Image.ALIGN_CENTER);
				doc.add(IMG);
				
				doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
				doc.add(new Paragraph(" La liste des tous les Médicaments:") );
				doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
				
			
				
				PdfPTable Table =new PdfPTable(5);//9 et le nombre de colone
				Table.setWidthPercentage(110);//la taille de la table
				
				PdfPCell c;//cellule
				//les cellules de ma table 
				c= new PdfPCell(new Phrase("N°",FontFactory.getFont("Arial", 12)));//titre
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.GREEN);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Nom Medicament",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.RED);
				//new Color(153, 204, 255)
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Classe",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.RED);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Forme",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.YELLOW);
				Table.addCell(c);
				
				c= new PdfPCell(new Phrase("Observation",FontFactory.getFont("Arial", 12)));
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBackgroundColor(BaseColor.YELLOW);
				Table.addCell(c);
				
				
				//Remplissage ligne par ligne------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
					while(resultat.next())
					{
						if (resultat.getString("ID_Medoc")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));// le cas ou il est a null on met "-"
						}
						else {
								c= new PdfPCell(new Phrase(resultat.getString("ID_Medoc").toString(),FontFactory.getFont("Arial", 12)));//creation de la cellule
						}
					
						c.setHorizontalAlignment(Element.ALIGN_CENTER);//alignement
						c.setBackgroundColor(BaseColor.GREEN);//colorer
						Table.addCell(c);//ajouter a la table
						
						if (resultat.getString("Nom")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Nom").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						
						if (resultat.getString("Famille")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Famille").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						if (resultat.getString("Forme")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("Forme").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.YELLOW);
						Table.addCell(c);
						
						if (resultat.getString("obs")==null) {
							c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							
						} else {
							c= new PdfPCell(new Phrase(resultat.getString("obs").toString(),FontFactory.getFont("Arial", 12)));
						}
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.YELLOW);
						Table.addCell(c);
	
						
					}
					doc.add(Table);//ajouter la table 
					

				
				doc.close();//fermer le doc
				Desktop.getDesktop( ).open(new File("PDF's\\Liste Medicaments.pdf"));//ouverture du pdf a la fin 
				
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
