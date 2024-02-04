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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
////////////////////////////////////////////////////////////////////////////////-----------Fenetre Agenda------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class AgendaFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableRDV;
	private String datactuelle="";

	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	private String num_rdv;// le id recupere en cliquant sur le tableau
	private JTable tableMinin;
	
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
					AgendaFrame frame = new AgendaFrame("2");
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
	
	public AgendaFrame(String ID_MED) {
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
// initialisation///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200d, 550d, 25d, 25d));
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AgendaFrame.class.getResource("/Menu_Medcin_img/calendar.png")));
		setTitle("Staff Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 550);
		
		
		contentPane = new JPanel();
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
// Fields-//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Combo box  avec les nom des patients et leur ID
		JComboBox<String> comboBoxPAtients = new JComboBox<String>();
		comboBoxPAtients.addItem("Selectionner Patient");
		INComboboxPat(comboBoxPAtients);
		comboBoxPAtients.setBackground(new Color(51, 204, 153));
		comboBoxPAtients.setForeground(new Color(255, 255, 255));
		comboBoxPAtients.setBounds(263, 364, 217, 32);
		contentPane.add(comboBoxPAtients);

		//Spinner pour l'Heur
		JSpinner spinnerH = new JSpinner();
		spinnerH.setSize(100, 25);
		spinnerH.setLocation(263, 457);
		spinnerH.setModel(new SpinnerDateModel());
		spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));
		contentPane.add(spinnerH);
		
		//Date chooser
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setToolTipText("Date");
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setForeground(new Color(102, 0, 153));
		dateChooser.setBackground(new Color(51, 204, 153));
		dateChooser.setBorder(null);
		dateChooser.setDate(new Date());
		dateChooser.setBounds(263, 414, 217, 32);
		contentPane.add(dateChooser);
//ScrollPane/Table Patient(Grande table)//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Table Patient Grande**************************************************************************************************************************************************************************************
		JScrollPane scrollPaneRDV = new JScrollPane();
		scrollPaneRDV.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneRDV.setBounds(90, 142, 1025, 196);
		contentPane.add(scrollPaneRDV);
		
		tableRDV = new JTable();
		tableRDV.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableRDV.setForeground(new Color(255, 255, 255));
		tableRDV.setBorder(null);
		tableRDV.addMouseListener(new MouseAdapter() {
			// s
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//si je clique sur une ligne (selectionne) la  valeurs selected de la combobox() , la date(datechooser) , l'heure(spinner) cheangront de valeurs et prendront les vleurs de la ligne
				
				int ligne=tableRDV.getSelectedRow();//recuperer le numero de ligne
				num_rdv =tableRDV.getValueAt(ligne, 0).toString();//recuperer l'ID du RDV qui est a la premiere colone (0) de la table
				String sql="Select * from RDV where NUM_RDV='"+num_rdv+"'";//Selectionner toute les info du RDV aec l'ID selectionné de la bdd
				
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					if(resultat.next())
					{
					
						String RDVDH =resultat.getString("Date_RDV").toString();//l'heure
						
						if (RDVDH!=null)
						{
							
				    	String RDVD=RDVDH.substring(0, 10);//recupererer la date et prendre que les 10 premiers caracteres car elle est de la forme yyyy-MM-dd HH:mm:ss
				    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    	Date date = format.parse(RDVD);// transformer RDVD (de String en Date) pour pouvoir la modifier sur le datechooser
				    	dateChooser.setDate(date);// mettre cette valeur sur le date chooser
							
						}
						
				    	
						String RDVH=resultat.getString("H_RDV").toString();		//resultat de la requete				
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//le format
						Date H_RDV = sdf.parse(RDVH); // transformet le string de la table en format date pour pouoir modifier 
						spinnerH.setValue(H_RDV); // mettre l'Heure du Rdv dansle spinner
						
						//parcourir toutr la combo box
						for (int j = 0; j < comboBoxPAtients.getItemCount(); j++) 
						{
					
								if (comboBoxPAtients.getItemAt(j).equals(resultat.getString("ID_Pat").toString()+"-"+resultat.getString("Patient_Name").toString())) {// selectionner cet element
									comboBoxPAtients.setSelectedIndex(j);
								}
							
						}
						String ID=resultat.getString("ID_Pat").toString();
						int i=0;//le nombre de RDV
						sql="Select * from Consult where ID_pat='"+ID+"'";
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
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
			}
		});
		tableRDV.setBackground(new Color(0, 204, 153));
		scrollPaneRDV.setViewportView(tableRDV);
		
	//Table Patient Mini**************************************************************************************************************************************************************************************
		JScrollPane scrollPaneMini = new JScrollPane();
		scrollPaneMini.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneMini.setBounds(520, 354, 589, 170);
		contentPane.add(scrollPaneMini);
		
		tableMinin = new JTable();
		tableMinin.setFont(new Font("Segoe UI", Font.BOLD, 11));
		tableMinin.setForeground(new Color(255, 255, 255));
		tableMinin.setBorder(null);
		tableMinin.setBackground(new Color(0, 204, 153));
		scrollPaneMini.setViewportView(tableMinin);
		
		
