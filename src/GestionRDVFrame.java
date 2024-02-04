import java.awt.Color;
import java.awt.Component;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.itextpdf.text.Document;

import net.proteanit.sql.DbUtils;
import java.awt.SystemColor;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Gestion Rendez Vous------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class GestionRDVFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable Minitable;
	private JTable RDVtable;
	private String datactuelle="";
	private int b= 0;
	private JButton btnModRDV;
	private JButton btnRemoveRDV;
	private JTextField txtrecherche;
	private JList<String> listPatients;
	private GestionPatientFrame framePat= new GestionPatientFrame("G","");
	private JComboBox<String> comboBoxPAtients;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String num_rdv;// le id recupere en cliquant sur le tableau
	
	
	
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
					GestionRDVFrame frame = new GestionRDVFrame("S","2");
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
	
	public GestionRDVFrame(String indice,String ID_med) {
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

// initialisation////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 650d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GestionRDVFrame.class.getResource("/Menu_Medcin_img/RDV.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 650);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//Commentaire textArea
		JTextArea ComtextArea = new JTextArea();
		ComtextArea.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (ComtextArea.getText().length() >= 500 ) 
		            e.consume(); 
		    }  
		});
		ComtextArea.setForeground(new Color(255, 255, 255));
		ComtextArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
		ComtextArea.setBackground(new Color(102, 0, 153));
		ComtextArea.setBorder(null);
		ComtextArea.setBounds(134, 407, 217, 32);
		contentPane.add(ComtextArea);
	//Liste des docteurs (ComboBox)	
		JComboBox<String> comboBoxDoctors = new JComboBox<String>();
		comboBoxDoctors.addItem("Selectionner Medecin");
		INComboboxDoc(comboBoxDoctors);// appler la fonction pour les mettre dans la combo box
		comboBoxDoctors.setBackground(new Color(102, 0, 153));
		comboBoxDoctors.setForeground(new Color(255, 255, 255));
		comboBoxDoctors.setBounds(134, 252, 217, 32);
		contentPane.add(comboBoxDoctors);
	//Liste des patients (ComboBox)	
		comboBoxPAtients = new JComboBox<String>();
		comboBoxPAtients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) {
					// a la selection d'un patient on va retirer le nom et le prenom de ce patient et le mettre dans le text field de la recherche 
					 String Name_pat=" ";//elle va retenir le nom
					 String FirstName_pat=" ";//le prenom

					String PatInfo=comboBoxPAtients.getSelectedItem().toString();//Patinfo et de la forme ID_pat-Nom Prenom
						 int finIDindice=PatInfo.indexOf("-");//donc lindice de fin du l ID est "-"(va retourner lindice de fin de l'ID)
						 int finnomindice=PatInfo.indexOf(" ");//le premier blanc sera pour deli*emiter entre le Nom et le prenom(va retourner lindice de fin du Nom)
						 int finprenomindice=PatInfo.length();
						 if (finIDindice != -1 && finnomindice != -1 && finprenomindice != -1) 
						 {
							 Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);
							  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);
						 }
						 txtrecherche.setText(Name_pat+" "+FirstName_pat);// remtre le nom prenom dans le text field de la recherche
					}
			}
		});
		comboBoxPAtients.addItem("Selectionner Patient");
		INComboboxPat(comboBoxPAtients);// appler la fonction pour les mettre dans la combo box
		comboBoxPAtients.setBackground(new Color(102, 0, 153));
		comboBoxPAtients.setForeground(new Color(255, 255, 255));
		comboBoxPAtients.setBounds(134, 192, 217, 32);
		contentPane.add(comboBoxPAtients);
		
    // recuperer l annee du systeme pour la metre dans  CByear		
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		String annee = dateFormat.format(actuelle);
	//Année ComboBox	
		JComboBox<Object> CBYear = new JComboBox<Object>();
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {}));		
		for (int i = 2020; i<=2100 ; i++) {
			String Year =""+i+"";
			CBYear.addItem(Year);		
			}
		CBYear.setSelectedItem(annee);//selectionner l année actuelle
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBackground(new Color(102, 0, 153));
		CBYear.setBounds(273, 316, 78, 29);
		contentPane.add(CBYear);
		
	// recuperer le mois du systeme pour la metre dans  CBMonth			
		dateFormat = new SimpleDateFormat("MM");
		String mois=dateFormat.format(actuelle);
	// Mois comoboBox	
		JComboBox<Object> CBMonth = new JComboBox<Object>();
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setSelectedItem(mois);//selectionner le mois actuelle
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBackground(new Color(102, 0, 153));
		CBMonth.setBounds(196, 316, 70, 29);
		contentPane.add(CBMonth);
		
	// recuperer le jour du systeme pour la metre dans  CBDay			
		dateFormat = new SimpleDateFormat("dd");
		String jour=dateFormat.format(actuelle);
	// Jours ComboBox	
		JComboBox<Object> CBDay = new JComboBox<Object>();
		CBDay.setModel(new DefaultComboBoxModel<Object>(new String[] {}));
		for (int j = 1; j<=31 ; j++) {
			
			if (j<=9) {
				String Day ="0"+j+"";CBDay.addItem(Day);	
			}
			else {
				String Day =""+j+"";CBDay.addItem(Day);	
			}
				
			}
		CBDay.setSelectedItem(jour);//selectionner le jours actuelle
		CBDay.setBackground(new Color(102, 0, 153));
		CBDay.setForeground(new Color(255, 255, 255));
		CBDay.setBounds(130, 316, 58, 29);
		contentPane.add(CBDay);
		
	// heure du RDV Spinner
		JSpinner spinnerH = new JSpinner();
		spinnerH.setSize(100, 25);
		spinnerH.setLocation(134, 368);
		spinnerH.setModel(new SpinnerDateModel());
		spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));
		contentPane.add(spinnerH);
