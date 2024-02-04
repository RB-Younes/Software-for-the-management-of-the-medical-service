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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Consultation------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class ConsultationFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableCons;
	private JButton btnAddCons;
	private JButton btnSuppCons;
	private JButton btnModCons;
	private BilanFrame frame =new BilanFrame("");
	private PrescrireTraitementFrame frame2=new PrescrireTraitementFrame("","");

	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String numCons;// le id de la consultation recupere en cliquant sur le tableau
	private JTable tableRDV; //table RDV
	private JTextField textFieldPoid;
	private JTextField textFieldTaux_Diab;
	private JTextField textFieldTension;
	private JTextField textFieldMontant;
	private String ID_PAT="1";
	private String NUM_RDV;
	private String ID_MEDgene="1";
	@SuppressWarnings("unused")
	private String D_RDV;
	private JTextField textFieldMaladie1;
	private JTextField txtMaladie2;
	private JTextField textFieldMaladie3;
	private JTextField textFieldMaladie_4;
	private JButton btnPresTrait;
	private JButton btnBilan;
	private JCheckBox checkBoxDemanderBilan;
	private JSpinner spinner;
	private JSlider sliderPoid;
	private JCheckBox chckbxDiabO_N;
	private JTextArea textArea_observation;
	private JTextArea textAreaDiag;
	
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
					ConsultationFrame frame = new ConsultationFrame("2");
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
	
