public class Kargo {

    private String gonderici;
    private String alici;
    private String telefon;
    public String firma;
    private String durum;

    public Kargo(String gonderici, String alici,
            String telefon, String firma, String durum) {
        this.gonderici = gonderici;
        this.alici = alici;
        this.telefon = telefon;
        this.firma = firma;
        this.durum = durum;
    }

    public String getGonderici() {
        return gonderici;
    }

    public String getAlici() {
        return alici;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getFirma() {
        return firma;
    }

    public String getDurum() {
        return durum;
    }
}