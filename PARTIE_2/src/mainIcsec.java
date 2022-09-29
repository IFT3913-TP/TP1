import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class mainIcsec {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub


        String cheminDossier = inputDossier();
        String fichierCSV = inputFichier();

        //la liste des fichiers dans le repertoire
        File repertoire = new File(cheminDossier);
        String liste[] = repertoire.list();

        //la list de classe avec leurs CSEC (initialiser)
        ArrayList<CouplageClass> listeClouplage = lectureRepertoire(liste);

        //calcule des couplage
        listeClouplage = Couplage(cheminDossier, liste, listeClouplage);

        //ecrit les resultats dans le csv
        ecrireFichie(cheminDossier,fichierCSV,listeClouplage);

        //afficher le contenue du csv
        lectureFichie(cheminDossier,fichierCSV);

    }

    /**
     * calcule le couplage (CSEC) de la liste dans le chemin cheminDossier et retourne le resultat
     * dans une liste ArrayList<CouplageClass>
     *
     * //@param String cheminDossier: chemin du Dossier
     * 		String [] liste : liste des fichiers du Dossiere
     * 		ArrayList<CouplageClass> listeClouplage : liste des noms de class et couplage.
     *
     * @return ArrayList<CouplageClass>
     */
    public static ArrayList<CouplageClass> Couplage( String cheminDossier ,String [] liste,ArrayList<CouplageClass> listeClouplage) throws IOException {

        if (liste != null) {

            //boucle du dossier
            for (int i = 0; i < liste.length; i++) {
                try {

                    BufferedReader br = new BufferedReader(new FileReader(cheminDossier+"/"+liste[i]));
                    String ligne = null;

                    //boucle du fichier
                    while ((ligne = br.readLine()) != null){

                        String[] data = ligne.split(",");

                        //boucle des ligne
                        for (String val : data){

                            //boucle de occurance du nom des classes
                            for (int j = 0; j < listeClouplage.size(); j++) {

                                if(val.contains(listeClouplage.get(j).nomClasse) && (!liste[i].split("\\.")[0].equals(listeClouplage.get(j).nomClasse))) {
                                    int n = listeClouplage.get(j).getCSEC();
                                    listeClouplage.get(j).setCSEC(n+1);
                                }

                            }

                        }

                    }
                    br.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }

        return listeClouplage;
    }

    /**
     * ecrit dans le fichier sur le chemin cheminDossier, le contenue de aEcrire
     *
     //* @param String cheminDossier,String fichier
     * @return
     */
    public static void ecrireFichie(String cheminDossier,String fichier,ArrayList<CouplageClass> aEcrire) throws IOException {
        try {

            File file = new File(cheminDossier+"/"+fichier);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < aEcrire.size(); i++) {
                bw.write(""+aEcrire.get(i).nomClasse+" : "+aEcrire.get(i).CSEC+"\n");
            }

            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * lit et affhiche le fichier en param dans la console
     *
     //* @param String cheminDossier,String fichier
     * @return
     */
    public static void lectureFichie(String cheminDossier,String fichier) throws IOException {
        try {

            BufferedReader br = new BufferedReader(new FileReader(cheminDossier+"/"+fichier));
            String ligne = null;

            while ((ligne = br.readLine()) != null){

                String[] data = ligne.split(",");

                for (String val : data){
                    System.out.println(val);
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * initialise la list de classe avec nom et leurs CSEC = 0
     *
     //* @param String [] liste : liste des fichiers dans le dossier
     * @return ArrayList<CouplageClass> listeClouplage
     */
    public static ArrayList<CouplageClass> lectureRepertoire( String [] liste) {

        ArrayList<CouplageClass> listeClouplage = null;

        if (liste != null) {

            listeClouplage = new ArrayList<CouplageClass>();

            for (int i = 0; i < liste.length; i++) {

                String temp = liste[i];
                String[] data = temp.split("\\.");

                //constructer => CouplageClass (String nomClasse, int cSEC)
                CouplageClass classeC = new CouplageClass(data[0],0);
                listeClouplage.add(classeC);

            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }

        return listeClouplage;
    }


    /**
     * Demande le chemin du dossier
     *
     //* @param le chemin (ex C:Desktop\ift3913\tp1)
     * @return String le chemin du repertoire
     */
    public static String inputDossier () {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Entrez le chemin d un dossier");

        return myObj.nextLine();
    }

    /**
     * Demande le nom du fichier csv
     *
     //* @param le nom du fichier (ex test.csv)
     * @return String le fichier
     */
    public static String inputFichier () {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Entrez un fichier CSV");

        return myObj.nextLine();
    }

}