//ScrollPane Table Patient////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
	//Grande table avec toute les informations***************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneRDV = new JScrollPane();
		scrollPaneRDV.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneRDV.setBackground(new Color(204, 102, 255));
		scrollPaneRDV.setBounds(413, 405, 765, 217);
		contentPane.add(scrollPaneRDV);
		RDVtable = new JTable();
		RDVtable.setFont(new Font("Segoe UI", Font.BOLD, 11));
		RDVtable.setForeground(new Color(255, 255, 255));
		RDVtable.setBorder(null);
		RDVtable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les deux boutons Modifier et supprimer
				btnRemoveRDV.setEnabled(true);
				btnModRDV.setEnabled(true);
				//si je clique sur une ligne (selectionne)  les vleurs de la ligne selectionné seron automatiquement affiché
				int ligne=RDVtable.getSelectedRow();//recuperer le numero de ligne
		
				num_rdv =RDVtable.getValueAt(ligne, 0).toString();//recuperer l'ID du RDV qui est a la premiere colone (0) de la table
				// le getModel causer une erreu c que la table gardé l'ancien model 
			
				String sql="Select * from RDV where NUM_RDV='"+num_rdv+"'";//Selectionner toute les info du RDV aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					
						String RDVDH =resultat.getString("Date_RDV").toString();//recupperer la date du rdv
						
						if (RDVDH!=null)
						{
							//Deviser  la date sur les combobox Mois Jours Année
				    	String RDVD=RDVDH.substring(0, 10); // on recupaire la date du RDV 
				    	//parcourir la combobox Année
				    	for (int i = 0; i < CBYear.getItemCount(); i++) 
							{
								if (CBYear.getItemAt(i).equals(RDVD.subSequence(0,4 ))) { CBYear.setSelectedIndex(i);}//selectionner cet element
							}
				    	//parcourir la combobox MOis
				    	for (int i = 0; i < CBMonth.getItemCount(); i++) 
							{
								if (CBMonth.getItemAt(i).equals(RDVD.subSequence(5,7 ))) { CBMonth.setSelectedIndex(i);}//selectionner cet element
							}
				    	//parcourir la combobox Jours
				    	for (int i = 0; i < CBDay.getItemCount(); i++) 
							{
								if (CBDay.getItemAt(i).equals(RDVD.subSequence(8,10 ))) { CBDay.setSelectedIndex(i);}//selectionner cet element
							}
				    	
						}
						// mettre l Heure du Rdv dansle spinner
						String RDVH=resultat.getString("H_RDV").toString();	// recupperer la date du RDV				
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");// definir le format
						Date H_RDV = sdf.parse(RDVH); // transformet le string de la table en format date pour pouoir modifier (avec les boutons du spinner)
						spinnerH.setValue(H_RDV); //mettre la valur dans le spinner
					
						// le parcours de la comboboxDoc
						for (int i = 0; i < comboBoxDoctors.getItemCount(); i++) 
						{
								if (comboBoxDoctors.getItemAt(i).equals(resultat.getString("ID_Med").toString()+"-"+resultat.getString("Doctor_Name").toString()+"("+resultat.getString("SpE").toString()+")")) {// selectionner cet element
									comboBoxDoctors.setSelectedIndex(i);
								}
							
						}
						// le parcours de la comboboxPAtient
						for (int j = 0; j < comboBoxPAtients.getItemCount(); j++) 
						{
							
								if (comboBoxPAtients.getItemAt(j).equals(resultat.getString("ID_Pat").toString()+"-"+resultat.getString("Patient_Name").toString())) { // selectionner cet element
									comboBoxPAtients.setSelectedIndex(j);
								}
							
						}
						
						ComtextArea.setText(resultat.getString("Commentary"));// mettre le Commentaire du RDV dans le TextArea Commentaire
					
					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		RDVtable.setBackground(new Color(102, 0, 153));
		scrollPaneRDV.setViewportView(RDVtable);
		
		
		
	//Mini table (que pour laffichage des RDV specifique)***************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneMini = new JScrollPane();
		scrollPaneMini.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneMini.setBackground(new Color(204, 102, 255));
		scrollPaneMini.setBounds(533, 213, 545, 142);
		contentPane.add(scrollPaneMini);
		
		Minitable = new JTable();
		Minitable.setFont(new Font("Segoe UI", Font.BOLD, 11));
		Minitable.setForeground(new Color(255, 255, 255));
		Minitable.setBorder(null);

		Minitable.setBackground(new Color(102, 0, 153));
		scrollPaneMini.setViewportView(Minitable);
		
		
//afficher le contenue de la table juste a l'ouverture////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		UpedateTableRDV();
	
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1059, 11, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// remtre les valeurs par default de la combobox
				CBYear.setSelectedIndex(0);
				CBDay.setSelectedIndex(0);
				CBMonth.setSelectedIndex(0);
				comboBoxDoctors.setSelectedIndex(0);
				comboBoxPAtients.setSelectedIndex(0);
				spinnerH.setModel(new SpinnerDateModel());
				spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));
				//vider les text filds
				ComtextArea.setText("");
				comboBoxDoctors.setSelectedIndex(0);
			
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/rubbish.png")));
		btnViderFields.setBounds(321, 491, 32, 32);
		contentPane.add(btnViderFields);

// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						//reinitialiser les comboBoxs
						CBYear.setSelectedIndex(0);
						CBDay.setSelectedIndex(0);
						CBMonth.setSelectedIndex(0);
						ComtextArea.setText("");
						comboBoxDoctors.setSelectedIndex(0);
						
						if (framePat.isVisible()) {/*si la fenere de gestion de patient est vesible alors si on ferme la fenetre principale qui a fait appelle a 
							cette derniere(Gestion de patient) alors la fentre secondaire sera elle aussi fermé*/
							framePat.dispose();
						}
						
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1143, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
// Bouton Imprimer ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnImprimer = new JButton("");
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/print pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
					btnImprimer.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/print pat.png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/print pat.png")));
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc= new Document();
				RDV rdv = new RDV();
				rdv.Imprimer(doc);// appeler la methode imprimer de la classe RDV
			}
				
		});
		btnImprimer.setToolTipText("Imprimer");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(184, 534, 64, 64);
		contentPane.add(btnImprimer);
		
