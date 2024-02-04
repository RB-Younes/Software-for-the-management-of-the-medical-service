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

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatIntelliJLaf;

import net.proteanit.sql.DbUtils;
////////////////////////////////////////////////////////////////////////////////-----------Fenetre Bilan------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BilanFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private JTextField txtAnalyseDem;
	private JTable table_1;
	private JTextArea textAreaResult ;
	private JTextArea textAreaCom ;
	private String id_Bilan;
	private JButton btnModCons;
	private JButton btnSuppCons;
	private JButton btnAddCons ;

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
					
					BilanFrame frame = new BilanFrame("");
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
	public BilanFrame(String NUM_RDV) {
		
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

		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 800, 650, 25d, 25d));
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(BilanFrame.class.getResource("/Menu_Medcin_img/imprimer donnee patient.png")));
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Bouton Exit ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(BilanFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(BilanFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
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
		Exit_BTN.setIcon(new ImageIcon(BilanFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(746, 11, 32, 32);
		contentPane.add(Exit_BTN);
		
// Fields////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//Commentaire textField
		txtAnalyseDem = new JTextField();
		txtAnalyseDem.setBackground(new Color(51, 204, 153));
		txtAnalyseDem.setForeground(new Color(255, 255, 255));
		txtAnalyseDem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtAnalyseDem.setBounds(42, 129, 228, 38);
		txtAnalyseDem.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //limiter le nombre caractere a 1000
		        if (		txtAnalyseDem.getText().length() >= 1000 ) 
		            e.consume(); 
		    }  
		});
		contentPane.add(txtAnalyseDem);
	//scroll pane +textArea Resuktat	
		JScrollPane scrollPaneResult = new JScrollPane();
		scrollPaneResult.setBounds(42, 209, 418, 134);
		contentPane.add(scrollPaneResult);
		textAreaResult = new JTextArea();
		textAreaResult.setForeground(Color.WHITE);
		textAreaResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textAreaResult.setBackground(new Color(51, 204, 153));
		textAreaResult.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //limiter le nombre de caractere a 2000
		        if (		textAreaResult.getText().length() >= 2000 ) 
		            e.consume(); 
		    }  
		});
		scrollPaneResult.setViewportView(textAreaResult);
	//ScrollPane+ textArea Commentaire	
		JScrollPane scrollPaneCom = new JScrollPane();
		scrollPaneCom.setBounds(504, 209, 234, 134);
		contentPane.add(scrollPaneCom);
		textAreaCom = new JTextArea();
		textAreaCom.setFont(new Font("Segoe UI", Font.BOLD, 14));
		textAreaCom.setForeground(Color.WHITE);
		textAreaCom.setBackground(new Color(51, 204, 153));
		textAreaCom.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { //limiter le nombre de caractere a 2000
		        if (		textAreaCom.getText().length() >= 2000 ) 
		            e.consume(); 
		    }  
		});
		scrollPaneCom.setViewportView(textAreaCom);
		