public ConsultationFrame(String ID_MED) {//faire passer le id du medecin pour afficher que les rdv concernanat ce medecin
// CNX 
		cnx = ConnexionDB.CnxDB();
		ID_MEDgene=ID_MED;
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
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 680, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConsultationFrame.class.getResource("/Menu_Medcin_img/consultation pat.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 680);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//le bale pour l'arrier plan----------------------------------------------------------------------------------------------------------------------------------------------------------
		JLabel BGStaff = new JLabel("BGStaffM");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	//Combo BOX Groupe sanguin----------------------------------------------------------------------------------------------------------------------------------------
		
		
		JComboBox<String> comboBoxGRPS = new JComboBox<String>();
		comboBoxGRPS.setFont(new Font("Segoe UI", Font.BOLD, 12));
		comboBoxGRPS.setModel(new DefaultComboBoxModel<String>(new String[] {"Groupe Sanguin","A+","A-","B+","B-","AB+","AB-","O+","O-"} ));
		comboBoxGRPS.setBackground(new Color(51, 204, 153));
		comboBoxGRPS.setForeground(new Color(255, 255, 255));
		comboBoxGRPS.setBounds(378, 151, 156, 28);
		contentPane.add(comboBoxGRPS);
		
	//Combo BOX Patients---------------------------------------------------------------------------------------------------------------------------------------------
		JComboBox<String> comboBoxPAtients = new JComboBox<String>();
		comboBoxPAtients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (comboBoxPAtients.getSelectedIndex()!=0) {//choisir le patient
					String PatInfo=comboBoxPAtients.getSelectedItem().toString();
					
					if (!PatInfo.equals("Select Patient")) {
						 int finIDindice=PatInfo.indexOf("-");//indice de fin d l'id

						 if (finIDindice != -1) 
						 {
							   ID_PAT= PatInfo.substring(0 , finIDindice); //recuperer l'id du patient
							 
						 }
						 String sql="Select GRP_SNG from Consult where ID_Pat='"+ID_PAT+"'";//selectionner le groupe sanguin du patient
							
						  try {
							//recuperer la date actuelle
						   Date actuelle = new Date();
						   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
						   String datactuelle = dateFormat.format(actuelle);
						   
						   sql="Select NUM_RDV from RDV where ID_Pat='"+ID_PAT+"'and Date_RDV='"+datactuelle+"'";//selectioner le numero du RDV 
						   prepared=cnx.prepareStatement(sql); 
							resultat=prepared.executeQuery();
							 if(resultat.next())
							   {
								    NUM_RDV=resultat.getString("NUM_RDV");//recuperer le ID du RDV
							   }
						   
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e);
						} 
						}

				}
				
			}
		});
		comboBoxPAtients.addItem("Selectionner Patient");
		INComboboxPat(comboBoxPAtients,ID_MED);
		comboBoxPAtients.setBackground(new Color(51, 204, 153));
		comboBoxPAtients.setForeground(new Color(255, 255, 255));
		comboBoxPAtients.setBounds(38, 154, 240, 32);
		contentPane.add(comboBoxPAtients);
		
	//slider pour le poid---------------------------------------------------------------------------------------------------------------------------------
		sliderPoid = new JSlider(JSlider.HORIZONTAL,0, 160,80 );
		textFieldPoid = new JTextField();
		textFieldPoid.addKeyListener(new KeyAdapter(){
	            @Override
	            public void keyReleased(KeyEvent ke) {//reciperer la valeur du slider dans textField(poid)
	                String typed = textFieldPoid.getText();
	                sliderPoid.setValue(0);
	                if(!typed.matches("\\d+") || typed.length() > 3) {
	                    return;
	                }
	                int value = Integer.parseInt(typed);
	                sliderPoid.setValue(value);
	            }
	        });
		
	//text field poid-----------------------------------------------------------------------------------------------------------------------------------------
		textFieldPoid.setForeground(Color.WHITE);
		textFieldPoid.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldPoid.setText("Poids");
		textFieldPoid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textFieldPoid.getText().toString().equals("Poids")) {//vider le text de base au clique
					textFieldPoid.setText("");
				}
				
			}
		});
		textFieldPoid.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldPoid.getText().length() >= 20 )//limiter le nombre de carctere a 20
		            e.consume(); 
		    }  
		});
		textFieldPoid.setBounds(645, 217, 64, 25);
		textFieldPoid.setBackground(new Color(51, 204, 153));
		textFieldPoid.setColumns(4);
		contentPane.add(textFieldPoid);
		sliderPoid.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int poid=sliderPoid.getValue();
				textFieldPoid.setText(String.valueOf(poid));
			}
		});
		sliderPoid.setMajorTickSpacing(10);
		sliderPoid.setMinorTickSpacing(0);
		sliderPoid.setPaintTicks(true);
		sliderPoid.setPaintLabels(true);
		sliderPoid.setBounds(730, 206, 445, 42);
		contentPane.add(sliderPoid);
		
	//spinner pour la taille-----------------------------------------------------------------------------------------------------------------------------------------	
		spinner = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 2.5, 0.01));
		spinner.setForeground(Color.WHITE);
		spinner.setFont(new Font("Segoe UI", Font.BOLD, 12));
		spinner.setBackground(new Color(51, 204, 153));
		spinner.setBounds(454, 220, 156, 28);
		contentPane.add(spinner);
	//checkBox Diabetique ou pas----------------------------------------------------------------------------------------------------------------------------------------	
		chckbxDiabO_N = new JCheckBox("Diab\u00E9tique ");
		chckbxDiabO_N.setForeground(Color.WHITE);
		chckbxDiabO_N.setBackground(new Color(51, 204, 153));
		chckbxDiabO_N.setFont(new Font("Segoe UI", Font.BOLD, 12));
		chckbxDiabO_N.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (chckbxDiabO_N.isSelected()) {//si lme patient est diabetique alors
					textFieldTaux_Diab.setVisible(true);//rendre le text field taux de diabete vesible
					textFieldTaux_Diab.setText("Taux de Diabete");
					BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter Diab.png")));//changer le background
				}
			else {//sinon revenir au parametre par defaut
					textFieldTaux_Diab.setVisible(false);
					BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter.png")));
				}
			}
		});
		chckbxDiabO_N.setBounds(38, 222, 156, 25);
		contentPane.add(chckbxDiabO_N);
	//textField taux de diabete----------------------------------------------------------------------------------------------------------------------------------------------	
		textFieldTaux_Diab = new JTextField();
		textFieldTaux_Diab.setForeground(Color.WHITE);
		textFieldTaux_Diab.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldTaux_Diab.setBackground(new Color(51, 204, 153));
		textFieldTaux_Diab.setText("Taux de Diabete");
		textFieldTaux_Diab.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { // limiter le nombre de caractere pourr ne pas rencontrer des erreurs
		        if (		textFieldTaux_Diab.getText().length() >= 25 ) // limit textfield to 25 characters
		            e.consume(); 
		    }  
		});
		textFieldTaux_Diab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textFieldTaux_Diab.getText().toString().equals("Taux de Diabete")) {//vider le text field au clique 
					textFieldTaux_Diab.setText("");
				}
				
			}
		});
		textFieldTaux_Diab.setBounds(258, 221, 132, 28);
		textFieldTaux_Diab.setVisible(false);
		contentPane.add(textFieldTaux_Diab);
		textFieldTaux_Diab.setColumns(10);
		
	//textfield tension-----------------------------------------------------------------------------------------------------------------------------------------------------------	
		textFieldTension = new JTextField();
		textFieldTension.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldTension.getText().toString().equals("Pression Arterielle")) {
					textFieldTension.setText("");
				}
			}
		});
		textFieldTension.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldTension.getText().length() >= 25 ) 
		            e.consume(); 
		    }  
		});
		textFieldTension.setForeground(Color.WHITE);
		textFieldTension.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldTension.setBackground(new Color(51, 204, 153));
		textFieldTension.setText("Pression Arterielle");
		textFieldTension.setBounds(522, 280, 121, 28);
		contentPane.add(textFieldTension);
		textFieldTension.setColumns(6);

	//Diagnostique+scrollpane-----------------------------------------------------------------------------------------------------------------------------------------------------
		JScrollPane scrDiag = new JScrollPane();
		scrDiag.setSize(399, 127);
		scrDiag.setLocation(35, 289);
		contentPane.add(scrDiag);
		textAreaDiag = new JTextArea();
		textAreaDiag.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textAreaDiag.getText().length() >= 2000 ) 
		            e.consume(); 
		    }  
		});
		textAreaDiag.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textAreaDiag.setForeground(new Color(255, 255, 255));
		scrDiag.setViewportView(textAreaDiag);
		textAreaDiag.setBackground(new Color(51, 204, 153));
		
	//observation+scrollpane------------------------------------------------------------------------------------------------------------------------------------------------------
		JScrollPane  scr_observation = new JScrollPane();
		scr_observation.setSize(291, 127);
		scr_observation.setLocation(781, 289);
		contentPane.add(scr_observation);
		textArea_observation = new JTextArea();
		textArea_observation.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textArea_observation.getText().length() >= 2000 ) 
		            e.consume(); 
		    }  
		});
		textArea_observation.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textArea_observation.setForeground(new Color(255, 255, 255));
		textArea_observation.setBackground(new Color(51, 204, 153));
		scr_observation.setViewportView(textArea_observation);
		
	//le Montant-------------------------------------------------------------------------------------------------------------------------------------------------------------------	
		textFieldMontant = new JTextField();
		textFieldMontant.addKeyListener(new KeyAdapter() {//permetre decrire que des chifres
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
		textFieldMontant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldMontant.getText().toString().equals("Montanat a paye")) {
					textFieldMontant.setText("");
				}
			}
		});
		textFieldMontant.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldMontant.getText().length() >= 25 ) //limiter le nombre de caracter a 25
		            e.consume(); 
		    }  
		});
		
		textFieldMontant.setForeground(Color.WHITE);
		textFieldMontant.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldMontant.setText("Montanat a paye");
		textFieldMontant.setBackground(new Color(51, 204, 153));
		textFieldMontant.setBounds(581, 428, 156, 28);
		contentPane.add(textFieldMontant);
		textFieldMontant.setColumns(10);
		
