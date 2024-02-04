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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Trésorerie------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class TresorerieFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableCons;
	private JButton btnModPaiement;
	private JButton btnRemovePaiement;
	private JTextField txtrecherche;
	private JList<String> listPatients;
	private JButton btnAddPaiement;
	private String ID_Cons;
	private String ID_Pat;
	private String Num_Paiement;
	private JButton btnImprimer;
	private String Date_cons;//Date Consultation
	
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private JTable tableInfo;
	private JTextField txtPrixapayer;
	private JTextField txtPrixdejaPaye;
	private JTextArea textAreaCom;
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
					TresorerieFrame frame = new TresorerieFrame("","");
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
	
	public TresorerieFrame(String indice,String ID_med) {
// CNX 
		cnx = ConnexionDB.CnxDB();
		
//FAIRE bouger le menu////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
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
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(TresorerieFrame.class.getResource("/Menu_Medcin_img/RES MAN selected.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
	//la liste des patients CmboBox	
		JComboBox<String> comboBoxPAtients = new JComboBox<String>();
		comboBoxPAtients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) {//un element a ete selectionné
				 String Name_pat=" ";
				 String FirstName_pat=" ";
				 ID_Pat=" ";

				String PatInfo=comboBoxPAtients.getSelectedItem().toString();
					 int finIDindice=PatInfo.indexOf("-");//indice de fin de l'id
					 int finnomindice=PatInfo.indexOf(" ");//indice de fin du nom
					 int finprenomindice=PatInfo.length();//indice de fin du prenom
					 if (finIDindice != -1 && finnomindice  != -1 && finprenomindice!= -1) 
					 {
						 ID_Pat= PatInfo.substring(0, finIDindice);//recuperer l'id
						 Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom
						  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer le prenom
					 }
					 txtrecherche.setText(Name_pat+" "+FirstName_pat);//textField de recherche
				 UpedateTableCons(ID_Pat);UpedateTableinfo(ID_Pat);//actualiser les deux tables
				 txtPrixapayer.setText("");
				 txtPrixdejaPaye.setText("");
				 textAreaCom.setText("");
				}
					
			}
		});
		comboBoxPAtients.addItem("Selectionner Patient");
		INComboboxPat(comboBoxPAtients);
		comboBoxPAtients.setBackground(new Color(102, 0, 153));
		comboBoxPAtients.setForeground(new Color(255, 255, 255));
		comboBoxPAtients.setBounds(153, 138, 217, 32);
		contentPane.add(comboBoxPAtients);
		
	//le prix a payer	
		txtPrixapayer = new JTextField();
		txtPrixapayer.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPrixapayer.setEditable(false);
		txtPrixapayer.setForeground(Color.WHITE);
		txtPrixapayer.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtPrixapayer.setColumns(10);
		txtPrixapayer.setBorder(null);
		txtPrixapayer.setBackground(new Color(102, 0, 153));
		txtPrixapayer.setBounds(157, 194, 213, 30);
		contentPane.add(txtPrixapayer);
		
	//montant deja payé
		txtPrixdejaPaye = new JTextField();
		txtPrixdejaPaye.setHorizontalAlignment(SwingConstants.RIGHT);
		/*Restreindre l'entrée (que des entiers)*/
		txtPrixdejaPaye.addKeyListener(new KeyAdapter() {
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
		txtPrixdejaPaye.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		txtPrixdejaPaye.getText().length() >= 25 ) 
		            e.consume(); 
		    }  
		});
		txtPrixdejaPaye.setForeground(Color.WHITE);
		txtPrixdejaPaye.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtPrixdejaPaye.setColumns(10);
		txtPrixdejaPaye.setBorder(null);
		txtPrixdejaPaye.setBackground(new Color(102, 0, 153));
		txtPrixdejaPaye.setBounds(157, 256, 213, 30);
		contentPane.add(txtPrixdejaPaye);
	//Commentaire text Area
		JScrollPane scrCom = new JScrollPane();
		scrCom.setSize(217, 70);
		scrCom.setLocation(506, 216);
		scrCom.setBorder(null);
		contentPane.add(scrCom);
		textAreaCom = new JTextArea();
		textAreaCom.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaCom.getText().length() >= 1000 ) 
		            e.consume(); 
		    }  
		});
		textAreaCom.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textAreaCom.setBorder(null);
		textAreaCom.setForeground(new Color(255, 255, 255));
		scrCom.setViewportView(textAreaCom);
		textAreaCom.setBackground(new Color(102, 0, 153));
	
		
