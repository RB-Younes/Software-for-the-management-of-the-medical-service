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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu Medecin------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class MenuMedcinFrame extends JFrame {
	

	private static final long serialVersionUID = 1L;

	//protected static final String ID_Med = null;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    

	private JPanel contentPane;
	

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
					MenuMedcinFrame frame = new MenuMedcinFrame("2");
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
	public MenuMedcinFrame(String ID_Med) {
		//cnx
		cnx = ConnexionDB.CnxDB(); 

		setUndecorated(true);	
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/medical-care.png")));
		setTitle("Secritary Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1100, 650);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1100d, 650d, 25d, 25d));
		setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
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
	        
	        
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
// le BG et lannimation////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel Docanimation1 = new JLabel("");
		Docanimation1.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif")));//animation de base
		Docanimation1.setBounds(455, 185, 439, 389);
		contentPane.add(Docanimation1);
		
		JLabel BGMedecin = new JLabel("");
		BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// Back ground de base	
       
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Minimize ML .png")));
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Minimize ML .png")));
		Minimise_BTN.setBounds(943, 11, 32, 32);
		contentPane.add(Minimise_BTN);
// Exit bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Exit ML.png")));
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
		Exit_BTN.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Exit ML.png")));
		Exit_BTN.setBounds(1002, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
		
//boutons P1///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//Agenda bouton***************************************************************************************************************************************************************************************************************************	
		JButton btncalandar = new JButton();
			btncalandar.setToolTipText("Agenda");
			btncalandar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {//ouverture de l'agenda au clique
					AgendaFrame frame = new AgendaFrame(ID_Med);// on fait passer le ID du medecin pour afficher  son propre agenda et aussi pour pouvoir revenir a ca propre session avec le bouton home
					frame.setLocationRelativeTo(contentPane);//positionner au milieu
					frame.setVisible(true);
					dispose();
				}
			});
			btncalandar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/2.png")));//changer le BackGround(arrière plan)	du menu pour faire un effet visuelle
					ImageIcon animationbtn1 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Calendar animation.gif"));	//animation Agenda
					animationbtn1.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationbtn1);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));// remetre l'arriere plan de base (du debut)	
					ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
					animationBG.getImage().flush(); // réinitialiser l'animation du menu de base
					Docanimation1.setIcon(animationBG);
				}
			});
			ButtonStyle(btncalandar);
			btncalandar.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/calendar.png")));
			btncalandar.setBounds(128, 89, 128, 128);
			contentPane.add(btncalandar);

			
	//Consultation bouton*********************************************************************************************************************************************************************************************************************		
			JButton btnConsultpat = new JButton("");
			btnConsultpat.setToolTipText("Consultation");
			btnConsultpat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {//ouverture de la fenetre Consultation au clique
					ConsultationFrame frame = new ConsultationFrame(ID_Med);// on fait passer le ID du medecin pour afficher que les rdv du jours le concernanat et aussi pour pouvoir revenir a ca propre session avec le bouton home
					frame.setLocationRelativeTo(contentPane);//positionner au milieu
					frame.setVisible(true);
					dispose();
				}
			});
			btnConsultpat.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/3.png")));//changer le BackGround(arrière plan)		
					ImageIcon animationbtn2 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/cons animation.gif")); //animation Consultation
					animationbtn2.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationbtn2);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
					ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
					animationBG.getImage().flush();// réinitialiser l'animation
					Docanimation1.setIcon(animationBG);
				}
			});
			ButtonStyle(btnConsultpat);
			btnConsultpat.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/consultation pat.png")));
			btnConsultpat.setBounds(291, 185, 124, 124);
			contentPane.add(btnConsultpat);
			
	//Gestion Medicaments*************************************************************************************************************************************************************************************************************************		
			JButton btnGstMedoc = new JButton();
			btnGstMedoc.setToolTipText("Gestion Medicaments");
			btnGstMedoc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {//ouverture de la fenetre Consultation au clique
					ListeMedocFrame frame = new ListeMedocFrame(ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home
					frame.setLocationRelativeTo(contentPane);//positionner au milieu
					frame.setVisible(true);
					dispose();
				}
			});
			btnGstMedoc.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/4.png")));	//changer le BackGround(arrière plan)		
					ImageIcon animationbtn3 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/medoc animation.gif")); //animation Liste Medicament
					animationbtn3.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationbtn3);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
					ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
					animationBG.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationBG);
				}
			});
			ButtonStyle(btnGstMedoc);
			btnGstMedoc.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/medicine.png")));
			btnGstMedoc.setBounds(128, 271, 128, 128);
			contentPane.add(btnGstMedoc);
			
	//Statistique bouton*******************************************************************************************************************************************************************************************************************************		
			JButton btnstat = new JButton();
			btnstat.setToolTipText("Statistique");//ouverture de la fenetre Statistique au clique
			btnstat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {//ouverture de la fenetre Consultation au clique
					StatistiqueFrame frame = new StatistiqueFrame(ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home
					frame.setLocationRelativeTo(contentPane);
					frame.setVisible(true);
					dispose();
				}
			});
			btnstat.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/5.png")));	//changer le BackGround(arrière plan)	
					ImageIcon animationbtn4 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Site Stats.gif"));//animation Statistique
					animationbtn4.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationbtn4);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
					ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
					animationBG.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationBG);
				}
			});
			ButtonStyle(btnstat);
			btnstat.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/stat.png")));
			btnstat.setBounds(291, 370, 124, 124);
			contentPane.add(btnstat);
			
		//Gestion des utilisateurs bouton****************************************************************************************************************************************************************************************************************		

			JButton btngstusers = new JButton();
			btngstusers.setToolTipText("Gestion des utilisateurs");
			btngstusers.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent arg0) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/6.png")));	//changer le BackGround(arrière plan)
					ImageIcon animationbtn5 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/users gst animation.gif"));//animation Gestion des utilisateurs
					animationbtn5.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationbtn5);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
					ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
					animationBG.getImage().flush(); // réinitialiser l'animation
					Docanimation1.setIcon(animationBG);
				}
			});
			// enlever les bordures des buttons
			ButtonStyle(btngstusers);
			btngstusers.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {//ouverture de la fenetre Statistique au clique
					GestionUsersFrame open =new GestionUsersFrame(ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home
					open.setLocationRelativeTo(contentPane);
					open.setVisible(true);
					dispose();
				}
			});
			btngstusers.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/gst users.png")));
			btngstusers.setBounds(128, 462, 128, 128);
			contentPane.add(btngstusers);
			
