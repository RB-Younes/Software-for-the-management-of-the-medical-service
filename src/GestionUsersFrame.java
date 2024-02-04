import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
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

import net.proteanit.sql.DbUtils;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Gestion Users------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionUsersFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableMedecin;
	private JTable tableSecretaire;
	private JButton btnModMed ;
	private JButton btnSuppMed;
	private JButton btnAddMed;
	
	private JButton btnSuppSec;
	private JButton btnModSec;
	private JButton btnAddSec;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    
    
	private JTextField NomField;
	private JTextField PrenomField;
	private JTextField UserNameField;
	private JTextField MdpField;
	private JTextField MdpCField;
	private JTextField TelField;
	private String id_med;// le id recupere en cliquant sur le tableau
	private String id_sec;
	private JTextField textFieldADRmail;
	
	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
	UIManager.setLookAndFeel(new FlatIntelliJLaf() );
	
	
	JFrame.setDefaultLookAndFeelDecorated(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionUsersFrame frame = new GestionUsersFrame("2");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public GestionUsersFrame(String ID_MED) {
// CNX 
		cnx = ConnexionDB.CnxDB();
		
//FAIRE bouger la fenetre////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
        addMouseListener(new MouseAdapter() {
            @Override
            //on recupere les coordonnées de la souris
            public void mousePressed(MouseEvent e) {
                posX = e.getX();    //Position X de la souris au clic
                posY = e.getY();    //Position Y de la souris au clic
            }
        });
         
        addMouseMotionListener(new MouseMotionAdapter() {
            // A chaque deplacement on recalcul le positionnement de la fenetre
            @Override
            public void mouseDragged(MouseEvent e) {
                int depX = e.getX() - posX;
                int depY = e.getY() - posY;
                setLocation(getX()+depX, getY()+depY);
            }
        });

// initialisation/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 650d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionUsersFrame.class.getResource("/Menu_Medcin_img/gst users.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 650);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// Fields///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//passwordField pour le mot de passe
		MdpField = new JPasswordField();
		MdpField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		MdpField.getText().length() >= 30 ) //limiter le nombre de caractere a 30
		            e.consume(); 
		    }  
		});
		MdpField.setBounds(436, 395, 190, 28);
		MdpField.setForeground(new Color(255, 255, 255));
		MdpField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		MdpField.setBackground(new Color(153, 204, 255));
		MdpField.setBorder(null);
		contentPane.add(MdpField);
		MdpField.setColumns(10);
		
	//tet Area Adresse
		JTextArea AdrtextArea = new JTextArea();
		AdrtextArea.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		AdrtextArea.getText().length() >= 500 ) //limitée a 500
		            e.consume(); 
		    }  
		});
		AdrtextArea.setForeground(new Color(255, 255, 255));
		AdrtextArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
		AdrtextArea.setBackground(new Color(153, 204, 255));
		AdrtextArea.setBorder(null);
		AdrtextArea.setBounds(110, 534, 209, 46);
		contentPane.add(AdrtextArea);
		
	//extfield Numero de telephone	
		TelField = new JTextField();
		TelField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		TelField.getText().length() >= 10 ) //limitée a 10
		            e.consume(); 
		    }  
		});
		TelField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();//exrire que des chofres
		      if (!((c >= '0') && (c <= '9') ||
		         (c == KeyEvent.VK_BACK_SPACE) ||
		         (c == KeyEvent.VK_DELETE))) {
		        getToolkit().beep();
		        e.consume();
		      }
		    }
		  });
		TelField.setForeground(new Color(255, 255, 255));
		TelField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		TelField.setBackground(new Color(153, 204, 255));
		TelField.setBorder(null);
		TelField.setBounds(425, 542, 190, 28);
		contentPane.add(TelField);
		TelField.setColumns(10);
		
	//checkbox Homme & femme		
		JCheckBox CheckBoxFemme = new JCheckBox();
		JCheckBox CheckBoxHomme = new JCheckBox();
		CheckBoxFemme.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				CheckBoxHomme.setSelected(false);
				
			}
			
		});
		CheckBoxFemme.setBounds(188, 449, 21, 20);
		contentPane.add(CheckBoxFemme);
		CheckBoxHomme.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		
				CheckBoxFemme.setSelected(false);
			}
		});
		CheckBoxHomme.setBounds(98, 449, 21, 20);
		contentPane.add(CheckBoxHomme);
		
	//Confirmer Mot de passe 	(PasswordField)
		MdpCField = new JPasswordField();
		MdpCField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		MdpCField.getText().length() >= 30 ) 
		            e.consume(); 
		    }  
		});
		MdpCField.setForeground(new Color(255, 255, 255));
		MdpCField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		MdpCField.setBackground(new Color(153, 204, 255));
		MdpCField.setBorder(null);
		MdpCField.setBounds(416, 441, 190, 28);
		contentPane.add(MdpCField);
		MdpCField.setColumns(10);
		
	//Nom d'utilisateur text Field	
		UserNameField = new JTextField();
		UserNameField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		UserNameField.getText().length() >= 30 ) 
		            e.consume(); 
		    }  
		});
		UserNameField.setForeground(new Color(255, 255, 255));
		UserNameField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		UserNameField.setBackground(new Color(153, 204, 255));
		UserNameField.setBorder(null);
		UserNameField.setBounds(425, 341, 190, 28);
		contentPane.add(UserNameField);
		UserNameField.setColumns(10);
		
	//prenom Fiekd	
		PrenomField = new JTextField();
		PrenomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		PrenomField.getText().length() >= 50 ) 
		            e.consume(); 
		    }  
		});
		PrenomField.setBounds(138, 395, 190, 28);
		PrenomField.setForeground(new Color(255, 255, 255));
		PrenomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		PrenomField.setBackground(new Color(153, 204, 255));
		PrenomField.setBorder(null);
		contentPane.add(PrenomField);
		PrenomField.setColumns(10);
		
	//Nom Field
		NomField = new JTextField();
		NomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		NomField.getText().length() >= 50 ) 
		            e.consume(); 
		    }  
		});
		NomField.setBounds(104, 341, 190, 28);
		NomField.setForeground(new Color(255, 255, 255));
		NomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		NomField.setBackground(new Color(153, 204, 255));
		NomField.setBorder(null);
		contentPane.add(NomField);
		NomField.setColumns(10);
		
	//ComboBox specialité Medecin	
		JComboBox<Object> comboBoxSpé = new JComboBox<Object>();
		comboBoxSpé.setModel(new DefaultComboBoxModel<Object>(new String[] {"Select","Accident and emergency medicine" ,
				"Allergology" ,"Anaesthetics" , "Cardiology" , "Child psychiatry" , "Clinical biology", "Clinical chemistry" , "Clinical microbiology" , "Clinical neurophysiology" , "Craniofacial surgery",  "Dermatology",  
				"Endocrinology", "Family and General Medicine", "Gastroenterologic surgery", "Gastroenterology", "General Practice", "General surgery" , 	"Geriatrics"  ,	"Hematology"  ,"Immunology" , "Infectious diseases" , "Internal medicine" , 
				"Laboratory medicine" , "Nephrology",  "Neuropsychiatry","Neurology" ,"Neurosurgery" , "Nuclear medicine" , "Obstetrics and gynaecology" , "Occupational medicine" , "Ophthalmology" , "Oral and maxillofacial surgery", "Orthopaedics" , "Otorhinolaryngology" , 
				"Paediatric surgery" , "Paediatrics" , "Pathology", "Pharmacology", "Physical medicine and rehabilitation" , "Plastic surgery" , "Podiatric surgery" , "Preventive medicine", "Psychiatry" ,"Public health", "Radiation Oncology", "Radiology" , "Respiratory medicine" , "Rheumatology", "Stomatology" , 
				"Thoracic surgery" , "Tropical medicine" , "Urology", "Vascular surgery" , 
				"Venereology" } ));
		comboBoxSpé.setBackground(new Color(153, 204, 255));
		comboBoxSpé.setForeground(new Color(255, 255, 255));
		comboBoxSpé.setBounds(425, 606, 190, 27);
		contentPane.add(comboBoxSpé);
		
	//ComboBox Year
		JComboBox<Object> CBYear = new JComboBox<Object>();
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {"Year"}));		
		for (int i = 2020; i>=1900 ; i--) {
			String Year =""+i+"";
			CBYear.addItem(Year);		
			}
		
		CBYear.setBackground(new Color(153, 204, 255));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(261, 488, 70, 20);
		contentPane.add(CBYear);
	
	//ComboBox mois
		JComboBox<Object> CBMonth = new JComboBox<Object>();
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"Month","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setBackground(new Color(153, 204, 255));
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBounds(189, 488, 70, 20);
		contentPane.add(CBMonth);
		
	//ComboBox jours
		JComboBox<Object> CBDay = new JComboBox<Object>();
		CBDay.setModel(new DefaultComboBoxModel<Object>(new String[] {"Day"}));

		for (int j = 1; j<=31 ; j++) {
			
			if (j<=9) {
				String Day ="0"+j+"";CBDay.addItem(Day);	
			}
			else {
				String Day =""+j+"";CBDay.addItem(Day);	
			}
			
				
			}
		CBDay.setBackground(new Color(153, 204, 255));
		CBDay.setForeground(new Color(255, 255, 255));
		CBDay.setBounds(132, 488, 55, 20);
		contentPane.add(CBDay);
		
	//textField Adraisse mail
		textFieldADRmail = new JTextField();
		textFieldADRmail.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldADRmail.getText().length() >= 50 ) 
		            e.consume(); 
		    }  
		});
		textFieldADRmail.setForeground(Color.WHITE);
		textFieldADRmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldADRmail.setColumns(10);
		textFieldADRmail.setBorder(null);
		textFieldADRmail.setBackground(new Color(153, 204, 255));
		textFieldADRmail.setBounds(126, 606, 190, 28);
		contentPane.add(textFieldADRmail);
		