//afficher le contenue de la table juste a louverture://////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

		UpedateTableRDV(ID_MED);
	
		
// REduire bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1072, 5, 32, 32);
		contentPane.add(Minimise_BTN);
		
// Vider les champs bouton////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnViderFields = new JButton("");
		btnViderFields.setToolTipText("R\u00E9initialiser les champs");
		ButtonStyle(btnViderFields);
		btnViderFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				comboBoxPAtients.setSelectedIndex(0);// remtre la valeur de la combo box par default
				spinnerH.setModel(new SpinnerDateModel());
				spinnerH.setEditor(new JSpinner.DateEditor(spinnerH, "HH:mm"));//prendre la valeur du systeme
				
			}
		});
		btnViderFields.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Agenda_img/rubbish.png")));
		btnViderFields.setBounds(448, 506, 32, 32);
		contentPane.add(btnViderFields);
		
// Exit bouton/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						// et vider les fields
				
						dispose();
					}
			}
			
		});
		Exit_BTN.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1156, 5, 32, 32);
		contentPane.add(Exit_BTN);
//Grande table actualisation /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnalltheTable = new JButton("");
		btnalltheTable.setToolTipText("Actualiser");
		btnalltheTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Agenda_img/refresh selected .png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnalltheTable.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Agenda_img/refresh .png")));
			}
		});
		btnalltheTable.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Agenda_img/refresh .png")));
		btnalltheTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//actualiser la table au clique
				UpedateTableRDV(ID_MED);
			}
		});
		btnalltheTable.setBounds(1049, 99, 32, 32);
		contentPane.add(btnalltheTable);
