import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre authentification------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class AuthentificationFrame extends JFrame{

	
	 
	private static final long serialVersionUID = 1L;
	private JFrame frmLoginMenu;
	private JTextField UserNameField;
	
	private JPasswordField passwordField;
	private String ID;
	public String getID() {
		return ID;
	}
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 

	/*
	  Launch the application.
	 */
	public static void main(String[] args) throws Exception{
		FlatIntelliJLaf.install();	//application du Look&Feel plus Moderne 
		UIManager.setLookAndFeel(new FlatIntelliJLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AuthentificationFrame window = new AuthentificationFrame();
					window.frmLoginMenu.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AuthentificationFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginMenu = new JFrame();
		
		
		frmLoginMenu.setUndecorated(true);//enlever la bare de menu de base(et programmer nos propre button fermer et reduire)		
		frmLoginMenu.setTitle("Login Menu");
		frmLoginMenu.setIconImage(Toolkit.getDefaultToolkit().getImage(AuthentificationFrame.class.getResource("/Menu_Medcin_img/healthcare-and-medical.png")));
		frmLoginMenu.setSize(1100, 650);
		frmLoginMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmLoginMenu.getContentPane().setLayout(null);
		frmLoginMenu.setLocationRelativeTo(null);//localisation au centre de l'ecran
		
		cnx = ConnexionDB.CnxDB();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		// reduire bouton:	
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {//afficher une autre icone(rouge) a l'entree de la souris
				Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {// rendre l'icone de base
				Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
				
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoginMenu.setState(ICONIFIED);//reduire au clique
			}
		});
		// Exit bouton:		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML.png")));
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);//message de confirmation
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						UserNameField.setText("");// et vider les fields
						frmLoginMenu.dispose();// fermer la fenetre
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1058, 11, 32, 32);
		frmLoginMenu.getContentPane().add(Exit_BTN);
		Minimise_BTN.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1016, 11, 32, 32);
		frmLoginMenu.getContentPane().add(Minimise_BTN);
//Mot de passe indication ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		// dans le cas ou un utulisateurs a oublié son mot de passe une fenetre d'aide s'affichera(ndication les 2 premiers caracteres du mdp) 
		JLabel Passindication = new JLabel("Forgot your password ?");
		Passindication.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Indicationpass frame = new Indicationpass();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				Passindication.setForeground(new Color(0, 153, 102));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Passindication.setForeground(new Color(153, 153, 51));
			}
		});
		Passindication.setBounds(863, 422, 162, 14);
		Passindication.setForeground(new Color(153, 153, 51));
		Passindication.setFont(new Font("Segoe UI", Font.BOLD, 13));
		frmLoginMenu.getContentPane().add(Passindication);
		
//Text Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//Nom d'utilisateurs
		UserNameField = new JTextField();
		UserNameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {// vider le textField si il contient la phrase "User Name"
				if (UserNameField.getText().toString().equals("User Name")) {
					UserNameField.setText("");
				}
			}
		});
		UserNameField.setForeground(new Color(255, 255, 255));
		UserNameField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		UserNameField.setBackground(new Color(0, 153, 102));
		UserNameField.setBorder(null);
		UserNameField.setText("User Name");
		
		UserNameField.setBounds(774, 292, 240, 49);
		frmLoginMenu.getContentPane().add(UserNameField);
		UserNameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {//de meme pour le mot de passe
				if (passwordField.getText().toString().equals("XXXXXXXXXXXXXXX")) {
					passwordField.setText("");
				}
				
			}
		});
		passwordField.setBorder(BorderFactory.createEmptyBorder());// <------------- l Noborders
		passwordField.setBackground(new Color(0, 153, 102));//--------------2 mettre une couleur
		passwordField.setForeground(new Color(255, 255, 255));
		passwordField.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordField.setText("XXXXXXXXXXXXXXX");
		passwordField.setBounds(774, 363, 240, 49);
		frmLoginMenu.getContentPane().add(passwordField);