//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Ajouter un RDV**********************************************************************************************************************************************************************************************************************************
		JButton btnAddRDV = new JButton("");
		btnAddRDV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/add pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/add pat.png")));//remetre le bouton de base
			}
		});
		btnAddRDV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				icon.getImage().flush();// réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment ajouter ce Rendez-Vous ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					//les declarations			
					int ID_med=1 ;
					String Name_med=" ";
					String FirstName_med=" ";
					String Spe_Med=" ";

					int ID_pat = 1;
					 String Name_pat=" ";
					 String FirstName_pat=" ";

					String DocInfo=comboBoxDoctors.getSelectedItem().toString();//recupperer les infos du medecin de la forme ID_medecin-Nom Prenom(specialité) 
					
					if (!DocInfo.equals("Selectionner Medecin")) {
						 int finIDindice=DocInfo.indexOf("-");//indice de fin de l'id
						 int finnomindice=DocInfo.indexOf(" ");//indice de fin de nom
						 int finprenomindice=DocInfo.indexOf("(");//indice de fin prenom
						 int finspeindice=DocInfo.indexOf(")");//indice de fin specialite
						 
						 if (finIDindice != -1) 
						 {
							 String Id_med= DocInfo.substring(0 , finIDindice); //recuperer  ID du medecin
							 Name_med=DocInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du medecin
							 FirstName_med=DocInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du medecin
							 Spe_Med=DocInfo.substring(finprenomindice+1 , finspeindice);//recupererSpecialite du medecin
							 ID_med=Integer.parseInt(Id_med); //transformer l'id en int
						 }

					}
					String PatInfo=comboBoxPAtients.getSelectedItem().toString();//recupperer les infos du Patient de la forme ID_patient-Nom Prenom
					
					if (!PatInfo.equals("Selectionner Patient")) {
						 int finIDindice=PatInfo.indexOf("-");//indice de fin de l'id
						 int finnomindice=PatInfo.indexOf(" ");//indice de fin de nom
						 int finprenomindice=PatInfo.length();//indice de fin de prenom
						 if (finIDindice != -1) 
						 {
							  String Id_pat= PatInfo.substring(0 , finIDindice); //recuperer  ID du patient
							  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du patient
							  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du patient
							  ID_pat=Integer.parseInt(Id_pat); //transformer l'id en int
							
						 }
						}
					String RDVD=CBDay.getSelectedItem().toString()+"/"+CBMonth.getSelectedItem().toString()+"/"+CBYear.getSelectedItem().toString();// la date du RDV
					String RDVH=spinnerH.getValue().toString().substring(11,16);//heure du RDV
					
					
					String Com = ComtextArea.getText().toString();//commentaire
					
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());//la validite de la date retourn vrais si la date  est valide
							if (!B) 
								{//message d'erreur
									JOptionPane.showMessageDialog(null,  " Date de Rendez-vous Invalide !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}
							else 
							{
							if (DocInfo.equals("Selectionner Medecin") || PatInfo.equals("Selectionner Patient"))//si le medecin ou le patient n'on pas ete selectionné 
								 {//message d'erreur
									JOptionPane.showMessageDialog(null,  "S'il vous plait slectionnez un Medecin et un patient!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								 }
							else {
									if (VALIDRDV(ID_med, RDVD, RDVH, ID_pat)==1) //verifier si le rdv et valide (n'est pas deja pris par par un autre patient)
									{	//message d'erreur
										JOptionPane.showMessageDialog(null, "Rendez vous deja pris !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
									} 
									else 
									{
											if (VALIDRDV(ID_med, RDVD, RDVH, ID_pat)==2) //verifier si le rdv et valide si il n'a pas deja un rdv a cette heure
											{//message d'erreur
												JOptionPane.showMessageDialog(null, " Ce patient A deja un Rendez-vous a cette heure !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")) );
											} 
											else {
												RDV rdv = new RDV(RDVD,RDVH, ID_med	,ID_pat,Name_med+" "+FirstName_med, Spe_Med,Name_pat+" "+FirstName_pat, Com);// creer une instance de RDV
												rdv.Ajouter(rdv);//appler la fonction ajouter 
												//desactiver les boutons supprimer et modifier
												btnRemoveRDV.setEnabled(false);
												btnModRDV.setEnabled(false);
												UpedateTableRDV();//actualiser la table
												}
									}
								}
							}
						}
					}
			
		});
		btnAddRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/add pat.png")));
		btnAddRDV.setToolTipText("Ajouter RDV");
		btnAddRDV.setBounds(156, 491, 32, 32);
		contentPane.add(btnAddRDV);
		
	//Modifier un RDV**********************************************************************************************************************************************************************************************************************************
		btnModRDV = new JButton("");
		btnModRDV.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModRDV.setToolTipText("modifier RDV");
		btnModRDV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/mod pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/mod pat.png")));//remetre le bouton de base
			}
		});
		btnModRDV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				icon.getImage().flush();// réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment Modifier ce Rendez-vous  ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					
					
					int ID_med=1 ;
					String Name_med=" ";
					String FirstName_med=" ";
					String Spe_Med=" ";

					int ID_pat = 1;
					 String Name_pat=" ";
					 String FirstName_pat=" ";

						String DocInfo=comboBoxDoctors.getSelectedItem().toString();//recupperer les infos du medecin de la forme ID_medecin-Nom Prenom(specialité) 
						
						if (!DocInfo.equals("Selectionner Medecin")) {
							 int finIDindice=DocInfo.indexOf("-");//indice de fin de l'id
							 int finnomindice=DocInfo.indexOf(" ");//indice de fin de nom
							 int finprenomindice=DocInfo.indexOf("(");//indice de fin prenom
							 int finspeindice=DocInfo.indexOf(")");//indice de fin specialite
							 
							 if (finIDindice != -1) 
							 {
								 String Id_med= DocInfo.substring(0 , finIDindice); //recuperer  ID du medecin
								 Name_med=DocInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du medecin
								 FirstName_med=DocInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du medecin
								 Spe_Med=DocInfo.substring(finprenomindice+1 , finspeindice);//recupererSpecialite du medecin
								 ID_med=Integer.parseInt(Id_med); //transformer l'id en int
							 }

						}
						String PatInfo=comboBoxPAtients.getSelectedItem().toString();//recupperer les infos du Patient de la forme ID_patient-Nom Prenom
						
						if (!PatInfo.equals("Selectionner Patient")) {
							 int finIDindice=PatInfo.indexOf("-");//indice de fin de l'id
							 int finnomindice=PatInfo.indexOf(" ");//indice de fin de nom
							 int finprenomindice=PatInfo.length();//indice de fin de prenom
							 if (finIDindice != -1) 
							 {
								  String Id_pat= PatInfo.substring(0 , finIDindice); //recuperer  ID du patient
								  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du patient
								  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du patient
								  ID_pat=Integer.parseInt(Id_pat); //transformer l'id en int
								
							 }
							}
				
						String RDVD=CBDay.getSelectedItem().toString()+"/"+CBMonth.getSelectedItem().toString()+"/"+CBYear.getSelectedItem().toString();// la date du RDV
						String RDVH=spinnerH.getValue().toString().substring(11,16);//heure du RDV
					
					
					String Com = ComtextArea.getText().toString();//commentaire
				
				
					if (RDVtable.getSelectedRow()==-1) 
					{
						JOptionPane.showMessageDialog(null, "S'il vous plait slectionnez un Rendez-vous a Modifier de la table  !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else {
							//String DN=CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
							boolean B=verifier(CBYear.getSelectedItem().toString(), CBMonth.getSelectedItem().toString(), CBDay.getSelectedItem().toString());
							if (!B) 
								{
								JOptionPane.showMessageDialog(null,  "Date de Rendez-vous Invalide !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}	
							else 
								{
								if (DocInfo.equals("Selectionner Medecin") || PatInfo.equals("Selectionner Patient")) {
									
									JOptionPane.showMessageDialog(null,  "S'il vous plait slectionnez un Medecin et un patient!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								} 
								else {
									if (VALIDRDV(ID_med, RDVD, RDVH, ID_pat)==1) //verifier si le rdv et valide (n'est pas deja pris par par un autre patient)
									{	//message d'erreur
										JOptionPane.showMessageDialog(null, "Rendez vous deja pris !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
									} 
									else 
									{
											if (VALIDRDV(ID_med, RDVD, RDVH, ID_pat)==2) //verifier si le rdv et valide si il n'a pas deja un rdv a cette heure
											{//message d'erreur
												JOptionPane.showMessageDialog(null, " Ce patient A deja un Rendez-vous a cette heure !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")) );
											} 
											else {
													RDV rdv = new RDV();// creer une instance de RDV
													rdv.MetreAjour(RDVD, RDVH, ID_med, Name_med+" "+FirstName_med, Spe_Med, ID_pat, Name_pat+" "+FirstName_pat, Com, num_rdv);//appler la fonction ajouter
													//desactiver les boutons supprimer et modifier
													btnRemoveRDV.setEnabled(false);
													btnModRDV.setEnabled(false);
													UpedateTableRDV();//actualiser la table
												}
									}
								
									
								}
								}
						}
				}
			}
		});
		btnModRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/mod pat.png")));
		btnModRDV.setBounds(198, 491, 32, 32);
		contentPane.add(btnModRDV);
		
	//Supprimer un RDV**********************************************************************************************************************************************************************************************************************************
		btnRemoveRDV = new JButton("");
		btnRemoveRDV.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnRemoveRDV.setToolTipText("Supprumer RDV");
		btnRemoveRDV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRemoveRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/remove pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRemoveRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/remove pat.png")));//remetre le bouton de base
			}
		});
		btnRemoveRDV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (RDVtable.getSelectedRow()==-1) //verifier si une ligne de la table a ete selectionné
					{//message
						JOptionPane.showMessageDialog(null, " S'il vous plait slectionnez un Rendez-vous a supprimer de la table !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
				else{
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
					icon.getImage().flush();// réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment suprimer ce Rendez-vous de la table  ?","Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{	
						RDV rdv=new  RDV();
						rdv.Suprimer(num_rdv,indice);//appele de la Methode supprimer de la classe RDV
						//desactiver les boutons supprimer et modifier
						btnRemoveRDV.setEnabled(false);
						btnModRDV.setEnabled(false);
						UpedateTableRDV();//Actualiser la table
						}
					}
			}
		});
		btnRemoveRDV.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/remove pat.png")));
		btnRemoveRDV.setBounds(240, 491, 32, 32);
		contentPane.add(btnRemoveRDV);
		
