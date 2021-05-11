import java.util.EventObject;

public class PapotageEvent extends EventObject{
	
	
	private String sujet;
	private String corps;
	private String auteur;
	
	public PapotageEvent(Bavards source, String sujet, String corps) {
		this(source);
		this.auteur = source.getNom();
		this.sujet = sujet;
		this.corps = corps;
	}

	public PapotageEvent(Bavards source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getCorps() {
		return corps;
	}

	public void setCorps(String corps) {
		this.corps = corps;
	}

	@Override
	public String toString() {
		return " Auteur :" + this.auteur +"\n" + " Sujet : " + this.sujet + "\n" + " Contenu : " + this.corps + "\n";
	}
	
	
}
