import java.awt.BorderLayout;
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
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.formdev.flatlaf.FlatIntelliJLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Statistique------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class StatistiqueFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelChart;
	private JButton btnchart ;
	private JButton btncalcperiode;
	private JComboBox<Object> CBYear;
	private JComboBox<Object> CBMonth;
	private JComboBox<Object> CBMonth2;
	private JComboBox<Object> CBYear3;
	private JComboBox<Object> CBMonth3;
	private JButton btnMaladie ;
	private JComboBox<Object> CBMonth4;
	private JComboBox<Object> CBNumSemaine;
	private JButton btnchartSem;

	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	PreparedStatement prepared1 = null;
	ResultSet resultat1 =null; 
	
	private JTextField txtnbrcons;
	private JTextField txtNomMaladie;
	private JComboBox<Object> CBYear4;


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
					StatistiqueFrame frame = new StatistiqueFrame("2");
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
	public StatistiqueFrame(String ID_MED) {
		
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
 
        
		setLocationRelativeTo(null);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0d, 0d, 1200, 650, 25d, 25d));
		setType(Type.POPUP);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(StatistiqueFrame.class.getResource("/Menu_Medcin_img/stat.png")));
		setTitle("Forgot password ?");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1200, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
//le panepour les graphique //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 		
		panelChart = new JPanel();
		panelChart.setBackground(Color.LIGHT_GRAY);
		panelChart.setBounds(477, 188, 691, 446);
		contentPane.add(panelChart);
		
// Bouton Exit  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML.png")));//remetre le bouton de base
			}
		});
		Exit_BTN.setToolTipText("Exit");
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{
						dispose();
					}
			}
		});
		Exit_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Exit ML.png")));
		Exit_BTN.setBounds(1124, 5, 32, 32);
		contentPane.add(Exit_BTN);
		
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML .png")));//remetre le bouton de base
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
				
			}
		});
		Minimise_BTN.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Log_in_img/Minimize ML .png")));
		Minimise_BTN.setBounds(1040, 5, 32, 32);
		contentPane.add(Minimise_BTN);
//field le nombre de patients dans une periode(resultat)/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		txtnbrcons = new JTextField();
		txtnbrcons.setBorder(null);
		txtnbrcons.setHorizontalAlignment(SwingConstants.TRAILING);
		txtnbrcons.setText("0");
		txtnbrcons.setEditable(false);
		txtnbrcons.setBackground(new Color(51, 204, 153));
		txtnbrcons.setForeground(new Color(255, 255, 255));
		txtnbrcons.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtnbrcons.setBounds(716, 117, 107, 33);
		contentPane.add(txtnbrcons);

// les elements de lintervales de temps(nombres de patients par mois)//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	//annee		
		CBYear = new JComboBox<Object>();
		CBYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActiverbtnsparMois(CBMonth,CBMonth2,CBYear);
			}
		});
		CBYear.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2020; i<=2050 ; i++) {
			String Year =""+i+"";
			CBYear.addItem(Year);		
			}
		CBYear.setBackground(new Color(51, 204, 153));
		CBYear.setForeground(new Color(255, 255, 255));
		CBYear.setBounds(463, 119, 89, 32);
		contentPane.add(CBYear);
		
	//Mois du debut		
		CBMonth = new JComboBox<Object>();
		CBMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActiverbtnsparMois(CBMonth,CBMonth2,CBYear);
			}
		});
		CBMonth.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth.setBackground(new Color(51, 204, 153));
		CBMonth.setForeground(new Color(255, 255, 255));
		CBMonth.setBounds(300, 107, 82, 32);
		contentPane.add(CBMonth);

		//Mois de la fin			
		CBMonth2 = new JComboBox<Object>();
		CBMonth2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ActiverbtnsparMois(CBMonth,CBMonth2,CBYear);
			}
		});
		CBMonth2.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth2.setBackground(new Color(51, 204, 153));
		CBMonth2.setForeground(new Color(255, 255, 255));
		CBMonth2.setBounds(300, 163, 82, 32);
		contentPane.add(CBMonth2);
		