//Mini table boutons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Affichez le RDV du Patient Selectionné********************************************************************************************************************************************
		JButton btnPatApps = new JButton("Afficher les RDV du Patient Selectionn\u00E9");
		btnPatApps.setForeground(new Color(255, 255, 255));
		btnPatApps.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnPatApps.setBackground(new Color(0, 204, 153));
		btnPatApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					if (!comboBoxPAtients.getSelectedItem().toString().equals("Selectionner Patient")) //verifier que le patient a bien ete selectionné
					{
							String PatInfo =comboBoxPAtients.getSelectedItem().toString();//recuperer lèitem selectionné
							int iend=PatInfo.indexOf("-");//lindice du "-"(qui separe l'ID du patient du Nom et Prenom )
							String Id_PAT="00";
							
							if (iend != -1) 
							{
								Id_PAT= PatInfo.substring(0 , iend); //cette variable recoit le resultat
							}
							//recuperer Nom du patient Date du RDV et l'heure de la bdd
							/*Date actuelle = new Date();
				 			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				 			String datactuelle = dateFormat.format(actuelle);  */
							
							String sql ="Select Patient_Name ,  Date_RDV,H_RDV   from RDV where ID_PAT='"+ Id_PAT + "' and ID_Med ='"+ ID_MED+"'";
							try {
								
								prepared = cnx.prepareStatement(sql);
								resultat =prepared.executeQuery();
								tableMinin.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model la Mini table
								
								//utiliser les sorters pour trier la table
								TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableMinin.getModel());
								tableMinin.setRowSorter(sorter);
								List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
								sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));		
								sorter.setSortKeys(sortKeys);
								//cahnger les titres des colonnes de la table
								tableMinin.getColumnModel().getColumn( 0 ).setHeaderValue("Nom & Prenom du Patient");
								tableMinin.getColumnModel().getColumn( 1 ).setHeaderValue("Date du RDV");	
								tableMinin.getColumnModel().getColumn( 2 ).setHeaderValue("Heure du RDV");
							
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null,e);
							}
						}
				}

		});
		btnPatApps.setBounds(166, 490, 262, 23);
		contentPane.add(btnPatApps);
		
		//Affichez les RDV de la date Selectionné********************************************************************************************************************************************
		JButton btnDall = new JButton("Afficher les RDV de la date selectionn\u00E9e");
		btnDall.setForeground(new Color(255, 255, 255));
		btnDall.setBackground(new Color(0, 204, 153));
		btnDall.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnDall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!((JTextField) dateChooser.getDateEditor().getUiComponent()).getText().equals("")) {
					
					String Date = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText(); //recuperer la Date
					String aneee = Date.substring(0, 4);//annee
					String Mois=Date.substring(5, 7);//Mois
					String jours=Date.substring(8, 10);//Jours
						String Dateinverse =jours+"-"+Mois+"-"+aneee;// on l'inverse car elle est de format yyyy-MM-dd et on a besoisn de l'inverse dd-MM-yyyy
					
					
					String sql ="Select Date_RDV ,H_RDV ,  Patient_Name  from RDV where Date_RDV='"+ Dateinverse +"' and  id_med ='"+ID_MED +"'";
					try {
							prepared = cnx.prepareStatement(sql);
							resultat =prepared.executeQuery();
							tableMinin.setModel(DbUtils.resultSetToTableModel(resultat));//changer le model la Mini table 
							//utiliser les sorters pour trier la table
							TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableMinin.getModel());
							tableMinin.setRowSorter(sorter);
							List<RowSorter.SortKey> sortKeys = new ArrayList<>(100000000);
							sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
							sorter.setSortKeys(sortKeys);
							//cahnger les titres des colonnes de la table
							tableMinin.getColumnModel().getColumn( 0 ).setHeaderValue("Date du RDV");
							tableMinin.getColumnModel().getColumn( 1 ).setHeaderValue("Heure du RDV");	
							tableMinin.getColumnModel().getColumn( 2 ).setHeaderValue("Nom & Prenom du Patient");
							
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null,e);
						}
					}
				
			
			}
		});
		btnDall.setBounds(166, 515, 262, 23);
		contentPane.add(btnDall);