//Bouton actualiser la table(Grande)  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnalltheTable = new JButton("");
		btnalltheTable.setToolTipText("Actualiser");
		btnalltheTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/refresh seected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/refresh.png")));//remetre le bouton de base
			}
		});
		btnalltheTable.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/refresh.png")));
		btnalltheTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpedateTableRDV();//appele de la fonction Acualisation
				//desactiver les boutons supprimer et modifier
				btnRemoveRDV.setEnabled(false);
				btnModRDV.setEnabled(false);
			}
		});
		btnalltheTable.setBounds(371, 590, 32, 32);
		contentPane.add(btnalltheTable);
//Les boutons de la Mini table ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//Check Box qui va afficher sur la mini table que les RDV du jours***********************************************************************************************************************************************************************************
		JCheckBox chckbxcejour = new JCheckBox("Afficher que les Rendez vous d'aujourd'hui");
		chckbxcejour.setForeground(new Color(102, 0, 153));
		chckbxcejour.setFont(new Font("Segoe UI", Font.BOLD, 13));
		chckbxcejour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			if (chckbxcejour.isSelected()) {// lecas ou ona cheque la check box
					
					Date actuelle = new Date();
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					datactuelle = dateFormat.format(actuelle);//recuperer la date du systeme
						if(b==1)// le premier bouton est cliqué (afficher les rdv du medecin selectionné)
						{
							if (!comboBoxDoctors.getSelectedItem().toString().equals("Selectionner Medecin")) {//si le Medecin a ete selectionné
								String DocInfo =comboBoxDoctors.getSelectedItem().toString();//recuperer les info du Medecin de la comboBox
								int iend=DocInfo.indexOf("-");//indice de la fin de l'id
								String Id_med="00";
								if (iend != -1) 
								{
									Id_med= DocInfo.substring(0 , iend); //recuperer l'id du Medecin 
								}
								String sql ="Select Doctor_Name,SPE , Patient_Name , Date_RDV,H_RDV from RDV where ID_MED='"+ Id_med + "' and Date_RDV='" +datactuelle + "' ";//selectionner les rdv qui contiennent l'id du Medecin et la date actuelle (du systeme)
								try {
									prepared = cnx.prepareStatement(sql);
									resultat =prepared.executeQuery();
									Minitable.setModel(DbUtils.resultSetToTableModel(resultat));//mettre le model de la mini table 
									
									TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
									Minitable.setRowSorter(sorter);
									List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
									sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));// trier par apport a l'heure								
									sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport le nom du medecin
									sorter.setSortKeys(sortKeys);
									//renomer les noms des colones
									Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Medecin");
									Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Specialité");
									Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Patient");	
									Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Date");	
									Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Heure");
								
									} catch (SQLException e) {
										// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,e);
									}
							}
						}
						else
						{ 
							if (b==2) 
							{
								if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) //si le patient a ete selectionné
								{
									String PatInfo =comboBoxPAtients.getSelectedItem().toString();//recuperer les info du Medecin de la comboBox
									int iend=PatInfo.indexOf("-");//indice de la fin de l'id
									String Id_PAT="00";
									if (iend != -1) 
									{
										Id_PAT= PatInfo.substring(0 , iend); //recuperer l'id du patient 
									}
									String sql ="Select Patient_Name ,  Date_RDV,H_RDV ,Doctor_Name,SPE  from RDV where ID_PAT='"+ Id_PAT +"' and Date_RDV='" +datactuelle + "' ";//selectionner les rdv qui contiennent l'id du patient et la date actuelle (du systeme)
									try {
										prepared = cnx.prepareStatement(sql);
										resultat =prepared.executeQuery();
										Minitable.setModel(DbUtils.resultSetToTableModel(resultat));
										
										TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());//mettre le model de la mini table 
										Minitable.setRowSorter(sorter);
										List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
										sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//trier par apport a l'heure								
										sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));//sinon trier par apport le nom du medecin
										sorter.setSortKeys(sortKeys);
										//renomer les noms des colones
										Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Patient");
										Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
										Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Heure");
										Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Specialité");
										Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Nom du Medecin");
								
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null,e1);
									}
								}
						}	
					}
			    }
				else  //checkbox non coché
				{
					if(b==1)
					{
						if (!comboBoxDoctors.getSelectedItem().toString().equals("Selectionner Medecin")) { //si le Medecin a ete selectionné
							String DocInfo =comboBoxDoctors.getSelectedItem().toString();//recuperer les info du Medecin de la comboBox
							int iend=DocInfo.indexOf("-");//indice de la fin de l'id
							String Id_med="00";
							if (iend != -1) 
							{
								Id_med= DocInfo.substring(0 , iend);//recuperer l'id du Medecin 
							}
							String sql ="Select Doctor_Name,SPE , Patient_Name , Date_RDV,H_RDV from RDV where ID_MED='"+ Id_med + "'";//selectionner les rdv qui contiennent l'id du Medecin
							try {
									prepared = cnx.prepareStatement(sql);
									resultat =prepared.executeQuery();
									Minitable.setModel(DbUtils.resultSetToTableModel(resultat));//mettre le model de la mini table 
									//ajouter un sorter pour trier la table 
									TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
									Minitable.setRowSorter(sorter);
									List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
									sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));//trier par apport a la Date
									sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));//sinon trier par apport a l'heure
									sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport le nom du medecin
									sorter.setSortKeys(sortKeys);
									//renomer les noms des colones
									Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Medecin");
									Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Specialité");
									Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Patient");	
									Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Date");	
									Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Heure");
								
								} catch (SQLException e) {
								// TODO Auto-generated catch block
									JOptionPane.showMessageDialog(null,e);
								}
							
							
							}
					}
					else 
					{ 
						if (b==2) //le button d' affichage des rdv du patient a ete clique
						{
							if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) //si le patient a ete selectionné
							{
									String PatInfo =comboBoxPAtients.getSelectedItem().toString();//recuperer les info du patientt de la comboBox
									int iend=PatInfo.indexOf("-");//indice de la fin de l'id
									String Id_PAT="00";
									if (iend != -1) 
									{
										Id_PAT= PatInfo.substring(0 , iend);//recuperer l'id du patient 
									}
									String sql ="Select Patient_Name ,  Date_RDV,H_RDV ,Doctor_Name,SPE  from RDV where ID_PAT='"+ Id_PAT +"'";//selectionner les rdv qui contiennent l'id du patient
									try {
										prepared = cnx.prepareStatement(sql);
										resultat =prepared.executeQuery();
										Minitable.setModel(DbUtils.resultSetToTableModel(resultat));//mettre le model de la mini table 
										//ajouter un sorter pour trier la table 
										TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
										Minitable.setRowSorter(sorter);
										List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
										sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier par apport a la Date
										sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//sinon par apport a lheure
										sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));///sinon trier par apport le nom du patient
										sorter.setSortKeys(sortKeys);
										//renomer les noms des colones
										Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Patient");
										Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
										Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Heure");
										Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Specialité");
										Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Nom du Medecin");
								
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null,e1);
									}
								}
						}	
					}
				
				}
				
			
			
			}
		});
		chckbxcejour.setBounds(398, 183, 297, 23);
		contentPane.add(chckbxcejour);
		
	//bouton pour afficher les RDV du Medecin selectionné ***********************************************************************************************************************************************************************************
		// (ca prend en consideratiobn la chexk box rdv du jours donc si la check bex est cocher alors ca va afficher les rdv du medecin du jours )
		JButton btnDocapps = new JButton("Affichez les RDV du Medecin Selectionn\u00E9");
		btnDocapps.setForeground(new Color(102, 0, 153));
		btnDocapps.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnDocapps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				b=1;// pour dire qe le premier bouton est cliqué 
			if (chckbxcejour.isSelected())//si la check box pour afficher que les rdv du jours alors on va afficher que les rdv du medecin pour ce jours la 
			{
				
				if (!comboBoxDoctors.getSelectedItem().toString().equals("Selectionner Medecin")) {//si le Medecin a ete selectionné
					 String DocInfo =comboBoxDoctors.getSelectedItem().toString();//recuperer les info du Medecin de la comboBox
					 int iend=DocInfo.indexOf("-");//indice de la fin de l'id
					 String Id_med="00";
					 if (iend != -1) 
					 {
						 Id_med= DocInfo.substring(0 , iend);//recuperer l'id du medecin 
					 }
					String sql ="Select Doctor_Name,SPE , Patient_Name , Date_RDV,H_RDV from RDV where ID_MED='"+ Id_med + "' and Date_RDV='" +datactuelle + "' ";//selectionner les rdv qui contiennent l'id du Medecin et la date actuelle (du systeme)
					try {
							prepared = cnx.prepareStatement(sql);
							resultat =prepared.executeQuery();
							Minitable.setModel(DbUtils.resultSetToTableModel(resultat));
							//ajouter un sorter pour trier la table 
							TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());//mettre le model de la mini table 
							Minitable.setRowSorter(sorter);
							List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
							sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));//trier par apport a la Date
							sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));//sinon trier par apport a l'heure
							sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport le nom du medecin
							sorter.setSortKeys(sortKeys);
							//renomer les noms des colones
							Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Medecin");
							Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Specialité");
							Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Patient");	
							Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Date");	
							Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Heure");
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e);
						}
						
					}
			}
			else {
				if (!comboBoxDoctors.getSelectedItem().toString().equals("Selectionner Medecin")) {//si le Medecin a ete selectionné
					 String DocInfo =comboBoxDoctors.getSelectedItem().toString();//recuperer les info du Medecin de la comboBox
					 int iend=DocInfo.indexOf("-");//indice de la fin de l'id
					 String Id_med="00";
					 if (iend != -1) 
					 {
						 Id_med= DocInfo.substring(0 , iend);//recuperer l'id du medecin 
					 }
					String sql ="Select Doctor_Name,SPE , Patient_Name , Date_RDV,H_RDV from RDV where ID_MED='"+ Id_med + "'";//selectionner les rdv qui contiennent l'id du Medecin
					try {
							prepared = cnx.prepareStatement(sql);
							resultat =prepared.executeQuery();
							Minitable.setModel(DbUtils.resultSetToTableModel(resultat));
							//ajouter un sorter pour trier la table 
							TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
							Minitable.setRowSorter(sorter);
							List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
							sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));//trier par apport a la Date
							sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));//sinon trier par apport a l'heure
							sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport le nom du medecin
							sorter.setSortKeys(sortKeys);
							//renomer les noms des colones
							Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Medecin");
							Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Specialité");
							Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Patient");	
							Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Date");	
							Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Heure");
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e);
						}
						
						
					}
				
				
			}
				
			}
		});
		btnDocapps.setBounds(355, 368, 282, 23);
		contentPane.add(btnDocapps);
		
		
	//bouton pour afficher les RDV du Patient selectionné ***********************************************************************************************************************************************************************************
		//(prend en consideration la check box des RDV du jours)
		JButton btnPatApps = new JButton("Affichez les RDV du Patient Selectionn\u00E9");
		btnPatApps.setForeground(new Color(102, 0, 153));
		btnPatApps.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnPatApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b=2;
				if (chckbxcejour.isSelected())//si la check box pour afficher que les rdv du jours alors on va afficher que les rdv de ce patients pour ce jours la 
				{	
					if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) //si le patient a ete selectionné
					{
							String PatInfo =comboBoxPAtients.getSelectedItem().toString();//recuperer les info du patients de la comboBox
							int iend=PatInfo.indexOf("-");//indice de la fin de l'id
							String Id_PAT="00";
							if (iend != -1) 
							{
								Id_PAT= PatInfo.substring(0 , iend);//recuperer l'id
							}
							String sql ="Select Patient_Name ,  Date_RDV,H_RDV ,Doctor_Name,SPE from RDV where ID_PAT='"+ Id_PAT +"' and Date_RDV='" +datactuelle + "' ";//selectionner les rdv qui contiennent l'id du patient et la date actuelle (du systeme)
							try {
								prepared = cnx.prepareStatement(sql);
								resultat =prepared.executeQuery();
								Minitable.setModel(DbUtils.resultSetToTableModel(resultat));//mettre le model de la mini table 
								
								//ajouter un sorter pour trier la table 
								TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
								Minitable.setRowSorter(sorter);
								List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
								sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier par apport a la Date
								sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//sinon trier par apport a l'heure
								sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport au nom du patients
								sorter.setSortKeys(sortKeys);
								//renomer les noms des colones
								Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Patient");
								Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
								Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Heure");
								Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Specialité");
								Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Nom du Medecin");	
								
							
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,e);
							}
						}
				}
				else 
				{
					if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) //si le patient a ete selectionné
					{
							String PatInfo =comboBoxPAtients.getSelectedItem().toString();
							int iend=PatInfo.indexOf("-");
							String Id_PAT="00";
							if (iend != -1) 
							{
								Id_PAT= PatInfo.substring(0 , iend); //this will give abc
							}
							String sql ="Select Patient_Name ,  Date_RDV,H_RDV ,Doctor_Name,SPE  from RDV where ID_PAT='"+ Id_PAT +"'";//selectionner les rdv qui contiennent l'id du patient
							try {
								prepared = cnx.prepareStatement(sql);
								resultat =prepared.executeQuery();
								Minitable.setModel(DbUtils.resultSetToTableModel(resultat));
								//ajouter un sorter pour trier la table 
								TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
								Minitable.setRowSorter(sorter);
								List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
								sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier par apport a la Date
								sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//sinon trier par apport a l'heure
								sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon trier par apport au nom du patients
								sorter.setSortKeys(sortKeys);
								//renomer les noms des colones
								Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Nom du Patient");
								Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
								Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Heure");
								Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Specialité");
								Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Nom du Medecin");	
							
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,e);
							}
						}
					
				}
				
			}
		});
		btnPatApps.setBounds(647, 368, 262, 23);
		contentPane.add(btnPatApps);
		
	//bouton pour afficher les RDV de la Date selectionné ***********************************************************************************************************************************************************************************
		JButton btnDall = new JButton("Afficher les RDV de la date selectionn\u00E9e");
		btnDall.setForeground(new Color(102, 0, 153));
		btnDall.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnDall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!CBDay.getSelectedItem().toString().equals("Day")&&!CBMonth.getSelectedItem().toString().equals("Month")&&!CBYear.getSelectedItem().toString().equals("Year")) {//verifier si la date est selectionné
					 String Date =CBDay.getSelectedItem().toString()+"-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
					 
					String sql ="Select Date_RDV ,H_RDV , Doctor_Name, SPE , Patient_Name  from RDV where Date_RDV='"+ Date +"' ";
					try {
							prepared = cnx.prepareStatement(sql);
							resultat =prepared.executeQuery();
							Minitable.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
							
							//ajouter un sorter pour trier la table
							TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(Minitable.getModel());
							Minitable.setRowSorter(sorter);
							List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
							sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier par apport l'heure
							sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//sinon par apport Nom et prenom
							sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//sinon la date
							sorter.setSortKeys(sortKeys);
							//renomer les colones
							Minitable.getColumnModel().getColumn( 0 ).setHeaderValue("Date");
							Minitable.getColumnModel().getColumn( 1 ).setHeaderValue("Heure");
							Minitable.getColumnModel().getColumn( 4 ).setHeaderValue("Nom du Patient");	
							Minitable.getColumnModel().getColumn( 3 ).setHeaderValue("Specialité");
							Minitable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Medecin");	
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e);
						}
					}
				
			
			}
		});
		btnDall.setBounds(919, 368, 262, 23);
		contentPane.add(btnDall);
