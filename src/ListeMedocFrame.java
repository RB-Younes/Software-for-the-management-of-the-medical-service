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
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Liste des medicaments------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


public class ListeMedocFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableMedoc;


	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String numMedoc;// le id recupere en cliquant sur le tableau
	private JTextField textFieldNom;
	private JTextField textFieldObs;
	private JButton btnModMed ;
	private JButton btnSuppMed ;
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
					ListeMedocFrame frame = new ListeMedocFrame("2");
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
	
	public ListeMedocFrame(String ID_Med) {
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

// initialisation----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ListeMedocFrame.class.getResource("/Menu_Medcin_img/medicine.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Fields-//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Combo box famille du medicament (ANTIBIOTIQUES....
		JComboBox<String> comboBoxFamille = new JComboBox<String>();
		comboBoxFamille.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxFamille.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner classe","ANTALGIQUES","ANTI-DIABETIQUE","ANTI-ACIDES","ANTI-MIGRAINEUX","ANTI-THROMBOTIQUES","PRÉVENTION CARDIOVASCULAIRE","ANTI-HYPERTENSIVE","ANTIBIOTIQUES","ANTI-INFLAMMATOIRES","ANTI-DÉPRESSEURS","ANXIOLITIQUES","SOMNIFÈRES","ANTI-HISTAMINIQUES","ANTI-TUSSIF","ANTI-FONGIQUE","CONTRACEPTION" } ));
		comboBoxFamille.setBackground(new Color(51, 204, 153));
		comboBoxFamille.setForeground(new Color(255, 255, 255));
		comboBoxFamille.setBounds(229, 238, 237, 39);
		contentPane.add(comboBoxFamille);
		//combo box Forme du medicament
		JComboBox<String> comboBoxForme = new JComboBox<String>();
		comboBoxForme.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxForme.setModel(new DefaultComboBoxModel<String>(new String[] {"Sélectionner Forme" ,"Comprimés","Gélules","Formes Ophtalmiques","Suppositoires","Sirops","Pomade" } ));
		comboBoxForme.setForeground(Color.WHITE);
		comboBoxForme.setBackground(new Color(51, 204, 153));
		comboBoxForme.setBounds(229, 305, 237, 39);
		contentPane.add(comboBoxForme);
		
		//Text Field Nom du medicament
		textFieldNom = new JTextField();
		textFieldNom.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //limiter le nombre caractere a 200 (pour ne pas rencontrer de probleme avec la bdd )
		        if (		textFieldNom.getText().length() >= 200 ) 
		            e.consume(); 
		    }  
		});
		textFieldNom.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldNom.setForeground(new Color(255, 255, 255));
		textFieldNom.setBorder(null);
		textFieldNom.setBackground(new Color(51, 204, 153));
		textFieldNom.setBounds(229, 178, 237, 39);
		contentPane.add(textFieldNom);
		textFieldNom.setColumns(10);
		
		//Text Field Observation
		textFieldObs = new JTextField();
		textFieldObs.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //nombre de caractere de l'observation doit aussi etre inferieur  a 200
		        if (		textFieldObs.getText().length() >= 200 ) 
		            e.consume(); 
		    }  
		});
		textFieldObs.setForeground(new Color(255, 255, 255));
		textFieldObs.setBackground(new Color(51, 204, 153));
		textFieldObs.setBorder(null);
		textFieldObs.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textFieldObs.setColumns(10);
		textFieldObs.setBounds(226, 381, 261, 81);
		contentPane.add(textFieldObs);

		
//ScrollPane Table Medicament/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
		JScrollPane scrollPaneMedoc = new JScrollPane();
		scrollPaneMedoc.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneMedoc.setBounds(528, 135, 603, 393);
		contentPane.add(scrollPaneMedoc);
		
		tableMedoc = new JTable();
		tableMedoc.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableMedoc.setForeground(new Color(255, 255, 255));
		tableMedoc.setBorder(null);
		tableMedoc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les deux boutons Modifier et supprimer
				btnModMed.setEnabled(true);
				btnSuppMed.setEnabled(true);
				
				int ligne=tableMedoc.getSelectedRow();//recuperer le numero de ligne
				
				numMedoc =tableMedoc.getValueAt(ligne, 0).toString();//recuperer l'ID du medicament qui est a la premiere colone (0) de la table
				String sql="Select * from Medoc where ID_Medoc='"+numMedoc+"'";//Selectionner toute les info du Medicament aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						textFieldNom.setText(resultat.getString("Nom"));// mettre le Nom du medicament dans le TextField Nom
						textFieldObs.setText(resultat.getString("obs"));//metre l'observation dans le TextField obs
						//parcourir la combobox Famille
						for (int i = 0; i < comboBoxFamille.getItemCount(); i++) 
						{
								
								if (comboBoxFamille.getItemAt(i).equals(resultat.getString("Famille"))) { comboBoxFamille.setSelectedIndex(i);}//selectionner cet element
						}
						//parcourir la combobox Famille
						for (int i = 0; i < comboBoxForme.getItemCount(); i++) 
						{
								if (comboBoxForme.getItemAt(i).equals(resultat.getString("Forme"))) { comboBoxForme.setSelectedIndex(i);}//selectionner cet element
						}

					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableMedoc.setBackground(new Color(0, 204, 153));
		scrollPaneMedoc.setViewportView(tableMedoc);
		
		