// les bouttons de la grande table (jours de la semaine)	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//bouton Dimanche************************************************************************************************************************************************************************************************
		JButton btnDim = new JButton("Dimanche");
		btnDim.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnDim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");// Defenir le format
				datactuelle = dateFormat.format(date);// recuperer ljours de la semaine
				//on prend la periode exace dune semaine donc par exemple si on est le Vendredi et je clique sur le button Dimanche ca va m'afficher les rdv du dimanche de 6 jours d'avant 
				//on est le dimanche et je clique sur Vendredi ca va mafficher les rdv du Vendredi d'ici 6 jrs
				//Dpnc la semaine debute le Dimanche et ce termine le samedi 
				Date dateret; // la date retourner du calandar (Type Date)
				String daterech ;//la date retourner du calandar avec le nouveau format (String)
				Calendar cal = Calendar.getInstance(); // le calandar
			
				switch(datactuelle.substring(0,3)) //c' est la date du sys (le jours de la semaine)
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -5); // avoir la date du dimanche davant le vendredi (on est le vendredi et on clique sur dimanche)
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech); //actulaiser la table avec la date rechercher

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -6); // avoir la date du dimanche davant le samedi (on est le samedi et on clique sur dimanche)
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
					
				case "dim":

					UpedateTableRDV(ID_MED);// le cas ou on est le dimanche et il clique sur demanche
					break;

				case "lun":

					cal.add(Calendar.DATE, -1);// avoir la date du dimanche davant le lundi (on est le lundi et on clique sur dimanche)
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				case "mar":

					cal.add(Calendar.DATE, -2); // avoir la date du Dimanche davant le mardi (on est le mardi et on clique sur dimanche)
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				case "mer":

					cal.add(Calendar.DATE, -3);//avoir la date du Diamnche davant le mercredi (on est le mercredi et on clique sur dimanche)
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				case "jeu":

					cal.add(Calendar.DATE, -4);//avoir la date du Diamnche davant le jeudi (on est le jeudi et on clique sur dimanche)
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
					
				default:
					
				}	
			}
		});
		btnDim.setForeground(Color.WHITE);
		btnDim.setBackground(new Color(51, 204, 153));
		btnDim.setBounds(-14, 170, 94, 30);
		contentPane.add(btnDim);
		
	//bouton Lundi************************************************************************************************************************************************************************************************
		JButton btnLundi_1 = new JButton("Lundi");
		btnLundi_1.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnLundi_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");// Defenir le format
				datactuelle = dateFormat.format(date);// recuperer ljours de la semaine
				
				
				Date dateret; // la date retourner du calandar (Type Date)
				String daterech ;//la date retourner du calandar avec le nouveau format (String)
				Calendar cal = Calendar.getInstance(); // le calandar
			
				switch(datactuelle.substring(0,3)) //datactuelle.substring(0,3)
				{
				case "ven"://Vendredi
					
					cal.add(Calendar.DATE, -4); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -5); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "dim":
					//dans ce cas on est le dimanche et on clique sur Lundi ca va m'afficher les rdv de demain donc lundi
					cal.add(Calendar.DATE, +1);//avoir la date du Lundi d'apres le Dimanche 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
					
				case "lun":
					UpedateTableRDV(ID_MED);
					break;
					
				case "mar":
					//on est le mardi et on clique sur Lundi
					cal.add(Calendar.DATE, -1); 
					 dateret =cal.getTime();//avoir la date du lundi d'avant le Mardi 
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				case "mer":
					//on est le mercredi et on clique sur Lundi
					cal.add(Calendar.DATE, -2);//avoir la date du lundi d'avant le Mercredi 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				case "jeu":
					//on est le jeudi et on clique sur Lundi
					cal.add(Calendar.DATE, -3);//avoir la date du lundi d'avant le jeudi 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret);  //recuperer la date exacte avec le format  dd-MM-yyyy
					UpedateTableRDVJours(ID_MED,daterech);//actulaiser la table avec la date rechercher
					break;
				default:
					
				}	
			}
		});
		btnLundi_1.setForeground(Color.WHITE);
		btnLundi_1.setBackground(new Color(51, 204, 153));
		btnLundi_1.setBounds(-14, 199, 94, 27);
		contentPane.add(btnLundi_1);
		
		//de meme pour le reste
	//bouton Mardi************************************************************************************************************************************************************************************************
		JButton btnNewButton = new JButton("Mardi");
		btnNewButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");
				datactuelle = dateFormat.format(date);
				
				Date dateret; 
				String daterech ;
				Calendar cal = Calendar.getInstance(); 
			
				switch(datactuelle.substring(0,3)) 
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -3); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -4); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "dim":

					cal.add(Calendar.DATE, +2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "mar":

					UpedateTableRDV(ID_MED);
					break;
				case "mer":

					cal.add(Calendar.DATE, -1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "jeu":

					cal.add(Calendar.DATE, -2);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				default:
					
				}	
			}
		});
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(51, 204, 153));
		btnNewButton.setBounds(-14, 225, 94, 27);
		contentPane.add(btnNewButton);
		
	//bouton Mercredi************************************************************************************************************************************************************************************************
		JButton button = new JButton("Mercredi");
		button.setFont(new Font("Segoe UI", Font.BOLD, 11));
		button.setForeground(Color.WHITE);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");// Defenir le format
				datactuelle = dateFormat.format(date);// recuperer ljours de la semaine
				
				
				Date dateret; // la date retourner du calandar (Type Date)
				String daterech ;//la date retourner du calandar avec le nouveau format (String)
				Calendar cal = Calendar.getInstance(); // le calandar
			
				switch(datactuelle.substring(0,3)) //datactuelle.substring(0,3)
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -3); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +2 );
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "mar":

					cal.add(Calendar.DATE, +1); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "mer":

					UpedateTableRDV(ID_MED);
					break;
				case "dim":

					cal.add(Calendar.DATE, +3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "jeu":

					cal.add(Calendar.DATE, -1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				default:
					
				}	
			}
		});
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(51, 204, 153));
		button.setBounds(-14, 251, 94, 27);
		contentPane.add(button);
		
	//bouton Jeudi************************************************************************************************************************************************************************************************
		JButton btnJeudi = new JButton("Jeudi");
		btnJeudi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnJeudi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("E");// Defenir le format
				datactuelle = dateFormat.format(date);// recuperer ljours de la semaine
				
				
				Date dateret; // la date retourner du calandar (Type Date)
				String daterech ;//la date retourner du calandar avec le nouveau format (String)
				Calendar cal = Calendar.getInstance(); // le calandar
			
				switch(datactuelle.substring(0,3)) //datactuelle.substring(0,3)
				{
				case "ven"://Vendredi

					
					cal.add(Calendar.DATE, -1); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);

					break;
					
				case "sam":

					cal.add(Calendar.DATE, -2); 
					dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "lun":

					cal.add(Calendar.DATE, +3);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
					
				case "jeu":

					UpedateTableRDV(ID_MED);
					break;
				case "mar":

					cal.add(Calendar.DATE, +2); 
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "mer":

					cal.add(Calendar.DATE, +1);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				case "dim":

					cal.add(Calendar.DATE, +4);
					 dateret =cal.getTime();
					dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					daterech =dateFormat.format(dateret); 
					UpedateTableRDVJours(ID_MED,daterech);
					break;
				default:
					
				}	
			}
		});
		btnJeudi.setForeground(Color.WHITE);
		btnJeudi.setBackground(new Color(51, 204, 153));
		btnJeudi.setBounds(-14, 277, 94, 27);
		contentPane.add(btnJeudi);
		
	//bouton Samedi************************************************************************************************************************************************************************************************
		JButton btnSamedi = new JButton("Samedi");
		btnSamedi.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnSamedi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Date date = new Date();
						DateFormat dateFormat = new SimpleDateFormat("E");// Defenir le format
						datactuelle = dateFormat.format(date);// recuperer ljours de la semaine
						
						
						Date dateret; // la date retourner du calandar (Type Date)
						String daterech ;//la date retourner du calandar avec le nouveau format (String)
						Calendar cal = Calendar.getInstance(); // le calandar
					
						switch(datactuelle.substring(0,3)) //datactuelle.substring(0,3)
						{
						case "ven"://Vendredi
							cal.add(Calendar.DATE, +1); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);

							break;
						case "sam":

							UpedateTableRDV(ID_MED);
							break;
							
						case "dim":

							cal.add(Calendar.DATE, +6); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);
							break;
							
						case "lun":

							cal.add(Calendar.DATE, +5); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);
							break;
						case "mar":

							cal.add(Calendar.DATE, +4); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);
							break;
						case "mer":

							cal.add(Calendar.DATE, +3); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);
							break;
						case "jeu":

							cal.add(Calendar.DATE, +2); // avoir la date du jour davant le vendredi
							 dateret =cal.getTime();
							dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							daterech =dateFormat.format(dateret); 
							UpedateTableRDVJours(ID_MED,daterech);
							break;
						default:
							
						}		
						
					}
				});
		btnSamedi.setForeground(Color.WHITE);
		btnSamedi.setBackground(new Color(51, 204, 153));
		btnSamedi.setBounds(-14, 303, 94, 27);
		contentPane.add(btnSamedi);
		
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
							MenuMedcinFrame frame = new MenuMedcinFrame(ID_MED);// retourner au menu medecin
							frame.setLocationRelativeTo(null);
							frame.setVisible(true);
							dispose();
					}
				});
				btnHome.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Log_in_img/home med.png")));
				btnHome.setToolTipText("Retourner au Menu");
				btnHome.setBounds(1114, 5, 32, 32);
				contentPane.add(btnHome);
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel BGStaff = new JLabel("BGStaffM");
		BGStaff.setIcon(new ImageIcon(AgendaFrame.class.getResource("/Agenda_img/Agenda.png")));
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

