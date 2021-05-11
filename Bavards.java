
public class Bavards implements PapotageListener{
	
	private String nom;
	private FenetreBavard fe;
	
	public Bavards(String nom) {
		this.nom = nom;
	}

	public void creerFe(Concierge c) {
		this.fe = new FenetreBavard(this, c);
		//this.envoyerMessage("Hey ! Je viens d'arriver dans la place !", "Un nouvel arrivant", c);
	}
	
	
	public FenetreBavard getFe() {
		return fe;
	}

	@Override
	public void eventReceptionMessage(PapotageEvent pe) {
		// TODO Auto-generated method stub
		System.out.println("Je suis " + this.nom + " je viens de recevoir le message " + pe.toString());
	}
	
	
	public void envoyerMessage(String corps, String sujet, Concierge g) {
		PapotageEvent pe = new PapotageEvent(this, sujet, corps);
		g.nouveauMessage(pe);
	}

	public String getNom() {
		return nom;
	}
	
	
	/**
	 * 
	 * Lorsqu'on reçoit la connexion d'un nouveau membre !
	 * 
	 * */
	public void connexionAutre(Bavards b) {
		this.fe.connexion(b);
	}
	
	public void deconnexionAutre(Bavards b) {
		this.fe.deconnexion(b);
	}
	

}
