import java.util.ArrayList;
import java.util.Objects;

import javax.swing.event.EventListenerList;

public class Concierge {
	private final EventListenerList listeBavards = new EventListenerList();
	private ArrayList<Bavards> listeBavardsCo = new ArrayList<Bavards>();
	private String nomConcierge;
	private FenetreConcierge fc;
	ArrayList<String> listeNoms = new ArrayList<String>();
	private static Concierge con;
	
	/**
	 * 
	 * Utilisation du patron de conception Singleton pour s'assurer que l'on ne crée qu'une seule instance de Concierge.
	 * 
	 * */
	private Concierge(String nomConcierge) {
		this.nomConcierge = nomConcierge;
		this.fc = new FenetreConcierge(this);
	}
	
	public static Concierge creerConcierge(String nom) {
		if(Objects.isNull(con)) {
			con = new Concierge(nom);
		}
		return con;
	}
	
	/**
	 * 
	 * Dès que l'on crée un bavard celui-ci se connecte automatiquement 
	 * 
	 * */
	public void ajouterBavard(Bavards b) {
		this.listeBavards.add(Bavards.class, b);
		listeNoms.add(b.getNom());
		b.creerFe(this);
		this.connecterBavards(b);
		
	}
	
	public void retirerBavard(Bavards b) {
		this.listeBavards.remove(Bavards.class, b);
	}
	
	public Bavards creerBavards(String nom) {
		/**
		 * 
		 * Afin de bien identifier chaque bavard, on interdit pour le moment les doubles-noms, ci un nom est déjà pris on ajoute un nombre derrière. Ceci est géré par la méthode pseudoUnique
		 * */
		String nomFinal = pseudoUnique(nom);
		return new Bavards(nomFinal);
	}
	
	public String pseudoUnique(String nom) {
		boolean pseudoUnique = false;
		String pseudoFinal = nom;
		
		int i=2;
		while(!pseudoUnique) {
			if(listeNoms.contains(pseudoFinal)) {
				pseudoFinal = nom + i;
				i++;
			}
			else {
				pseudoUnique = true;
			}
		}
		return pseudoFinal;
	}
	
	public void connecterBavards(Bavards b) {
		listeBavardsCo.add(b);
		this.fc.inscrireBavard(b);
		/**
		 * 
		 * On notifie à tous les bavards la connexion d'un nouveau membre !
		 * */
		
		for(Bavards bav : listeBavardsCo) {
			bav.connexionAutre(b);
		}
		
	}
	
	public void deconnecterBavards(Bavards b) {
		listeBavardsCo.remove(b);
		this.fc.desinscrireBavards(b);
		for(Bavards bav : listeBavardsCo) {
			bav.deconnexionAutre(b);
		}
	}
	
	public ArrayList<Bavards> getBavarsCo() {
		return this.listeBavardsCo;
	}
	
	public void nouveauMessage(PapotageEvent pe) {
		this.fc.messageRecu(pe);
		/*for(Bavards b : this.listeBavards.getListeners(Bavards.class)) {
			b.eventReceptionMessage(pe);
			b.getFe().messageRecu(pe);
		}*/
		
		
		//Seul les bavards connectés reçoivent les messages. Procédure normale, c'est à dire qu'on envoie un message à tout le monde
		//Si le message contient /chuchoter en premier lieu on va chuchoter le pseudo qui arrive en 2e dans le split
		
		String[] messageSplit = pe.getCorps().split(" ");
		System.out.println(messageSplit[0]);
		if(messageSplit[0].equalsIgnoreCase("/chuchoter")) {
			System.out.println("je suis là !");
			//Si le pseudo est connecté, on envoie le message
			for(Bavards b : listeBavardsCo) {
				if(b.getNom().equalsIgnoreCase(messageSplit[1]) || b.equals(pe.getSource())) {
					pe.setSujet("[MESSAGE PRIVE] - " + pe.getSujet());
					/**
					 * On retire le /chuchoter et le pseudo du message
					 * */
					String mp = "";
					for(int i = 2; i<messageSplit.length; i++) {
						mp += " " + messageSplit[i];
					}
					pe.setCorps(mp);
					b.eventReceptionMessage(pe);
					b.getFe().messageRecu(pe);
				}
			}
		
		}
		else {
			for(Bavards b : listeBavardsCo) {
				b.eventReceptionMessage(pe);
				b.getFe().messageRecu(pe);
			}
		}	
	}
	
	
	public String getNom() {
		return this.nomConcierge;
	}

}