//ScrollPane Table Patient////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//Grande table *TABLE CONSULTATION****************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneCons = new JScrollPane();
		scrollPaneCons.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneCons.setBorder(null);
		scrollPaneCons.setBounds(25, 498, 1150, 164);
		contentPane.add(scrollPaneCons);
		
		tableCons = new JTable();
		tableCons.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableCons.setForeground(new Color(255, 255, 255));
		tableCons.setBorder(null);
		tableCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les deux boutons Modifier et supprimer et prescrire traitement
				btnModCons.setEnabled(true);
				btnSuppCons.setEnabled(true);
				btnPresTrait.setEnabled(true);
				
				int ligne=tableCons.getSelectedRow();//recuperer le numero de ligne
				

				numCons =tableCons.getValueAt(ligne, 0).toString();//le numero de consultation
				String sql="Select *  from Consult where ID_Cons='"+numCons+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						NUM_RDV=resultat.getString("NUM_RDV");//recuperer l'ID du RDV qui est a la premiere colone (0) de la table
						
						comboBoxGRPS.setSelectedItem(resultat.getString("GRP_SNG"));//recuperer le groupe sanguin de la bdd 
						textArea_observation.setText(resultat.getString("Obs"));//recuperer l'observation de la bdd 
						textAreaDiag.setText(resultat.getString("Diag"));//recuperer le diagnostique de la bdd 
						textFieldTension.setText(resultat.getString("tension"));//recuperer la tension de la bdd 
						textFieldMontant.setText(resultat.getString("MP"));//recuperer le montant de la bdd 
						
						String T =resultat.getString("taille");//recuperer la taille en string
						spinner.setValue(Float.valueOf(T));//la transformer en float
						
						textFieldPoid.setText(resultat.getString("poids"));//recuperer le poid
						 String typed = textFieldPoid.getText();
			                sliderPoid.setValue(0);
			                if(!typed.matches("\\d+") || typed.length() > 3) {
			                    return;
			                }
			                int value = Integer.parseInt(typed);
			                sliderPoid.setValue(value);//mettre la valeur dans le slider

						 String Maladie1=" ";//recuperer maladie 1
						 String Maladie2=" ";//recuperer maladie 2
						 String Maladie3=" ";//recuperer maladie 3
						 String Maladie4=" ";//recuperer maladie 4

						 String Maladies =resultat.getString("M_Diag");//recupereer toute les 4 maladies en un seul String tel que entre chaque maladie y'a une virgule pour separer
						//Maladie 1,Maladie 2,Maladie 3,Maladie 4 <------------------------ le format
						
							 int finM1indice=Maladies.indexOf(",");//recuperer lindice de fin de la maladie 1
							 
							 if (finM1indice != -1) 
							 {
								 Maladie1= Maladies.substring(0 , finM1indice); //recuperer maladie 1
								 
								 String M2=Maladies.substring(finM1indice+1 , Maladies.length());//enleve maladie 1 de lachaine proncipale 
								 int finM2indice=M2.indexOf(","); //donc deviens lindice de fin de la maladie 2
								 Maladie2=M2.substring(0 , finM2indice);//recuperer maladie 2
								 
								 String M3=M2.substring(finM2indice+1 , M2.length());//de meme on va enlever la maladie 2
								 int finM3indice=M3.indexOf(","); //indice de foin de l maladie 3
								 Maladie3=M3.substring(0 , finM3indice);//recuperer la maladie 3

								 String M4=M3.substring(finM3indice+1 , M3.length());//enlever maladie 3 il va rester que maladie 4
								 Maladie4=M4;
							 }
							 //les mettre dans les texts fields
							 textFieldMaladie1.setText(Maladie1);
							 txtMaladie2.setText(Maladie2);
							 textFieldMaladie3.setText(Maladie3);
							 textFieldMaladie_4.setText(Maladie4);
							 
							 String Bilan_O_N=resultat.getString("Bilan_O_N");//la valeur e bilan
							 if (Bilan_O_N.equals("Oui")) {btnBilan.setEnabled(true); checkBoxDemanderBilan.setSelected(true);}//si ila un bilan alors le bouton Bilan sera activé et la check box cauché
							 else {btnBilan.setEnabled(false); checkBoxDemanderBilan.setSelected(false);}//le contraire
							
						if (resultat.getString("Diab_O_N").equals("Oui")) {//dans le cas ou elle est diabetique 
							BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter Diab.png")));//changer le back ground
							chckbxDiabO_N.setSelected(true);//cocher la checkBox
							textFieldTaux_Diab.setVisible(true);//rendre le textfield taux de Diabete vesible
							textFieldTaux_Diab.setText(resultat.getString("T_Diab"));//mettre le taux dans le etxt field
						}
						else {
							if (chckbxDiabO_N.isSelected()) {//le cas contraire
								BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter.png")));
								chckbxDiabO_N.setSelected(false);
								textFieldTaux_Diab.setVisible(false);
								textFieldTaux_Diab.setText("Taux de Diabete");
							}chckbxDiabO_N.setSelected(false);
							
						}

					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				
				
				
			}
		});
		tableCons.setBackground(new Color(0, 204, 153));
		scrollPaneCons.setViewportView(tableCons);
	
	//Mini table Table RDV***************************************************************************************************************************************************************************************	
		JScrollPane scrollPaneRDV = new JScrollPane();
		scrollPaneRDV.setBounds(599, 60, 552, 107);
		contentPane.add(scrollPaneRDV);
		tableRDV = new JTable();
		tableRDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableRDV.setForeground(new Color(255, 255, 255));
		tableRDV.setBorder(null);
		tableRDV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//tout les elements recuperés vont etre vont etre mis dans leur text field(comboBox) respective
				//activer les deux boutons add cons et prescrire traitement
				btnAddCons.setEnabled(true);
				btnPresTrait.setEnabled(true); 
				
				int ligne=tableRDV.getSelectedRow();//recuperer le numero de ligne
				NUM_RDV =tableRDV.getValueAt(ligne, 0).toString();///recuperer l'ID du RDV qui est a la premiere colone (0) de la table
				String sql="Select Date_RDV,ID_Pat from RDV where NUM_RDV='"+NUM_RDV+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						
						ID_PAT=resultat.getString("ID_Pat").toString();//recupperer l'id du patient 
						D_RDV=resultat.getString("Date_RDV").toString();//recuperer la date du RDV
						//parcourir la combobox Patient
						for (int i = 1; i <comboBoxPAtients.getItemCount() ; i++) {
								   if (ID_PAT.equals(comboBoxPAtients.getItemAt(i).toString().substring(0 , comboBoxPAtients.getItemAt(i).toString().indexOf("-")))) //retirer l'ID de la combobox
								   {// selectionner cet element
									   comboBoxPAtients.setSelectedIndex(i);
								   } 
							}
						 UpedateTablecons(ID_PAT);//actualiser la table consuyltation avec l'id du patient du rdv
						   int row = tableCons.getRowCount();
							 if (row == 0) {
								 
								 btnPresTrait.setEnabled(true);
							}
						sql="Select GRP_SNG from Consult where ID_Pat='"+ID_PAT+"'";//a la selection du rdv le groupe santguin sera mis autmatiquement sauf pour la premiere consultation
								   prepared=cnx.prepareStatement(sql); 
								   resultat=prepared.executeQuery();
								   if(resultat.next())
								   {
									    comboBoxGRPS.setSelectedItem(resultat.getString("GRP_SNG").toString());
								   }
								   else {
									   	comboBoxGRPS.setSelectedIndex(0);
								   }
								   
									
									int i=0;//le nombre de RDV
									sql="Select * from Consult where ID_pat='"+ID_PAT+"'";
									prepared=cnx.prepareStatement(sql);
									resultat=prepared.executeQuery();
									while (resultat.next()) {
										i++;
									}
									if (i==0) {
										tableRDV.setSelectionBackground(Color.RED);
										tableRDV.setSelectionForeground(Color.WHITE);
										
									}
									else {
										tableRDV.setSelectionBackground(Color.GREEN);
										tableRDV.setSelectionForeground(Color.WHITE);
									}
						
					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				//vider les champs'et reinitialiser)
				spinner.setValue(0);
				sliderPoid.setValue(80);
				chckbxDiabO_N.setSelected(false);
				textFieldMaladie1.setText("");
				textArea_observation.setText("");
				textAreaDiag.setText("");
				textFieldMontant.setText("Montanat a paye");
				textFieldPoid.setText("Poids");
				textFieldTaux_Diab.setText("Taux de Diabete");
				textFieldTaux_Diab.setVisible(false);
				textFieldTension.setText("Pression Arterielle");
				BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter.png")));
				textFieldMaladie1.setText("Maladie 1");
				txtMaladie2.setText("Maladie 2");
				textFieldMaladie3.setText("Maladie 3");
				textFieldMaladie_4.setText("Maladie 4");
				checkBoxDemanderBilan.setSelected(false);
				btnBilan.setEnabled(false);
			}
		});
		tableRDV.setBackground(new Color(0, 204, 153));
		scrollPaneRDV.setViewportView(tableRDV);
		
		