//afficher le contenue de la table juste a l'ouverture////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		UpedateTableMedicament();
	
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1057, 5, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//vider les text filds
				textFieldNom.setText("");
				textFieldObs.setText("");
				// remtre les valeurs par default de la combobox
				comboBoxFamille.setSelectedIndex(0);
				comboBoxForme.setSelectedIndex(0);
			}
		});
		btnViderFields.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/rubbish.png")));
		btnViderFields.setBounds(455, 485, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						dispose();
					}
			}
		});
		Exit_BTN.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1141, 5, 32, 32);
		contentPane.add(Exit_BTN);
		
// Bouton Imprimer ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnImprimer = new JButton("");
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/print selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/print.png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/print.png")));
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc= new Document();
				Medicament med = new Medicament();// appeler la methode imprimer de la classe Medicament
				med.Imprimer(doc);
				
			}
		});
		btnImprimer.setToolTipText("Imprimer");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(106, 464, 64, 64);
		contentPane.add(btnImprimer);
//Bouton actualiser la table  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnactualiser = new JButton("");
		btnactualiser.setToolTipText("Actualiser");
		btnactualiser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnactualiser.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/refresh selected .png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnactualiser.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/refresh .png")));//remetre le bouton de base
			}
		});
		btnactualiser.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/refresh .png")));
		btnactualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpedateTableMedicament();//appele de la fonction Acualisation
				 //desactiver les deux boutons Modifier et supprimer
				btnModMed.setEnabled(false);
				btnSuppMed.setEnabled(false);
			}
		});
		btnactualiser.setBounds(1141, 185, 32, 32);
		contentPane.add(btnactualiser);

