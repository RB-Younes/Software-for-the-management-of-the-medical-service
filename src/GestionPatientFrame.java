import java.awt.Color;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.itextpdf.text.Document;

import net.proteanit.sql.DbUtils;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Gestion Patients------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionPatientFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablePat;
	private JButton btnModPat;
	private JButton btnRemovePat;
	
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    
    
	private JTextField NomField;
	private JTextField PrenomField;
	private JTextField TelField;
	private String id_Pat;// le id recupere en cliquant sur le tableau

	private JTextField profField;
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
					GestionPatientFrame frame = new GestionPatientFrame("S","");
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
	
	public GestionPatientFrame(String indice,String ID_med) {
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

// initialisation//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionPatientFrame.class.getResource("/Menu_Medcin_img/clipboard.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// Fields//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// textField Adresse
		JTextArea AdrtextArea = new JTextArea();
		AdrtextArea.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		AdrtextArea.getText().length() >= 300 ) //limiter le nombre de cactere a 300
		            e.consume(); 
		    }  
		});
		AdrtextArea.setForeground(new Color(255, 255, 255));
		AdrtextArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
		AdrtextArea.setBackground(new Color(102, 0, 153));
		AdrtextArea.setBorder(null);
		AdrtextArea.setBounds(130, 351, 190, 28);
		contentPane.add(AdrtextArea);
		//textField NUMTel
		TelField = new JTextField();
		TelField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		TelField.getText().length() >= 10 ) //limiter le nombre de cactere a 10
		            e.consume(); 
		    }  
		});
		TelField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
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
		TelField.setBackground(new Color(102, 0, 153));
		TelField.setBorder(null);
		TelField.setBounds(458, 157, 190, 28);
		contentPane.add(TelField);
		TelField.setColumns(10);
		
		//checkBox Homme & Femme	
		JCheckBox CheckBoxFemme = new JCheckBox();
		CheckBoxFemme.setToolTipText("Femme");
		JCheckBox CheckBoxHomme = new JCheckBox();
		CheckBoxHomme.setToolTipText("Homme");
		CheckBoxFemme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				CheckBoxHomme.setSelected(false);
			
			}
			
		});
		CheckBoxFemme.setBounds(172, 257, 21, 20);
		contentPane.add(CheckBoxFemme);
	
		CheckBoxHomme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		
				CheckBoxFemme.setSelected(false);
			}
		});
		CheckBoxHomme.setBounds(103, 257, 21, 20);
		contentPane.add(CheckBoxHomme);
	//textField Prenom	
		PrenomField = new JTextField();
		PrenomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		PrenomField.getText().length() >= 50 )  //limiter le nombre de cactere a 50
		            e.consume(); 
		    }  
		});
		PrenomField.setBounds(130, 210, 190, 28);
		PrenomField.setForeground(new Color(255, 255, 255));
		PrenomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		PrenomField.setBackground(new Color(102, 0, 153));
		PrenomField.setBorder(null);
		contentPane.add(PrenomField);
		PrenomField.setColumns(10);
		
	//textField nom	
		NomField = new JTextField();
		NomField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		NomField.getText().length() >= 50 )  //limiter le nombre de cactere a 50
		            e.consume(); 
		    }  
		});
		NomField.setBounds(130, 153, 190, 28);
		NomField.setForeground(Color.LIGHT_GRAY);
		NomField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		NomField.setBackground(new Color(102, 0, 153));
		NomField.setBorder(null);
		contentPane.add(NomField);
		NomField.setColumns(10);
		
	//textField Profession	
		profField = new JTextField();
		profField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		profField.getText().length() >= 200 )  //limiter le nombre de cactere a 200
		            e.consume(); 
		    }  
		});
		profField.setForeground(Color.WHITE);
		profField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		profField.setColumns(10);
		profField.setBorder(null);
		profField.setBackground(new Color(102, 0, 153));
		profField.setBounds(458, 210, 190, 28);
		contentPane.add(profField);
		
	//ComboBox Année
		JComboBox<Object> CBYear = new JComboBox<Object>();
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2020; i>=1900 ; i--) {
			String Year =""+i+"";
			CBYear.addItem(Year);		
			}
		
		CBYear.setBackground(new Color(102, 0, 153));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(250, 305, 70, 20);
		contentPane.add(CBYear);
		
	//ComboBox Mois	
		JComboBox<Object> CBMonth = new JComboBox<Object>();
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setBackground(new Color(102, 0, 153));
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBounds(182, 305, 70, 20);
		contentPane.add(CBMonth);
		
	//ComboBox jours
		JComboBox<Object> CBDay = new JComboBox<Object>();
		CBDay.setModel(new DefaultComboBoxModel<Object>(new String[] {"Jour"}));

		for (int j = 1; j<=31 ; j++) {
			
			if (j<=9) {
				String Day ="0"+j+"";CBDay.addItem(Day);	
			}
			else {
				String Day =""+j+"";CBDay.addItem(Day);	
			}
			
				
			}
		CBDay.setBackground(new Color(102, 0, 153));
		CBDay.setForeground(new Color(255, 255, 255));
		CBDay.setBounds(122, 305, 64, 20);
		contentPane.add(CBDay);
		
	//textField Adresse mail	
		textFieldADRmail = new JTextField();
		textFieldADRmail.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldADRmail.getText().length() >= 50 )  //limiter le nombre de cactere a 50
		            e.consume(); 
		    }  
		});
		textFieldADRmail.setForeground(Color.WHITE);
		textFieldADRmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldADRmail.setColumns(10);
		textFieldADRmail.setBorder(null);
		textFieldADRmail.setBackground(new Color(102, 0, 153));
		textFieldADRmail.setBounds(130, 400, 190, 28);
		contentPane.add(textFieldADRmail);
		