//ScrollPane Table Paiement////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
		//c'est la table qui contient la date de la consultation et le prix a payer***************************************************************************************************************************************************************************************	
		JScrollPane scrollPanePrix = new JScrollPane();
		scrollPanePrix.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePrix.setBackground(new Color(204, 102, 255));
		scrollPanePrix.setBounds(41, 316, 465, 187);
		contentPane.add(scrollPanePrix);
		
		tableCons = new JTable();
		tableCons.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableCons.setForeground(new Color(255, 255, 255));
		tableCons.setBorder(null);
		tableCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer le bouton ajouter
				btnAddPaiement.setEnabled(true);
				
				int ligne=tableCons.getSelectedRow();

				ID_Cons=tableCons.getValueAt(ligne, 0).toString(); //recuperer l'id de la consultation
					
				String Prix  =tableCons.getValueAt(ligne, 1).toString();// le getModel causer une erreu c que la table gardé lancien model 
				Date_cons=tableCons.getValueAt(ligne, 2).toString();//recuperer la date de consultation
				txtPrixapayer.setText(Prix);//metre la valeur dans le comboBox
			}
		});
		tableCons.setBackground(new Color(102, 0, 153));
		scrollPanePrix.setViewportView(tableCons);
		
	//la table qui va contenir es informations du paiement***************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneInfoPay = new JScrollPane();
		scrollPaneInfoPay.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneInfoPay.setBackground(new Color(204, 102, 255));
		scrollPaneInfoPay.setBounds(559, 319, 592, 181);
		contentPane.add(scrollPaneInfoPay);
		
		tableInfo = new JTable();
		tableInfo.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les boutons Imprimer ,supprimer,imprimer
				btnRemovePaiement.setEnabled(true);
				btnModPaiement.setEnabled(true);
				btnImprimer.setEnabled(true);
				
				int ligne=tableInfo.getSelectedRow();//recuperer le numero de ligne
				Num_Paiement=tableInfo.getValueAt(ligne, 0).toString(); //recuperer l'ID du paiement qui est a la premiere colone (0) de la table
				

				String sql="Select * from paiement where Num_Paiement='"+Num_Paiement+"'";//Selectionner toute les info du paiement aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						txtPrixapayer.setText(resultat.getString("prix_a_payer") );// recupperer le prix a payer	
						txtPrixdejaPaye.setText(resultat.getString("prix_payé") );// recupperer le montant deja payé
						textAreaCom.setText(resultat.getString("Commentaire") );//recuperer le commentaire
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableInfo.setForeground(new Color(255, 255, 255));
		tableInfo.setBorder(null);
		tableInfo.setBackground(new Color(102, 0, 153));
		scrollPaneInfoPay.setViewportView(tableInfo);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1054, 11, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// remtre les valeurs par default de la combobox
				comboBoxPAtients.setSelectedIndex(0);
				//vider les text area et les texts field
				textAreaCom.setText("");
				txtPrixapayer.setText("");
				txtrecherche.setText("");
				txtPrixdejaPaye.setText("");
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/GstRDV_img/rubbish.png")));
		btnViderFields.setBounds(971, 228, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1133, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
// Bouton Imprimer ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		btnImprimer = new JButton("");
		btnImprimer.setEnabled(false);//desactiver le bouton tant que acune ligne de la table info de paiemnt n'a ete selectionné
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/ticket  selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/ticket.png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/ticket.png")));
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Paiment P=new Paiment();
				P.Imprimer(ID_Pat,Num_Paiement);// appeler la methode imprimer de la classe Paiement
			}
		});
		btnImprimer.setToolTipText("Imprimer le ticket");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(1031, 216, 64, 64);
		contentPane.add(btnImprimer);