//Actualisation de table RDV la grande/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void UpedateTableRDV(String ID_MED)
	{ 
		//recuperer la date du systeme
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		datactuelle = dateFormat.format(actuelle);
		
		
		String sql ="Select NUM_RDV , Patient_Name  , Date_RDV, H_RDV ,Commentary  from RDV where id_med ='"+ID_MED +"' and Date_RDV='" +datactuelle + "'";// la comande sql
		try {
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableRDV.setModel(DbUtils.resultSetToTableModel(resultat));//changer le modelm de latable
			// trier ma table par apport a la colone de lheure vu que la date est la meme (Date du jour)
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableRDV.getModel());
			tableRDV.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));		
			sorter.setSortKeys(sortKeys);
			 //renomer les ctitres de colones
			tableRDV.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			tableRDV.getColumnModel().getColumn( 1 ).setHeaderValue("Nom & Prenom du Patient");	
			tableRDV.getColumnModel().getColumn( 2 ).setHeaderValue("Date du RDV");
			tableRDV.getColumnModel().getColumn( 3 ).setHeaderValue("Heure du RDV");
			tableRDV.getColumnModel().getColumn( 4 ).setHeaderValue("Commentaire");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		
	}

//Remplissage combobox qui contient les patient ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		public void INComboboxPat(JComboBox<String> Pat)
		{ //Nom ,Prenom  ,Ueser_Name ,password , DateN  ,sex ,ADR ,Numtel ,Specilité
			String sql ="Select ID_Patient,Name ,FirstName  from Patient";//sql commande
			try {
				prepared = cnx.prepareStatement(sql);
				resultat =prepared.executeQuery();
				while(resultat.next())
				{
					// elle sera de la forme ID_pat-Nom Prenom
					String item=resultat.getString("ID_Patient").toString()+"-"+resultat.getString("Name").toString()+" "+resultat.getString("FirstName").toString();
					Pat.addItem(item);// ajouter a  la combo bomx
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			}
			
		}
		
//Methode pour actualiser la mini table //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		public void UpedateTableRDVJours(String ID_MED, String Date)// elle va actualiser la table avec la date donné
		{ 	
			String sql ="Select NUM_RDV , Patient_Name  , Date_RDV, H_RDV ,Commentary  from RDV where id_med ='"+ID_MED +"' and Date_RDV='" +Date + "'";
		try {
			
			prepared = cnx.prepareStatement(sql);
			resultat =prepared.executeQuery();
			tableRDV.setModel(DbUtils.resultSetToTableModel(resultat));
			// trier ma table par rapport a la colone de lheure vu que la date est la meme (Date du jour)
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableRDV.getModel());
			tableRDV.setRowSorter(sorter);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
			sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));		
			sorter.setSortKeys(sortKeys);
			 //renomer les ctitres de colones
			tableRDV.getColumnModel().getColumn( 0 ).setHeaderValue("N °");
			tableRDV.getColumnModel().getColumn( 1 ).setHeaderValue("Nom & Prenom du Patient");	
			tableRDV.getColumnModel().getColumn( 2 ).setHeaderValue("Date du RDV");
			tableRDV.getColumnModel().getColumn( 3 ).setHeaderValue("Heure du RDV");
			tableRDV.getColumnModel().getColumn( 4 ).setHeaderValue("Commentaire");
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		}

}
	
	