//ScrollPane/Table Patient//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JScrollPane scrollPanePat = new JScrollPane();
		scrollPanePat.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePat.setBackground(Color.WHITE);
		scrollPanePat.setBounds(423, 272, 752, 217);
		contentPane.add(scrollPanePat);
		
		tablePat = new JTable();
		tablePat.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tablePat.setForeground(new Color(255, 255, 255));
		tablePat.setBorder(null);
		tablePat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les deux boutons Modifer et supprimer
				btnRemovePat.setEnabled(true);
				btnModPat.setEnabled(true);
				int ligne=tablePat.getSelectedRow();//recuperer le numero de ligne
				

				id_Pat =tablePat.getValueAt(ligne, 0).toString();//recuperer l'ID du Patient qui est a la premiere colone (0) de la table
				String sql="Select * from Patient where Id_Patient='"+id_Pat+"'";//Selectionner toute les info du Patient aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					NomField.setText(resultat.getString("Name"));// mettre le Nom du Patient recuperé dans le TextField Nom
					PrenomField.setText(resultat.getString("FirstName"));// mettre le PreNom du Patient dans le TextField PreNom
					
					
					String DN=resultat.getString("BirthDate");// recuperer la date de naissance du patient
				    if (DN!=null)
				    {
				    	DN=(String) DN.subSequence(0, 10);//pour ne pas prendre la partie HH:mm:ss
				    	//parcourir la combobox Année
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
						{
								if (CBYear.getItemAt(i).equals(DN.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);}//selectionner cet element
							
						}
				    	//parcourir la combobox Mois
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
						{
								if (CBMonth.getItemAt(i).equals(DN.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);}//selectionner cet element
							
						}
				    	//parcourir la combobox jour
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
						{
								if (CBDay.getItemAt(i).equals(DN.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);}//selectionner cet element
						}

				    }
					String sexe=resultat.getString("Sex");// recuperer la sexe  du patient
					if(sexe.equals("Homme")) {CheckBoxHomme.setSelected(true);CheckBoxFemme.setSelected(false);}// le cas Homme
					if(sexe.equals("Femme")) {CheckBoxFemme.setSelected(true);CheckBoxHomme.setSelected(false);}//le cas femme
					if(sexe.equals("null")) { CheckBoxFemme.setSelected(false);CheckBoxHomme.setSelected(false);}// le cas ou ce n'est pas mentionnée
						
					profField.setText(resultat.getString("Prof"));// mettre la profession du Patient recuperé dans le TextField profession
					AdrtextArea.setText(resultat.getString("ADR"));// mettre l'adresse du Patient recuperé dans le TextField adresse
					TelField.setText(resultat.getString("PhoneNum"));// mettre le Numero de tel du Patient recuperé dans le TextField Numero de tlephone
					textFieldADRmail.setText(resultat.getString("ADRmail"));// mettre l'adresse mail du Patient recuperé dans le TextField Nom
					
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
				
				
			}
		});
		tablePat.setBackground(new Color(102, 0, 153));
		scrollPanePat.setViewportView(tablePat);
