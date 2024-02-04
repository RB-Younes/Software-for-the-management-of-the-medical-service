import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

////////////////////////////////////////////////////////////////////////////////-----------Class Ordonnance------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Ordonnance {
	
	// les composants de ma connection connection a la BD:
		Connection cnx=null;
		PreparedStatement prepared = null;
		ResultSet resultat =null; 
		PreparedStatement prepared1 = null;
		ResultSet resultat1 =null; 
	

		private String nomMedoc1;//Nom du medicament 1
		private String Posologie1;	//poslogie du medicament1
		private String dosage1;			//dosage du medicament1
		private String Duree1;				//duree du traitement(medicament1)
		//le reste pareil
		private String nomMedoc2;
		private String Posologie2;	
		private String dosage2;
		private String Duree2;
		private String nomMedoc3;
		private String Posologie3;	
		private String dosage3;
		private String Duree3;
		private String nomMedoc4;
		private String Posologie4;	
		private String dosage4;
		private String Duree4;
		private String nomMedoc5;
		private String Posologie5;	
		private String dosage5;
		private String Duree5;	

		private String ID_MED;//ID du Medecin
		private String ID_PAT;//ID du Patient

	//constructeur
		public Ordonnance(String nomMedoc1, String posologie1, String dosage1, String duree1, String nomMedoc2,
				String posologie2, String dosage2, String duree2, String nomMedoc3, String posologie3, String dosage3,
				String duree3, String nomMedoc4, String posologie4, String dosage4, String duree4, String nomMedoc5,
				String posologie5, String dosage5, String duree5, String iD_MED, String id_pat) {
			
			this.nomMedoc1 = nomMedoc1;
			Posologie1 = posologie1;
			this.dosage1 = dosage1;
			Duree1 = duree1;
			this.nomMedoc2 = nomMedoc2;
			Posologie2 = posologie2;
			this.dosage2 = dosage2;
			Duree2 = duree2;
			this.nomMedoc3 = nomMedoc3;
			Posologie3 = posologie3;
			this.dosage3 = dosage3;
			Duree3 = duree3;
			this.nomMedoc4 = nomMedoc4;
			Posologie4 = posologie4;
			this.dosage4 = dosage4;
			Duree4 = duree4;
			this.nomMedoc5 = nomMedoc5;
			Posologie5 = posologie5;
			this.dosage5 = dosage5;
			Duree5 = duree5;
			ID_MED = iD_MED;
			ID_PAT = id_pat;
		}
	//constructeur vide
		public Ordonnance() {
		}


	
	public void ImprimerOrdonence(Ordonnance ORD) {//imprimer l'ordonnance "l'afficher"
		cnx = ConnexionDB.CnxDB();
		Document doc =new Document(PageSize.A5, 25, 25, 10, 10);//creation du document avec une fruille A5
		//recuperer la date du systeme
			Date actuelle = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String datactuelle = dateFormat.format(actuelle);
			
				String sql = "Select Nom , Prenom  , Numtel, Specilité  from Medecin where id_med ='"+ORD.ID_MED +"'";//recuperer les infos du medecin
				
				String sql1 = "Select Name , FirstName , BirthDate  from Patient where ID_Patient ='"+ORD.ID_PAT +"'";//recuperer les infos du Patient

				
					try {
						prepared = cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
						prepared1=cnx.prepareStatement(sql1);
						resultat1=prepared1.executeQuery();
						
					
						if (resultat.next() && resultat1.next()) 	{
							PdfWriter.getInstance(doc, new FileOutputStream(new File("PDF's\\Ordonnance.pdf")));
							doc.open();
							//le header de l'ordonnance() //Nom du medecin + la date \n specilité du medecin \n Numero de l'ordonnance
							Paragraph Head = new Paragraph();
							Head.add(new Chunk("                 Dr."+ resultat.getString("Nom")+" "+resultat.getString("Prenom")+"                                                            Date:"+datactuelle+"\n"+
											   "        Medecin en "+resultat.getString("Specilité")+"\n"+
											   "                  N° Ordre:"+50000
											   
									
									, new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
							Head.setAlignment(Element.ALIGN_LEFT);
			                doc.add(Head);//ajouter le header
			                doc.add(new Paragraph(" "));

							//Nom prenom du patient et sa date de naissance
							Paragraph NOm_prenom_age = new Paragraph();
							NOm_prenom_age.add(new Chunk(" Nom: "+resultat1.getString("Name")+"  "+"Prenom: "+resultat1.getString("FirstName")+" "+"Date de naissance: "+resultat1.getString("BirthDate").substring(0,10)+"                                                  ", 
									new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)).setUnderline(0.2f, -2f));
							NOm_prenom_age.setAlignment(Element.ALIGN_LEFT);
			                doc.add(NOm_prenom_age);
			                doc.add(new Paragraph(" "));
			                
			                
			        
			                //titre "Ordonnance"
							Paragraph para1 = new Paragraph();
							para1.add(new Chunk(" Ordonnance "  , new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)).setUnderline(0.2f, -2f));
							para1.setAlignment(Element.ALIGN_CENTER);
			                doc.add(para1);
			                //le medicament 1 et ses infos
			                if (ORD.nomMedoc1.length()>1) {
			                	
								doc.add(new Paragraph(" "+ORD.nomMedoc1.substring(1, nomMedoc1.length())+"                                         "+ORD.Duree1+"\n"+"      "+ORD.dosage1+" "+ORD.Posologie1));
							}
			                else
			                {
			                	doc.add(new Paragraph(" "+ORD.nomMedoc1+"                                          "+ORD.Duree1+"\n"+"      "+ORD.dosage1+" "+ORD.Posologie1));
			                }
			                doc.add(new Paragraph("\n"));
			                //le medicament 2 et ses infos
			                if (ORD.nomMedoc2.length()>1) {
								doc.add(new Paragraph(" "+ORD.nomMedoc2.substring(1, nomMedoc2.length())+"                                         "+ORD.Duree2+"\n"+"      "+ORD.dosage2+" "+ORD.Posologie2));
							}
			                else
			                {
			                	doc.add(new Paragraph(" "+ORD.nomMedoc2+"                                          "+ORD.Duree2+"\n"+"      "+ORD.dosage2+" "+ORD.Posologie2));
			                }
			                doc.add(new Paragraph("\n"));
			                //le medicament 3 et ses infos
			                if (ORD.nomMedoc3.length()>1) {
								doc.add(new Paragraph(" "+ORD.nomMedoc3.substring(1, nomMedoc3.length())+"                                         "+ORD.Duree3+"\n"+"      "+ORD.dosage3+" "+ORD.Posologie3));
							}
			                else
			                {
			                	doc.add(new Paragraph(" "+ORD.nomMedoc3+"                                          "+ORD.Duree3+"\n"+"      "+ORD.dosage3+" "+ORD.Posologie3));
			                }
			                doc.add(new Paragraph("\n"));
			                //le medicament 4 et ses infos
			                if (ORD.nomMedoc4.length()>1) {
								doc.add(new Paragraph(" "+ORD.nomMedoc4.substring(1, nomMedoc4.length())+"                                         "+ORD.Duree4+"\n"+"      "+ORD.dosage4+" "+ORD.Posologie4));
							}
			                else
			                {
			                	doc.add(new Paragraph(" "+ORD.nomMedoc4+"                                          "+ORD.Duree4+"\n"+"      "+ORD.dosage4+" "+ORD.Posologie4));
			                }
			                doc.add(new Paragraph("\n"));
			                if (ORD.nomMedoc5.length()>1) {
								doc.add(new Paragraph(" "+ORD.nomMedoc5.substring(1, nomMedoc5.length())+"                                         "+ORD.Duree5+"\n"+"      "+ORD.dosage5+" "+ORD.Posologie5));
							}
			                else
			                {
			                	doc.add(new Paragraph(" "+ORD.nomMedoc5+"                                          "+ORD.Duree5+"\n"+"      "+ORD.dosage5+" "+ORD.Posologie5));
			                }
			                //daes blans
			                doc.add(new Paragraph(""));
			                doc.add(new Paragraph("\n"));
			                doc.add(new Paragraph("\n"));
			                doc.add(new Paragraph("\n"));
			                doc.add(new Paragraph("\n"));
			                doc.add(new Paragraph("\n"));
			                doc.add(new Paragraph("\n"));
			               
			                
			                //separateur
			                doc.add(new Paragraph("-------------------------------------------------------------------------------------------"));
			               
			                //plus dinformation
			                Paragraph adresse = new Paragraph();
			                adresse.add(new Chunk("Adresse : Cite 40 Logements Batiment 'A',Zéralda"+"                                               N°Tel:0"+resultat.getString("Numtel") , new Font(Font.FontFamily.HELVETICA,7, Font.NORMAL)));
			                adresse.setAlignment(Element.ALIGN_CENTER);
			                doc.add(adresse);
			                doc.add(new Paragraph(""));
			                
			                Paragraph genrer = new Paragraph();
			                genrer.add(new Chunk("Generé par logiciel RE&BE Gestion clinique,              Pour lus dinformation https://www.RE&BE-clinique-GST.com/  ", new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL)).setUnderline(0.2f, -2f));
			                genrer.setAlignment(Element.ALIGN_CENTER);
			                doc.add(genrer);
							
							
							doc.close();
						Desktop.getDesktop( ).open(new File("PDF's\\Ordonnance.pdf"));//ouverture du document
							
										}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e);
					}
					
				
			}

	
	
	
}