//ScrollPane+table *Secretaire*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPaneSec = new JScrollPane();
		scrollPaneSec.setBounds(646, 406, 533, 204);
		scrollPaneSec.setBackground(new Color(0, 51, 102));
		contentPane.add(scrollPaneSec);
		
		tableSecretaire = new JTable();
		tableSecretaire.setForeground(new Color(255, 255, 255));
		tableSecretaire.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableSecretaire.setBorder(null);
		tableSecretaire.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//tout les elements recuperés vont etre vont etre mis dans leur text field(comboBox) respective
				//Activer les deux boutons Modifier et supprimer secretaire
				btnSuppSec.setEnabled(true);
				btnModSec.setEnabled(true);
				
				int ligne=tableSecretaire.getSelectedRow();//recuperer le numero de ligne
				

				id_sec =tableSecretaire.getValueAt(ligne, 0).toString();//id de la secretaire
				String sql="Select * from Secretaire where Id_sec='"+id_sec+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					NomField.setText(resultat.getString("Nom"));//recuperer le Nom 
					PrenomField.setText(resultat.getString("Prenom"));//recuperer le Prenom
					UserNameField.setText(resultat.getString("Ueser_Name"));//recuperer le Nom d'utilisateurs
					MdpField.setText(resultat.getString("password"));//recuperer le Mot de passe
					MdpCField.setText(resultat.getString("password"));//recuperer le mot de passe
					textFieldADRmail.setText(resultat.getString("ADRmail"));//recuperer l' Adresse mail
					
					String DN=resultat.getString("DateN");//recuperer la date de n'aissance de la forme yyyy-MM-dd HH:mm:ss
				    if (DN!=null)
				    {
				    	DN=(String) DN.subSequence(0, 10);//prendre que la premiere partie de la date
				    	//parcourir la combobox Année
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
						{
								if (CBYear.getItemAt(i).equals(DN.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);
								//selectionner cet element
								}
							
						}
				    	//parcourir la combobox MOis
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
						{
								if (CBMonth.getItemAt(i).equals(DN.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);
								//selectionner cet element
								}
							
						}
				    	//parcourir la combobox Jours
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
						{
								if (CBDay.getItemAt(i).equals(DN.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);
								//selectionner cet element
								}
							
						}
				    }
					//recuperer le sexe
					String sexe=resultat.getString("Sex");
					if(sexe.equals("Homme")) {CheckBoxHomme.setSelected(true);CheckBoxFemme.setSelected(false);}
					if(sexe.equals("Femme")) {CheckBoxFemme.setSelected(true);CheckBoxHomme.setSelected(false);}
					if(sexe.equals("null")) { CheckBoxFemme.setSelected(false);CheckBoxHomme.setSelected(false);}
					
					AdrtextArea.setText(resultat.getString("Adr"));//recuperer L'Adresse
					TelField.setText(resultat.getString("NumTel"));//recuperer le numero de telephone
					
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
				
				
			}
		});
		tableSecretaire.setBackground(new Color(153, 204, 255));
		scrollPaneSec.setViewportView(tableSecretaire);