//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//Supprimer un medicament**********************************************************************************************************************************************************************************************************************************
		btnSuppMed = new JButton();
		btnSuppMed.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnSuppMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSuppMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/remove selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSuppMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/remove.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnSuppMed);
		btnSuppMed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableMedoc.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
				{//afficher un message d'erreur
					JOptionPane.showMessageDialog(null, " S'il vous plait veuillez selectioner une ligne a modifer !","Warning", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));

				}
				else {
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Supprimer ce médicament ?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{	
					Medicament Medec=new  Medicament();
					Medec.Suprimer(numMedoc); //appele de la Methode supprimer de la classe Medicament
					//desactiver les deux boutons Modifier et supprimer
					btnModMed.setEnabled(false);
					btnSuppMed.setEnabled(false);
					UpedateTableMedicament();//Actualiser la table 
				}
			
				}
			}
		});
		btnSuppMed.setToolTipText("Supprimer un M\u00E9dicament");
		btnSuppMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/remove.png")));
		btnSuppMed.setBounds(353, 485, 32, 32);
		contentPane.add(btnSuppMed);
		
		
	//Ajouter un medicament**********************************************************************************************************************************************************************************************************************************
		JButton btnAddMedoc = new JButton();
		btnAddMedoc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddMedoc.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/plus selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddMedoc.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/plus.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnAddMedoc);
		btnAddMedoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				 //message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Ajouter ce médicament ?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					
					String Nom =textFieldNom.getText().toString();//recuperer le contenu du Textfield Nom
					String obs=textFieldObs.getText().toString();//recuperer le contenu du Textfield Observation
					String Famille=comboBoxFamille.getSelectedItem().toString();//recuperer l'element selectionné de la combo Box Famille
					String Forme=comboBoxForme.getSelectedItem().toString();//recuperer l'element selectionné de la combo Box Forme
				
					if (Nom.equals("")) //verifier si le nom du medicament a ete donné
					{//un message d'erreur
						JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplir le Nom du Médicament ! ","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else 
					{ if (Forme.equals("Sélectionner classe")) //selectionner la famille du médicament ("Sélectionner classe" c'est la valeur par default donc aucun elemnt choisi)
						{ // un message d'erreur
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez choisir la Familledu Médicament!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						}
					   else 
					   {
						   if (Famille.equals("Sélectionner Forme"))//de meme pour la forme
						   { //un message d'erreur
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez choisir la Familledu Médicament!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						   }
						   else 
						   {
							   Medicament medoc=new Medicament(Nom, Forme, Famille, obs);//creeer une instance de Medicament
							   medoc.Ajouter(medoc);//ajouter le medicament a la table a l'aide de la methode ajouter de la classe Medicament
							 //desactiver les deux boutons Modifier et supprimer
								btnModMed.setEnabled(false);
								btnSuppMed.setEnabled(false);
							   //vider tout aprees
							   textFieldNom.setText("");
								textFieldObs.setText("");
								comboBoxFamille.setSelectedIndex(0);
								comboBoxForme.setSelectedIndex(0);
								UpedateTableMedicament();//actualiser la table des medicament
						   }
					   }
					}
				}
			}
		});
		btnAddMedoc.setToolTipText("Ajouter M\u00E9dicament");
		btnAddMedoc.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/plus.png")));
		btnAddMedoc.setBounds(229, 485, 32, 32);
		contentPane.add(btnAddMedoc);
		
	//Modifier un medicament**********************************************************************************************************************************************************************************************************************************	
		btnModMed = new JButton();
		btnModMed.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModMed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/Modify selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/Modify.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnModMed);
		btnModMed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableMedoc.getSelectedRow()==-1) //aucune ligne de la table n'est selectionnée 
				{ // message 
					JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne a modifer !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else 
				{
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					 //message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier Les informations de ce médicament ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						String Nom =textFieldNom.getText().toString();//recuperer le contenu du Textfield Nom
						String obs=textFieldObs.getText().toString();//recuperer le contenu du Textfield Observation
						String Famille=comboBoxFamille.getSelectedItem().toString();//recuperer l'element selectionné de la combo Box Famille
						String Forme=comboBoxForme.getSelectedItem().toString();//recuperer l'element selectionné de la combo Box Forme
						if (Nom.equals(""))//verifier si le nom du medicament 
						{//un message d'erreur
							JOptionPane.showMessageDialog(null, " S'il vous plait veuillez Remplir le Nom du Médicament ! ","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						}
						else 
						{ if (Forme.equals("Sélectionner classe"))  //selectionner la famille du médicament ("Sélectionner classe" c'est la valeur par default donc aucun elemnt choisi)
							{ // un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez choisir la Familledu Médicament!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
						   else 
						   {
							   if (Famille.equals("Sélectionner Forme")) //de meme pour la forme
							   { //un message d'erreur
								JOptionPane.showMessageDialog(null, " S'il vous plait veuillez choisir la Familledu Médicament!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							   }
							   else 
							   {
								  Medicament medoc=new Medicament();
								   medoc.MetreAjour(Nom, Famille, Forme, obs, numMedoc);// appler la methode mettreajour pour modifier les infos d'un Medicament
								 //desactiver les deux boutons Modifier et supprimer
									btnModMed.setEnabled(false);
									btnSuppMed.setEnabled(false);
								   UpedateTableMedicament();//actualiser la table
							   }
						   }
						
						}

					}
				}
			}
		});
		btnModMed.setToolTipText("Modifier info M\u00E9dicament");
		btnModMed.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/Modify.png")));
		btnModMed.setBounds(293, 485, 32, 32);
		contentPane.add(btnModMed);
		
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
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
					MenuMedcinFrame frame = new MenuMedcinFrame(ID_Med);// retourner au menu medecin
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					dispose();
			}
		});
		btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med.png")));
		btnHome.setToolTipText("Retourner au Menu");
		btnHome.setBounds(1099, 5, 32, 32);
		contentPane.add(btnHome);		

		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(ListeMedocFrame.class.getResource("/Medoc_img/Gst Medicaments.png")));
		BGStaff.setBounds(0, 0, 1200, 550);
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
		public void UpedateTableMedicament()
		{ 	
			String sql ="Select *  from Medoc ";
		try {
			
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableMedoc.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//renomer les noms de colones
			tableMedoc.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			tableMedoc.getColumnModel().getColumn( 1 ).setHeaderValue("Nom du medicament");	
			tableMedoc.getColumnModel().getColumn( 2 ).setHeaderValue("Famille");
			tableMedoc.getColumnModel().getColumn( 3 ).setHeaderValue("Forme");
			tableMedoc.getColumnModel().getColumn( 4 ).setHeaderValue("Observation");
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableMedoc.getModel());
			tableMedoc.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//trier  par apport au Nom Du medicament
			sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));//trier  par apport a la famille (si ils ont le meme nom)
			sorter.setSortKeys(sortKeys);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		}
}
	
	

