////////////////////////////////////////////////////////////////////////////////-----------Class Personne------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public abstract  class Personne {
	
	private String Nom; //Nom de la personne
	 private String Prenom;
	 private String DateN;
	 private String  sex;
	 private String ADR;
	 private String ADRmail;
	private String Num;
	 

	
	 public Personne(String nom, String prenom, String dateN, String sex, String aDR, String aDRmail, String num) {
		super();
		Nom = nom;
		Prenom = prenom;
		DateN = dateN;
		this.sex = sex;
		ADR = aDR;
		ADRmail = aDRmail;
		Num = num;
	}
	 
	 public void Ajouter() {
			// TODO Auto-generated method stub
			
		}

	public void MetreAjour() {
			// TODO Auto-generated method stub
			
		}
	public void Suprimer() {
			// TODO Auto-generated method stub
			
		}
		
//getters and setters	 
	public String getADRmail() {
		return ADRmail;
	}

	public void setADRmail(String aDRmail) {
		ADRmail = aDRmail;
	}

	public Personne()
	{
		
	}
	
	
	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}

	public String getDateN() {
		return DateN;
	}

	public void setDateN(String dateN) {
		DateN = dateN;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getADR() {
		return ADR;
	}

	public void setADR(String aDR) {
		ADR = aDR;
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = num;
	}

	
	
	

}