//les elements de la rechrche de nombre de patients par maladie(par mois)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
		//Mois
		CBMonth3 = new JComboBox<Object>();
		CBMonth3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBYear3.getSelectedIndex()!=0 && CBMonth3.getSelectedIndex()!=0 && !txtNomMaladie.getText().toString().equals("Nom de la Maladie")) {//tout les champs et les cmbo box on ete modifié
					btnMaladie.setEnabled(true);//activer le bouutton d'affichage du graphique(NBR de patients par maladie(par mois))
				}
			}
		});
		CBMonth3.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth3.setForeground(Color.WHITE);
		CBMonth3.setBackground(new Color(51, 204, 153));
		CBMonth3.setBounds(99, 404, 82, 32);
		contentPane.add(CBMonth3);
		//annee
		CBYear3 = new JComboBox<Object>();
		CBYear3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBYear3.getSelectedIndex()!=0 && CBMonth3.getSelectedIndex()!=0 && !txtNomMaladie.getText().toString().equals("Nom de la Maladie")) {//tout les champs et les cmbo box on ete modifié
					btnMaladie.setEnabled(true);//activer le bouutton d'affichage du graphique(NBR de patients par maladie(par mois))
				}
			}
		});
		CBYear3.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2020; i<=2050 ; i++) {
			String Year =""+i+"";
			CBYear3.addItem(Year);		
			}
		CBYear3.setForeground(Color.WHITE);
		CBYear3.setBackground(new Color(51, 204, 153));
		CBYear3.setBounds(245, 404, 89, 32);
		contentPane.add(CBYear3);
		
// les elements de lintervales de temps(nombres de patients par semaine)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		//N° de la semaine
		CBNumSemaine = new JComboBox<Object>();
		CBNumSemaine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (CBYear4.getSelectedIndex()!=0 && CBMonth4.getSelectedIndex()!=0 && CBNumSemaine.getSelectedIndex()!=0  )//tout les champs et les cmbo box on ete modifié
				{
					btnchartSem.setEnabled(true);//activer le bouutton d'affichage du graphique(nombres de patients par semaine)
				}
			}
		});
		CBNumSemaine.setModel(new DefaultComboBoxModel<Object>(new String[] {"N° de la semaine","semaine N°01","semaine N°02","semaine N°03","semaine N°04"}));
		CBNumSemaine.setForeground(Color.WHITE);
		CBNumSemaine.setBackground(new Color(51, 204, 153));
		CBNumSemaine.setBounds(43, 254, 138, 32);
		contentPane.add(CBNumSemaine);
		//Mois
		CBMonth4 = new JComboBox<Object>();
		CBMonth4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBYear4.getSelectedIndex()!=0 && CBMonth4.getSelectedIndex()!=0 && CBNumSemaine.getSelectedIndex()!=0  )//tout les champs et les cmbo box on ete modifié
				{
					btnchartSem.setEnabled(true);//activer le bouutton d'affichage du graphique(nombres de patients par semaine)
				}
			}
		});
		CBMonth4.setModel(new DefaultComboBoxModel<Object>(new String[] {"Mois","01","02","03","04","05","06","07","08","09","10","11","12"	}));
		CBMonth4.setForeground(Color.WHITE);
		CBMonth4.setBackground(new Color(51, 204, 153));
		CBMonth4.setBounds(245, 254, 82, 32);
		contentPane.add(CBMonth4);
		//année
		CBYear4 = new JComboBox<Object>();
		CBYear4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBYear4.getSelectedIndex()!=0 && CBMonth4.getSelectedIndex()!=0 && CBNumSemaine.getSelectedIndex()!=0  )//tout les champs et les cmbo box on ete modifié
				{
					btnchartSem.setEnabled(true);//activer le bouutton d'affichage du graphique(nombres de patients par semaine)
				}
			}
		});
		CBYear4.setModel(new DefaultComboBoxModel<Object>(new String[] {"Anneé"}));		
		for (int i = 2020; i<=2050 ; i++) {
			String Year =""+i+"";
			CBYear4.addItem(Year);		
			}
		CBYear4.setForeground(Color.WHITE);
		CBYear4.setBackground(new Color(51, 204, 153));
		CBYear4.setBounds(367, 254, 89, 32);
		contentPane.add(CBYear4);
		
