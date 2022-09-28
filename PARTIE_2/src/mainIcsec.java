import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class mainIcsec {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        String cheminDossier = inputDossier();
        //String fichierCSV = inputFichier();


        File repertoire = new File(cheminDossier);
        String liste[] = repertoire.list();

        String [] classeList = lectureRepertoire(liste);

        for (String val : classeList){
            System.out.println(val);
        }

    }

    public static void lectureFichie(String cheminDossier,String liste) throws IOException {
        try {

            BufferedReader br = new BufferedReader(new FileReader(cheminDossier+"/"+liste+".java"));
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

    public static String[] lectureRepertoire( String [] liste) {

        String [] classeList = null;

        if (liste != null) {

            classeList = new String[liste.length];

            for (int i = 0; i < liste.length; i++) {

                //////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!/////////
                classeList[i]=liste[i].substring(0,liste[i].length()-5);


            }
        } else {
            System.err.println("Nom de repertoire invalide");
        }

        return classeList;
    }

    public static String inputDossier () {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Entrez le chemin d un dossier");

        return myObj.nextLine();
    }

    public static String inputFichier () {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Entrez un fichier CSV");

        return myObj.nextLine();
    }

}