//afficher le contenue de la table RDV juste a louverture/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//recuperer la datedu systeme pur affiher que les rdc du jours-----------------
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String datactuelle = dateFormat.format(actuelle);
		UpedateTableRDVJours(ID_MED,datactuelle );//afficher les rdv que du medcin  (identifié) avec la date du jours
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1077, 3, 32, 32);
		contentPane.add(Minimise_BTN);

// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// remtre les valeurs par default de la combobox
				spinner.setValue(0);
				comboBoxPAtients.setSelectedIndex(0);
				comboBoxGRPS.setSelectedIndex(0);
				sliderPoid.setValue(80);
				chckbxDiabO_N.setSelected(false);
				textFieldMaladie1.setText("");
				textArea_observation.setText("");
				textAreaDiag.setText("");
				textFieldMontant.setText("Montanat a paye");
				textFieldPoid.setText("Poids");
				textFieldTaux_Diab.setText("Taux de Diabete");
				textFieldTaux_Diab.setVisible(false);
				textFieldTension.setText("Pression Arterielle");
				BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter.png")));
				textFieldMaladie1.setText("Maladie 1");
				txtMaladie2.setText("Maladie 2");
				textFieldMaladie3.setText("Maladie 3");
				textFieldMaladie_4.setText("Maladie 4");
				checkBoxDemanderBilan.setSelected(false);
				btnBilan.setEnabled(false);
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/rubbish.png")));
		btnViderFields.setBounds(1119, 415, 32, 32);
		contentPane.add(btnViderFields);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						
						if (frame.isVisible()) {//si la fenetre Prescrire traitement est vesible alors la fermer
							frame.dispose();
						}
						if (frame2.isVisible()) {//de meme pour la fenetre Bilan
							frame2.dispose();
						}
						
						dispose();
					}
					
					
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1161, 3, 32, 32);
		contentPane.add(Exit_BTN);