//afficher les batons du nombre des patients âr periode(mois)////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		btncalcperiode = new JButton("calculer");
		btncalcperiode.setForeground(new Color(51, 204, 153));
		btncalcperiode.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btncalcperiode.setEnabled(false);
		btncalcperiode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (CBYear.getSelectedIndex()!=0 && CBMonth.getSelectedIndex()!=0 && CBMonth.getSelectedIndex()!=0) {
				//D1 date de debut D2 date de fin
				String D1="01-"+CBMonth.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();
				String D2=DonnerJour(CBYear.getSelectedItem().toString(), CBMonth2.getSelectedItem().toString())+"-"+CBMonth2.getSelectedItem().toString()+"-"+CBYear.getSelectedItem().toString();

				cnx = ConnexionDB.CnxDB();
				int j= Integer.parseInt(( CBMonth2.getSelectedItem().toString()));
				int i=Integer.parseInt(( CBMonth.getSelectedItem().toString()));
				while (j<i) {
				j++;
				}
				String sql = "SELECT Patient_Name  FROM Consult WHERE Date_consult BETWEEN '"+D1+"' AND '"+D2+"'";
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					 i=0;
					while (resultat.next()) {
						i++;
					}
					txtnbrcons.setText(String.valueOf(i));
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				}
				
			}
		});
		btncalcperiode.setBounds(839, 116, 144, 40);
		contentPane.add(btncalcperiode);
//afficher les batosn du nombre des patients (consultation) pâr periode(mois)-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
		btnchart = new JButton("      chart");
		btnchart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnchart.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnchart.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot.png")));//remetre le bouton de base
			}
		});
		btnchart.setForeground(new Color(51, 204, 153));
		btnchart.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnchart.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot.png")));
		btnchart.setEnabled(false);
		btnchart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cnx = ConnexionDB.CnxDB();
				if (CBYear.getSelectedIndex()!=0 && CBMonth.getSelectedIndex()!=0 && CBMonth.getSelectedIndex()!=0) {//si tout lechamps et combobox on ete modifier (pas les valeurs initiales)
				DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); //declaration du dataset
				
				int i= Integer.parseInt(( CBMonth2.getSelectedItem().toString()));//le numero du mois 2
				int j=Integer.parseInt(( CBMonth.getSelectedItem().toString()));//le numero du mois 1
				while (j<=i) {
					String D1="01-"+String.valueOf(j)+"-"+CBYear.getSelectedItem().toString();// La date 1
					String D2=DonnerJour(CBYear.getSelectedItem().toString(), String.valueOf(j))+"-"+String.valueOf(j)+"-"+CBYear.getSelectedItem().toString();//la date 2
					//           ^ cette fonction on met en entreer l'annee et elle va retourner le dernierjours de ce mois (pour ausssi differencier netre une anneée ,normale et une bissextile)
					String sql = "SELECT Patient_Name  FROM Consult WHERE Date_consult BETWEEN '"+D1+"' AND '"+D2+"'";
					try {
						prepared=cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
						 int k=0;// le nombre de consultation
						while (resultat.next()) {k++;}
						barChartData.addValue(k,"Nombre de Ptients",DonnerNomMois(j) );//ajouter la donnée
						
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null,e);
					}
				j++;
				}
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Nombre de patients (par mois)", //titre           
				         "Mois", //x            
				         "Nombre de Ptient",  //y         
				         barChartData, // les données           
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Changer Couleurs */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));//changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();//vider le panel
				panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le cPanel
				panelChart.validate();//valider
				}
			}
		});
		btnchart.setBounds(993, 116, 132, 40);
		contentPane.add(btnchart);
		
