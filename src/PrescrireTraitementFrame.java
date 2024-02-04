import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Prescrire traitement------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class PrescrireTraitementFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	private JTextField txtMedoc1;//Nom medicament 1
	private JTextField txtMedoc2;//Nom medicament 2
	private JTextField txtMedoc3;//Nom medicament 3
	private JTextField txtMedoc4;//Nom medicament 4
	private JTextField txtMedoc5;//Nom medicament 5
	
	private JTextField txtDose1;//dose du medicament 1
	private JTextField txtDose2;//dose du medicament 2
	private JTextField txtDose3;//dose du medicament 3
	private JTextField txtDose4;//dose du medicament 4
	private JTextField txtDose5;//dose du medicament 5
	
	private JTextField txtFreq1;//posologie du Medicament 1
	private JTextField txtDuree1;//duree du traitement 1
	
	private JTextField txtFreq2;//posologie du Medicament 2
	private JTextField txtDuree2;//duree du traitement 2
	
	private JTextField txtFreq3;//posologie du Medicament 3
	private JTextField txtDuree3;//duree du traitement 3
	
	private JTextField txtFreq4;//posologie du Medicament 4
	private JTextField txtDuree4;//duree du traitement 4
	
	private JTextField txtFreq5;//posologie du Medicament 5
	private JTextField txtDuree5;//duree du traitement 5
	
	//Combo box des medicament enregistre deja sur la table medicament
	private JComboBox<String> ComboMedoc1;//medicament 1
	private JComboBox<String> ComboMedoc2;//medicament 2
	private JComboBox<String> ComboMedoc3;//medicament 3
	private JComboBox<String> ComboMedoc4;//medicament 4
	private JComboBox<String> ComboMedoc5;//medicament 5

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatIntelliJLaf.install();	
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrescrireTraitementFrame frame = new PrescrireTraitementFrame("2","2");
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
	public PrescrireTraitementFrame(String ID_med,String ID_pat) { //pour communiquer entre les deux frame (consultation *principale* et prescrire traitement*secondaire*)

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
        
		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 800d, 650, 25d, 25d));//des bors arrondi
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PrescrireTraitementFrame.class.getResource("/Menu_Medcin_img/medicine.png")));
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cnx = ConnexionDB.CnxDB();
		

		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(746, 11, 32, 32);
		contentPane.add(Exit_BTN);