//bouton actualiser pour la table consultation //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnactualiser = new JButton("");
		btnactualiser.setToolTipText("Actualiser");
		btnactualiser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnactualiser.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/refresh selected .png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnactualiser.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/refresh .png")));//remetre le bouton de base
			}
		});
		btnactualiser.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/refresh .png")));
		btnactualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				if (!comboBoxPAtients.getSelectedItem().toString().equals("")) {//il faut dabord le patient doit etre choisi
					UpedateTablecons(ID_PAT);
					btnModCons.setEnabled(false);//desactiver le bouton Modifier
					btnSuppCons.setEnabled(false);//desactiver le bouton supprimer
				}
	
			}
		});
		btnactualiser.setBounds(1129, 464, 32, 32);
		contentPane.add(btnactualiser);

//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//Supprimer une consultation**********************************************************************************************************************************************************************************************************************************
		btnSuppCons = new JButton();
		btnSuppCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnSuppCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnSuppCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/remove selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnSuppCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/remove.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnSuppCons);
		btnSuppCons.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tableCons.getSelectedRow()==-1) // return  quand aucune ligne esr selectionnée
				{//message d'erreur
					JOptionPane.showMessageDialog(null, " S'il vous plait veuillez selectioner une ligne(Consultation) a supprimer !","WArning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));

				}
				else {
					ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Supprimer cette consultations ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{	
					Consultation cons=new  Consultation();
					cons.Supprimer(numCons); //appele de la Methode supprimer de la classe consultation
					btnAddCons.setEnabled(false);//desactiver le bouton ajouter
					btnPresTrait.setEnabled(false); //desactiver le bouton prescrire traitement
					btnModCons.setEnabled(false);//desactiver le bouton Modifier
					btnSuppCons.setEnabled(false);//desactiver le bouton supprimer
					UpedateTablecons(ID_PAT);//Actualiser la table
				}
				}
				
				
			}
		});
		btnSuppCons.setToolTipText("Supprimer une Consultation");
		btnSuppCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/remove.png")));
		btnSuppCons.setBounds(1119, 372, 32, 32);
		contentPane.add(btnSuppCons);
		
		
	//Ajouter une consultation**********************************************************************************************************************************************************************************************************************************
		btnAddCons = new JButton();
		btnAddCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table RDV(mini table) n'a ete selectionné
		btnAddCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnAddCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/plus selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnAddCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/plus.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnAddCons);
		btnAddCons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
				 icon.getImage().flush(); // réinitialiser l'animation
				//message de confirmation
				int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Ajouter une consultation a ce patient?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
				if(ClickedButton==JOptionPane.YES_OPTION)
				{
					
					int ID_pat = 1;
					 String Name_pat=" ";
					 String FirstName_pat=" ";
					 String Id_pat="";
					String PatInfo=comboBoxPAtients.getSelectedItem().toString();//recupperer les infos du Patient de la forme ID_patient-Nom Prenom
					if (!PatInfo.equals("Selectionner Patient")) {
						 int finIDindice=PatInfo.indexOf("-");//indice de fin de l'id
						 int finnomindice=PatInfo.indexOf(" ");//indice de fin de nom
						 int finprenomindice=PatInfo.length();//indice de fin prenom
						 if (finIDindice != -1) 
						 {
							 Id_pat = PatInfo.substring(0 , finIDindice);//recuperer  ID du patient
							  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du patient
							  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du patient
							  ID_pat=Integer.parseInt(Id_pat); //transformer l'id en int
						 }
						 String diab_O_N;String TauxDiab;
						 
						 			if (chckbxDiabO_N.isSelected()) { diab_O_N="Oui";  TauxDiab =textFieldTaux_Diab.getText().toString();}
						 			else { diab_O_N="Non"; TauxDiab="-";}

						 			Date actuelle = new Date();
						 			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
						 			String datactuelle = dateFormat.format(actuelle);
						 			
						 	String M1=textFieldMaladie1.getText().toString();	//recuperer Maladie 1	
						 	String M2=txtMaladie2.getText().toString();		//recuperer Maladie 2	
						 	String M3=textFieldMaladie3.getText().toString();	//recuperer Maladie 3			
						 	String M4=textFieldMaladie_4.getText().toString();	//recuperer Maladie 4
						 	
						 	String taille =spinner.getValue().toString();//recuperer la taille
						 	
						 if (taille.length()>3) {
							 taille =spinner.getValue().toString().substring(0, 4);//prendre que les 4 premier caractere
						 }
						 String Bilan_O_N;
						//bilan oui iu non
						 if (checkBoxDemanderBilan.isSelected()) { Bilan_O_N="Oui";}
				 		else { Bilan_O_N="Non"; }
						 
						Consultation cons = new Consultation(datactuelle , ID_pat,NUM_RDV, Name_pat+" "+FirstName_pat, comboBoxGRPS.getSelectedItem().toString(),taille, textFieldPoid.getText().toString(),
								textFieldTension.getText().toString(), diab_O_N, TauxDiab, textAreaDiag.getText().toString(),M1+","+M2+","+M3+","+M4, textArea_observation.getText().toString(),
								Bilan_O_N,textFieldMontant.getText().toString());//creer une instance de consultation
						 
						cons.Ajouter(cons);//ajouter consultation
						UpedateTablecons(Id_pat);//actualiser la table
						btnAddCons.setEnabled(false);//desactiver le bouton ajouter
						btnPresTrait.setEnabled(false); //desactiver le bouton prescrire traitement
						btnModCons.setEnabled(false);//desactiver le bouton Modifier
						btnSuppCons.setEnabled(false);//desactiver le bouton supprimer
						}
					else {
						//message
						JOptionPane.showMessageDialog(null,  "S'il vous plait slectionnez un Patient de la combobox ou bien de la table RDV !","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));				
					}
			
				}
				UpedateTableRDVJours(ID_MED, datactuelle);
			}
		});
		btnAddCons.setToolTipText("Ajouter Consultation");
		btnAddCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/plus.png")));
		btnAddCons.setBounds(1119, 278, 32, 32);
		contentPane.add(btnAddCons);
		
	//Modifie une consultation**********************************************************************************************************************************************************************************************************************************
		btnModCons = new JButton();
		btnModCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table consultation n'a ete selectionné
		btnModCons.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnModCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/Modify selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnModCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/Modify.png")));//remetre le bouton de base
			}
		});
		ButtonStyle(btnModCons);
		btnModCons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (tableCons.getSelectedRow()==-1 || comboBoxPAtients.getSelectedIndex()==0 || comboBoxGRPS.getSelectedIndex()== 0)  //verifier si une ligne de la table consultation a ete selectionné aussi pour la combo Box patient et grupe sanguin si on bien ete choisi
				{//message
					JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne de la table consultation a modifer !( Verifiez bien que vous avez choisi le bon patient et le groupe sanguin)","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
				}
				else 
				{
					int ID_pat = 1;
					 String Name_pat=" ";
					 String FirstName_pat=" ";
					 String Id_pat="";
					String PatInfo=comboBoxPAtients.getSelectedItem().toString();//recuperer les infos du patients
					
					 int finIDindice=PatInfo.indexOf("-");//indice de fin de l'id
					 int finnomindice=PatInfo.indexOf(" ");//indice de fin de nom
					 int finprenomindice=PatInfo.length();//indice de fin prenom
					 if (finIDindice != -1) 
					 {
						 Id_pat = PatInfo.substring(0 , finIDindice);//recuperer  ID du patient
						  Name_pat=PatInfo.substring(finIDindice+1 , finnomindice);//recuperer Nom du patient
						  FirstName_pat=PatInfo.substring(finnomindice+1 , finprenomindice);//recuperer Prenom du patient
						  ID_pat=Integer.parseInt(Id_pat);  //transformer l'id en int
					 }
					
					String Grp=comboBoxGRPS.getSelectedItem().toString();//recuperer le groupe sanguin
					String taille =spinner.getValue().toString();//recuperer la taille
					 if (taille.length()>3) {
						 taille =spinner.getValue().toString().substring(0, 4);
					 }
					String poids=textFieldPoid.getText().toString();//recuperer le poid
					String ts=textFieldTension.getText().toString();//recuperer la tension
					
					String diab_O_N;String TauxDiab;
					 //Diabetique oui ou Non et le taux de diabete
		 			if (chckbxDiabO_N.isSelected()) { diab_O_N="Oui";  TauxDiab =textFieldTaux_Diab.getText().toString();}
		 			else { diab_O_N="Non"; TauxDiab="-";}

		 			String M1=textFieldMaladie1.getText().toString();//recuperer Maladie 1		
		 			String M2=txtMaladie2.getText().toString();	//recuperer Maladie	2
		 			String M3=textFieldMaladie3.getText().toString();	//recuperer Maladie 3					
		 			String M4=textFieldMaladie_4.getText().toString();	//recuperer Maladie	4
					
		 			 String Bilan_O_N;//bilan oui iu non
					 if (checkBoxDemanderBilan.isSelected()) { Bilan_O_N="Oui";}
			 			else { Bilan_O_N="Non"; }
					 
					 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
					 icon.getImage().flush(); // réinitialiser l'animation
					//message de confirmation
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Vouler Vous vraiment Modifier Les informations de cette consultation ?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						Consultation cns = new Consultation();//creer une instance de consultation 
						cns.MetreAjour(numCons, ID_pat,NUM_RDV, Name_pat+""+FirstName_pat, Grp, taille, poids, ts, diab_O_N, TauxDiab,textAreaDiag.getText().toString(),M1+","+M2+","+M3+","+M4, textArea_observation.getText().toString(),
								textFieldMontant.getText().toString(),Bilan_O_N );
						btnAddCons.setEnabled(false);//desactiver le bouton ajouter
						btnPresTrait.setEnabled(false); //desactiver le bouton prescrire traitement
						btnModCons.setEnabled(false);//desactiver le bouton Modifier
						btnSuppCons.setEnabled(false);//desactiver le bouton supprimer
						
						UpedateTablecons(Id_pat);//actualiser la table
					}
				}
			}
		});
		btnModCons.setToolTipText("Modifier info Consutation");
		btnModCons.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/Modify.png")));
		btnModCons.setBounds(1119, 324, 32, 32);
		contentPane.add(btnModCons);