//afficher le Camembert du sexe des patients/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btncamemSEXE = new JButton("     sexe");
		btncamemSEXE.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btncamemSEXE.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btncamemSEXE.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam.png")));//remetre le bouton de base
			}
		});
		btncamemSEXE.setForeground(new Color(51, 204, 153));
		btncamemSEXE.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btncamemSEXE.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam.png")));
		btncamemSEXE.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int i=0;// le nbombre d'Homme
				int j=0;// le nombre de femme
				String sql = "SELECT sex  FROM Patient WHERE sex = 'Homme'"; //selectionner que les Hommes
				try {
					prepared=cnx.prepareStatement(sql);
					resultat=prepared.executeQuery();
					
					while (resultat.next()) {
						i++;
					}
					
					 sql = "SELECT sex  FROM Patient WHERE sex = 'Femme'"; //selectionner que les Femmes
					 prepared=cnx.prepareStatement(sql);
						resultat=prepared.executeQuery();
						
						while (resultat.next()) {
							j++;
						}
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e);
				}
				final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset

				Dataset.setValue("Femme ("+j+")",j);//ajouter comme donnée
				Dataset.setValue("Homme ("+i+")", i);//ajouter comme donnée
				 JFreeChart chart = ChartFactory.createPieChart3D( 
				         "Ratio H/F" ,  // chart title                   
				         Dataset ,         // data 
				         true ,                          
				         true, 
				         false);
				      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
				      R.setStartAngle( 270 );    
				      R.setForegroundAlpha( 0.60f );             
				      R.setInteriorGap( 0.02 );    
				      R.setLabelGenerator(null);
				      chart.getTitle().setPaint(new Color(51, 204, 153));
				final ChartPanel cPanel = new ChartPanel(chart);
				panelChart.removeAll();//vider le pannel
				panelChart.add(cPanel,BorderLayout.CENTER);//ajouter le graphique
				panelChart.validate();//Valider
				
			}
		});
		btncamemSEXE.setBounds(165, 573, 144, 40);
		contentPane.add(btncamemSEXE);
		
		
//le field pour ecrire le nom de la maladie///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		txtNomMaladie = new JTextField();
		txtNomMaladie.setBorder(null);
		txtNomMaladie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (txtNomMaladie.getText().toString().equals("Nom de la Maladie")) {
					txtNomMaladie.setText("");
					if (CBYear3.getSelectedIndex()!=0 && CBMonth3.getSelectedIndex()!=0 && !txtNomMaladie.getText().toString().equals("Nom de la Maladie") ) {//tout les champs et les cmbo box on ete modifié
						btnMaladie.setEnabled(true);//activer le bouutton d'affichage du graphique(NBR de patients par maladie(par mois))
					}
				}
			}
		});
		txtNomMaladie.setText("Nom de la Maladie");
		txtNomMaladie.setHorizontalAlignment(SwingConstants.LEFT);
		txtNomMaladie.setForeground(Color.WHITE);
		txtNomMaladie.setFont(new Font("Segoe UI", Font.BOLD, 14));
		txtNomMaladie.setBackground(new Color(51, 204, 153));
		txtNomMaladie.setBounds(59, 470, 154, 33);
		contentPane.add(txtNomMaladie);
