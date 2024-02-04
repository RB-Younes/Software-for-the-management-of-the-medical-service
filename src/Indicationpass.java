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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;


////////////////////////////////////////////////////////////////////////////////-----------Fenetre Mot de passe indication------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Indicationpass extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   
    private int posY = 0;

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
					Indicationpass frame = new Indicationpass();
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
	public Indicationpass() {
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
// initialisation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Indicationpass.class.getResource("/Passeindixation_img/question.png")));
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 575, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		cnx = ConnexionDB.CnxDB();
// Exit bouton/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(Indicationpass.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(533, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
// Fields //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		textField = new JTextField();
		textField.setForeground(new Color(51, 153, 102));
		textField.setFont(new Font("Segoe UI", Font.BOLD, 13));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				String username = textField.getText().toString();
				
				// verification dans latable medecin
				String sql = "select password from Medecin where Ueser_Name = ?";// le champ vide == ?
				
				try {
					prepared = cnx.prepareStatement(sql);
					prepared.setString(1, username); // le 1 fait referance au premier "?"
					resultat= prepared.executeQuery();
					if (resultat.next())
					{
						
					String pass = resultat.getString("password");//recuperer le mot de passe
					
					String pass1 = pass.substring(0,2);
					textField_1.setText("2 premiers caracteres du mot de passe : "+ pass1 );
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// verification dans la table secretaire
				String sql2 = "select password from secretaire where Ueser_Name = ?";// le champ vide == ?
				
				try {
					prepared = cnx.prepareStatement(sql2);
					prepared.setString(1, username); // le 1 fait referance au premier" ?"
					resultat= prepared.executeQuery();//executer la commande
					if (resultat.next())
					{
						
					String pass = resultat.getString("password");//recuperer le mot de passe
					
					String pass1 = pass.substring(0,2);//prendre que les 2 premiers caracters 
					textField_1.setText("2 premiers caracteres du mot de  premier passe : "+ pass1 );
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		textField.setBounds(327, 188, 238, 35);
		contentPane.add(textField);
		textField.setColumns(10);
	//le text field qui va contenir le resultat(la suggestion)	
		textField_1 = new JTextField();
		textField_1.setForeground(new Color(51, 153, 102));
		textField_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textField_1.setEditable(false);
		textField_1.setBounds(280, 247, 285, 42);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
//labels//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//titre	
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNewLabel_1.setText("<html><body><font color='grey'>User Name :</body></html>" );
		lblNewLabel_1.setBounds(339, 164, 103, 20);
		contentPane.add(lblNewLabel_1);
	//label pour contenr une nimation
		JLabel lblanimtion = new JLabel("Animation");
		lblanimtion.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/Questions (1).gif")));
		ImageIcon animationbtn1 =new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/Questions (1).gif"));	//animation
		animationbtn1.getImage().flush(); // reinitialiser the  animation
		lblanimtion.setBounds(10, 41, 238, 248);
		contentPane.add(lblanimtion);
	//label pour positioner une icone de'interogation
		JLabel lblNewLabel = new JLabel("icon");
		lblNewLabel.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/question.png")));
		lblNewLabel.setBounds(433, 63, 64, 64);
		contentPane.add(lblNewLabel);
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblBG = new JLabel("BG");
		lblBG.setIcon(new ImageIcon(Indicationpass.class.getResource("/Passeindixation_img/Passe indication (1).png")));
		lblBG.setBounds(0, 0, 575, 300);
		contentPane.add(lblBG);
		
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlever les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	
}
}
