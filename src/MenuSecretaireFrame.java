import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu Secretaire------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class MenuSecretaireFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
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
					MenuSecretaireFrame frame = new MenuSecretaireFrame();
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
	public MenuSecretaireFrame() {
		setUndecorated(true);	
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MenuSecretaireFrame.class.getResource("/Menu_sec_img/secretary.png")));
		setTitle("Secritary Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1100, 650);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1100d, 650d, 25d, 25d));// bord arrondi
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
// l'arriere plan initilaisation//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		JLabel BGSec = new JLabel("BG");
		
		JLabel animationlbl = new JLabel("New label");
		animationlbl.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/menu in.gif")));
		animationlbl.setBounds(484, 71, 517, 496);
		contentPane.add(animationlbl);
		
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
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Minimize ML .png")));
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1016, 11, 32, 32);
		getContentPane().add(Minimise_BTN);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Exit ML.png")));
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
		Exit_BTN.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1058, 11, 32, 32);
		getContentPane().add(Exit_BTN);
//Btns secretaire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//Gestion des Patients****************************************************************************************************************************************************************************************************************		
		JButton btnPatMAn = new JButton("");
		btnPatMAn.setToolTipText("Gestion des Patients");
		btnPatMAn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnPatMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/Pat Man selected.png")));//changer l'icone  du boutton
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/2.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn1 =new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/gst patient.gif"));//animation Gestion des patients
				animationbtn1.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationbtn1);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnPatMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/Pat Man.png")));//remetre celle de base
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/1.png")));// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG = new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/menu in.gif"));//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationBG);
			}
		});
		btnPatMAn.setForeground(new Color(204, 153, 255));
		btnPatMAn.setFont(new Font("Segoe UI", Font.BOLD, 18));
		ButtonStyle(btnPatMAn);
		btnPatMAn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//ouverture de la fenetre  Gestion des RDV au clique
				GestionPatientFrame open =new GestionPatientFrame("S","");//faire passer lindice S pour savoir que c'est une secretaire (pas besoin de ID secretaire)
				open.setVisible(true);
				open.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnPatMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/Pat Man.png")));
		btnPatMAn.setBounds(112, 252, 64, 64);
		contentPane.add(btnPatMAn);
		
	//Gestion des Patients****************************************************************************************************************************************************************************************************************		
		JButton btnRDV = new JButton("");
		btnRDV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//ouverture de la fenetre  Gestion des RDV au clique
				GestionRDVFrame open =new GestionRDVFrame("S","");//faire passer lindice S pour savoir que c'est une secretaire (pas besoin de ID secretaire)
				open.setVisible(true);
				open.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnRDV.setToolTipText("Gstion RDV");
		btnRDV.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseEntered(MouseEvent e) {
				btnRDV.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RDV selected.png")));//changer l'icone  du boutton
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/3.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn2 = new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/gst RDV.gif"));//animation Gestion des RDV
				animationbtn2.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationbtn2);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnRDV.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RDV.png")));//remetre celle de base
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/1.png")));// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG = new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/menu in.gif"));//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationBG);
			}
		});
		btnRDV.setForeground(new Color(204, 153, 255));
		btnRDV.setFont(new Font("Segoe UI", Font.BOLD, 18));
		ButtonStyle(btnRDV);
		btnRDV.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RDV.png")));
		btnRDV.setBounds(245, 363, 64, 64);
		contentPane.add(btnRDV);

	//Gestion des Patients****************************************************************************************************************************************************************************************************************		
		JButton btnResMAn = new JButton("");
		btnResMAn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//ouverture de la fenetre  Gestion des RDV au clique
				TresorerieFrame frame = new TresorerieFrame("S","");//faire passer lindice S pour savoir que c'est une secretaire (pas besoin de ID secretaire)
				frame.setLocationRelativeTo(contentPane);
				frame.setVisible(true);
				dispose();
			}
		});
		btnResMAn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnResMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RES MAN selected.png")));//changer l'icone  du boutton
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/4.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn3= new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/comptabilete.gif"));//animation Tresorerie
				animationbtn3.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationbtn3);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnResMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RES MAN.png")));//remetre celle de base
				BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/1.png")));// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG = new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/menu in.gif"));//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				animationlbl.setIcon(animationBG);
			}
		});
		btnResMAn.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/RES MAN.png")));
		btnResMAn.setForeground(new Color(204, 153, 255));
		btnResMAn.setFont(new Font("Segoe UI", Font.BOLD, 18));
		ButtonStyle(btnResMAn);
		btnResMAn.setToolTipText("Comptabilit\u00E9");
		btnResMAn.setBounds(291, 539, 64, 64);
		contentPane.add(btnResMAn);

// Arriere plan SEcretaire ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGSec.setIcon(new ImageIcon(MenuSecretaireFrame.class.getResource("/Menu_sec_img/1.png")));
		BGSec.setBounds(0, 0, 1100, 650);
		contentPane.add(BGSec);
	}
	
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 public void ButtonStyle(JButton btn)
	 {
		 //enlecer les bordures des btn
		 btn.setOpaque(false);
		 btn.setFocusPainted(false);
		 btn.setBorderPainted(false);
		 btn.setContentAreaFilled(false);
	 }
}