//afficher le Camembert par rapport au nombre de patient de ce mois/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		btnMaladie = new JButton("     Maladie");
		btnMaladie.setEnabled(false);
		btnMaladie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CBYear3.getSelectedIndex()!=0 && CBMonth3.getSelectedIndex()!=0 && !txtNomMaladie.getText().toString().equals("Nom de la Maladie")) {//si tout lechamps et combobox on ete modifier (pas les valeurs initiales)
				cnx = ConnexionDB.CnxDB();
				String ID=null;
				int k=0;//le nombre de patients non atteint
				int J=0;// le nombre de patients atteint par la maladie
				int i= Integer.parseInt(( CBMonth3.getSelectedItem().toString()));// le numero du mois
				boolean trouv=false; //pour  savoir si il est touché" ou pas (false n'est pas touché)
				Vector <String> IDs =new Vector<String>();// creer un vecteur pour stocker les ID si ce patient est deja traité on va pas le refaire deux fois
				
				String D1="01-"+String.valueOf(i)+"-"+CBYear3.getSelectedItem().toString();// La date 1
				String D2=DonnerJour(CBYear3.getSelectedItem().toString(), String.valueOf(i))+"-"+String.valueOf(i)+"-"+CBYear3.getSelectedItem().toString();//date 2
				String sql1= "SELECT ID_Pat  FROM Consult WHERE Date_consult BETWEEN '"+D1+"' AND '"+D2+"'";
				try {
					prepared1=cnx.prepareStatement(sql1);
					resultat1=prepared1.executeQuery();
					while (resultat1.next()) {
						ID=resultat1.getString("ID_Pat").toString();//recuperer l'ID
						
						if (!IDs.contains(ID)) {//pour ne pas repeter les patients
						String sql = "SELECT M_Diag  FROM Consult WHERE ID_Pat='" +ID+"'and Date_consult BETWEEN '"+D1+"' AND '"+D2+"'";
						try {
							prepared=cnx.prepareStatement(sql);
							resultat=prepared.executeQuery();
							 trouv=false;
							while (resultat.next()) {
								
								 String Maladie1=" ";//Maladie 1
								 String Maladie2=" ";//Maladie 2
								 String Maladie3=" ";//Maladie 3
								 String Maladie4=" ";//Maladie 4
								 String Maladies =resultat.getString("M_Diag");
							// on va decouper la chaine de caractère pour avoir les maladies du patients k
							// les maladies 1,2,3et 4 sont separé par des virgules	 
									 int finM1indice=Maladies.indexOf(",");//l'indice de fin de la maladie 1
									 
									 if (finM1indice != -1) 
									 {
										 Maladie1= Maladies.substring(0 , finM1indice); //retourne Maladie 1
										 
										 String M2=Maladies.substring(finM1indice+1 , Maladies.length());//enlever Maladie 1
										 int finM2indice=M2.indexOf(","); //l'indice de fin de la maladie 2
										 Maladie2=M2.substring(0 , finM2indice);//retourne Maladie 2
										 
										 String M3=M2.substring(finM2indice+1 , M2.length());//enlever Maladie 2
										 int finM3indice=M3.indexOf(","); //l'indice de fin de la maladie 3
										 Maladie3=M3.substring(0 , finM3indice);//retourne Maladie 3

										 String M4=M3.substring(finM3indice+1 , M3.length());//enlever Maladie 3
										 Maladie4=M4;//retourne Maladie 4
									 }
									 String MladieEcrite=txtNomMaladie.getText().toString().toLowerCase();//recuperer le nom de la mamadie ecrit par le medecin dans le texte field
									 
									 if (MladieEcrite.equals(Maladie1.toLowerCase()))
									 	{
										 J ++;trouv=true;break;//icremante
									 	}
									else 
										{ 
											if (MladieEcrite.equals(Maladie2.toLowerCase())) 
											{
												J++;trouv=true;break;
											}
											else
											{
												if (MladieEcrite.equals(Maladie3.toLowerCase())) 
												{
													J ++;trouv=true;break;
												}
												else
												{
													if (MladieEcrite.equals(Maladie4.toLowerCase())) 
													{
														J ++;trouv=true;break;
													}
													
												}
											}
										
										}
							} 
						//trouv==false le patient n'est pas touchéé par cette maladie dans cette periode 
						if (!trouv) {k++;}
							
						} catch (SQLException eve) {
							JOptionPane.showMessageDialog(null,eve);
						}
					
					}
						IDs.add(ID);//ajouter le ID dans le vecteur donc le patient sera deja traité
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,e1);
				}
					final DefaultPieDataset Dataset = new DefaultPieDataset();//le dataset
					Dataset.setValue("Nombre de patients atteint ("+J+")",J);//donnée 1
					Dataset.setValue("Nombre de patients non atteint ("+k+")",k);//donnée 2
					 JFreeChart chart = ChartFactory.createPieChart3D( 
					         "Ratio  (Patients atteint/Patients Non atteint) pour le mois de "+DonnerNomMois(i) , //titre              
					         Dataset ,         // data 
					         true ,                          
					         true, 
					         false);
					 //effet 3D
					      final PiePlot3D R = ( PiePlot3D ) chart.getPlot( );             
					      R.setStartAngle( 270 );    
					      R.setForegroundAlpha( 0.60f );             
					      R.setInteriorGap( 0.02 );    
					      R.setLabelGenerator(null);
					      chart.getTitle().setPaint(new Color(51, 204, 153));
					final ChartPanel cPanel = new ChartPanel(chart);
					panelChart.removeAll();
					panelChart.add(cPanel,BorderLayout.CENTER);
					panelChart.validate();
			}
			}
		});
		btnMaladie.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnMaladie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnMaladie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam.png")));//selectionner que les Femmes
			}
		});
		btnMaladie.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/cam.png")));
		btnMaladie.setForeground(new Color(51, 204, 153));
		btnMaladie.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnMaladie.setBounds(250, 467, 144, 40);
		contentPane.add(btnMaladie);
		