//ScrollPane+table *Secretaire*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPaneMed = new JScrollPane();
		scrollPaneMed.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneMed.setBackground(new Color(0, 51, 102));
		scrollPaneMed.setBounds(638, 151, 541, 218);
		contentPane.add(scrollPaneMed);
		
		tableMedecin = new JTable();
		tableMedecin.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableMedecin.setForeground(new Color(255, 255, 255));
		tableMedecin.setBorder(null);
		tableMedecin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnSuppMed.setEnabled(true);
				btnModMed.setEnabled(true);
				int ligne=tableMedecin.getSelectedRow();
				

				id_med =tableMedecin.getValueAt(ligne, 0).toString();
				String sql="Select * from Medecin where Id_med='"+id_med+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					NomField.setText(resultat.getString("Nom"));//recuperer le Nom 
					PrenomField.setText(resultat.getString("Prenom"));//recuperer le Prenom
					UserNameField.setText(resultat.getString("Ueser_Name"));//recuperer le Nom d'utilisateurs
					MdpField.setText(resultat.getString("password"));//recuperer le Mot de passe
					MdpCField.setText(resultat.getString("password"));//recuperer le Mot de passe
					textFieldADRmail.setText(resultat.getString("ADRmail"));//recuperer l' Adresse mail
					
					String DN=resultat.getString("DateN");//recuperer la date de n'aissance de la forme yyyy-MM-dd HH:mm:ss
				    if (DN!=null)
				    {
				    	DN=(String) DN.subSequence(0, 10);//prendre que la premiere partie de la date
				    	//parcourir la combobox anneé
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
						{
								if (CBYear.getItemAt(i).equals(DN.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);
								// selectionner cet element
								}
							
						}
				    	//parcourir la combobox jours
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
						{
								if (CBMonth.getItemAt(i).equals(DN.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);
								// selectionner cet element
								}
							
						}
				    	//parcourir la combobox Mois
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
						{
								if (CBDay.getItemAt(i).equals(DN.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);
								// selectionner cet element
								}
							
						}
				    }
					//recuperer le sexe
					String sexe=resultat.getString("Sex");
					if(sexe.equals("Homme")) {CheckBoxHomme.setSelected(true);CheckBoxFemme.setSelected(false);}
					if(sexe.equals("Femme")) {CheckBoxFemme.setSelected(true);CheckBoxHomme.setSelected(false);}
					if(sexe.equals("null")) { CheckBoxFemme.setSelected(false);CheckBoxHomme.setSelected(false);}
						
					AdrtextArea.setText(resultat.getString("Adr"));//recuperer L'Adresse
					TelField.setText(resultat.getString("NumTel"));//recuperer le numero de telephone
					//parcourir la combobox Specialité
					for (int i = 0; i < comboBoxSpé.getItemCount(); i++) 
					{
							if (comboBoxSpé.getItemAt(i).equals(resultat.getString("Specilité"))) { comboBoxSpé.setSelectedIndex(i);
							// selectionner cet element
							}
						
					}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
			}
		});
		tableMedecin.setBackground(new Color(153, 204, 255));
		scrollPaneMed.setViewportView(tableMedecin);