//afficher le nom du medecin(pour lui faire comprendre qu'il a sa propre session) //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

			if(!ID_Med.equals(null)) {
				String sql ="select ID_Med,Nom,Prenom from Medecin  where  ID_Med ='"+ ID_Med+"'";
				try {
					prepared = cnx.prepareStatement(sql);
					
							resultat =prepared.executeQuery();
							while (resultat.next()) {
								String Nom1 =resultat.getString("Nom");
								String Prenom1 =resultat.getString("Prenom");
								
								JLabel lblNewLabel = new JLabel("Dr."+Nom1+" "+Prenom1);

								lblNewLabel.setForeground(Color.GRAY);
								lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
								lblNewLabel.setBounds(270, 44, 267, 32);
								contentPane.add(lblNewLabel);
							}
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, e1);
					}
				
			}
			
			//lheure---------------------------------------------------------------------
			 JLabel horloge = new JLabel();
			 horloge.setForeground(Color.GRAY);
			 horloge.setBounds(267, 11, 162, 21);
	         horloge.setHorizontalAlignment(JLabel.CENTER);
	         horloge.setFont(
	           new Font("Segoe UI", Font.BOLD, 13)
	         );
	         horloge.setText(
	           DateFormat.getDateTimeInstance().format(new Date())
	         );
	         getContentPane().add(horloge);
	         Timer t = new Timer(500, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	              horloge.setText(
	                 DateFormat.getDateTimeInstance().format(new Date())
	              );
	            }
	         });
	         t.setRepeats(true);
	         t.setCoalesce(true);
	         t.setInitialDelay(0);
	         t.start();
			