//afficher les batons du nombre des patients (consultation) pâr periode(semaine)/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		btnchartSem = new JButton("      chart");
		btnchartSem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnchartSem.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot selected.png")));//changer les couleurs button
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				btnchartSem.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot.png")));//selectionner que les Femmes
			}
		});
		btnchartSem.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/plot.png")));
		btnchartSem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cnx = ConnexionDB.CnxDB();
				if (CBYear4.getSelectedIndex()!=0 && CBMonth4.getSelectedIndex()!=0 && CBNumSemaine.getSelectedIndex()!=0) {//si tout lechamps et combobox on ete modifier (pas les valeurs initiales)
				DefaultCategoryDataset barChartData=new DefaultCategoryDataset(); // le dataset
				
				int i= Integer.parseInt(( CBMonth4.getSelectedItem().toString()));// le numero du mois
				int j=0;//pour le jour de la fin de la smaine 
				int l=0;//pour lejour du  debut de la semaine
					if (CBNumSemaine.getSelectedIndex()==1) //delimter la premiere semaine
					{		l=01;
							j=07;
					}
					else 
					{ 
						if (CBNumSemaine.getSelectedIndex()==2) //delimter la 2eme semaine
								{
									l=8;
									j=14;
								}
						else 
								{
									if (CBNumSemaine.getSelectedIndex()==3) //delimter la 3 eme semaine
										{
												l=15;
												j=21;
										}
									else 
										{if (CBNumSemaine.getSelectedIndex()==4) {//delimter la 4 eme semaine
												l=22;
												j=Integer.parseInt(DonnerJour(CBYear4.getSelectedItem().toString(), String.valueOf(i)));  //le dernier jours du mois
										}
										}
							
								}
						
					}
					while (l<=j) {
						String  D1=l+"-"+String.valueOf(i)+"-"+CBYear4.getSelectedItem().toString();//date
						 
						 String sql = "SELECT Patient_Name  FROM Consult WHERE Date_consult='"+D1+"'";
							try {
								prepared=cnx.prepareStatement(sql);
								resultat=prepared.executeQuery();
								 int k=0;
								while (resultat.next()) {;k++;}
								barChartData.addValue(k,"Nombre de consultations",l+" "+DonnerNomMois(i));//ajouter la donnée
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(null,e);
							}
							l++;
					}
					
				 JFreeChart barChart = ChartFactory.createBarChart3D(
				         "Nombre de consultations (par semaine) "+DonnerNomMois(i), //titre           
				         "Jours",    //x         
				         "Nombre de consultations",   //y        
				         barChartData,            
				         PlotOrientation.VERTICAL,             
				         false, true, false);
				 CategoryPlot plot = barChart.getCategoryPlot();

					/* Change Bar colors */
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setSeriesPaint(0, new Color(51, 204, 153));
					renderer.setDrawBarOutline(false);
					renderer.setItemMargin(0);

				 barChart.getTitle().setPaint(new Color(51, 204, 153));// changer la couleurs du titre
				 final ChartPanel cPanel = new ChartPanel(barChart);       
				panelChart.removeAll();
				panelChart.add(cPanel,BorderLayout.CENTER);
				panelChart.validate();
			}
			}
		});
		btnchartSem.setForeground(new Color(51, 204, 153));
		btnchartSem.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnchartSem.setEnabled(false);
		btnchartSem.setBounds(165, 304, 132, 40);
		contentPane.add(btnchartSem);
		
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
		btnHome.setBounds(1082, 5, 32, 32);
		contentPane.add(btnHome);
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(StatistiqueFrame.class.getResource("/Stat_img/Statistique.png")));
		lblNewLabel.setBounds(0, 0, 1200, 650);
		contentPane.add(lblNewLabel);
	}
	