//afficher le contenue des deux tables  juste a louverture/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableMedecin();
		UpedateTableSecretaire();
		
//les boutons Secretaire Ajouter,Supprimer,Modifier//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Supprimer une Secretaire**********************************************************************************************************************************************************************************************************************************
		btnSuppSec = new JButton();
		btnSuppSec.setEnabled(false);//desactiver le bouton tant que acune ligne de la table secretaire n'a ete selectionné
		btnSuppSec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSuppSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSuppSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnSuppSec);
		btnSuppSec.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				if (tableSecretaire.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{ ///message d'erreur
					JOptionPane.showMessageDialog(null, "Veuillez  selectionner une ligne de la table secretaire a suprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else {	
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment supprimer cette secretaire  ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{	
						Secretaire Sec=new  Secretaire();
						Sec.Suprimer(id_sec);//appele de la Methode supprimer de la classe Secretaire
						UpedateTableSecretaire();//Actualiser la table secretaire
						//desactiver les boutons supprimer et modifier secretaire
						btnSuppSec.setEnabled(false);
						btnModSec.setEnabled(false);
					}
				}
				
			}
		});
		btnSuppSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove.png")));
		btnSuppSec.setToolTipText("Supprimer Secretaire");
		btnSuppSec.setBounds(493, 283, 32, 32);
		contentPane.add(btnSuppSec);
		
	//Modifier une Secretaire**********************************************************************************************************************************************************************************************************************************
		btnModSec = new JButton();
		btnModSec.setEnabled(false);//desactiver le bouton tant que acune ligne de la table secretaire n'a ete selectionné
		btnModSec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnModSec);
		btnModSec.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (tableSecretaire.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
					{//message d'erreur
					JOptionPane.showMessageDialog(null, "Veuillez  selectionner une ligne de la table medecin a Modifer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
				else 
				{
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(btnModSec," Voulez vous vraiment modifier cette ligne ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{
							//recuperer le sexe
							String sexe=null; 
							if(CheckBoxHomme.isSelected()) {  sexe ="Homme";}
							if(CheckBoxFemme.isSelected()) {  sexe ="Femme";}
				
							String mdp=MdpField.getText().toString();//mot de passe 
							String mdpc =MdpCField.getText().toString();//confirmation mot de passe
							String Adrmail=textFieldADRmail.getText().toString();//Adresse Mail
							if(!mdp.equals(mdpc)) //si le mot de passe et la confiramation sont different alors
								{
								//message
								JOptionPane.showMessageDialog(null, "S'il vous plaît resaisissez le mot de passe!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								//Vider les champs
								MdpField.setText("");
								MdpCField.setText("");
								}
				

							String id =id_sec;//recuperer l'id secretaire
								//vrifier si les champs avec * sont pas vide
								if (( UserNameField.getText().toString().equals("") && MdpField.getText().toString().equals("") && NomField.getText().toString().equals("") && PrenomField.getText().toString().equals("") )) 
									{
									//message
										JOptionPane.showMessageDialog(null, " Veuillez remplire les champs marqué par  etoile '*'!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
									}
				
								else {
						
										String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
										//verifier la validité de la date de n'aissance
										boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
										if (!B) 
											{
											//messgae
											JOptionPane.showMessageDialog(null,  "Date de naissance invalide !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
											}
										else 
										{
											Secretaire Sec=new  Secretaire();
											Sec.MetreAjour(id,UserNameField,MdpField,NomField,PrenomField,DN,sexe,AdrtextArea,Adrmail,TelField);
											//appele de la Methode MettreAjour de la classe Secretaire
											//desactiver les boutons supprimer et modifier secretaire
											btnSuppSec.setEnabled(false);
											btnModSec.setEnabled(false);		
											UpedateTableSecretaire();//actualiser la table secretaire
										}
							
									}
						}
				}
		
			}
		});
		ButtonStyle(btnModSec);
		btnModSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify.png")));
		btnModSec.setToolTipText("Modifier Secretaire");
		btnModSec.setBounds(493, 223, 32, 32);
		contentPane.add(btnModSec);
		
	//Ajouter une Secretaire**********************************************************************************************************************************************************************************************************************************
		btnAddSec = new JButton();
		btnAddSec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAddSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus selected.png")));//changer les couleurs button
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnAddSec);
		btnAddSec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment ajouter cette personne en tant que secretaire ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{

				String Nom =NomField.getText().toString();//recuperer le nom
				String Prenom=PrenomField.getText().toString();//le prenom
				String UserName=UserNameField.getText().toString();// le nom d'utilisateur
				String Password=MdpField.getText().toString();//mot de passe 
				String PasswordC=MdpCField.getText().toString();//confirmation mot de passe
				
				if (!Password.equals(PasswordC)) {//la comparaison du mot de passe et sa confirmation
					//si ils sony different alors vider le deux fields
					MdpField.setText("");
					MdpCField.setText("");
					//afficher un messge
					JOptionPane.showMessageDialog(null, " S'il vous plaît resaisissez le mot de passe!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else {
					String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
					//verifier la validiter de la date de n'aissance
					boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
					if (!B) {
						JOptionPane.showMessageDialog(null,  "Date de naissance invalide !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
					else 
					{
						//recuperer le sexe
						String Sexe = null;   
						if (CheckBoxHomme.isSelected()) { Sexe="Homme";}
						if (CheckBoxFemme.isSelected()) { Sexe="Femme";}
						
						String Adr=AdrtextArea.getText().toString();//Adraisse
						String NumT=TelField.getText().toString();//Numero Telephone
						String Adrmail=textFieldADRmail.getText().toString();//Adresse mail
					
						if (UserName.equals("") ||Password.equals("") || Nom.equals("")|| Prenom.equals("")) //verifier si les champs avec * sont pas vide
							{
								//afficher un message d'erreur
								JOptionPane.showMessageDialog(null, "Veuillez remplire les champs marqué par  etoile '*'!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
						else 
							{
							Secretaire sec=new  Secretaire(UserName, Password, Nom, Prenom, DN, Sexe, Adr,Adrmail, NumT);
							sec.Ajouter(sec);//appele de la Methode Ajouter de la classe Secretaire
							//desactiver les boutons supprimer et modifier medecin
							btnSuppSec.setEnabled(false);
							btnModSec.setEnabled(false);
							UpedateTableSecretaire();//metre ajour la table secretaire
							}
						}
					
						}
				}
			
			}
		});
		btnAddSec.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus.png")));
		btnAddSec.setToolTipText("Ajouter Secretaire");
		btnAddSec.setBounds(493, 165, 32, 32);
		contentPane.add(btnAddSec);
		
//les boutons MEdecin Ajouter,Supprimer,Modifier//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//supprimer un Medecin**********************************************************************************************************************************************************************************************************************************
		btnSuppMed = new JButton();
		btnSuppMed.setEnabled(false);//desactiver le bouton tant que acune ligne de la table Medecin n'a ete selectionné
		btnSuppMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSuppMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSuppMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnSuppMed);
		btnSuppMed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableMedecin.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{
					JOptionPane.showMessageDialog(null, " Veuillez  selectionner une ligne de la table medecin a suprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));

				}
				else {
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(	null, "Voulez vous vraiment supprimer ce Medecin  ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{	
					
					Medecin Med=new  Medecin();
					if (!id_med.equals(ID_MED)) {//pour ne pas supprimer sa propre session
						Med.Suprimer(id_med);;//appele de la Methode supprimer de la classe Medecin
					UpedateTableMedecin();//actualiser la table 
					//desactiver les boutons supprimer et modifier medecin
					btnSuppMed.setEnabled(false);
					btnModMed.setEnabled(false);
					}
					else {
						JOptionPane.showMessageDialog(null, " Vous ne pouvez s supprimer votre propre session (ca peux causer des erreurs) !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					
				}
			
				}
				
				
			}
		});
		btnSuppMed.setToolTipText("Supprimer Medecin");
		btnSuppMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/remove.png")));
		btnSuppMed.setBounds(158, 283, 32, 32);
		contentPane.add(btnSuppMed);
		
		
	//Ajouter un Medecin**********************************************************************************************************************************************************************************************************************************
		btnAddMed = new JButton();
		btnAddMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnAddMed);
		btnAddMed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment ajouter cette personne en tant que medecin ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					String Nom =NomField.getText().toString();//recuperer le nom
					String Prenom=PrenomField.getText().toString();//le prenom
					String UserName=UserNameField.getText().toString();// le nom d'utilisateur
					String Password=MdpField.getText().toString();//mot de passe 
					String PasswordC=MdpCField.getText().toString();//confirmation mot de pass
					
				
					if (!Password.equals(PasswordC)) //la comparaison du mot de passe et sa confirmation
					{	//vider les champs
						MdpField.setText("");
						MdpCField.setText("");
						//afficher un message 
						JOptionPane.showMessageDialog(null, "S'il vous plaît resaisissez le mot de passe!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else 
					{
						String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
						//verifier la validiter de la date de n'aissance
						boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
						if (!B) 
						{
							JOptionPane.showMessageDialog(null,  "Date de naissance invalide !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						}
						else 
						{
							//recuperer le sexe
							String Sexe = null;   
							if (CheckBoxHomme.isSelected()) { Sexe="Homme";}
							if (CheckBoxFemme.isSelected()) { Sexe="Femme";}
							
							String Adr=AdrtextArea.getText().toString();//Adraisse
							String NumT=TelField.getText().toString();//Numero Telephone
							String SpeDip=comboBoxSpé.getSelectedItem().toString();//Specialite medecin
							String Adrmail=textFieldADRmail.getText().toString();//Adresse mail
							
							
					
							if (UserName.equals("") ||Password.equals("") || Nom.equals("")|| Prenom.equals("")) //verifier si les champs avec * sont pas vide
							{	//message de confirmation
								JOptionPane.showMessageDialog(null,  " veuillez remplire les champs marqué par  etoile '*' !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
							else 
							{

							Medecin Med=new  Medecin(UserName, Password, Nom, Prenom, DN, Sexe, Adr,Adrmail,NumT, SpeDip);
							Med.Ajouter(Med);//appele de la Methode ajouter de la classe Medecin
							//desactiver les boutons supprimer et modifier medecin
							btnSuppMed.setEnabled(false);
							btnModMed.setEnabled(false);
							
							UpedateTableMedecin();
							}
						}
					
					}
				}
			}
		});
		btnAddMed.setToolTipText("Ajouter Medecin");
		btnAddMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/plus.png")));
		btnAddMed.setBounds(158, 165, 32, 32);
		contentPane.add(btnAddMed);
		
	//Modifier un Medecin**********************************************************************************************************************************************************************************************************************************
		btnModMed = new JButton();
		btnModMed.setEnabled(false);//desactiver le bouton tant que acune ligne de la table Medecin n'a ete selectionné
		btnModMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnModMed);
		btnModMed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableMedecin.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{//message d'erreur
					JOptionPane.showMessageDialog(null, "Veuillez  selectionner une ligne de la table medecin a modifier !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else 
				{
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment modifier cette ligne ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						//recuperer le sexe
						String sexe=null; 
						if(CheckBoxHomme.isSelected()) {  sexe ="Homme";}
						if(CheckBoxFemme.isSelected()) {  sexe ="Femme";}
				
						String mdp=MdpField.getText().toString();//le mot de passe
						String mdpc =MdpCField.getText().toString();//confirmation
						String Adrmail=textFieldADRmail.getText().toString();//adresse mail
						if(!mdp.equals(mdpc)) //la comparaison du mot de passe et sa confirmation
						{
							//afficher un message 
							JOptionPane.showMessageDialog(null, "S'il vous plaît resaisissez le mot de passe!","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							//vider les champs
							MdpField.setText("");
							MdpCField.setText("");
						}

						String id =id_med;
					
						if (( UserNameField.getText().toString().equals("") && MdpField.getText().toString().equals("") && NomField.getText().toString().equals("") && PrenomField.getText().toString().equals("") )) //verifier si les champs avec * sont pas vide
						{//message
							JOptionPane.showMessageDialog(null, " veuillez remplire les champs marqué par  etoile '*' !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						}
						
						else 
						{
					
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							//verifier la validiter de la date de n'aissance
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							if (!B) 
							{//message
								JOptionPane.showMessageDialog(null,  "Date de naissance invalide !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
							else {
								Medecin Med=new  Medecin();
								Med.MetreAjour(id,UserNameField,MdpField,NomField,PrenomField,DN,sexe,AdrtextArea,Adrmail,TelField,comboBoxSpé.getSelectedItem().toString());//appele de la Methode ajouter de la classe Medecin
								//desactiver les boutons supprimer et modifier medecin
								btnSuppMed.setEnabled(false);
								btnModMed.setEnabled(false);
								
								UpedateTableMedecin();
							}
						}
					}
				}
			}
		});
		btnModMed.setToolTipText("Modifier Medecin");
		btnModMed.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/Modify.png")));
		btnModMed.setBounds(158, 223, 32, 32);
		contentPane.add(btnModMed);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1065, 11, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Vider les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// remtre les valeurs par default de la combobox
				UserNameField.setText("");
				MdpField.setText("");
				MdpCField.setText("");
				CBYear.setSelectedIndex(0);
				CBDay.setSelectedIndex(0);
				CBMonth.setSelectedIndex(0);
				AdrtextArea.setText("");
				comboBoxSpé.setSelectedIndex(0);
				TelField.setText("");
				NomField.setText("");	
				PrenomField.setText("");
				textFieldADRmail.setText("");
				CheckBoxFemme.setSelected(false);
				CheckBoxHomme.setSelected(false);
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/rubbish.png")));
		btnViderFields.setBounds(584, 283, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						dispose();
					}
			}
		});
		Exit_BTN.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1147, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
//bouton imprimer la liste des utilisateurs //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnImprimer = new JButton("");
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/print selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/print .png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/print .png")));
		btnImprimer.addActionListener(new ActionListener() {//imprimer la liste de tout les Medicaments
			public void actionPerformed(ActionEvent e) {
				Document doc= new Document();
				String sql = "Select * from  Medecin";
				String sql2 = "Select * from  Secretaire";
			
				try {
					prepared = cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
						PdfWriter.getInstance(doc, new FileOutputStream(new File("PDF's\\Users.pdf")));//creer le pdf
						doc.open();
						Image IMG = Image.getInstance(GestionUsersFrame.class.getResource("/PDF_img/Document  TOP.png"));// le top de la feuille	
						//IMG.scaleAbsoluteHeight(95);
						//IMG.scaleAbsoluteWidth(600);
						IMG.setAlignment(Image.ALIGN_CENTER);
						doc.add(IMG);
						//separation
						doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
						doc.add(new Paragraph("Doctors List") );
						doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
						
						PdfPTable Table =new PdfPTable(10);//10 et le nombre de colone (table medecin)
						Table.setWidthPercentage(110);//la taille de la table
						
						PdfPCell c;//cellule
						//les cellules de ma table 
						c= new PdfPCell(new Phrase("Nom",FontFactory.getFont("Arial", 12)));//titre
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("Prenom",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						//new Color(153, 204, 255)
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("Sexe",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("Date de naissance",FontFactory.getFont("Arial", 12)));
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
						
						c= new PdfPCell(new Phrase("Nom d'utilisteur",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("Mot de passe",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("Numero de telephone",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table.addCell(c);
						
						c= new PdfPCell(new Phrase("specialtié Medecin",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table.addCell(c);
						
						//Remplissage ligne par ligne-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
							while(resultat.next())
							{
								if (resultat.getString("Nom")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));// le cas ou il est a null on met "-"
								}
								else {
										c= new PdfPCell(new Phrase(resultat.getString("Nom").toString(),FontFactory.getFont("Arial", 12)));//creation de la cellule
								}
							
								c.setHorizontalAlignment(Element.ALIGN_CENTER);//alignement
								Table.addCell(c);//ajouter a la table
								
								if (resultat.getString("Prenom")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("Prenom").toString(),FontFactory.getFont("Arial", 12)));
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
								
								if (resultat.getString("DateN")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("DateN").toString(),FontFactory.getFont("Arial", 12)));
								}
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								Table.addCell(c);

								if (resultat.getString("ADR")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("ADR").toString(),FontFactory.getFont("Arial", 12)));
								}
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								Table.addCell(c);
								
								if (resultat.getString("ADRmail")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("ADRmail").toString(),FontFactory.getFont("Arial", 12)));
								}
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								Table.addCell(c);
								
								c= new PdfPCell(new Phrase(resultat.getString("Ueser_Name").toString(),FontFactory.getFont("Arial", 12)));
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								c.setBackgroundColor(BaseColor.RED);
								Table.addCell(c);
								
								c= new PdfPCell(new Phrase("******",FontFactory.getFont("Arial", 12)));
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								c.setBackgroundColor(BaseColor.RED);
								Table.addCell(c);
								
								if (resultat.getString("Numtel")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("Numtel").toString(),FontFactory.getFont("Arial", 12)));
								}
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								Table.addCell(c);
								
								if (resultat.getString("Specilité")==null) {
									c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
									
								} else {
									c= new PdfPCell(new Phrase(resultat.getString("Specilité").toString(),FontFactory.getFont("Arial", 12)));
								}
								c.setHorizontalAlignment(Element.ALIGN_CENTER);
								Table.addCell(c);
								
							}
							doc.add(Table);//ajouter la table 
						//separation	
						doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
						doc.add(new Paragraph("Secretory List") );
						doc.add(new Paragraph("----------------------------------------------------------------------------------------------------"));
						prepared = cnx.prepareStatement(sql2);
						resultat=prepared.executeQuery();
						PdfPTable Table2 =new PdfPTable(9);//9 et le nombre de colone de latable secretaire
						Table.setWidthPercentage(110);
						//les cellules de ma table (la premiere ligne qui contient les titres)
						c= new PdfPCell(new Phrase("Nom",FontFactory.getFont("Arial", 12)));//titre
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Prenom",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						//new Color(153, 204, 255)
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Sexe",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Date de naissnce",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Adresse",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Adresse @",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Nom d'utilisateur",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Mot de passe",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.RED);
						Table2.addCell(c);
						
						c= new PdfPCell(new Phrase("Numero de telephone",FontFactory.getFont("Arial", 12)));
						c.setHorizontalAlignment(Element.ALIGN_CENTER);
						c.setBackgroundColor(BaseColor.CYAN);
						Table2.addCell(c);
						//Remplissage ligne par ligne table secretaire------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

							while(resultat.next())
							{
							if (resultat.getString("Nom")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));// le cas ou il est a null on met "-"
							}
							else {
									c= new PdfPCell(new Phrase(resultat.getString("Nom").toString(),FontFactory.getFont("Arial", 12)));//creation de la cellule
							}
						
							c.setHorizontalAlignment(Element.ALIGN_CENTER);//alignement
							Table2.addCell(c);//ajouter a la table
							
							if (resultat.getString("Prenom")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("Prenom").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);
							
							
							if (resultat.getString("sex")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("sex").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);
							
							if (resultat.getString("DateN")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("DateN").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);

							if (resultat.getString("ADR")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("ADR").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);
							
							if (resultat.getString("ADRmail")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("ADRmail").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);
							
							c= new PdfPCell(new Phrase(resultat.getString("Ueser_Name").toString(),FontFactory.getFont("Arial", 12)));
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(BaseColor.RED);
							Table2.addCell(c);
							
							c= new PdfPCell(new Phrase("**********",FontFactory.getFont("Arial", 12)));
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							c.setBackgroundColor(BaseColor.RED);
							Table2.addCell(c);
							
							if (resultat.getString("Numtel")==null) {
								c= new PdfPCell(new Phrase("-",FontFactory.getFont("Arial", 12)));
								
							} else {
								c= new PdfPCell(new Phrase(resultat.getString("Numtel").toString(),FontFactory.getFont("Arial", 12)));
							}
							c.setHorizontalAlignment(Element.ALIGN_CENTER);
							Table2.addCell(c);
							
						}
						
						doc.add(Table2);//ajouter la table 
						
						doc.close();//fermer le doc
						Desktop.getDesktop( ).open(new File("PDF's\\Users.pdf"));//ouverture du pdf a la fin 
						
				} catch (FileNotFoundException | DocumentException  |SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1);
					
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1);
				}		
			}
		});
		btnImprimer.setToolTipText("Imprimer la liste des utilisateurs");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(562, 191, 64, 64);
		contentPane.add(btnImprimer);
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		ButtonStyle(btnHome);
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med.png")));//remetre le bouton de base
			}
		});
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					MenuMedcinFrame frame = new MenuMedcinFrame(ID_MED);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
			}
		});
		btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1107, 11, 32, 32);
		contentPane.add(btnHome);		
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionUsersFrame.class.getResource("/GstUsers_img/staff management.png")));
		BGStaff.setBounds(0, 0, 1200, 650);
		contentPane.add(BGStaff);
		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		//enlecer les bordures des btn
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
		
	}

