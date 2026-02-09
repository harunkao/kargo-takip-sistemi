public class Kurye {

    private String isim;
    private String telefon;
    private String aracPlaka;

    public Kurye(String isim, String telefon, String aracPlaka) {
        this.isim = isim;
        this.telefon = telefon;
        this.aracPlaka = aracPlaka;
    }

    public String getIsim() {
        return isim;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getAracPlaka() {
        return aracPlaka;
    }
}