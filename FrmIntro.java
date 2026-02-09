import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmIntro extends JFrame {

    JTable tableAktif;
    JTable tableTeslim;
    JTable tableDagitim;
    JTextField txtGonderici, txtAlici, txtTelefon;
    JComboBox<String> cmbDurum;
    JComboBox<String> cmbFirma;

    KargoService service = new KargoService();

    public FrmIntro() {
        setTitle("Kargo Takip Sistemi");
        setSize(950, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblGonderici = new JLabel("Gönderici Adı:");
        JLabel lblAlici = new JLabel("Alıcı Adı:");
        JLabel lblTelefon = new JLabel("Telefon:");
        JLabel lblFirma = new JLabel("Firma:");
        JLabel lblDurum = new JLabel("Kargo Durumu:");
        JLabel lblAktifKargo = new JLabel("Aktif Kargolar");
        JLabel lblDagitimdaOlanlar = new JLabel("Dağıtımda Olan Kargolar");
        JLabel lblTeslimEdilenler = new JLabel("Teslim Edilen Kargolar");

        txtGonderici = new JTextField();
        txtAlici = new JTextField();
        txtTelefon = new JTextField();

        cmbDurum = new JComboBox<>(new String[] {
                "Kabul Edildi", "Yolda",
                "Dağıtım Merkezinde", "Dağıtımda", "Teslim Edildi"
        });

        cmbFirma = new JComboBox<>(new String[] {
                "Yurtiçi Kargo", "MNG Kargo",
                "Aras Kargo", "Sürat Kargo", "PTT Kargo"
        });
        cmbFirma.setSelectedIndex(-1);

        JButton btnEkle = new JButton("Ekle");
        JButton btnGuncelle = new JButton("Güncelle");
        JButton btnSil = new JButton("Sil");
        JButton btnKuryeBilgi = new JButton("Kurye Bilgileri");

        // Tablo modelleri ve tablolar
        tableAktif = new JTable(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Gönderici", "Alıcı", "Telefon", "Firma", "Durum" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // tablo hucreleri duzenlenemez
                return false;
            }
        });

        tableDagitim = new JTable(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Gönderici", "Alıcı", "Telefon", "Firma", "Durum" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tableTeslim = new JTable(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Gönderici", "Alıcı", "Telefon", "Firma", "Durum" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // aktif tablosuna tiklandiginda digerlerini temizle
        tableAktif.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableAktif.getSelectedRow() != -1) {
                tableDagitim.clearSelection();
                tableTeslim.clearSelection();
            }
        });

        // dagitim tablosuna tiklandiginda digerlerini temizle
        tableDagitim.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableDagitim.getSelectedRow() != -1) {
                tableAktif.clearSelection();
                tableTeslim.clearSelection();
            }
        });

        // teslim tablosuna tiklandiginda digerlerini temizle
        tableTeslim.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableTeslim.getSelectedRow() != -1) {
                tableAktif.clearSelection();
                tableDagitim.clearSelection();
            }
        });

        JScrollPane spAktif = new JScrollPane(tableAktif);
        JScrollPane spDagitim = new JScrollPane(tableDagitim);
        JScrollPane spTeslim = new JScrollPane(tableTeslim);

        lblGonderici.setBounds(20, 0, 100, 30);
        lblAlici.setBounds(20, 60, 100, 30);
        lblTelefon.setBounds(20, 120, 100, 30);
        lblFirma.setBounds(20, 180, 100, 30);
        lblDurum.setBounds(20, 240, 100, 30);
        lblAktifKargo.setBounds(250, 0, 200, 30);
        lblDagitimdaOlanlar.setBounds(250, 240, 200, 30);
        lblTeslimEdilenler.setBounds(250, 475, 200, 30);

        txtGonderici.setBounds(20, 25, 200, 30);
        txtAlici.setBounds(20, 85, 200, 30);
        txtTelefon.setBounds(20, 145, 200, 30);
        cmbFirma.setBounds(20, 205, 200, 30);
        cmbDurum.setBounds(20, 265, 200, 30);
        btnEkle.setBounds(20, 320, 200, 30);
        btnGuncelle.setBounds(20, 360, 200, 30);
        btnSil.setBounds(20, 400, 200, 30);
        btnKuryeBilgi.setBounds(20, 440, 200, 30);
        spAktif.setBounds(250, 25, 600, 200);
        spDagitim.setBounds(250, 265, 600, 200);
        spTeslim.setBounds(250, 500, 600, 200);

        add(lblGonderici);
        add(lblAlici);
        add(lblTelefon);
        add(lblFirma);
        add(lblDurum);
        add(lblAktifKargo);
        add(lblDagitimdaOlanlar);
        add(lblTeslimEdilenler);

        add(txtGonderici);
        add(txtAlici);
        add(txtTelefon);
        add(cmbFirma);
        add(cmbDurum);
        add(btnEkle);
        add(btnGuncelle);
        add(btnSil);
        add(btnKuryeBilgi);
        add(spAktif);
        add(spDagitim);
        add(spTeslim);

        btnEkle.addActionListener(e -> service.ekle(tableAktif, txtGonderici, txtAlici,
                txtTelefon, cmbFirma, cmbDurum));

        btnGuncelle
                .addActionListener(e -> service.guncelle(tableAktif, tableTeslim, tableDagitim, txtGonderici, txtAlici,
                        txtTelefon, cmbFirma, cmbDurum));

        btnSil.addActionListener(e -> service.sil(tableAktif, tableTeslim, tableDagitim));

        btnKuryeBilgi.addActionListener(e -> service.kuryeBilgileri(tableDagitim));
    }
}