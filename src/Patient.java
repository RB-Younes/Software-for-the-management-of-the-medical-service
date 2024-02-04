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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

////////////////////////////////////////////////////////////////////////////////-----------Class Patient------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Patient extends Personne
{
	// les composants de ma connection connection a la BD:
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 
	
	
	 private String Prof;//profession
	 private String Dateeng;//la date de l'enregistrement 
	


	

	//constucteur 
		public Patient(String nom, String prenom, String dateN, String sex, String aDR, String aDRmail, String num,
			String prof, String dateeng) {
		super(nom, prenom, dateN, sex, aDR, aDRmail, num);
		Prof = prof;
		Dateeng = dateeng;
	}


		//constucteur vide
		public Patient() {}
	

		public void Ajouter (Patient P) {//Ajouter le patient
				// etablire la connection:
					cnx = ConnexionDB.CnxDB();
					String sql = "insert into Patient values (ID_Patient.nextval,'"+P.getNom()+"','"+P.getPrenom()+"','"+P.getDateN()+"','"+P.getSex()+"','"+P.getADR()+"','"+P.getADRmail()+"','"+P.getNum()+"','"+P.Prof+"','"+P.Dateeng+"') ";
					try {
						prepared=cnx.prepareStatement(sql);
						prepared.executeQuery();//executer la commande
						//Message
						JOptionPane.showMessageDialog(null, "Patient ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e);
					}

		}


		public void Suprimer(String ID,String indice) {//differencier entre le medecin et la secretaire le medecin a le droit de supprimer un patient meme si ca va causer la perte de tout ses informations  
				cnx = ConnexionDB.CnxDB();
			if (indice.equals("S")) {// indice S donc authentifier en tant que secretaire
				String sql ="Select * from Consult where ID_Pat="+ID;//verifier si le patient a des consultations
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if (resultat.next()) {
						JOptionPane.showMessageDialog(null, "la suppression de ce patient causera la perte d'information tres imporatnate!","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else {
							sql ="delete from RDV where ID_Pat="+ID;//supprimer les RDV du patient vu quil na pas de consultation
							try {
								prepared=cnx.prepareStatement(sql);	
								prepared.execute();
								sql ="delete from Patient where ID_Patient="+ID;// supprimer le patient
								prepared=cnx.prepareStatement(sql);	
								prepared.execute();
								//message
								JOptionPane.showMessageDialog(null, "Patient supprimé avec succés !","Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
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
			else//en tant que medecin
			{
				 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png"));
				int ClickedButton	=JOptionPane.showConfirmDialog(null,  " Cela peut causer la perte de plusieurs données !! oui pour continuer.", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String sql ="Select * from Consult where ID_Pat="+ID;//verifier si le patient a des consultations
				try {	
						prepared=cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
			if (resultat.next()) {
							//Si oui on verifie si 5 ans on passé du dernier RDV
							sql ="Select ID_Cons,Date_consult from Consult where ID_Pat="+ID;
							prepared=cnx.prepareStatement(sql);
							resultat=prepared.executeQuery();
							int ID_max=0;	 // vu que L'ID est un nombre sequenciel le Dernier RDV aura le plus grand ID (pour prendre la date du dernier RDV)
							String Date = null;
							while (resultat.next()) {
								if (Integer.parseInt(resultat.getString("ID_Cons"))>ID_max) {
									ID_max=Integer.parseInt(resultat.getString("ID_Cons"));
									Date=resultat.getString("Date_consult");//recuperer La date de  la consultation
								}
							}
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
							    Date firstDate = null;
							    Date secondDate = null;
								try {
									Date actuelle = new Date();
						 			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						 			String datactuelle = dateFormat.format(actuelle); 
									firstDate = sdf.parse(datactuelle); 
									secondDate = sdf.parse(Date.substring(0, 10));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null, e);
								}

							    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
							    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
							
							    if (diff>=1825) {// en calucle la difference (diff) en jours si elle est supperieur ou egale a 1825 dnoc 5 ans sont passe de la derniere consultartion
							    	 sql ="delete from paiement where ID_Pat="+ID;//supprimer le paiement
										
										prepared=cnx.prepareStatement(sql);
										prepared.executeQuery();
										
										sql ="Select NUM_RDV from Consult where ID_Pat="+ID;//avant de supprimer les consultations on doit supprimer les bilans
										prepared=cnx.prepareStatement(sql);	
										resultat=prepared.executeQuery();
										while (resultat.next()) {
											sql ="delete from Bilan where NUM_RDV="+resultat.getString("NUM_RDV");//supprimer les bilans
											prepared1=cnx.prepareStatement(sql);
											prepared1.executeQuery();
										}
										sql ="delete from Consult where ID_Pat="+ID;//supprimer les consultations
										prepared=cnx.prepareStatement(sql);	
										prepared.execute();
										
										sql ="delete from RDV where ID_Pat="+ID;//supprimmer les rdv
										prepared=cnx.prepareStatement(sql);	
										prepared.execute();
										
										sql ="delete from Patient where ID_Patient="+ID;//supprimer le patients
										prepared=cnx.prepareStatement(sql);	
										prepared.execute();
										//message
										JOptionPane.showMessageDialog(null, "Patient supprimé avec succés !", "Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
							    }
							    else {
							    	//sinon
							    	//message
							    	JOptionPane.showMessageDialog(null, "Vous ne pouvez pas supprimer ce patient car ca derniere consultation ne remonte pas a 5 ans!", "Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}
							
						}
			else {
				sql ="delete from RDV where ID_Pat="+ID;//supprimer les RDV du patient vu quil na pas de consultation
				try {
					prepared=cnx.prepareStatement(sql);	
					prepared.execute();
					sql ="delete from Patient where ID_Patient="+ID;// supprimer le patient
					prepared=cnx.prepareStatement(sql);	
					prepared.execute();
					//message
					JOptionPane.showMessageDialog(null, "Patient supprimé avec succés !","Suppression", JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
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
			}
		}


		public void MetreAjour(String id,JTextField NameF,JTextField FirstNameF,String BirthDateF,String sex,JTextArea AdrF,String Adrmail,JTextField PhoneNumF,JTextField ProfF) {
			// etablire la connection:
			cnx = ConnexionDB.CnxDB();
			// modifier les info du patient
			String sql = "Update Patient set Name =?,FirstName  =?, BirthDate =?,sex =?,ADR =?,ADRmail =?,PhoneNum =?,Prof=? where ID_Patient="+id+"";
			try {
				
				prepared=cnx.prepareStatement(sql);
				// remplire les ? avec ces infos
				prepared.setString(1, NameF.getText().toString());		
				prepared.setString(2, FirstNameF.getText().toString());
				prepared.setString(3, BirthDateF);
				prepared.setString(4, sex);
				prepared.setString(5, AdrF.getText().toString());
				prepared.setString(6, AdrF.getText().toString());
				prepared.setString(7, PhoneNumF.getText().toString());
				prepared.setString(8, ProfF.getText().toString());
				//executer
				prepared.execute();
				//message
				JOptionPane.showMessageDialog(null, "Patient modifé avec succé!","Modification",JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, e);
			}

		}


		public void Imprimer(Document doc) {//imprimer la liste de tout les patients
			// etablire la connection:
			cnx = ConnexionDB.CnxDB();
			String sql = "Select * from  Patient";//selectionner tout les patients
			
			
			
			try {
				prepared = cnx.prepareStatement(sql);
				resultat=prepared.executeQuery();
					PdfWriter.getInstance(doc,new FileOutputStream(new File("PDF's\\Liste des patients.pdf")));
					doc.open();
					Image IMG = Image.getInstance(GestionUsersFrame.class.getResource("/PDF_img/Document  TOP.png"));// le top de la feuille	
					//IMG.scaleAbsoluteHeight(95);
					//IMG.scaleAbsoluteWidth(600);
					IMG.setAlignment(Image.ALIGN_CENTER);
					doc.add(IMG);
					
					
					doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
					doc.add(new Paragraph("Liste des patients") );
					doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
					
					PdfPTable Table =new PdfPTable(9);//9 et le nombre de colone
					Table.setWidthPercentage(110);//la taille de la table
					
					PdfPCell c;//cellule
					//les cellules de ma table (la premiere ligne qui contient les titres)
					c= new PdfPCell(new Phrase("Nom",FontFactory.getFont("Arial", 12)));//titre
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.RED);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Prenom",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.RED);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Date de naissance",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Sexe",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Adresse",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Adresse @",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("N° tlephone",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Profession",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					c= new PdfPCell(new Phrase("Date enregistrement",FontFactory.getFont("Arial", 12)));
					c.setHorizontalAlignment(Element.ALIGN_CENTER);
					c.setBackgroundColor(BaseColor.CYAN);
					Table.addCell(c);
					
					//Remplissage ligne par ligne------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
						while(resultat.next())
						{
							if (resultat.getString("Name")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));// la cas ou il est a null on met "-"
							}
							else {
									c= new PdfPCell(new Phrase(resultat.getString("Name").toString(),FontFactory.getFont("Arial", 12))); //creation de la cellule
							}
						
							c.setHorizontalAlignment(Element.ALIGN_CENTER);//alignement
							c.setBackgroundColor(BaseColor.RED);//colorer en rouge
							Table.addCell(c);//ajouter a la table
							
							if (resultat.getString("FirstName")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("FirstName").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(BaseColor.RED);
							Table.addCell(c);
							
							
							if (resultat.getString("BirthDate")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("BirthDate").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							
							if (resultat.getString("sex")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("sex").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);

							
							if (resultat.getString("Adr")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							}
							else {
									c= new PdfPCell(new Phrase(resultat.getString("adr").toString(),FontFactory.getFont("Arial", 12)));
							}
						
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							if (resultat.getString("Adrmail")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
							}
							else {
									c= new PdfPCell(new Phrase(resultat.getString("adrmail").toString(),FontFactory.getFont("Arial", 12)));
							}
						
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							
							if (resultat.getString("PhoneNum")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("PhoneNum").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							
							if (resultat.getString("Prof")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("Prof").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							
							if (resultat.getString("DateEng")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("DateEng").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table.addCell(c);
							
							
						}
						doc.add(Table);//ajouter la table 

					
					doc.close();//fermer le doc
					Desktop.getDesktop( ).open(new File("PDF's\\Liste des patients.pdf"));//ouverture du pdf a la fin 
			
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