//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Ajouter un Paiement**********************************************************************************************************************************************************************************************************************************
		btnAddPaiement = new JButton("");
		btnAddPaiement.setEnabled(false);//desactiver le bouton tant que acune ligne de la table liste de consultation n'a ete selectionné
		btnAddPaiement.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/add pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/add pat.png")));//remetre le bouton de base
			}
		});
		btnAddPaiement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				icon.getImage().flush();// réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment ajouter ce Paiement ?","Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
					{
					String etat="";
					String Prix = txtPrixapayer.getText().toString();//recuperer leprix de la consultation
					String prixpayé=txtPrixdejaPaye.getText().toString();//recuperer le montant payé par le patient
					String comm=textAreaCom.getText().toString();//recuperer le commentaire
					if (!prixpayé.equals("")) {
						if (Integer.parseInt(Prix) < Integer.parseInt(prixpayé)  ) //verifier si le prix paye par le patient est plus grand que le prix de la consultation
						{////message
							JOptionPane.showMessageDialog(null,  "S'il vous plait verfiez le montatnt remis!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						}
						else 
						{ 
							if (Integer.parseInt(Prix) > Integer.parseInt(prixpayé)  ) //si le prix de la consultation  eet plus grand que le montant payé par le patient alors 
							{	//etat= A payé que une partie
								etat="A payé que une partie";
							}
							else 
							{ 
								if (Integer.parseInt(Prix) == Integer.parseInt(prixpayé)  )//dansle cas ou ils sont egaux
								{	//etat= A payé que une partie
									etat="A payé  la totaliter";
								}
							}
							Date actuelle = new Date();
			 				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			 				String datactuelle = dateFormat.format(actuelle); //recuperer la date du systeme
			 				
			 				String RDVD=Date_cons.substring(0, 10); // on recupaire la date de la consultation
					    	String Year=RDVD.substring(0,4 );
					    	String Month=RDVD.substring(5,7 );
					    	String Day=RDVD.substring(8,10 );
					    	String Date=Day+"-"+Month+"-"+Year;//changer le format
			 			
			 				Paiment P=new Paiment(datactuelle,Date, ID_Cons, ID_Pat, Prix, prixpayé, etat, comm);
			 				P.Ajouter(P);//appler la fonction ajouter
			 				//desactiver le bouton ajouter
			 				btnAddPaiement.setEnabled(false);
			 				UpedateTableinfo(ID_Pat);//actualiser la table
					}
					}
					else {
						JOptionPane.showMessageDialog(null,  "S'il vous plait verfiez le montatnt remis!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					}
				UpedateTableCons(ID_Pat);
				}
		});
		btnAddPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/add pat.png")));
		btnAddPaiement.setToolTipText("Ajouter Paiement");
		btnAddPaiement.setBounds(801, 228, 32, 32);
		contentPane.add(btnAddPaiement);
		
	//Modifier un Paiement**********************************************************************************************************************************************************************************************************************************
		btnModPaiement = new JButton("");
		btnModPaiement.setEnabled(false);//desactiver le bouton tant que acune ligne de la table info de paiemnt n'a ete selectionné
		btnModPaiement.setToolTipText("modifier Paiement");
		btnModPaiement.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/mod pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/mod pat.png")));//remetre le bouton de base
			}
		});
		btnModPaiement.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				icon.getImage().flush();// réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Voulez vous vraiment Modifier ce Paiement  ?","Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					String etat="";
					String Prix = txtPrixapayer.getText().toString();//recuperer leprix de la consultation
					String prixpayé=txtPrixdejaPaye.getText().toString();//recuperer le montant payé par le patient
					String comm=textAreaCom.getText().toString();//recuperer le commentaire
					if (Integer.parseInt(Prix) < Integer.parseInt(prixpayé)  ) //verifier si le prix paye par le patient est plus grand que le prix de la consultation
					{////message
						JOptionPane.showMessageDialog(null,  "S'il vous plait verfiez le montatnt remis!","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
					else 
					{ 
						if (Integer.parseInt(Prix) > Integer.parseInt(prixpayé)  ) //si le prix de la consultation  eet plus grand que le montant payé par le patient alors 
						{	//etat= A payé que une partie
							etat="A payé que une partie";
						}
						else 
						{ 
							if (Integer.parseInt(Prix) == Integer.parseInt(prixpayé)  )//dansle cas ou ils sont egaux
							 {	//etat= A payé que une partie
								etat="A payé  la totaliter";
							 }
						}
						Paiment P=new Paiment();
						P.MetreAjour(comm, prixpayé, etat, Num_Paiement);//appler la fonction Modifier
						//desactiver les boutons Imprimer ,supprimer,imprimer
						btnRemovePaiement.setEnabled(false);
						btnModPaiement.setEnabled(false);
						btnImprimer.setEnabled(false);
						UpedateTableinfo(ID_Pat);//actualiser la table
					}
				}
				
			}
		});
		btnModPaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/mod pat.png")));
		btnModPaiement.setBounds(856, 228, 32, 32);
		contentPane.add(btnModPaiement);
		
	//Supprimer un paiement**********************************************************************************************************************************************************************************************************************************
		btnRemovePaiement = new JButton("");
		btnRemovePaiement.setEnabled(false);//desactiver le bouton tant que acune ligne de la table info de paiemnt n'a ete selectionné
		btnRemovePaiement.setToolTipText("Supprumer Paiement");
		btnRemovePaiement.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRemovePaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/remove pat selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRemovePaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/remove pat.png")));//remetre le bouton de base
			}
		});
		btnRemovePaiement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions secretaire.gif"));//Animation "Question"
				icon.getImage().flush();// réinitialiser l'animation
				if (tableInfo.getSelectedRow()==-1) //verifier si une ligne de la table a ete selectionné
					{//message
						JOptionPane.showMessageDialog(null, " S'il vous plait slectionnez un Paiement a supprimer de la table !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					}
				else{
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(btnRemovePaiement, "Voulez vous vraiment suprimer ce Paiement de la table  ?","Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
						{	
						Paiment P= new Paiment();
						P.Suprimer(Num_Paiement);//appele de la Methode supprimer de la classe RDV
						//desactiver les boutons Imprimer ,supprimer,imprimer
						btnRemovePaiement.setEnabled(false);
						btnModPaiement.setEnabled(false);
						btnImprimer.setEnabled(false);
						UpedateTableinfo(ID_Pat);//Actualiser la table
						}
					}
			}
		});
		btnRemovePaiement.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/remove pat.png")));
		btnRemovePaiement.setBounds(916, 228, 32, 32);
		contentPane.add(btnRemovePaiement);
		