//methode pour reactiver les btns charts et calculer//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void ActiverbtnsparMois(JComboBox<Object> cBMonth3,JComboBox<Object> cBMonth22,JComboBox<Object> cBYear2)
	{ 
		if (cBMonth3.getSelectedIndex()!=0 && cBMonth22.getSelectedIndex()!=0 && cBYear2.getSelectedIndex()!=0) {
			btncalcperiode.setEnabled(true);
			btnchart.setEnabled(true);
		}
		
	}
	
//on donne l annee et elle va renvoyer le  derniers jours (numero) de ce mois //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public String DonnerJour(String Year,String Month)
		{
			String value = null;
			switch(Month) 
			{
			case "01"  :
			case "03":
			case "05":
			case "07":
			case "08":
			case "10":
			case "12":
				value="31";
				break;
				
			case "02":
				if(  (Integer.parseInt(Year) % 400 )==0) 
				{
					 value="28";
				}
				else {
					value="29";
				}
				
				
				break;
				
			
			case "04":
			case "06":
			case "09":
			case "11":
				
				value="30";	
				
				break;
				
			
		case "1"  :
		case "3":
		case "5":
		case "7":
		case "8":
			value="31";
			break;
			
		case "2":
			if(  (Integer.parseInt(Year) % 400 )==0) 
			{
				 value="29";
			}
			else {
				value="28";
			}
			
			
			break;
			
		
		case "4":
		case "6":
		case "9":
			value="30";	
			
			break;
			
		}
				
			return value;
		}
//on donne un  mois et elle va renvoyé le nom  du mois ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public String DonnerNomMois(int Month)
		{
			String value = null;
			switch(Month) {
		case 1:
			value="Janv";
			break;
		case 3:
			value="Mars";
			break;
		case 5:
			value="Mai";
			break;
		case 7:
			value="Juill";
			break;
		case 8:
			value="Août";
			break;
		case 2:
			value="Févr";
			break;
		case 4:
			value="Avr";
			break;
		case 6:
			value="Juin";
			break;
		case 9:
			value="Sept";
			break;
		case 10:
			value="Octs";
			break;
		case 11:
			value="Nov";
			break;
		case 12:
			value="Déc";
			break;
		}
			return value;
		}
}