//CE connecter/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JButton connecter = new JButton("");
		connecter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/log in selected 1.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/log in.png")));
			}
		});
		ButtonStyle(connecter);
		connecter.setToolTipText("Log in ");
		connecter.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/log in.png")));
		connecter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username =UserNameField.getText().toString();//le username saisie
				@SuppressWarnings("deprecation")
				String password =passwordField.getText().toString();//le Mot de passe saisie
				
				//auth Med:
				String sql = "select ID_Med,Ueser_Name ,password,Nom,Prenom from Medecin ";// la requette c la ligne nrml comme si on lexecuté sur sql dev //1 la commande
				int i=0;//i==1 authentifié avec succee ;i==0 echec de l'authentification
				try {
					prepared =cnx.prepareStatement(sql);// le try catch va se generer auto car cette comande est suseptiblede generer pluseurs erreurs // 2 sêcifier lacnx
					resultat = prepared.executeQuery(); //3 executer la commande
					
				if (username.equals("") || password.equals("") || username.equals("User Name") || password.equals("XXXXXXXXXXXXXXX")  )//dans le cas ou les deux champs sont vide(ou non changé) alors afficher un message 
				{
					JOptionPane.showMessageDialog(connecter, "Veuillez vous identifier !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
					
				}	
				else	
				{
					
					while(resultat.next())
						{ //recuperer les informations du medecin 
							String username1 =resultat.getString("Ueser_Name");
							String password1 =resultat.getString("password");
							int IDi =resultat.getInt("ID_Med"); 
							
							ID=String.valueOf(IDi);
						//comparer les valeurs saisi avec celle de notre table Medecin
							if (username.equals(username1) && password.equals(password1)) //dans le cas ou c'est egale
								{//affichge de message
								JOptionPane.showMessageDialog(connecter, "successful login!","connexion" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
									//ouverture d'une session du medecin authentifié
								MenuMedcinFrame newMed=new MenuMedcinFrame(ID);	
									newMed.setVisible(true);
									frmLoginMenu.dispose();
									i=1;
									
								}
						}
				}
				} catch (SQLException  e) {
					JOptionPane.showMessageDialog(null,e);
				}
				
				
				//auth sec:// de meme pour la secretaire
				String sql2 = "select Ueser_Name ,password, sex from Secretaire ";
				try {
					prepared=cnx.prepareStatement(sql2);
					resultat=prepared.executeQuery();
					
					if (username.equals("") || password.equals("") )
					{
						JOptionPane.showMessageDialog(connecter, "veuillez vous identifier !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
						
					}	
					else	
					{
						
						while(resultat.next())
							{
								String username1 =resultat.getString("Ueser_Name");
								String password1 =resultat.getString("password");
							
								if (username.equals(username1) && password.equals(password1)) 
									{
									
									JOptionPane.showMessageDialog(connecter, "successful login!","connexion" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
									MenuSecretaireFrame newSecmenu =  new MenuSecretaireFrame();
									newSecmenu.setVisible(true);
									frmLoginMenu.dispose();
									
									i=1;
									}
							}
						
					
						//cas authentification echouée
						if (i==0) 
							{//afficher un message 
								JOptionPane.showMessageDialog(connecter, "Mot de passe ou username erroné !","!", JOptionPane.QUESTION_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							} 
					}
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		connecter.setBounds(834, 447, 64, 64);
		frmLoginMenu.getContentPane().add(connecter);
		
//animation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JLabel lblanim1 = new JLabel("anim1");
		ImageIcon animation =new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Medecins Entrance.gif"));
		lblanim1.setIcon(animation);
		Timer chrono=new Timer();// on va utiliser un timer our attendre la fin de la premiere animation(d'entree qui dure 1.5 sec)apres lancer le looping
		chrono.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblanim1.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/Medecins loop animation.gif")));
			}
		}, 1500);
		lblanim1.setBounds(27, 91, 475, 455);
		frmLoginMenu.getContentPane().add(lblanim1);
		
//BG//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGlogin = new JLabel("Banniere");
		BGlogin.setIcon(new ImageIcon(AuthentificationFrame.class.getResource("/Log_in_img/BG login.png")));
		BGlogin.setBounds(0, 0, 1100, 650);
		frmLoginMenu.getContentPane().add(BGlogin);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlever les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	
}
}