//actualiser la table MEdecin///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void UpedateTableMedecin()
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select ID_Med,Nom ,Prenom  ,Ueser_Name  , DateN  ,sex ,ADR,ADRmail,Numtel ,Specilité from Medecin";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableMedecin.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableMedecin.getModel());
			tableMedecin.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'id
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier  par apport au nom
			sorter.setSortKeys(sortKeys);
			//renomer les  colones
			tableMedecin.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tableMedecin.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tableMedecin.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");	
			tableMedecin.getColumnModel().getColumn( 3 ).setHeaderValue("Nom d'utilisateur");
			tableMedecin.getColumnModel().getColumn( 4 ).setHeaderValue("Date de naissance");
			tableMedecin.getColumnModel().getColumn( 5 ).setHeaderValue("sexe");
			tableMedecin.getColumnModel().getColumn( 6 ).setHeaderValue("Adresse");
			tableMedecin.getColumnModel().getColumn( 7 ).setHeaderValue("Adresse mail");
			tableMedecin.getColumnModel().getColumn( 8 ).setHeaderValue("Numero de tlephone");
			tableMedecin.getColumnModel().getColumn( 9 ).setHeaderValue("Specialite");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
//actualiser la table Secretaire///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void UpedateTableSecretaire()
	{
		String sql ="Select  ID_sec,Nom ,Prenom  ,Ueser_Name  , DateN  ,sex ,ADR,ADRmail,Numtel   from Secretaire";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableSecretaire.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableSecretaire.getModel());
			tableSecretaire.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'id
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier  par apport au nom
			sorter.setSortKeys(sortKeys);
			//renomer les  colones
			tableSecretaire.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tableSecretaire.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tableSecretaire.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");	
			tableSecretaire.getColumnModel().getColumn( 3 ).setHeaderValue("Nom d'utilisateur");
			tableSecretaire.getColumnModel().getColumn( 4 ).setHeaderValue("Date de naissance");
			tableSecretaire.getColumnModel().getColumn( 5 ).setHeaderValue("sexe");
			tableSecretaire.getColumnModel().getColumn( 6 ).setHeaderValue("Adresse");
			tableSecretaire.getColumnModel().getColumn( 7 ).setHeaderValue("Adresse mail");
			tableSecretaire.getColumnModel().getColumn( 8 ).setHeaderValue("Numero de tlephone");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
//la verfication de la validité de la date///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public boolean verifier(String Year,String Month,String Day )
	{
		int value=1;
		switch(Month) 
		{
		case "01":
		case "03":
		case "05":
		case "07":
		case "08":
		case "10":
		case "12":
			if (Integer.parseInt(Day)>31) 
			{
			value=0;	
			}
			break;
			
		case "02":
			if( ((Integer.parseInt(Day)>29) && (Integer.parseInt(Year) % 400 )==0)||( ((Integer.parseInt(Day)>28) && (Integer.parseInt(Year) % 400 )!=0))) 
			{
				 value=0;
			}
			
			break;
			
		
		case "04":
		case "06":
		case "09":
		case "11":
			if(Integer.parseInt(Day)>30) 
			{
			value=0;	
			}
			break;
			
		default:
			value=0 ;
		}	
		if(value==0) return false;
			
		else return true;
		
		
	}
}