//Recherche suggestion(recherche live)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//le text Field pour la recherche*****************************************************************************************************
		txtrecherche = new JTextField();
		//avec le mechanisme de recherche live
		txtrecherche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
            	Recherche(evt);//appler la fonction recherche
            }
        });
		txtrecherche.addKeyListener(new KeyAdapter() {//limiter le nombre de caractere du textfield
		    public void keyTyped(KeyEvent e) { 
		        if (		txtrecherche.getText().length() >= 50 ) 
		            e.consume(); 
		    }  
		});
		txtrecherche.setBounds(411, 136, 231, 30);
		txtrecherche.setForeground(new Color(102, 0, 153));
		txtrecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtrecherche.setBackground(Color.WHITE);
		txtrecherche.setBorder(null);
		contentPane.add(txtrecherche);
		txtrecherche.setColumns(10);
	//scrollPane avec la list des patient ************************************************************************************************************************************
		JScrollPane scrollPanePatientsList = new JScrollPane();
		scrollPanePatientsList.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePatientsList.setBackground(Color.LIGHT_GRAY);
		scrollPanePatientsList.setBounds(705, 31, 240, 142);
		scrollPanePatientsList.setBorder(null);
		contentPane.add(scrollPanePatientsList);
		
		listPatients = new JList<String>();
		listPatients.setOpaque(false);
		listPatients.setBackground(SystemColor.control);
		listPatients.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {// au cas ou une ligne est selectionnée de la liste
				for (int j = 0; j < comboBoxPAtients.getItemCount(); j++) //parcourir la comboBox des patients
				{
					 String Name_pat=" ";
					 String FirstName_pat=" ";

					String PatInfo=comboBoxPAtients.getItemAt(j).toString();//recuperer le contenu de la  combobox a l'element numero j
					
				
						 int finIDindice=PatInfo.indexOf("-");//marque la fin de l'ID
						 int finnomindice=PatInfo.indexOf(" ");//la fin du Nom
						 int finprenomindice=PatInfo.length();//la fin du prenom
						 if (finIDindice != -1) 
						 {
							  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recupperer le NOM
							  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer le Prenom 
							
						 }
					String Nom_Prenom=Name_pat+" "+FirstName_pat;//sous format Nom" "Prenom
					
						if (Nom_Prenom.equals(listPatients.getSelectedValue())) {
							comboBoxPAtients.setSelectedIndex(j);
							//selectionner l'element correspandant 
						}
				}
				txtrecherche.setText(listPatients.getSelectedValue());//mettre le contenu_ de la ligne selectionne de la liste dans le textField recherche 
			}
		});
		listPatients.setForeground(new Color(102, 0, 153));
		listPatients.setFont(new Font("Segoe UI", Font.BOLD, 12));
		UpdateListPAT(listPatients);
		scrollPanePatientsList.setViewportView(listPatients);
	//boutoun pour actuliser la comb box des patients et la list des patients **************************************************************************************************************	
		JButton btnOuvrire = new JButton("");
		btnOuvrire.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnOuvrire.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/REFRESH SELECTED.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnOuvrire.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/REFRESH 2.png")));//remetre le bouton de base
			}
		});
		btnOuvrire.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/REFRESH 2.png")));
		btnOuvrire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateListPAT(listPatients);//actualiser la liste des patient en cas d'ajout
				UpedateTableRDV();//actualiser la table RDV
				//desactiver les boutons supprimer et modifier
				btnRemoveRDV.setEnabled(false);
				btnModRDV.setEnabled(false);
				INComboboxPat(comboBoxPAtients);//actualiser la comboBox
			}
		});
		btnOuvrire.setBounds(663, 35, 32, 32);
		contentPane.add(btnOuvrire);
		
	//boutoun pour ouvrir la fenetre Gestient patient **************************************************************************************************************	
		JButton btnOpenMenuGStTPAT = new JButton("   Gestion Patient");
		btnOpenMenuGStTPAT.setForeground(new Color(102, 0, 153));
		btnOpenMenuGStTPAT.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnOpenMenuGStTPAT.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/arrows.png")));
		btnOpenMenuGStTPAT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				framePat.setLocationRelativeTo(null);
				framePat.setVisible(true);
			}
		});
		btnOpenMenuGStTPAT.setBounds(955, 170, 190, 32);
		contentPane.add(btnOpenMenuGStTPAT);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
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
					MenuSecretaireFrame frame = new MenuSecretaireFrame();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
				}
				else {//ouvert par un medecin donc retourner au menu medecin
					MenuMedcinFrame frame = new MenuMedcinFrame(ID_med);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
					
				}
			}
		});
		btnHome.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/Log_in_img/home sec.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1101, 11, 32, 32);
		contentPane.add(btnHome);
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(GestionRDVFrame.class.getResource("/GstRDV_img/BG gestionRDV fr.png")));
		BGStaff.setBounds(0, 0, 1200, 650);
		contentPane.add(BGStaff);
		

		
	}
