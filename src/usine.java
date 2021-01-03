import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class usine {
	
	private int nb_Lignes;
	private int nb_Magasins;
	private int couts[][];
	private int demande[];
	private int offre[];
	private int Tab_Sol[][];
	
	public usine() {
		
	}
	
	
		//cout total :
	
	
	public void Cout_total() {
		int Cout_total=0;
		
		for (int i=0; i<nb_Magasins;i++) {
			
			for (int j=0; j<nb_Lignes;j++) {
				
				if (Tab_Sol[i][j]!=0) {
					Cout_total+= couts[i][j]*Tab_Sol[i][j];
				}
			}

		}
		System.out.println("Le cout total est : "+ Cout_total);
		}
	
	
		//niveau 0 : recuperation des donnees
	
	
	public void lectureFichier(String fichiertxt) {
		
		FileReader fr;
		BufferedReader br;
		
		try {
			
			String ligne;
			
			fr = new FileReader(fichiertxt);			// recup du fichier txt
			br = new BufferedReader(fr);				// construire un objet de ce type pour pouvoir le lire apres
			
			
			// Premiere ligne du txt : def des attributs nb_Magasins et nb_Lignes
			ligne = br.readLine();
			String Liste1[] = ligne.split(" ");			
			nb_Lignes = Integer.parseInt(Liste1[0]);
			nb_Magasins = Integer.parseInt(Liste1[1]);
			
			// creation des tableaux en fonction du nombre des lignes et magasins
			couts = new int[nb_Magasins][nb_Lignes];
			demande = new int[nb_Lignes];
			offre = new int[nb_Magasins];
			
			// Lignes du txt du cout : def de l'attribut cout
			for (int i=0; i<nb_Magasins; i++) {
					
				ligne = br.readLine();
				String Liste2[] = ligne.split(" ");
				
				for (int j=0; j<nb_Lignes; j++) {
							
					couts[i][j] = Integer.parseInt(Liste2[j]);
								
				}
			}
			
			// Ligne demande du txt : def de l'attribut demande
			ligne = br.readLine();
			String Liste3[] = ligne.split(" ");
			for (int i=0; i<nb_Lignes; i++) {
			
				demande[i] = Integer.parseInt(Liste3[i]);
			}
			
			// Ligne offre du txt : def de l'attribut offre
			ligne = br.readLine();
			String Liste4[] = ligne.split(" ");
			for (int i=0; i<nb_Magasins; i++) {
				
				offre[i] = Integer.parseInt(Liste4[i]);
			}
			
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}

	public void afficher() {
		
		System.out.println("Nombre de lignes : " + nb_Lignes + "\n");

		System.out.println("Nombre de magasins : " + nb_Magasins + "\n");
		
		System.out.println("Les couts sont: ");
		for(int k = 0; k < nb_Magasins; k++){ 
			
		  for(int l = 0; l < nb_Lignes; l++){
			  
		    System.out.print(couts[k][l]+" ");    
		    
		  }
		  System.out.println();     
		}
		
		System.out.println();
		System.out.println("La demande est: ");
		for(int k = 0; k < nb_Lignes; k++){
			
			System.out.print(demande[k]+" ");       
			   
			}
		System.out.println();
		
		System.out.println();
		System.out.println("L'offre est: ");
		for(int k = 0; k < nb_Magasins; k++){	
			System.out.print(offre[k]+" ");          
		}
		System.out.println();
	}
	
	
	
		//niveau 1 : ballas-hammer
	
	// methode ballas-hammer, retourne le tableau solution
	public void ballas_hammer() {
		
		boolean stop_iteration = stop();
		
		int quantite_deplacee;
		int []coordonnees_solution;
		Tab_Sol = new int[nb_Magasins][nb_Lignes];
		
		// on copie les couts, l'offre et la demande
		int [][]coutsOriginaux = copie(couts);
		int []demandeOriginale = copie(demande);
		int []offreOriginale = copie(offre);
		
		
		while (stop_iteration == false) {
			
			// calcul regrets, calul regret max, trouve les coordonees du cout minimum dans le regret maximum
			coordonnees_solution = coordonnees_solution();

			// trouve le cout qui doit etre mis dans le tableau solution, remplace la ligne ou colonne avec offre ou demande pleine par -1
			quantite_deplacee = quantite_deplacee(coordonnees_solution);
			
			// rempli le tableau solution
			Tab_Sol[coordonnees_solution[0]][coordonnees_solution[1]] = quantite_deplacee;
			
			//on regarde si on peut faire une autre iteration
			stop_iteration = stop();
		
		}
		
		// on reinitialise les couts d'origine
		couts  = copie(coutsOriginaux);
		demande = copie(demandeOriginale);
		offre = copie(offreOriginale);
		
		System.out.println();
		System.out.println("Le tableau solution est :");
		for(int k = 0; k < nb_Magasins; k++){ 
		  for(int l = 0; l < nb_Lignes; l++){
		    System.out.print(Tab_Sol[k][l]+" ");    
		  	}
		  System.out.println();
		}
		System.out.println();
	}
	
	
	
		// fonctions utiles pour ballas hammer
	
	
	// copie un tableau de n elements
	public int[][] copie(int [][]tab){
		
		int[][]copie = new int [nb_Magasins][nb_Lignes];
		for(int k = 0; k < nb_Magasins; k++){ 
			  for(int l = 0; l < nb_Lignes; l++){
				  copie[k][l] = tab[k][l];
			  	}
			}
		return copie;
	}
	
	public int[] copie(int []tab){
		
		int taille = tab.length;
		int[]copie = new int [taille];
		for(int k = 0; k < taille; k++){ 
			copie[k] = tab[k];
		}
		return copie;
	}
	
	//fonction retournant le nombre d'occurence d'un élément dans une liste 	
	public int Nbre_occurence(int[]liste,int n) {    
			int compteur=0;
			for (int i =0;i<liste.length;i++) {
				if (liste[i]==n) {
					compteur+=1;
				}
				
			}
			return compteur;
			
		}

	// fonction retourant une liste avec le min ou le max et son index dans une liste
	// utile pour les calculs de regrets ou comparer offre/demande
	public int[] Min_Index_liste(int[] liste) {
		
		// on verifie que le premier min n'est pas -1
		
		for (int j = 0; j<liste.length; j++) {
			if (liste[j]<0) {
				liste[j] = Max_Index_liste(liste)[0]+1;
			}
		}
		
		int min = liste[0];
		int index = 0;
		for (int i = 0; i < liste.length; i++) {
			if (liste[i] < min)  {
				min = liste[i];
				index = i;
			}
		}
		int[] MinIndex = { min, index };
		return MinIndex;
	}

	public int[] Max_Index_liste(int[] liste) {
		int max = liste[0];
		int index = 0;
		for (int i = 0; i < liste.length; i++) {
			if (liste[i] > max) {
				max = liste[i];
				index = i;
			}
		}
		int[] MaxIndex = { max, index };
		return MaxIndex;
	}

	public int[] Deux_Mins_liste(int[] liste) {
		
		int min1[] = Min_Index_liste(liste);
		int liste2[] = new int[liste.length - 1];
		System.arraycopy(liste, 0, liste2, 0, min1[1]);
		System.arraycopy(liste, min1[1] + 1, liste2, min1[1], liste.length - min1[1] - 1);
		int min2[] = Min_Index_liste(liste2);
		int tab_min[] = { min1[0], min2[0] };
		return tab_min;

	}
	
	//fonction retournant la différence des deux minimums dans une liste (ligne ou colonne)
	public int DC(int[][] Tab, int k) {
		int DC = 0;

		int colonne[] = new int[nb_Magasins];

		for (int i = 0; i < nb_Magasins; i++) {
			colonne[i] = Tab[i][k];
		}
		int nbre1 = Nbre_occurence(colonne, -1);
		
		if (nbre1 == nb_Magasins) {
			DC = -1;
		} else if (nbre1 == nb_Magasins - 1) {
			DC = Max_Index_liste(colonne)[0];
		} else {
			int compteur = 0;
			
			int colonnebis[] = new int[nb_Magasins - nbre1];
			for (int j = 0; j < nb_Magasins; j++) {

				if (colonne[j] != -1) {

					colonnebis[compteur] = colonne[j];
					compteur += 1;
				}
			}
			
			int deuxmins[] = Deux_Mins_liste(colonnebis);
			
			if (deuxmins[0] < deuxmins[1]) {
				DC = deuxmins[1] - deuxmins[0];
			} else {
				DC = deuxmins[0] - deuxmins[1];
			}

		}
		return DC;
	}

	public int DL(int[][] Tab, int k) {

		int ligne[] = new int[nb_Lignes];
		int DL;
		for (int i = 0; i < nb_Lignes; i++) {
			ligne[i] = Tab[k][i];
		}
		int nbre1 = Nbre_occurence(ligne, -1);
		
		if (nbre1 == nb_Lignes) {
			DL = -1;
		} else if (nbre1 == nb_Lignes - 1) {
			DL = Max_Index_liste(ligne)[0];
		} else {
			int compteur = 0;
			
			int lignebis[] = new int[nb_Lignes - nbre1];
			for (int j = 0; j < nb_Lignes; j++) {

				if (ligne[j] != -1) {

					lignebis[compteur] = ligne[j];
					compteur += 1;
				}
			}
			
			int deuxmins[] = Deux_Mins_liste(lignebis);
			
			if (deuxmins[0] < deuxmins[1]) {
				DL = deuxmins[1] - deuxmins[0];
			} else {
				DL = deuxmins[0] - deuxmins[1];
			}
		}

		return DL;

	}

	// on met tous les regrets dans deux listes, une pour les DC l'autre pour les DL
	// on regarde quel regret est max parmis les deux listes confondues et on le choisi
	// on retourne un couple pour le regret max : [ligne(0) ou colonne(1) , index dans la liste]
	public int[] Regret_max() { 
		
		int[] ToutDL = new int[nb_Magasins];
		int[] ToutDC = new int[nb_Lignes];
		int[] choix_regret = new int[2];

		for (int i = 0; i < nb_Lignes; i++) {

			ToutDC[i] = DC(couts, i);
		}
		
		for (int i = 0; i < nb_Magasins; i++) {

			ToutDL[i] = DL(couts, i);
		}
		
		int[] MaxDL = Max_Index_liste(ToutDL);
		int[] MaxDC = Max_Index_liste(ToutDC);
		
		if (MaxDL[0] <= MaxDC[0]) {
			choix_regret[0] = 1;
			choix_regret[1] =  MaxDC[1];
		} 
		
		else if (MaxDL[0] > MaxDC[0]){
			choix_regret[0] = 0;
			choix_regret[1] =  MaxDL[1];
		}
		return choix_regret;
	}


	// on recupere une liste qui indique une ligne ou colonne et l'index d'un regret
	// on retourne les coordonees de la case a rempiir dans le tableau solution [ligne , colonne]
	public int[] coordonnees_solution() { 
		
		int [] coordonnees_solution = new int [2];
		int elements_dans_ligne[] = new int[nb_Lignes];
		int elements_dans_colonne[] = new int[nb_Magasins];
		
		int []choix_regret = Regret_max();
		
		//on se place dans une ligne, on trouve quelle colonne choisir
		if(choix_regret[0] == 0) {
			
			coordonnees_solution[0] = choix_regret[1];
			
			for (int i = 0; i < nb_Lignes; i++) {
				
				elements_dans_ligne[i] = couts[choix_regret[1]][i];
				
			}
			coordonnees_solution[1] = Min_Index_liste(elements_dans_ligne)[1];
		}
		
		//on se place dans une colonne, on trouve quelle ligne choisir
		else if(choix_regret[0] == 1) {
			
			coordonnees_solution[1] = choix_regret[1];
			for (int i = 0; i < nb_Magasins; i++) {
				elements_dans_colonne[i] = couts[i][choix_regret[1]];
				}
			coordonnees_solution[0] = Min_Index_liste(elements_dans_colonne)[1];
			
		}
		return coordonnees_solution;
	}

	// fonction retournant le nombre de marchandises à transporter en fonction de la solution trouvee
	// remplace l'offre ou la demande choisie ou les deux par -1
	public int quantite_deplacee(int[] coordonnees_solution) { 
		
		int quantite_deplacee;

		// cas ou l'offre est rempli, on vide l'offre (-1) et on modifie la demande qui reste
		if (offre[coordonnees_solution[0]] < demande[coordonnees_solution[1]]) {
			quantite_deplacee = offre[coordonnees_solution[0]];
			offre[coordonnees_solution[0]] = -1;
			demande[coordonnees_solution[1]] = demande[coordonnees_solution[1]]-quantite_deplacee;
			
			// on supprime la ligne en mettant des -1
			for (int i = 0; i<nb_Lignes; i++) {
				
				couts[coordonnees_solution[0]][i] = -1;
				
			}
		}
		// cas ou la demande est rempli, on vide la demande et on modifie l'offre qui reste
		else if(offre[coordonnees_solution[0]] > demande[coordonnees_solution[1]]) {
			quantite_deplacee = demande[coordonnees_solution[1]];
			demande[coordonnees_solution[1]] = -1;
			offre[coordonnees_solution[0]] = offre[coordonnees_solution[0]] - quantite_deplacee;
			
			for (int i = 0; i<nb_Magasins; i++) {
				
				couts[i][coordonnees_solution[1]] = -1;
				
			}
		}
		// cas ou l'offre et la demande sont remplis en même temps
		else {
			quantite_deplacee = demande[coordonnees_solution[1]];
			demande[coordonnees_solution[1]] = -1;
			offre[coordonnees_solution[0]] = -1;
			
			for (int i = 0; i<nb_Lignes; i++) {
				
				couts[coordonnees_solution[0]][i] = -1;
				
			}
			
			for (int i = 0; i<nb_Magasins; i++) {
				
				couts[i][coordonnees_solution[1]] = -1;
				
			}
			
		}
		return quantite_deplacee;

	}
	
	// boucle stop pour ballas-hammer
	// permet de savoir quand le programme arrete les iterations de ballas-hammer
	// tant que des valeurs du couts sont differentes de -1 stop est faux, on continuera les iterations
	public boolean stop() {
		
		boolean stop = false;
		int compteur_positifs = 0;
		
		for(int i = 0; i<nb_Magasins; i++) {
			
			for (int j = 0; j<nb_Lignes; j++) {
				
				if (couts[i][j] != -1) {
					
					compteur_positifs+=1;
				}
			}
		}
		
		if (compteur_positifs == 0) {
			stop = true;
		}
		return stop;
	}

}