// Bouton Imprimer //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnImprimer = new JButton("");
		btnImprimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Consultation_img/print selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnImprimer.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Consultation_img/print.png")));//remetre le bouton de base
			}
		});
		btnImprimer.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Consultation_img/print.png")));
		btnImprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// les infos du medicament 1---------------------------------------------------------------------------------------------------------------------------------------------------------
				String nomMedoc1 = "";
				String posologie1 ="";
				String dosage1 ="";
				String duree1 ="";
				//le medicament est choisi soit de la combo box ou ecrit manuelement
				if (!ComboMedoc1.getSelectedItem().toString().equals("Selectionner Medicament 1") && !txtMedoc1.isEnabled() ) {//si le medicament a ete choisi de la bdd a l'aide du comboBox(le textfield medicament 1 restra Enabled) alors 
					 nomMedoc1 = ComboMedoc1.getSelectedItem().toString(); //recuperer le nom du medicament de la combo Box Medicament 1
					 posologie1 =txtFreq1.getText().toString();//frequence 1 du text field freqquence 1
					 dosage1 =txtDose1.getText().toString();//dosage 1 du text field dosage 1
					 duree1 =txtDuree1.getText().toString();//duree 1
					 
				}
				if (ComboMedoc1.getSelectedItem().toString().equals("Selectionner Medicament 1") && !txtMedoc1.getText().equals("") && !txtMedoc1.getText().equals("Medicament 1")) {//le contraire le medicament a ete ecrit sur le text field 1 alors
					nomMedoc1 = txtMedoc1.getText().toString(); //recuperer le nom du medicament du text field Medicament 1
					 posologie1 =txtFreq1.getText().toString();//frequence 1 du text field freqquence 1
					 dosage1 =txtDose1.getText().toString();//dosage 1 du text field dosage 1
					 duree1 =txtDuree1.getText().toString();//duree 1
				}
				//de meme pour le reste
				// les infos du medicament 2---------------------------------------------------------------------------------------------------------------------------------------------------------------
				String nomMedoc2 = "";
				String posologie2 ="";
				String dosage2="";
				String duree2 ="";
				if (!ComboMedoc2.getSelectedItem().toString().equals("Selectionner Medicament 2") && !txtMedoc2.isEnabled()) {
					 nomMedoc2 = ComboMedoc2.getSelectedItem().toString();
					 posologie2 =txtFreq2.getText().toString();
					 dosage2 =txtDose2.getText().toString();
					 duree2 =txtDuree2.getText().toString();
				}
				if (ComboMedoc2.getSelectedItem().toString().equals("Selectionner Medicament 2") && !txtMedoc2.getText().equals("") && !txtMedoc2.getText().equals("Medicament 2")) {
					nomMedoc2 = txtMedoc2.getText().toString();
					 posologie2 =txtFreq2.getText().toString();
					 dosage2 =txtDose2.getText().toString();
					 duree2 =txtDuree2.getText().toString();
				}
				// les infos du medicament 3------------------------------------------------------------------------------------------------------------------------------------------------------------------
				String nomMedoc3 = "";
				String posologie3 ="";
				String dosage3 ="";
				String duree3 ="";
				if (!ComboMedoc3.getSelectedItem().toString().equals("Selectionner Medicament 3") && !txtMedoc3.isEnabled()) {
					 nomMedoc3 = ComboMedoc3.getSelectedItem().toString();
					 posologie3 =txtFreq3.getText().toString();
					 dosage3 =txtDose3.getText().toString();
					 duree3 =txtDuree3.getText().toString();
				}
				if (ComboMedoc3.getSelectedItem().toString().equals("Selectionner Medicament 3") && !txtMedoc3.getText().equals("") && !txtMedoc3.getText().equals("Medicament 3")) {
					nomMedoc3 = txtMedoc3.getText().toString();
					posologie3 =txtFreq3.getText().toString();
					dosage3 =txtDose3.getText().toString();
					duree3 =txtDuree3.getText().toString();
				}
				// les infos du medicament 4--------------------------------------------------------------------------------------------------------------------------------------------------------------------
				String nomMedoc4 = "";
				String posologie4 ="";
				String dosage4 ="";
				String duree4 ="";
				if (!ComboMedoc4.getSelectedItem().toString().equals("Selectionner Medicament 4") && !txtMedoc4.isEnabled()) {
					 nomMedoc4 = ComboMedoc4.getSelectedItem().toString();
					 posologie4 =txtFreq4.getText().toString();
					 dosage4 =txtDose4.getText().toString();
					 duree4 =txtDuree4.getText().toString();
				}
				if (ComboMedoc4.getSelectedItem().toString().equals("Selectionner Medicament 4") && !txtMedoc4.getText().equals("") && !txtMedoc4.getText().equals("Medicament 4")) {
					nomMedoc4= txtMedoc4.getText().toString();
					posologie4 =txtFreq4.getText().toString();
					 dosage4 =txtDose4.getText().toString();
					 duree4 =txtDuree4.getText().toString();
				}
				// les infos du medicament 5------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				String nomMedoc5 = "";
				String posologie5 ="";
				String dosage5 ="";
				String duree5 ="";
				if (!ComboMedoc5.getSelectedItem().toString().equals("Selectionner Medicament 5") && !txtMedoc5.isEnabled()) {
					 nomMedoc5 = ComboMedoc5.getSelectedItem().toString();
					 posologie5 =txtFreq5.getText().toString();
					 dosage5 =txtDose5.getText().toString();
					 duree5 =txtDuree5.getText().toString();
				}
				if (ComboMedoc5.getSelectedItem().toString().equals("Selectionner Medicament 5") && !txtMedoc5.getText().equals("") && !txtMedoc5.getText().equals("Medicament 5")) {
					nomMedoc5 = txtMedoc5.getText().toString();
					 posologie5 =txtFreq5.getText().toString();
					 dosage5 =txtDose5.getText().toString();
					 duree5 =txtDuree5.getText().toString();
				}
				//creer une instance 
				Ordonnance ord= new Ordonnance(nomMedoc1, posologie1, dosage1, duree1, nomMedoc2, posologie2, dosage2, duree2, nomMedoc3, posologie3, dosage3, duree3, nomMedoc4, posologie4, dosage4, duree4, nomMedoc5, posologie5, dosage5, duree5,  ID_med,ID_pat );
				ord.ImprimerOrdonence(ord);//appeler la fonction imprimer ordonance e la classe ordonnance
				
			}
				
		});
		btnImprimer.setToolTipText("Imprimer");
		ButtonStyle(btnImprimer);
		btnImprimer.setBounds(697, 297, 64, 64);
		contentPane.add(btnImprimer);