//Recherche suggestion(recherche live)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
		txtrecherche.setBounds(458, 80, 237, 30);
		txtrecherche.setForeground(new Color(102, 0, 153));
		txtrecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtrecherche.setBackground(Color.WHITE);
		txtrecherche.setBorder(null);
		contentPane.add(txtrecherche);
		txtrecherche.setColumns(10);
		
		//scrollPane avec la list des patient ************************************************************************************************************************************
		JScrollPane scrollPanePatientsList = new JScrollPane();
		scrollPanePatientsList.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPanePatientsList.setBackground(new Color(204, 102, 255));
		scrollPanePatientsList.setBounds(745, 25, 237, 140);
		contentPane.add(scrollPanePatientsList);
		
		listPatients = new JList<String>();
		listPatients.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {// au cas ou une ligne est selectionnée de la liste
				for (int j = 0; j < comboBoxPAtients.getItemCount(); j++) //parcourir la comboBox des patients
				{
					 String Name_pat=" ";
					 String FirstName_pat=" ";
					 ID_Pat=" ";

					String PatInfo=comboBoxPAtients.getItemAt(j).toString();
					
				
						 int finIDindice=PatInfo.indexOf("-");//marque la fin de l'ID
						 int finnomindice=PatInfo.indexOf(" ");//la fin du Nom
						 int finprenomindice=PatInfo.length();//la fin du prenom
						 if (finIDindice != -1) 
						 {
							 ID_Pat= PatInfo.substring(0, finIDindice);//recuperer l'id du patient 
							 Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recupperer le NOM
							  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer le Prenom 
							
						 }
					String Nom_Prenom=Name_pat+" "+FirstName_pat;//sous format Nom" "Prenom
					
						if (Nom_Prenom.equals(listPatients.getSelectedValue())) { comboBoxPAtients.setSelectedIndex(j);
						}
				}
				txtrecherche.setText(listPatients.getSelectedValue());
				String PatInfo=comboBoxPAtients.getSelectedItem().toString();
				
				 int finIDindice=PatInfo.indexOf("-");//selectionner l'element correspandant
				 if (finIDindice != -1) 
				 {
					  ID_Pat= PatInfo.substring(0, finIDindice);//recuperer l'ID
				 }
				 UpedateTableCons(ID_Pat);UpedateTableinfo(ID_Pat);//actualiser les tables
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
				btnOuvrire.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/REFRESH SELECTED.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnOuvrire.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/REFRESH 2.png")));//changer les couleurs button
			}
		});
		btnOuvrire.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/REFRESH 2.png")));
		btnOuvrire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateListPAT(listPatients);//actualiser la liste des patient en cas d'ajout
				INComboboxPat(comboBoxPAtients);//actualiser la comboBox
			}
		});
		btnOuvrire.setBounds(691, 35, 32, 32);
		contentPane.add(btnOuvrire);
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
		btnHome.setBounds(1093, 11, 32, 32);
		contentPane.add(btnHome);
		
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(TresorerieFrame.class.getResource("/Tresorerie_img/BG tresorerie fr.png")));
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
//Actualisation des tables//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				

	public void UpedateTableCons(String ID_Pat)
	{ 
		String sql ="Select  ID_Cons,MP,Date_consult from Consult where  ID_Pat='"+ID_Pat+"'";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			//changer le model de la table
			tableCons.setModel(DbUtils.resultSetToTableModel(resultat));
			//ajouter un sorter pour trier la table	
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableCons.getModel());
			tableCons.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sorter.setSortKeys(sortKeys);
			//renomer les  colones
			tableCons.getColumnModel().getColumn( 0 ).setHeaderValue("ID consultation");
			tableCons.getColumnModel().getColumn( 1 ).setHeaderValue("Prix à payer");
			tableCons.getColumnModel().getColumn( 2 ).setHeaderValue("Date consultation");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}
  //actualiser la table info de paiement*********************************************************************************************************************************
	public void UpedateTableinfo(String ID_Pat)
	{ 
		String sql ="Select Num_Paiement,Date_paiement,Date_Consult,prix_a_payer,prix_payé,etat,Commentaire from paiement where  ID_Pat='"+ID_Pat+"'";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			//changer le model de la table
			tableInfo.setModel(DbUtils.resultSetToTableModel(resultat));
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableInfo.getModel());
			tableInfo.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sorter.setSortKeys(sortKeys);
			//renomer les  colones
			tableInfo.getColumnModel().getColumn( 0 ).setHeaderValue("N°");
			tableInfo.getColumnModel().getColumn( 1 ).setHeaderValue("Date du paiement");
			tableInfo.getColumnModel().getColumn( 2 ).setHeaderValue("Date de la consultation");
			tableInfo.getColumnModel().getColumn( 3 ).setHeaderValue("Prix à payer");	
			tableInfo.getColumnModel().getColumn( 4 ).setHeaderValue("Montant Payé");	
			tableInfo.getColumnModel().getColumn( 5 ).setHeaderValue("état du paiement");	
			tableInfo.getColumnModel().getColumn( 6 ).setHeaderValue("Commentaire");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}

//Remplire la comboBox Doc//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
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
// limiter les char dans un text field//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
		public void txtLimiter(Component O,int x) {
			((Component) O).addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (		((AbstractButton) O).getText().length() >= x ) 
			            e.consume(); 
			    }  
			});
		}
	
///Recherche///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
		private void Recherche(java.awt.event.KeyEvent evt) {                                      
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
	
	