//afficher le contenue de la table juste a louverture//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTablePat();
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Minimize ML .png")));	//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1039, 11, 32, 32);
		contentPane.add(Minimise_BTN);
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						//  vider les fields et remetre les combobox et check box par default(les valeurs)
						
						CBYear.setSelectedIndex(0);
						CBDay.setSelectedIndex(0);
						CBMonth.setSelectedIndex(0);
						AdrtextArea.setText("");
						TelField.setText("");
						NomField.setText("");	
						PrenomField.setText("");
						profField.setText("");
						CheckBoxFemme.setSelected(false);
						CheckBoxHomme.setSelected(false);
						
						
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1123, 11, 32, 32);
		contentPane.add(Exit_BTN);

// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("Clear the fields");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CBYear.setSelectedIndex(0);
				CBDay.setSelectedIndex(0);
				CBMonth.setSelectedIndex(0);
				AdrtextArea.setText("");
				TelField.setText("");
				NomField.setText("");	
				PrenomField.setText("");
				profField.setText("");
				CheckBoxFemme.setSelected(false);
				CheckBoxHomme.setSelected(false);
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/rubbish.png")));
		btnViderFields.setBounds(616, 105, 32, 32);
		contentPane.add(btnViderFields);
// Bouton Imprimer ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnImprimer = new JButton("");
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/print pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/print pat.png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/print pat.png")));
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc= new Document();
				Patient pat= new Patient();
				pat.Imprimer(doc);// appeler la methode imprimer de la classe M
			
			}
		});
		btnImprimer.setToolTipText("Print");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(830, 140, 64, 64);
		contentPane.add(btnImprimer);