//boutons P2///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
			
		//Gestion des Patients****************************************************************************************************************************************************************************************************************		
		JButton buttonGstPat = new JButton();
		ButtonStyle(buttonGstPat);
		buttonGstPat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/7.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn5 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Doctor animation.gif"));//animation Gestion des patients
				animationbtn5.getImage().flush(); // réinitialiser l'animation
				Docanimation1.setIcon(animationbtn5);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				Docanimation1.setIcon(animationBG);
			}
		});
		buttonGstPat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //ouverture de la fenetre  Gestion des patients au clique
				GestionPatientFrame frame = new GestionPatientFrame("M",ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home et l'indice M pour differencier entre Medecin et secretaire (on a pas les memes fonctionaliée)
				frame.setLocationRelativeTo(contentPane);
				frame.setVisible(true);
				dispose();
			}
		});
		buttonGstPat.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/clipboard.png")));
		buttonGstPat.setToolTipText("Gestion Patients");
		buttonGstPat.setBounds(925, 89, 128, 128);
		contentPane.add(buttonGstPat);
		
		//Gestion des RDV****************************************************************************************************************************************************************************************************************		
		JButton buttonGstRDV = new JButton();
		ButtonStyle(buttonGstRDV);
		buttonGstRDV.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/8.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn5 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/gst RDV animation.gif"));//animation Gestion des RDV
				animationbtn5.getImage().flush();  // réinitialiser l'animation
				Docanimation1.setIcon(animationbtn5);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				Docanimation1.setIcon(animationBG);
			}
		});
		buttonGstRDV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//ouverture de la fenetre  Gestion des RDV au clique
				GestionRDVFrame frame = new GestionRDVFrame("M",ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home et l'indice M pour differencier entre Medecin et secretaire (on a pas les memes fonctionaliée)
				frame.setLocationRelativeTo(contentPane);
				frame.setVisible(true);
				dispose();
			}
		});
		buttonGstRDV.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/RDV.png")));
		buttonGstRDV.setToolTipText("Gestion RDV");
		buttonGstRDV.setBounds(925, 271, 128, 128);
		contentPane.add(buttonGstRDV);
		
		//Tresorerie****************************************************************************************************************************************************************************************************************		
		JButton buttonTresorerie = new JButton();
		ButtonStyle(buttonTresorerie);
		buttonTresorerie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/9.png")));//changer le BackGround(arrière plan)
				ImageIcon animationbtn5 =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/Credit card animation.gif"));//animation Tresorerie
				animationbtn5.getImage().flush(); // réinitialiser l'animation
				Docanimation1.setIcon(animationbtn5);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGMedecin.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/1.png")));	// remetre l'arriere plan de base (du debut)
				ImageIcon animationBG =new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/menu animation ent.gif"));	//animation de base
				animationBG.getImage().flush(); // réinitialiser l'animation
				Docanimation1.setIcon(animationBG);
			}
		});
		buttonTresorerie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//ouverture de la fenetre  Gestion des Tresorerie au clique
				TresorerieFrame frame = new TresorerieFrame("M",ID_Med);//faire passer le ID du medecin pour pouvoir revenir a ca propre session avec le bouton home et l'indice M pour differencier entre Medecin et secretaire (on a pas les memes fonctionaliée)
				frame.setLocationRelativeTo(contentPane);
				frame.setVisible(true);
				dispose();
			}
		});
		buttonTresorerie.setIcon(new ImageIcon(MenuMedcinFrame.class.getResource("/Menu_Medcin_img/RES MAN selected.png")));
		buttonTresorerie.setToolTipText("Tresorerie");
		buttonTresorerie.setBounds(925, 462, 128, 128);
		contentPane.add(buttonTresorerie);
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGMedecin.setBounds(0, 0, 1100, 650);
		contentPane.add(BGMedecin);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlecer les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	
}
}