//text fields Maladie x4---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
	//text Field maladie 1
		textFieldMaladie1 = new JTextField();
		textFieldMaladie1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textFieldMaladie1.getText().toString().equals("Maladie 1")) {//vider le text field au clique (supprimer le text par efault)
					textFieldMaladie1.setText("");
				}
				
			}
		});
		textFieldMaladie1.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldMaladie1.getText().length() >= 250 ) //limiter le nombre de caractere a 250
		            e.consume(); 
		    }  
		});
		textFieldMaladie1.setText("Maladie 1");
		textFieldMaladie1.setForeground(Color.WHITE);
		textFieldMaladie1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldMaladie1.setColumns(4);
		textFieldMaladie1.setBackground(new Color(51, 204, 153));
		textFieldMaladie1.setBounds(475, 350, 121, 25);
		contentPane.add(textFieldMaladie1);
		//le meme traitement pour le reste des texts fields
	//text Field maladie 2
		txtMaladie2 = new JTextField();
		txtMaladie2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMaladie2.getText().toString().equals("Maladie 2")) {
					txtMaladie2.setText("");
				}
			}
		});
		txtMaladie2.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		txtMaladie2.getText().length() >= 250 ) 
		            e.consume(); 
		    }  
		});
		txtMaladie2.setText("Maladie 2");
		txtMaladie2.setForeground(Color.WHITE);
		txtMaladie2.setFont(new Font("Segoe UI", Font.BOLD, 12));
		txtMaladie2.setColumns(4);
		txtMaladie2.setBackground(new Color(51, 204, 153));
		txtMaladie2.setBounds(475, 386, 121, 25);
		contentPane.add(txtMaladie2);
		
	//text Field maladie3
		textFieldMaladie3 = new JTextField();
		textFieldMaladie3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldMaladie3.getText().toString().equals("Maladie 3"))
				{
					textFieldMaladie3.setText("");
				}
			}
		});
		textFieldMaladie3.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldMaladie3.getText().length() >= 250 ) 
		            e.consume(); 
		    }  
		});
		textFieldMaladie3.setText("Maladie 3");
		textFieldMaladie3.setForeground(Color.WHITE);
		textFieldMaladie3.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldMaladie3.setColumns(4);
		textFieldMaladie3.setBackground(new Color(51, 204, 153));
		textFieldMaladie3.setBounds(617, 350, 121, 25);
		contentPane.add(textFieldMaladie3);
		
	//text Field maladie4	
		textFieldMaladie_4 = new JTextField();
		textFieldMaladie_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldMaladie_4.getText().toString().equals("Maladie 4")) {
					textFieldMaladie_4.setText("");
				}
			}
		});
		textFieldMaladie_4.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (		textFieldMaladie_4.getText().length() >= 250 ) 
		            e.consume(); 
		    }  
		});
		textFieldMaladie_4.setText("Maladie 4");
		textFieldMaladie_4.setForeground(Color.WHITE);
		textFieldMaladie_4.setFont(new Font("Segoe UI", Font.BOLD, 12));
		textFieldMaladie_4.setColumns(4);
		textFieldMaladie_4.setBackground(new Color(51, 204, 153));
		textFieldMaladie_4.setBounds(617, 385, 121, 25);
		contentPane.add(textFieldMaladie_4);
