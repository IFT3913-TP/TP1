public class CouplageClass {
    String nomClasse;
    int CSEC;

    /**
     * @param nomClasse
     * @param cSEC
     */
    public CouplageClass(String nomClasse, int cSEC) {
        this.nomClasse = nomClasse;
        CSEC = cSEC;
    }
    public String getNomClasse() {
        return nomClasse;
    }
    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }
    public int getCSEC() {
        return CSEC;
    }
    public void setCSEC(int cSEC) {
        CSEC = cSEC;
    }
}