//Fields/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Nom du medicament X5(les 5 fields)----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		txtMedoc1 = new JTextField();
		txtMedoc1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!txtMedoc1.getText().toString().equals("Medicament 1") && txtMedoc1.getText().toString().length()>=0 ) {//si on ecrit dans le text field nom medicament alors on poura ecriresur les autres field (dose frequence duree)
					 txtDose1.setEnabled(true);//dose
					 txtDuree1.setEnabled(true);//frequence
					 txtFreq1.setEnabled(true);//posologie
				}
			}
		});
		txtMedoc1.setText("Medicament 1");
		txtMedoc1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMedoc1.getText().toString().equals("Medicament 1") && txtMedoc1.isEnabled()) {//vider le text field au clique (si il contient la valeur par default)
					txtMedoc1.setText("");
				}
			}
		});
		txtMedoc1.setBackground(new Color(51, 204, 153));
		txtMedoc1.setForeground(Color.WHITE);
		txtMedoc1.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtMedoc1.setBounds(258, 107, 188, 28);
		contentPane.add(txtMedoc1);
		txtMedoc1.setColumns(10);
		//2(le meeme traitement)
		txtMedoc2 = new JTextField();
		txtMedoc2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!txtMedoc2.getText().toString().equals("Medicament 2") && txtMedoc2.getText().toString().length()>=0 ) {
					 txtDose2.setEnabled(true);
					 txtDuree2.setEnabled(true);
					 txtFreq2.setEnabled(true);
				}
			}
		});
		txtMedoc2.setText("Medicament 2");
		txtMedoc2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMedoc2.getText().toString().equals("Medicament 2") && txtMedoc2.isEnabled()) {
					txtMedoc2.setText("");
				}
			}
		});
		txtMedoc2.setBackground(new Color(51, 204, 153));
		txtMedoc2.setForeground(Color.WHITE);
		txtMedoc2.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtMedoc2.setColumns(10);
		txtMedoc2.setBounds(258, 219, 188, 28);
		contentPane.add(txtMedoc2);
		//3
		txtMedoc3 = new JTextField();
		txtMedoc3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!txtMedoc3.getText().toString().equals("Medicament 3") && txtMedoc3.getText().toString().length()>=0 ) {
					 txtDose3.setEnabled(true);
					 txtDuree3.setEnabled(true);
					 txtFreq3.setEnabled(true);
				}
			}
		});
		txtMedoc3.setText("Medicament 3");
		txtMedoc3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMedoc3.getText().toString().equals("Medicament 3") && txtMedoc3.isEnabled()) {
					txtMedoc3.setText("");
				}
			}
		});
		txtMedoc3.setBackground(new Color(51, 204, 153));
		txtMedoc3.setForeground(Color.WHITE);
		txtMedoc3.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtMedoc3.setColumns(10);
		txtMedoc3.setBounds(258, 333, 188, 28);
		contentPane.add(txtMedoc3);
		//4
		txtMedoc4 = new JTextField();
		txtMedoc4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!txtMedoc4.getText().toString().equals("Medicament 4") && txtMedoc4.getText().toString().length()>=0 ) {
					 txtDose4.setEnabled(true);
					 txtDuree4.setEnabled(true);
					 txtFreq4.setEnabled(true);
				}
			}
		});
		txtMedoc4.setText("Medicament 4");
		txtMedoc4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMedoc4.getText().toString().equals("Medicament 4") && txtMedoc4.isEnabled()) {
					txtMedoc4.setText("");
				}
			}
		});
		txtMedoc4.setBackground(new Color(51, 204, 153));
		txtMedoc4.setForeground(Color.WHITE);
		txtMedoc4.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtMedoc4.setColumns(10);
		txtMedoc4.setBounds(269, 446, 188, 28);
		contentPane.add(txtMedoc4);
		//5
		txtMedoc5 = new JTextField();
		txtMedoc5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!txtMedoc5.getText().toString().equals("Medicament 5") && txtMedoc5.getText().toString().length()>=0 ) {
					 txtDose5.setEnabled(true);
					 txtDuree5.setEnabled(true);
					 txtFreq5.setEnabled(true);
				}
			}
		});
		txtMedoc5.setText("Medicament 5");
		txtMedoc5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtMedoc5.getText().toString().equals("Medicament 5") && txtMedoc5.isEnabled()) {
					txtMedoc5.setText("");
				}
			}
		});
		txtMedoc5.setBackground(new Color(51, 204, 153));
		txtMedoc5.setForeground(Color.WHITE);
		txtMedoc5.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtMedoc5.setColumns(10);
		txtMedoc5.setBounds(258, 558, 188, 28);
		contentPane.add(txtMedoc5);
		
	//les Doses X5(les 5 fields)----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		txtDose1 = new JTextField();
		txtDose1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDose1.getText().toString().equals("Dose 1") && txtDose1.isEnabled()) {//vider le text field au clique (si il contient la valeur par default)
					txtDose1.setText("");
				}
			}
		});
		txtDose1.setEnabled(false);
		txtDose1.setText("Dose 1");
		txtDose1.setBackground(new Color(51, 204, 153));
		txtDose1.setForeground(Color.WHITE);
		txtDose1.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDose1.setBounds(475, 108, 140, 28);
		contentPane.add(txtDose1);
		txtDose1.setColumns(10);
		//2
		txtDose2 = new JTextField();
		txtDose2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDose2.getText().toString().equals("Dose 2") && txtDose2.isEnabled()) {
					txtDose2.setText("");
				}
			}
		});
		txtDose2.setEnabled(false);
		txtDose2.setText("Dose 2");
		txtDose2.setBackground(new Color(51, 204, 153));
		txtDose2.setForeground(Color.WHITE);
		txtDose2.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDose2.setColumns(10);
		txtDose2.setBounds(475, 219, 140, 28);
		contentPane.add(txtDose2);
		//3
		txtDose3 = new JTextField();
		txtDose3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDose3.getText().toString().equals("Dose 3") && txtDose3.isEnabled()) {
					txtDose3.setText("");
				}
			}
		});
		txtDose3.setEnabled(false);
		txtDose3.setText("Dose 3");
		txtDose3.setBackground(new Color(51, 204, 153));
		txtDose3.setForeground(Color.WHITE);
		txtDose3.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDose3.setColumns(10);
		txtDose3.setBounds(475, 333, 140, 28);
		contentPane.add(txtDose3);
		//4
		txtDose4 = new JTextField();
		txtDose4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDose4.getText().toString().equals("Dose 4") && txtDose4.isEnabled()) {
					txtDose4.setText("");
				}
			}
		});
		txtDose4.setEnabled(false);
		txtDose4.setText("Dose 4");
		txtDose4.setBackground(new Color(51, 204, 153));
		txtDose4.setForeground(Color.WHITE);
		txtDose4.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDose4.setColumns(10);
		txtDose4.setBounds(489, 446, 140, 28);
		contentPane.add(txtDose4);
		//5
		txtDose5 = new JTextField();
		txtDose5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDose5.getText().toString().equals("Dose 5") && txtDose5.isEnabled() ) {
					txtDose5.setText("");
				}
			}
		});
		txtDose5.setEnabled(false);
		txtDose5.setText("Dose 5");
		txtDose5.setBackground(new Color(51, 204, 153));
		txtDose5.setForeground(Color.WHITE);
		txtDose5.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDose5.setColumns(10);
		txtDose5.setBounds(475, 558, 140, 28);
		contentPane.add(txtDose5);
	//frequence X5 & dureeX5(les text fields)	
		txtFreq1 = new JTextField();
		txtFreq1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtFreq1.getText().toString().equals("Fréquence 1") && txtFreq1.isEnabled()) {
					txtFreq1.setText("");
				}
			}
		});
		txtFreq1.setEnabled(false);
		txtFreq1.setText("Fr\u00E9quence 1");
		txtFreq1.setBackground(new Color(51, 204, 153));
		txtFreq1.setForeground(Color.WHITE);
		txtFreq1.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtFreq1.setColumns(10);
		txtFreq1.setBounds(162, 156, 135, 28);
		contentPane.add(txtFreq1);
	
		txtDuree1 = new JTextField();
		txtDuree1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDuree1.getText().toString().equals("Durée 1") && txtDuree1.isEnabled()) {
					txtDuree1.setText("");
				}
			}
		});
		txtDuree1.setEnabled(false);
		txtDuree1.setText("Dur\u00E9e 1");
		txtDuree1.setBackground(new Color(51, 204, 153));
		txtDuree1.setForeground(Color.WHITE);
		txtDuree1.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDuree1.setColumns(10);
		txtDuree1.setBounds(408, 156, 140, 28);
		contentPane.add(txtDuree1);
		
		txtFreq2 = new JTextField();
		txtFreq2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtFreq2.getText().toString().equals("Fréquence 2") && txtFreq2.isEnabled()) {
					txtFreq2.setText("");
				}
			}
		});
		txtFreq2.setEnabled(false);
		txtFreq2.setText("Fr\u00E9quence 2");
		txtFreq2.setBackground(new Color(51, 204, 153));
		txtFreq2.setForeground(Color.WHITE);
		txtFreq2.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtFreq2.setColumns(10);
		txtFreq2.setBounds(162, 267, 135, 28);
		contentPane.add(txtFreq2);
		
		txtDuree2 = new JTextField();
		txtDuree2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDuree2.getText().toString().equals("Durée 2") && txtDuree2.isEnabled()) {
					txtDuree2.setText("");
				}
			}
		});
		txtDuree2.setEnabled(false);
		txtDuree2.setText("Dur\u00E9e 2");
		txtDuree2.setBackground(new Color(51, 204, 153));
		txtDuree2.setForeground(new Color(255, 255, 255));
		txtDuree2.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDuree2.setColumns(10);
		txtDuree2.setBounds(408, 267, 135, 28);
		contentPane.add(txtDuree2);
		
		txtFreq3 = new JTextField();
		txtFreq3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtFreq3.getText().toString().equals("Fréquence 3") && txtFreq3.isEnabled()) {
					txtFreq3.setText("");
				}
			}
		});
		txtFreq3.setEnabled(false);
		txtFreq3.setText("Fr\u00E9quence 3");
		txtFreq3.setBackground(new Color(51, 204, 153));
		txtFreq3.setForeground(Color.WHITE);
		txtFreq3.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtFreq3.setColumns(10);
		txtFreq3.setBounds(162, 381, 135, 28);
		contentPane.add(txtFreq3);
		
		txtDuree3 = new JTextField();
		txtDuree3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDuree3.getText().toString().equals("Durée 3") && txtDuree3.isEnabled()) {
					txtDuree3.setText("");
				}
			}
		});
		txtDuree3.setEnabled(false);
		txtDuree3.setText("Dur\u00E9e 3");
		txtDuree3.setBackground(new Color(51, 204, 153));
		txtDuree3.setForeground(Color.WHITE);
		txtDuree3.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDuree3.setColumns(10);
		txtDuree3.setBounds(408, 381, 135, 28);
		contentPane.add(txtDuree3);
		
		txtFreq4 = new JTextField();
		txtFreq4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtFreq4.getText().toString().equals("Fréquence 4") && txtFreq4.isEnabled()) {
					txtFreq4.setText("");
				}
			}
		});
		txtFreq4.setEnabled(false);
		txtFreq4.setText("Fr\u00E9quence 4");
		txtFreq4.setBackground(new Color(51, 204, 153));
		txtFreq4.setForeground(Color.WHITE);
		txtFreq4.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtFreq4.setColumns(10);
		txtFreq4.setBounds(174, 494, 135, 28);
		contentPane.add(txtFreq4);
		
		txtDuree4 = new JTextField();
		txtDuree4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDuree4.getText().toString().equals("Durée 4") && txtDuree4.isEnabled()) {
					txtDuree4.setText("");
				}
			}
		});
		txtDuree4.setEnabled(false);
		txtDuree4.setText("Dur\u00E9e 4");
		txtDuree4.setBackground(new Color(51, 204, 153));
		txtDuree4.setForeground(Color.WHITE);
		txtDuree4.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDuree4.setColumns(10);
		txtDuree4.setBounds(421, 494, 135, 28);
		contentPane.add(txtDuree4);
		
		txtFreq5 = new JTextField();
		txtFreq5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtFreq5.getText().toString().equals("Fréquence 5") && txtFreq5.isEnabled()) {
					txtFreq5.setText("");
				}
			}
		});
		txtFreq5.setEnabled(false);
		txtFreq5.setText("Fr\u00E9quence 5");
		txtFreq5.setBackground(new Color(51, 204, 153));
		txtFreq5.setForeground(Color.WHITE);
		txtFreq5.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtFreq5.setColumns(10);
		txtFreq5.setBounds(162, 605, 135, 28);
		contentPane.add(txtFreq5);
		
		txtDuree5 = new JTextField();
		txtDuree5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtDuree5.getText().toString().equals("Durée 5") && txtDuree5.isEnabled()) {
					txtDuree5.setText("");
				}
			}
		});
		txtDuree5.setEnabled(false);
		txtDuree5.setText("Dur\u00E9e 5");
		txtDuree5.setBackground(new Color(51, 204, 153));
		txtDuree5.setForeground(Color.WHITE);
		txtDuree5.setFont(new Font("Segoe UI", Font.BOLD, 13));
		txtDuree5.setColumns(10);
		txtDuree5.setBounds(408, 605, 135, 28);
		contentPane.add(txtDuree5);
	//les combo Box x5--------------------------------------------------------------------------------------------------------------------------------------------------------------------
		ComboMedoc1 = new JComboBox<String>();
		ComboMedoc1.addItem("Selectionner Medicament 1");
		INComboboxMedoc(ComboMedoc1);
		ComboMedoc1.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent event) {
						   if (event.getStateChange() == ItemEvent.SELECTED) {
							   if (! ComboMedoc1.getSelectedItem().equals("Selectionner Medicament 1")) {//au changement de l'etat de la comboBox on desactivera 
								   //le text field pour ecrire le nom du medicament car il est consideré comme choisi et activer les textsfield (duree dosage requence)
								   txtMedoc1.setEnabled(false);
								   txtMedoc1.setText("Medicament 1");
								   txtDose1.setEnabled(true);
								   txtDuree1.setEnabled(true);
								   txtFreq1.setEnabled(true); 
								   txtDose1.setText("Dose 1");
								   txtDuree1.setText("Durée 1");
								   txtFreq1.setText("Fréquence 1");
								  
							   		}
							   else {//sinon  le contraire les desactiver et donner a l'utilisateur le choix decrire manuelement sur le text field
								   txtMedoc1.setEnabled(true);
								   txtDose1.setEnabled(false);
								   txtDuree1.setEnabled(false);
								   txtFreq1.setEnabled(false);
								   txtDose1.setText("Dose 1");
								   txtDuree1.setText("Durée 1");
								   txtFreq1.setText("Fréquence 1");
								   
								  
							   		}
								
					  }
					}
				});
		ComboMedoc1.setBackground(new Color(51, 204, 153));
		ComboMedoc1.setForeground(Color.WHITE);
		ComboMedoc1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		ComboMedoc1.setBounds(36, 107, 186, 28);
		contentPane.add(ComboMedoc1);
		//le meme traitement pour le reste des combo BOx
		//2 
		ComboMedoc2 = new JComboBox<String>();
		ComboMedoc2.addItem("Selectionner Medicament 2");
		INComboboxMedoc(ComboMedoc2);
		ComboMedoc2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					   if (event.getStateChange() == ItemEvent.SELECTED) {
						   if (! ComboMedoc2.getSelectedItem().equals("Selectionner Medicament 2")) {
							   txtMedoc2.setEnabled(false);
							   txtMedoc2.setText("Medicament 2");
							   txtDose2.setEnabled(true);
							   txtDuree2.setEnabled(true);
							   txtFreq2.setEnabled(true);
							   txtDose2.setText("Dose 2");
							   txtDuree2.setText("Durée 2");
							   txtFreq2.setText("Fréquence 2");
						   		}
						   else {
							   txtMedoc2.setEnabled(true);
							   txtDose2.setEnabled(false);
							   txtDuree2.setEnabled(false);
							   txtFreq2.setEnabled(false);
							   txtDose2.setText("Dose 2");
							   txtDuree2.setText("Durée 2");
							   txtFreq2.setText("Fréquence 2");
						   		}
							
				  }
				}
			});
		ComboMedoc2.setBackground(new Color(51, 204, 153));
		ComboMedoc2.setForeground(Color.WHITE);
		ComboMedoc2.setFont(new Font("Segoe UI", Font.BOLD, 13));
		ComboMedoc2.setBounds(36, 219, 186, 28);
		contentPane.add(ComboMedoc2);
		//3
		ComboMedoc3 = new JComboBox<String>();
		ComboMedoc3.addItem("Selectionner Medicament 3");
		INComboboxMedoc(ComboMedoc3);
		ComboMedoc3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					   if (event.getStateChange() == ItemEvent.SELECTED) {
						   if (! ComboMedoc3.getSelectedItem().equals("Selectionner Medicament 3")) {
							   txtMedoc3.setEnabled(false);
							   txtMedoc3.setText("Medicament 3");
							   txtDose3.setEnabled(true);
							   txtDuree3.setEnabled(true);
							   txtFreq3.setEnabled(true);
							   txtDose3.setText("Dose 3");
							   txtDuree3.setText("Durée 3");
							   txtFreq3.setText("Fréquence 3");
						   		}
						   else {
							   txtMedoc3.setEnabled(true);
							   txtDose3.setEnabled(false);
							   txtDuree3.setEnabled(false);
							   txtFreq3.setEnabled(false);
							   txtDose3.setText("Dose 3");
							   txtDuree3.setText("Durée 3");
							   txtFreq3.setText("Fréquence 3");
						   		}
							
				  }
				}
			});
		ComboMedoc3.setBackground(new Color(51, 204, 153));
		ComboMedoc3.setForeground(Color.WHITE);
		ComboMedoc3.setFont(new Font("Segoe UI", Font.BOLD, 13));
		ComboMedoc3.setBounds(36, 333, 186, 28);
		contentPane.add(ComboMedoc3);
		//4
		ComboMedoc4 = new JComboBox<String>();
		ComboMedoc4.addItem("Selectionner Medicament 4");
		INComboboxMedoc(ComboMedoc4);
		ComboMedoc4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					   if (event.getStateChange() == ItemEvent.SELECTED) {
						   if (! ComboMedoc4.getSelectedItem().equals("Selectionner Medicament 4")) {
							   txtMedoc4.setEnabled(false);
							   txtMedoc4.setText("Medicament 4");
							   txtDose4.setEnabled(true);
							   txtDuree4.setEnabled(true);
							   txtFreq4.setEnabled(true);
							   txtDose4.setText("Dose 4");
							   txtDuree4.setText("Durée 4");
							   txtFreq4.setText("Fréquence 4");
						   		}
						   else {
							   txtMedoc4.setEnabled(true);
							   txtDose4.setEnabled(false);
							   txtDuree4.setEnabled(false);
							   txtFreq4.setEnabled(false);
							   txtDose4.setText("Dose 4");
							   txtDuree4.setText("Durée 4");
							   txtFreq4.setText("Fréquence 4");
						   		}
							
				  }
				}
			});
		ComboMedoc4.setBackground(new Color(51, 204, 153));
		ComboMedoc4.setFont(new Font("Segoe UI", Font.BOLD, 13));
		ComboMedoc4.setForeground(Color.WHITE);
		ComboMedoc4.setBounds(49, 446, 186, 28);
		contentPane.add(ComboMedoc4);
		//5
		ComboMedoc5 = new JComboBox<String>();
		ComboMedoc5.addItem("Selectionner Medicament 5");
		INComboboxMedoc(ComboMedoc5);
		ComboMedoc5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					   if (event.getStateChange() == ItemEvent.SELECTED) {
						   if (! ComboMedoc5.getSelectedItem().equals("Selectionner Medicament 5")) {
							   txtMedoc5.setEnabled(false);
							   txtMedoc5.setText("Medicament 5");
							   txtDose5.setEnabled(true);
							   txtDuree5.setEnabled(true);
							   txtFreq5.setEnabled(true);
							   txtDose5.setText("Dose 5");
							   txtDuree5.setText("Durée 5");
							   txtFreq5.setText("Fréquence 5");
						   		}
						   else {
							   txtMedoc5.setEnabled(true);
							   txtDose5.setEnabled(false);
							   txtDuree5.setEnabled(false);
							   txtFreq5.setEnabled(false);
							   txtDose5.setText("Dose 5");
							   txtDuree5.setText("Durée 5");
							   txtFreq5.setText("Fréquence 5");
						   		}
							
				  }
				}
			});
		ComboMedoc5.setBackground(new Color(51, 204, 153));
		ComboMedoc5.setForeground(Color.WHITE);
		ComboMedoc5.setFont(new Font("Segoe UI", Font.BOLD, 13));
		ComboMedoc5.setBounds(36, 558, 186, 28);
		contentPane.add(ComboMedoc5);
		
//BG///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(PrescrireTraitementFrame.class.getResource("/Consultation_img/Prescrire Traitement.png")));
		lblNewLabel.setBounds(0, 0, 800, 650);
		contentPane.add(lblNewLabel);
	}
	
// fonctions///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	 private void ButtonStyle(JButton btn) {
	//enlecer les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	
}
// le contenu des comboboxs medoc-/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public void INComboboxMedoc(JComboBox<String> Pat)
	 { //recuperer les infos du medicament de la base de donneé
	 	String sql ="Select ID_Medoc,Nom  from Medoc";
	 	try {
	 		prepared = cnx.prepareStatement(sql);
	 		resultat =prepared.executeQuery();
	 		while(resultat.next())
	 		{
	 			String item=resultat.getString("ID_Medoc").toString()+"-"+resultat.getString("Nom").toString()+" ";
	 			Pat.addItem(item);
	 		}
	 		
	 	} catch (SQLException e) {
	 		// TODO Auto-generated catch block
	 		JOptionPane.showMessageDialog(null, e);
	 	}
	 	
	 }
}