//Bilan bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		 btnBilan = new JButton("Bilan");
		 btnBilan.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {//ouvrir le menu Bilan
		 		 frame = new BilanFrame(NUM_RDV);
		 		frame.setLocationRelativeTo(null);
				frame.setVisible(true);
		 	}
		 });
		btnBilan.setEnabled(false);
		btnBilan.setForeground(new Color(51, 204, 153));
		btnBilan.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnBilan.setBounds(240, 431, 194, 25);
		contentPane.add(btnBilan);
//btn ordonance	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		btnPresTrait = new JButton("Prescrire Traitement");
		btnPresTrait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetvesibleTrait();//appler la fonctionde communication
			}
		});
		btnPresTrait.setForeground(new Color(0, 204, 153));
		btnPresTrait.setEnabled(false);
		btnPresTrait.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnPresTrait.setBounds(848, 431, 194, 25);
		contentPane.add(btnPresTrait);
//check box//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		checkBoxDemanderBilan = new JCheckBox("Demander un Bilan");
		checkBoxDemanderBilan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (checkBoxDemanderBilan.isSelected()) {//check box cocher ----->activer le bouton pour ouvrir la frame bilan 
					btnBilan.setEnabled(true);
				}
			else {
				btnBilan.setEnabled(false);
				}
			}
		});
		checkBoxDemanderBilan.setForeground(Color.WHITE);
		checkBoxDemanderBilan.setFont(new Font("Segoe UI", Font.BOLD, 12));
		checkBoxDemanderBilan.setBackground(new Color(51, 204, 153));
		checkBoxDemanderBilan.setBounds(54, 432, 156, 25);
		contentPane.add(checkBoxDemanderBilan);
		
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
							MenuMedcinFrame frame1 = new MenuMedcinFrame(ID_MED);
							frame1.setLocationRelativeTo(null);
							frame1.setVisible(true);
							
							if (frame.isVisible()) {//fermer la fenetre prescrire traitement
								frame.dispose();
							}
							if (frame2.isVisible()) {//fermer la fenetre Bilan
								frame2.dispose();
							}
							
							dispose();//fermer la fenetre consultation
					}
				});
				btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(1119, 3, 32, 32);
				contentPane.add(btnHome);