//ScrollPane Table Bilan////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 447, 684, 176);
		contentPane.add(scrollPane);
		table_1 = new JTable();
		table_1.setForeground(Color.WHITE);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//activer les boutons modifer bilan et supprimer
				btnModCons.setEnabled(true);
				btnSuppCons.setEnabled(true);
				int ligne=table_1.getSelectedRow();
				
				id_Bilan =table_1.getValueAt(ligne, 0).toString();
				String sql="Select * from Bilan where ID_Bilan='"+id_Bilan+"'";
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
						txtAnalyseDem.setText(resultat.getString("analyses_dem"));
						textAreaResult.setText(resultat.getString("resultat"));
						textAreaCom.setText(resultat.getString("Comm"));
					}
				} catch (SQLException  e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		table_1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		table_1.setBackground(new Color(51, 204, 153));
		scrollPane.setViewportView(table_1);
		
//afficher le contenue de la table juste a l'ouverture////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		UpedateTable_1(NUM_RDV);
		
// Vider les champs bouton(reinitialiser)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtAnalyseDem.setText("");
				textAreaCom.setText("");
				textAreaResult.setText("");
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/rubbish.png")));
		btnViderFields.setBounds(606, 372, 32, 32);
		contentPane.add(btnViderFields);
		
//Boutons Ajouter,Supprimer,Modifier///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//Modifer un Bilan**********************************************************************************************************************************************************************************************************************************
		btnModCons = new JButton();
		btnModCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
		btnModCons.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						btnModCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/Modify selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnModCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/Modify.png")));//remetre le bouton de base
					}
				});
				
				btnModCons.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (table_1.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
						{//message d'erreur
							JOptionPane.showMessageDialog(null, "S'il vous plait veuillez selectioner une ligne de la table consultation a modifer !");
						}
						else 
						{
							ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
							 icon.getImage().flush(); // réinitialiser l'animation
							//message de confirmation
							int ClickedButton	=JOptionPane.showConfirmDialog(btnAddCons, "Vouler Vous vraiment Modifier  ce Bilan?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
							if(ClickedButton==JOptionPane.YES_OPTION)
							{
								if (txtAnalyseDem.getText().toString().equals("")) {
									JOptionPane.showMessageDialog(null, "SVP donnez les analyses demandé!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
								}
								else
								{
									
								
								String sql = "Update Bilan set analyses_dem =?,resultat  =?, Comm =? where ID_Bilan="+id_Bilan+"";//metre ajour
								try {
								
									prepared=cnx.prepareStatement(sql);
								
									prepared.setString(1, txtAnalyseDem.getText().toString());		
									prepared.setString(2, textAreaResult.getText().toString());
									prepared.setString(3, textAreaCom.getText().toString());
									
									prepared.execute();//executer la commande
									UpedateTable_1(NUM_RDV);//actualiser la table
									//desactiver les boutons modifer bilan et supprimer
									btnModCons.setEnabled(false);
									btnSuppCons.setEnabled(false);
									//message
									JOptionPane.showMessageDialog(null, "Bilan modifé avec succé!","Modification" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
								
								} catch (SQLException exp) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, exp);
								}
								}
							}
						}
						
					}
				});
				btnModCons.setToolTipText("Modifier analyse");
				btnModCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/Modify.png")));
				btnModCons.setBounds(371, 372, 32, 32);
				contentPane.add(btnModCons);
				
		//ajouter un Bilan**********************************************************************************************************************************************************************************************************************************
				btnAddCons = new JButton();
				btnAddCons.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent arg0) {
						btnAddCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/plus selected.png")));//changer les couleurs button
					}
					@Override
					public void mouseExited(MouseEvent e) {
						btnAddCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/plus.png")));//remetre le bouton de base
					}
				});
				btnAddCons.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
						 icon.getImage().flush(); // réinitialiser l'animation
						 //message de confirmation
						int ClickedButton	=JOptionPane.showConfirmDialog(btnAddCons, "Vouler Vous vraiment Ajouter  ce Bilan?", "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
						if(ClickedButton==JOptionPane.YES_OPTION)
						{
							if (txtAnalyseDem.getText().toString().equals("")) {
								JOptionPane.showMessageDialog(null, "SVP donnez les analyses demandé!","Warning" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/error.png")));
							}
							else
							{
							//recuperer la date du systeme
							Date actuelle = new Date();
							DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							String datactuelle = dateFormat.format(actuelle);
							//la commande sql avec les infos
							String sql = "insert into Bilan values (Num_SEQ_Bilan.nextval,'"+NUM_RDV+"','"+datactuelle+"','"+txtAnalyseDem.getText().toString()+"','"+textAreaResult.getText().toString()+"','"+textAreaCom.getText().toString()+"') ";
							try {
								prepared=cnx.prepareStatement(sql);
								prepared.executeQuery();//executer la commande
								UpedateTable_1(NUM_RDV);//actualiser la table
								//desactiver les boutons modifer bilan et supprimer
								btnModCons.setEnabled(false);
								btnSuppCons.setEnabled(false);
								//afficher un message
								JOptionPane.showMessageDialog(null, "Bilan ajouté avc succés!","Ajout" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, e);
							}
							}
						}
					}
				});
				btnAddCons.setToolTipText("Ajouter Bilan");
				btnAddCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/plus.png")));
				btnAddCons.setBounds(282, 372, 32, 32);
				contentPane.add(btnAddCons);
				
		//Supprimer un Bilan**********************************************************************************************************************************************************************************************************************************
		
				btnSuppCons = new JButton();
				btnSuppCons.setEnabled(false);//desactiver le bouton tant que acune ligne de la table n'a ete selectionné
				btnSuppCons.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					btnSuppCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/remove selected.png")));//changer les couleurs button
					}
				@Override
				public void mouseExited(MouseEvent e) {
					btnSuppCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/remove.png")));//remetre le bouton de base
							}
					});
				btnSuppCons.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
								if (table_1.getSelectedRow()==-1) // return -1 quand aucune ligne est selectionnée
								{//afficher un message d'erreur
									JOptionPane.showMessageDialog(null, " S'il vous plait veuillez selectioner une ligne(Consultation) a supprimer !");

								}
								else {
									 ImageIcon icon = new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/Questions menu medcin.gif"));//Animation "Question"
									 icon.getImage().flush();// réinitialiser l'animation
									//message de confirmation
									int ClickedButton	=JOptionPane.showConfirmDialog(btnSuppCons, "Vouler Vous vraiment Supprimer ce Bilan ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
								if(ClickedButton==JOptionPane.YES_OPTION)
								{
									String sql ="delete from Bilan where ID_Bilan="+id_Bilan;//supprimer la ligne
									
									try {
										
										prepared=cnx.prepareStatement(sql);	
										prepared.execute();//executer
										UpedateTable_1(NUM_RDV);//Actualiser la table
										//desactiver les boutons modifer bilan et supprimer
										btnModCons.setEnabled(false);
										btnSuppCons.setEnabled(false);
										//message
										JOptionPane.showMessageDialog(null, "Bilan supprimé avec succés !","Suppretion" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(GestionPatientFrame.class.getResource("/messages_img/stamp.png")));
									} catch (SQLException eve) {
										// TODO Auto-generated catch block
										JOptionPane.showMessageDialog(null,eve);
									}
								}
								}
							}
						});
						btnSuppCons.setToolTipText("Supprimer Bilan");
						btnSuppCons.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/remove.png")));
						btnSuppCons.setBounds(466, 372, 32, 32);
						contentPane.add(btnSuppCons);
						
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(BilanFrame.class.getResource("/Consultation_img/Bilan.png")));
		lblNewLabel.setBounds(0, 0, 800, 650);
		contentPane.add(lblNewLabel);
		
		
	}
	

//Actualiser la table//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				
	public void UpedateTable_1(String NUM_RDV)
	{ 	
		String sql ="Select ID_Bilan , Date_ajout  , analyses_dem, resultat ,Comm from Bilan where NUM_RDV ='"+NUM_RDV +"'";//
		try {
			
			prepared = cnx.prepareStatement(sql);
			
			
			table_1.setModel(DbUtils.resultSetToTableModel(prepared.executeQuery()));
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table_1.getModel());
			table_1.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));		
			sorter.setSortKeys(sortKeys);
			table_1.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			table_1.getColumnModel().getColumn( 1 ).setHeaderValue("Date");	
			table_1.getColumnModel().getColumn( 2 ).setHeaderValue("Analyses demandées");
			table_1.getColumnModel().getColumn( 3 ).setHeaderValue("Résultat");
			table_1.getColumnModel().getColumn( 4 ).setHeaderValue("Commentaire");
			

	} catch (SQLException e) {
	// TODO Auto-generated catch block
		JOptionPane.showMessageDialog(null,e);
	}
	}
}