//Validité d'un RDV////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public int VALIDRDV(int Dr_iD,String RDVD,String RDVH,int Pat_ID)
	{ 
	
		int i = 3; // si i ==3 donc la date et l hure sont valide 
		String sql ="Select NUM_RDV , ID_Med ,ID_Pat , Date_RDV,H_RDV   from RDV";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			while(resultat.next())
			{ 
				String D="";
					 int finYearindice=resultat.getString("Date_RDV").toString().indexOf("-");//recupperer lindice de la fin de l'année (la date est de la forme yyyy-MM-dd HH:mm:ss)
					 if (finYearindice != -1) 
					 {
						 String Year= resultat.getString("Date_RDV").toString().substring(0 , finYearindice); //recuperer l'annee
						 String Mois =resultat.getString("Date_RDV").toString().substring(finYearindice+1 , finYearindice+3);//recuperere le mois
						 String Jour =resultat.getString("Date_RDV").toString().substring(finYearindice+4 , finYearindice+6);//recupererer le jours
					
						 	 D=Jour+"/"+Mois+"/"+Year;// m'ecrire sous la forme dd/MM/yyyy
					 }
				
				if (resultat.getInt("ID_Med")==Dr_iD && D.equals(RDVD) && resultat.getString("H_RDV").toString().equals(RDVH) ) {//le cas ou il a un RDV chez le meme medecin a la meme date et la meme heure
					i =1;
				}
				
				if (resultat.getInt("ID_Pat")==Pat_ID && D.equals(RDVD) && resultat.getString("H_RDV").toString().equals(RDVH) ) {// le cas ou il a un rdv chez un autre medeecin ce jour la et cette date
					i=2;
				}

				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
			} 
		return i;
		
	}	
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}
//mettre a jour la liste des patients//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void UpdateListPAT(JList<String> List)
	{ 
		
		String sql ="Select Name ,FirstName  from Patient";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			ArrayList<String> L=new ArrayList<String>();//une liste qui va contenir les patients
			while(resultat.next())
			{
				String item=resultat.getString("Name").toString()+" "+resultat.getString("FirstName").toString();//sous format Nom Prenom
				
				L.add(item);//ajouter a la liste
				
			}
			
			 DefaultListModel<String> defaultListModel=new DefaultListModel<String>();// remplissage de la liste
			 L.stream().forEach((star) -> {
		            defaultListModel.addElement(star);//ajouter l'element
		        });
			 List.setModel(defaultListModel);//changer le modele de la liste
			 List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
	
//Actualiser la table RDV//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void UpedateTableRDV()
	{ 
		String sql ="Select NUM_RDV , Patient_Name  ,Doctor_Name,SPE , Date_RDV,H_RDV ,Commentary  from RDV";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			
			RDVtable.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(RDVtable.getModel());
			RDVtable.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));//trier  par apport a la date du RDV
			sortKeys.add(new RowSorter.SortKey(5, SortOrder.ASCENDING));//trier  par apport a l'heure du RDVt
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport a l'id
			sorter.setSortKeys(sortKeys);
			
		//renomer les  colones
			RDVtable.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			RDVtable.getColumnModel().getColumn( 1 ).setHeaderValue("Nom du Patient");
			RDVtable.getColumnModel().getColumn( 2 ).setHeaderValue("Nom du Docteur");	
			RDVtable.getColumnModel().getColumn( 3 ).setHeaderValue("Specialite");	
			RDVtable.getColumnModel().getColumn( 4 ).setHeaderValue("Date");
			RDVtable.getColumnModel().getColumn( 5 ).setHeaderValue("Heure");
			RDVtable.getColumnModel().getColumn( 6 ).setHeaderValue("Commentaire");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
			}
		
	}
	
