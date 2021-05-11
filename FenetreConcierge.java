import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FenetreConcierge extends JFrame implements ActionListener{
	
	private JTextField inscriptionBavard = new JTextField("Quel pseudo pour votre bavards ?");
	private JButton boutonInscription = new JButton("Inscription !");
	private JButton deconnecterBavard = new JButton("Déconnecter");
	private Concierge c;
	DefaultListModel listeBavardsCo = new DefaultListModel();
	JList liste = new JList(listeBavardsCo);
	private JTextArea messageEnvoyes = new JTextArea();
	

	
	public FenetreConcierge(Concierge c) {
		this.c = c;
		this.setSize(800, 800);
	    this.setLocationRelativeTo(null);  
	    this.setTitle("Fenêtre d'administration du concierge " + c.getNom());
	    this.setLayout(new GridLayout(8,1,5,5));
	    
	    /**
	     * 
	     * Inscription d'un bavard
	     * 
	     * */
	    JPanel nord = new JPanel();
	    nord.setLayout(new GridLayout(1,2,10,10));
	    nord.add(inscriptionBavard);
	    nord.add(boutonInscription);
	    this.getContentPane().add(nord);
	    boutonInscription.addActionListener(this);
	    
	    /**
	     * 
	     * affichage Bavards connectés et gestion de leur déconnexion par le concierge pour la modération.
	     * 
	     * */
	    
	    JPanel lesBavards = new JPanel();
	    lesBavards.setLayout(new GridLayout(1,2,10,10));
	    
	    JScrollPane center = new JScrollPane(liste);
	    listeBavardsCo.addElement("Listes des bavards connectés");
	    
	    lesBavards.add(center);
	    lesBavards.add(deconnecterBavard);
	    this.getContentPane().add(lesBavards);
	    
	    deconnecterBavard.addActionListener(this);
	    
	    
	    
	    /**
	     * 
	     * Affichage des messages qui sont envoyés
	     * 
	     * */
	    messageEnvoyes.setLineWrap(true);
	    JScrollPane lePanoPane = new JScrollPane(messageEnvoyes);
	    this.getContentPane().add(lePanoPane);
	    
	    
	   
	    
	    
	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == boutonInscription) {
			Bavards b = c.creerBavards(inscriptionBavard.getText());
			c.ajouterBavard(b);
		}
		/**
		 * 
		 * Si on appuie sur le bouton de déconnexion, on vérifie qu'un élement de la liste est sélectionné. Si c'est le cas, on déconnecte le bavard.
		 * Ici, il y a une erreur un peu bizarre => lorsqu'on on déconnecte le dernier élément de la liste visiblement le programme essaie de faire une boucle de plus
		 * ce qui crée une erreur d'itération, TODO à corriger !
		 * 
		 * */
		if(e.getSource() == deconnecterBavard) {
			if(!this.liste.isSelectionEmpty()) {
				for(Bavards b : this.c.getBavarsCo()) {
					if(b.getNom().equalsIgnoreCase((String)this.liste.getSelectedValue())) {
						this.c.deconnecterBavards(b);
					}
				}
			}
		}
		
	}
	
	public void inscrireBavard(Bavards b) {
		listeBavardsCo.addElement(b.getNom());
		/**
		 * 
		 * Notification de connexion d'un nouveau Bavard, d'abord on le met dans le chat du Concierge, puis on envoie un message à tout le monde en précisant qu'il vient du concierge... 
		 * 
		 * */
		messageEnvoyes.setText(messageEnvoyes.getText() + "\n" + b.getNom() + " vient de rejoindre l'arène, bienvenu à lui !");
		
	}
	
	public void desinscrireBavards(Bavards b) {
		//System.out.println("Je dois supprimer" + b.getNom());
		listeBavardsCo.removeElement(b.getNom());
	}
	
	public void messageRecu(PapotageEvent pe) {
		this.messageEnvoyes.setText(this.messageEnvoyes.getText() + "\n" + pe.toString());
	}
	

}