//boutn Ajouter,Supprimer,Modifier////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Ajouter un Patient**********************************************************************************************************************************************************************************************************************************
		JButton btnAddPat = new JButton("");
		btnAddPat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/add pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/add pat.png")));//remetre le bouton de base
			}
		});
		btnAddPat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment ajouter ce Patient ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
								
						String Nom =NomField.getText().toString();//recuperer le contenu du Textfield Nom
						String Prenom=PrenomField.getText().toString();//recuperer le contenu du Textfield PreNom
						
						//recuperer le Sexe
						String Sexe = null;   
						if (CheckBoxHomme.isSelected()) { Sexe="Homme";}
						if (CheckBoxFemme.isSelected()) { Sexe="Femme";}
					
						String Adr=AdrtextArea.getText().toString();//recuperer le contenu du Textfield Adresse
						String NumT=TelField.getText().toString();//recuperer le contenu du Textfield Numero de tlephone
						String prof =profField.getText().toString();//recuperer le contenu du Textfield Profession
						String ADRmail = textFieldADRmail.getText().toString();//recuperer le contenu du Textfield Adresse Mail
					
						if ( Nom.equals("")|| Prenom.equals("")) //verifier si le nom et le prenom on ete bien ecrit 
							{//un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez remplir tous les champs marqué par '*' !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
						else 
						{	
							//recuperer la date de naiisance et la rendre sous forme dd-MM-yyyy
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							//verifier la validite de la date (bissextile...)
							if (!B) //si la date est invalide
								{//un message d'erreur
									JOptionPane.showMessageDialog(null,  " Date de naissance Invalide !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}
							else 
							{
								//recuperer la date du systeme
								Date actuelle = new Date();
					 			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					 			String datactuelle = dateFormat.format(actuelle);  
					 			
								Patient Pat=new  Patient(Nom, Prenom, DN, Sexe, Adr,ADRmail,NumT,prof,datactuelle);//creeer une instance de Patient
								Pat.Ajouter(Pat);//ajouter le medicament a la table a l'aide de la methode ajouter de la classe Patient
								//desactiver les boutons supprimer et modifier
								btnRemovePat.setEnabled(false);
								btnModPat.setEnabled(false);
								UpedateTablePat();//actualiser la table des Patient
							}
						}
					}
			}
		});
		btnAddPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/add pat.png")));
		btnAddPat.setToolTipText("Ajouter Patient");
		btnAddPat.setBounds(804, 215, 32, 32);
		contentPane.add(btnAddPat);
	//Modifier un Patient**********************************************************************************************************************************************************************************************************************************	
		btnModPat = new JButton("");
		btnModPat.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModPat.setToolTipText("Modifier Patient");
		btnModPat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/mod pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/mod pat.png")));//remetre le bouton de base
			}
		});
		btnModPat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier les informations de ce Patient ? ", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					//recuperer le Sexe
					String sexe=null; 
					if(CheckBoxHomme.isSelected()) {  sexe ="Homme";}
					if(CheckBoxFemme.isSelected()) {  sexe ="Femme";}
					String ADRmail = textFieldADRmail.getText().toString();
				
					String id =id_Pat;	//recuperer l'id du patient
				
					if (tablePat.getSelectedRow()==-1) //aucune ligne de la table n'est selectionnée 
					{// message 
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a modifer !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else {
							String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							//verifier la validite de la date (bissextile...)
							if (!B) //si la date est invalide
								{
								//un message d'erreur
								JOptionPane.showMessageDialog(null,  "Date de naissance Invalide!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}
							else 
								{
								Patient Pat=new  Patient();
								Pat.MetreAjour(id,NomField,PrenomField,DN,sexe,AdrtextArea,ADRmail,TelField,profField);// appler la methode mettreajour pour modifier les infos d'un Patient
								//desactiver les boutons supprimer et modifier
								btnRemovePat.setEnabled(false);
								btnModPat.setEnabled(false);
								UpedateTablePat();//actualiser la table
								}
						}
				}
			}
		});
		btnModPat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/mod pat.png")));
		btnModPat.setBounds(888, 215, 32, 32);
		contentPane.add(btnModPat);
	
	//Supprimer un medicament**********************************************************************************************************************************************************************************************************************************
		btnRemovePat = new JButton("");
		btnRemovePat.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnRemovePat.setToolTipText("Supprimer Patient");
		btnRemovePat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRemovePat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/remove pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRemovePat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/remove pat.png")));//remetre le bouton de base
			}
		});
		btnRemovePat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (tablePat.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
					{//afficher un message d'erreur
						JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a supprimer !","Message", JOptionPane.QUESTION_MESSAGE,new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
				else{
					 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment supprimer ce Patient de la table ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{	
					
						Patient Pat=new  Patient();
						Pat.Suprimer(id_Pat,indice);//appele de la Methode supprimer de la classe Medicament
						//desactiver les boutons supprimer et modifier
						btnRemovePat.setEnabled(false);
						btnModPat.setEnabled(false);
						UpedateTablePat();//Actualiser la table
						}
					}
			}
		});
		
		btnRemovePat.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/remove pat.png")));
		btnRemovePat.setBounds(846, 215, 32, 32);
		contentPane.add(btnRemovePat);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		if (indice.equals("G")) {//le cas ou il est appeler par la fenetre de gestionde RDV
			btnHome.setVisible(false);//enlever le bouton home
			btnRemovePat.setVisible(false);// et le bouton supprimer patient
		}
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/home sec Selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHome.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/home sec.png")));//remetre le bouton de base
			}
		});
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (indice.equals("S")) {//S pour secretaire donc retourner au menu secretaire
					MenuSecretaireFrame frame = new MenuSecretaireFrame();// retourner au menu secretaire
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {
					if (indice.equals("M")) {// lappel se fait par la frame Medecin
						MenuMedcinFrame frame = new MenuMedcinFrame(ID_med);// retourner au menu medecin
						frame.setLocationRelativeTo(null);
						frame.setVisible(true);
					dispose();
					}
					
				}
			}
		});
		btnHome.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/home sec.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1081, 11, 32, 32);
		contentPane.add(btnHome);
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionPatientFrame.class.getResource("/GstPat_img/BG gestion pat fr.png")));
		BGStaff.setBounds(0, 0, 1201, 550);
		contentPane.add(BGStaff);
		

		
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}
//actualisation de la table/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void UpedateTablePat()
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select ID_Patient,Name,FirstName,BirthDate ,sex ,ADR,ADRmail ,PhoneNum ,Prof ,DateEng from Patient";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tablePat.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tablePat.getModel());
			tablePat.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));//trier  par apport a la date d'ajout(date d'enregistrement)
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'ID
			sorter.setSortKeys(sortKeys);
		
			//renomer les noms de colones
			tablePat.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tablePat.getColumnModel().getColumn( 1 ).setHeaderValue("Nom");
			tablePat.getColumnModel().getColumn( 2 ).setHeaderValue("Prenom");	
			tablePat.getColumnModel().getColumn( 3 ).setHeaderValue("Date de naissance");	
			tablePat.getColumnModel().getColumn( 4 ).setHeaderValue("sexe");
			tablePat.getColumnModel().getColumn( 5 ).setHeaderValue("Adresse");
			tablePat.getColumnModel().getColumn( 6 ).setHeaderValue("@");
			tablePat.getColumnModel().getColumn( 7 ).setHeaderValue("N°phone");
			tablePat.getColumnModel().getColumn( 8 ).setHeaderValue("Profession");
			tablePat.getColumnModel().getColumn( 9 ).setHeaderValue("Date Enregistrement");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
	
//la verfication de la validité de la date ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
	
	