//Remplire la comboBox Doc//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void INComboboxDoc(JComboBox<String> Doc)
	{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
		String sql ="Select ID_Med,Nom ,Prenom,Specilité   from Medecin";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			while(resultat.next())
			{
				String item=resultat.getString("ID_Med").toString()+"-"+resultat.getString("Nom").toString()+" "+resultat.getString("Prenom").toString()+"("+resultat.getString("Specilité").toString()+")";
				Doc.addItem(item);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	
	}
	
//Remplire la ComboBox Patient//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
		public void INComboboxPat(JComboBox<String> Pat)
		{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
			boolean b= false;
			String sql ="Select ID_Patient,Name ,FirstName  from Patient";
			try {
				prepared = cnx.prepareStatement(sql);
				resultat =prepared.executeQuery();
				while(resultat.next())
				{
					b= false;//pour differncier entre les patients qui sont deja dans la liste(ComboBox) et ceux y sont pas
					String item=resultat.getString("ID_Patient").toString()+"-"+resultat.getString("Name").toString()+" "+resultat.getString("FirstName").toString();//sous format ID_pat-Nom Prenom
					for (int i = 0; i < Pat.getItemCount(); i++) {//parcourir toute la liste
						if(Pat.getItemAt(i).toString().equals(item))// le cas ou il se trouve dans la liste(ComboBox)
						{
							b=true;
						}
					}
					if (!b) {//donc le patient ne se trouve pas dans la liste(ComboBox)
						Pat.addItem(item);//ajouter le patient
					}
					
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			}
			
		}
	
//la verfication de la validité de la date//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
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
	
// limiter les char dans un text field//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void txtLimiter(Component O,int x) {
		((Component) O).addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		((AbstractButton) O).getText().length() >= x ) 
		            e.consume(); 
		    }  
		});
	}
	
//Recherche///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	private void Recherche(java.awt.event.KeyEvent evt) 
	{                                      
		RechercheFiltre(txtrecherche.getText());
	}        

/*
Fltrer les donneés */

private void RechercheFiltre(String searchTerm)
{
	String sql ="Select Name ,FirstName  from Patient";
	try {
		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		ArrayList<String> L=new ArrayList<String>();//declarer une array list qui va contenir les nom+Prenom Des Patients
		while(resultat.next())
		{
			String item=resultat.getString("Name").toString()+" "+resultat.getString("FirstName").toString();
			
			L.add(item);//ajouter le patient
			
		}
		DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
		DefaultListModel<String> filteredItems=new DefaultListModel<String>();

	    L.stream().forEach((star) -> {//pour chaque element
	        String starName=star.toString().toLowerCase();
	        if (starName.contains(searchTerm.toLowerCase())) {//si la list contient le term recherché
	        	filteredItems.addElement(star);//alors afficher que les String de la lmist le contenat
	        }
	    });
	    defaultListModel=filteredItems;
	    listPatients.setModel(defaultListModel);//changer le model de la list
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
	}
}
}
	
	