//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGStaff.setIcon(new ImageIcon(ConsultationFrame.class.getResource("/Consultation_img/consulter.png")));
		BGStaff.setBounds(0, 0, 1200, 680);
		contentPane.add(BGStaff);
		


	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void ButtonStyle(JButton btn) {
		btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	}



//actualiser la table consultation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		public void UpedateTablecons(String ID_PAT)// elle va actualiser la table avec l'Id du patient
		{ 	
			String sql ="Select ID_Cons,Date_consult , Patient_Name ,GRP_SNG ,taille ,poids ,tension ,Diab_O_N ,T_Diab,Diag,M_Diag,Obs,MP,Bilan_O_N from Consult where ID_Pat='"+ ID_PAT+"'";
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableCons.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
			//ajouter un sorter pour trier la table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableCons.getModel());
			tableCons.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//trier  par apport au Numero de la consultation
			sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));//sinon par apport a la date
			sorter.setSortKeys(sortKeys);
		//renomer les  colones
			tableCons.getColumnModel().getColumn( 0 ).setHeaderValue("N° Cons");
			tableCons.getColumnModel().getColumn( 1 ).setHeaderValue("Date");
			tableCons.getColumnModel().getColumn( 2 ).setHeaderValue("Nom & Prenom");	
			tableCons.getColumnModel().getColumn( 3 ).setHeaderValue("Groupe Sanguin");
			tableCons.getColumnModel().getColumn( 4 ).setHeaderValue("Taille");
			tableCons.getColumnModel().getColumn( 5 ).setHeaderValue("Poid");
			tableCons.getColumnModel().getColumn( 6 ).setHeaderValue("Tension");
			tableCons.getColumnModel().getColumn( 7 ).setHeaderValue("Diabétique?");
			tableCons.getColumnModel().getColumn( 8 ).setHeaderValue("Taux de Diabète");
			tableCons.getColumnModel().getColumn( 9 ).setHeaderValue("Diagnostic");
			tableCons.getColumnModel().getColumn( 10 ).setHeaderValue("Maladie(s) Diagnostiquée");
			tableCons.getColumnModel().getColumn( 11 ).setHeaderValue("Observation");
			tableCons.getColumnModel().getColumn( 12 ).setHeaderValue("Montant Payé ");
			tableCons.getColumnModel().getColumn( 13 ).setHeaderValue("Bilan?");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		}
//actualiser la table RDV///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void UpedateTableRDVJours(String ID_MED, String Date)// elle va actualiser la table avec la date donné et le ID 
{ 	
	String sql ="Select NUM_RDV , Patient_Name  , Date_RDV, H_RDV ,Commentary  from RDV where id_med ='"+ID_MED +"' and Date_RDV='" +Date + "'";
	try {

		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		tableRDV.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model de la table
		// trier ma table par rapport a la colone de lheure vu que la date et la meme (Date du jour)
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableRDV.getModel());
		tableRDV.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));		
		sorter.setSortKeys(sortKeys);
		//renomer les  colones
		tableRDV.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
		tableRDV.getColumnModel().getColumn( 1 ).setHeaderValue("Nom & Prenom");	
		tableRDV.getColumnModel().getColumn( 2 ).setHeaderValue("Date du RDV");
		tableRDV.getColumnModel().getColumn( 3 ).setHeaderValue("Heure du RDV");
		tableRDV.getColumnModel().getColumn( 4 ).setHeaderValue("Commentaire");

} catch (SQLException e) {
// TODO Auto-generated catch block
	JOptionPane.showMessageDialog(null,e);
}
	 /*sql ="Select ID_pat  from RDV where id_med ='"+ID_MED +"' and Date_RDV='" +Date + "'";
	try {

		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		while (resultat.next()) {
		String 	sql2 ="Select *  from Consult where ID_Pat ='"+resultat +"'";
		
			prepared1 = cnx.prepareStatement(sql2);
			resultat1 =prepared1.executeQuery();
			if (resultat.next()) {System.out.println("pas de couleurs");
			}
			else {
				System.out.println("colore");
			}
		}
		

} catch (SQLException e) {
// TODO Auto-generated catch block
	e.printStackTrace();
}
	*/
	
}

//Dans la comboBox Patients/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void INComboboxPat(JComboBox<String> Pat,String ID_MED)
{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
	 	Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String datactuelle = dateFormat.format(actuelle);
		//recupereer la date du systeme
	String sql ="Select ID_Pat from RDV where Date_RDV='"+datactuelle+"' and ID_Med ='"+ID_MED+"'"; 
	try {
		prepared = cnx.prepareStatement(sql);
		resultat =prepared.executeQuery();
		//mettre dans la combo Box que les patients qui on RDV ce jourset qui on edv chez ce medecin
		while(resultat.next())
		{
			String  sql1 ="Select Name ,FirstName  from Patient where ID_Patient='"+ resultat.getString("ID_Pat")+"'";
			 prepared1 = cnx.prepareStatement(sql1);
			 resultat1 = prepared1.executeQuery();
			 while (resultat1.next()) {
				 String item=resultat.getString("ID_Pat").toString()+"-"+resultat1.getString("Name").toString()+" "+resultat1.getString("FirstName").toString();
				 Pat.addItem(item);//ajouter a la comboBox
			}
			
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
	}
	
}

// appel de la frame Prescrire Traitement///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public void SetvesibleTrait()
		{ 	
			frame2 = new PrescrireTraitementFrame(ID_MEDgene,ID_PAT);
			frame2.setLocationRelativeTo(null);
			frame2.setVisible(true);
		}
}

	

