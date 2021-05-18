import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 
public class FenetreBavard extends JFrame implements ActionListener{
	
	private JTextField textfield = new JTextField("C'est � quel propos ?");
	private JButton bouton = new JButton("Envoyer message ");
    private JButton boutonDeco = new JButton("D�connexion");
    private JButton messagePrive = new JButton("Chuchoter");
	private JTextArea message = new JTextArea("Saisir votre message...");
	private JTextArea listeMessage = new JTextArea("Bienvenu sur Ebavards !",4,10);
	DefaultListModel listeBavardsCo = new DefaultListModel();
	JList liste = new JList(listeBavardsCo);
	
	private Concierge c;
	private Bavards b;
	
	
	
  public FenetreBavard(Bavards nomBavard, Concierge c){
	this.b = nomBavard;
    this.c = c;
    this.setTitle("Chat de : " + nomBavard.getNom());
    this.setSize(700, 500);
    this.setLocationRelativeTo(null);  
    this.setLayout(new BorderLayout());

    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();
    Color randomColor = new Color(r, g, b);
    this.getContentPane().setBackground(randomColor);
    
    /**
     * 
     * Zone sud
     * 
     * */
    JPanel sudeuh = new JPanel();
    
    sudeuh.add(bouton);
    sudeuh.add(boutonDeco);
    sudeuh.add(messagePrive);
    this.getContentPane().add(sudeuh, BorderLayout.SOUTH);
    
    /**
     * 
     * Zone nord
     * 
     * */
    JPanel nord = new JPanel();
    
    nord.add(textfield);
    this.getContentPane().add(nord, BorderLayout.NORTH);
    
    /**
     * 
     * Zone centrale
     * 
     * */
    JPanel centre = new JPanel();
    listeMessage.setLineWrap(true); //Retour � la ligne quand on d�passe la zone de texte
    listeMessage.setEditable(false); //Impossibilit� d'�diter le texte
    
    JScrollPane lePanoListeMessage = new JScrollPane(listeMessage);
    message.setLineWrap(true);
    
    JScrollPane lePanoEcritureMessage = new JScrollPane(message);
    centre.setBackground(randomColor);
    centre.setLayout(new GridLayout(2,1,0,15));
    centre.add(lePanoListeMessage);
    centre.add(lePanoEcritureMessage);
    this.getContentPane().add(centre, BorderLayout.CENTER);
    
    
    /**
     * 
     * Affiche les bavards connect�s sauf moi m�me, utile pour le syst�me de messagerie priv�e.
     * */
    
    listeBavardsCo.addElement("Listes des bavards connect�s");
    for(Bavards bava : this.c.getBavarsCo()) {
    	if(bava.getNom() != this.b.getNom()) {
    		listeBavardsCo.addElement(bava.getNom());
    	}
    	
    }
    
    JScrollPane center = new JScrollPane(liste);
    this.getContentPane().add(center, BorderLayout.EAST);
   
    bouton.addActionListener(this);
    boutonDeco.addActionListener(this);
    messagePrive.addActionListener(this);
    
    //new OnLineBavardEvent(this.b, c);
    this.setVisible(true);
  }
  


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		
		/**
		 * 
		 * Si on appuie sur envoyer message, le message s'envoie on v�rifie cependant que le message n'est pas vide
		 * 
		 * */
		if(e.getSource() == bouton) {
			String texte = message.getText();
			String objet = textfield.getText();
			if(!texte.trim().equals("") && !objet.trim().equals("")) {
				this.b.envoyerMessage(texte, objet, this.c);
				this.liste.clearSelection();
				this.message.setText("");
			}
			
		}
		
			
		/**
		 * 
		 * Si on appuie sur d�connecter on fait la fonction processWindowEvent
		 * 
		 * */	
		if(e.getSource() == boutonDeco) {
			if(boutonDeco.getText() == "D�connexion") {
				this.seDeconnecter();
			}
			else if(boutonDeco.getText() == "Connexion") {
				this.seConnecter();
			}
			
		}
		
		/**
		 * 
		 * Si un des membres connect� est selectionn�, pr�-r�dige le /chuchoter pseudo qui permet d'envoyer un message priv� et d�selectionne le membre
		 * */
		if(e.getSource() == messagePrive) {
		    if(!this.liste.isSelectionEmpty()) {
		    	this.message.setText("/chuchoter " + (String)this.liste.getSelectedValue());
		    	
		    }
		}
	}
	
	public void seDeconnecter() {
		this.c.deconnecterBavards(this.b);
		
		/**
		 * 
		 * Un bavard d�connecter implique une zone de saisi d�sactiv� et �galement le fait qu'il ne re�oive plus les messages on g�re donc �a ici.
		 * Il garde sa fen�tre ouverte le temps qu'il est deconnect�, ce qui permet qu'ensuite il puisse se reconnecter. 
		 * 
		 * */
		
		this.message.setEditable(false);
		this.boutonDeco.setText("Connexion");
		this.listeBavardsCo.clear();
		
		bouton.setVisible(false);
		//this.dispose();
	}
	
	@Override
	protected void processWindowEvent(WindowEvent we) {
		if(we.WINDOW_CLOSING == we.getID()) {
			this.c.deconnecterBavards(this.b);
			this.dispose();
		}

	}
	
	public void messageRecu(PapotageEvent pe) {
		
		//On v�rifie que le Bavards est connect�, s'il est on met � jour les messages re�us, sinon on ne le mets pas � jour
		if(this.c.getBavarsCo().contains(this.b)) {
			this.listeMessage.setText(this.listeMessage.getText() + "\n" + pe.toString());
		}
		
	}
	
	private void seConnecter() {
		this.c.connecterBavards(this.b);
		
		/**
		 * 
		 * On vient de se reconnecter, donc on rend de nouveau �ditable la zone de texte; On r�cup�re la liste des connect�s
		 * 
		 * */
		this.message.setEditable(true);
		this.boutonDeco.setText("D�connexion");
		
		listeBavardsCo.addElement("Listes des bavards connect�s");
		for(Bavards bava : this.c.getBavarsCo()) {
		    if(bava.getNom() != this.b.getNom()) {
		    	this.listeBavardsCo.addElement(bava.getNom());
		    }   	
		}
		
		/**
		 * 
		 * On rend visible le bouton d'envoie
		 * 
		 * */
		bouton.setVisible(true);
		
	}
	/**
	 * 
	 * M�thode qui re�oit les nouvelles connexions
	 * 
	 * */
	public void connexion(Bavards ba) {
		ArrayList<String> disquetteConnexion = new ArrayList<String>();
		
		disquetteConnexion.add(" vient d'arriver dans le serveur fa�tes lui de la place !");
		disquetteConnexion.add(" est dans la zone, �a va fumer !");
		disquetteConnexion.add(" d�barque comme en Normandie");
		disquetteConnexion.add(" arrive, que tout le monde danse !");
		int index = (int) (Math.random() * (disquetteConnexion.size() - 1));
		this.listeMessage.setText(this.listeMessage.getText() + "\n" + ba.getNom() + disquetteConnexion.get(index));
		if(ba.getNom() != this.b.getNom()) {
    		listeBavardsCo.addElement(ba.getNom());
    	}
	}
	
	/**
	 * 
	 * M�thode qui re�oit les notifications de d�connexion des Bavards
	 * 
	 * */
	public void deconnexion(Bavards b) {
		this.listeMessage.setText(this.listeMessage.getText() + "\n" + b.getNom() + " est parti, quel dommage, �a allait devenir int�ressant !");
		this.listeBavardsCo.removeElement(b.getNom());
	}
}
