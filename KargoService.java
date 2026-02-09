import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class KargoService {

    // <<<<<<<<<<<<<<<< EKLE >>>>>>>>>>>>>>>
    public void ekle(JTable tableAktif,
            JTextField txtGonderici,
            JTextField txtAlici,
            JTextField txtTelefon,
            JComboBox<String> cmbFirma,
            JComboBox<String> cmbDurum) {

        try {
            String gonderici = txtGonderici.getText().trim();
            String alici = txtAlici.getText().trim();
            String telefon = txtTelefon.getText().trim();
            Object firmaObj = cmbFirma.getSelectedItem();
            Object durumObj = cmbDurum.getSelectedItem();

            // bos alan kontrolu
            if (gonderici.isEmpty() || alici.isEmpty()
                    || telefon.isEmpty()
                    || firmaObj == null || durumObj == null) {
                throw new Exception("Tüm alanları doldurunuz!");
            }

            // telefon numarasi dogrulamalari
            if (!telefon.matches("\\d+")) {
                throw new Exception("Telefon numarası sadece rakamlardan oluşmalıdır!");
            } else if (!telefon.startsWith("0")) {
                throw new Exception("Telefon numarası 0 ile başlamalıdır!");
            } else if (telefon.length() != 11) {
                throw new Exception("Telefon numarası 11 karakter olmalıdır!");
            }

            // yeni eklenen kargonun durumu sadece "Kabul Edildi" olabilir
            if (!durumObj.equals("Kabul Edildi")) {
                throw new Exception("Yeni kargo eklerken durum sadece \"Kabul Edildi\" olabilir!");
            }

            DefaultTableModel model = (DefaultTableModel) tableAktif.getModel();
            // tabloya ekleme
            model.addRow(new Object[] {
                    gonderici,
                    alici,
                    telefon,
                    firmaObj.toString(),
                    durumObj.toString()
            });

            temizle(txtGonderici, txtAlici, txtTelefon, cmbFirma, cmbDurum);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // <<<<<<<<<<<<<<<< GUNCELLE >>>>>>>>>>>>>>>
    public void guncelle(JTable tableAktif,
            JTable tableTeslim,
            JTable tableDagitim,
            JTextField txtGonderici,
            JTextField txtAlici,
            JTextField txtTelefon,
            JComboBox<String> cmbFirma,
            JComboBox<String> cmbDurum) {

        try {
            int viewRowAktif = tableAktif.getSelectedRow();
            int viewRowDagitim = tableDagitim.getSelectedRow();
            int modelRowAktif = -1;
            int modelRowDagitim = -1;

            // satır seçilmemişse hata ver
            if (viewRowAktif == -1 && viewRowDagitim == -1) {
                throw new Exception("Güncellenecek satırı seçiniz!");
            }

            // dağıtıma çıkmamış kargolar doğrudan "Teslim Edildi" durumuna güncellenemez
            if (viewRowAktif != -1) {
                if ("Teslim Edildi".equals(cmbDurum.getSelectedItem().toString())) {
                    throw new Exception(
                            "Dağıtıma çıkmamış kargolar doğrudan \"Teslim Edildi\" durumuna güncellenemez!");
                }
            }

            // dağıtımda olan kargolar sadece "Teslim Edildi" durumuna güncellenebilir
            if (viewRowDagitim != -1 && "Kabul Edildi".equals(cmbDurum.getSelectedItem().toString())
                    || viewRowDagitim != -1 && "Yolda".equals(cmbDurum.getSelectedItem().toString())
                    || viewRowDagitim != -1 && "Dağıtım Merkezinde".equals(cmbDurum.getSelectedItem().toString())
                    || viewRowDagitim != -1 && "Dağıtımda".equals(cmbDurum.getSelectedItem().toString())) {
                throw new Exception(
                        "Dağıtımda olan kargolar sadece \"Teslim Edildi\" durumuna güncellenebilir!");
            }

            if (viewRowAktif != -1) {
                modelRowAktif = tableAktif.convertRowIndexToModel(viewRowAktif);
            }

            if (viewRowDagitim != -1) {
                modelRowDagitim = tableDagitim.convertRowIndexToModel(viewRowDagitim);
            }

            DefaultTableModel aktifModel = (DefaultTableModel) tableAktif.getModel();
            DefaultTableModel dagitimModel = (DefaultTableModel) tableDagitim.getModel();
            DefaultTableModel teslimModel = (DefaultTableModel) tableTeslim.getModel();

            String yeniDurum = cmbDurum.getSelectedItem().toString();

            // dagitimda > aktar
            if ("Dağıtımda".equals(yeniDurum)) {
                Object[] satir = new Object[] {
                        aktifModel.getValueAt(modelRowAktif, 0), // Gönderici
                        aktifModel.getValueAt(modelRowAktif, 1), // Alıcı
                        aktifModel.getValueAt(modelRowAktif, 2), // Telefon
                        aktifModel.getValueAt(modelRowAktif, 3), // Firma
                        yeniDurum
                };

                dagitimModel.addRow(satir);
                aktifModel.removeRow(modelRowAktif);

                JOptionPane.showMessageDialog(
                        null,
                        "Kargo dağıtıma çıktı ve dağıtımda olanlar listesine aktarıldı.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);

                // teslim edildi > aktar
            } else if ("Teslim Edildi".equals(yeniDurum)) {

                int model1Row;
                DefaultTableModel kaynakModel;
                if (modelRowDagitim >= 0) {
                    model1Row = modelRowDagitim;
                    kaynakModel = dagitimModel;
                } else {
                    model1Row = modelRowAktif;
                    kaynakModel = aktifModel;
                }

                Object[] satir = new Object[] {
                        kaynakModel.getValueAt(model1Row, 0), // Gönderici
                        kaynakModel.getValueAt(model1Row, 1), // Alıcı
                        kaynakModel.getValueAt(model1Row, 2), // Telefon
                        kaynakModel.getValueAt(model1Row, 3), // Firma
                        yeniDurum
                };

                teslimModel.addRow(satir);
                if (modelRowDagitim >= 0) {
                    dagitimModel.removeRow(modelRowDagitim);
                } else {
                    aktifModel.removeRow(modelRowAktif);
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Kargo teslim edildi ve teslim edilenler listesine aktarıldı.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                // normal guncelleme
                aktifModel.setValueAt(yeniDurum, modelRowAktif, 4);
            }

            temizle(txtGonderici, txtAlici, txtTelefon, cmbFirma, cmbDurum);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // <<<<<<<<<<<<<<<< SIL >>>>>>>>>>>>>>>
    public void sil(JTable tableAktif, JTable tableTeslim, JTable tableDagitim) {
        try {
            int viewRowAktif = tableAktif.getSelectedRow();
            int viewRowTeslim = tableTeslim.getSelectedRow();
            int viewRowDagitim = tableDagitim.getSelectedRow();
            if (viewRowAktif == -1 && viewRowTeslim == -1 && viewRowDagitim == -1) {
                throw new Exception("Silinecek satırı seçiniz!");
            }

            int onay = JOptionPane.showConfirmDialog(
                    null,
                    "Seçili kaydı silmek istediğinize emin misiniz?",
                    "Silme Onayı",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (onay != JOptionPane.YES_OPTION) {
                return;
            }

            int modelRowAktif = tableAktif.convertRowIndexToModel(viewRowAktif);
            int modelRowTeslim = tableTeslim.convertRowIndexToModel(viewRowTeslim);
            int modelRowDagitim = tableDagitim.convertRowIndexToModel(viewRowDagitim);
            DefaultTableModel modelAktif = (DefaultTableModel) tableAktif.getModel();
            DefaultTableModel modelDagitim = (DefaultTableModel) tableDagitim.getModel();
            DefaultTableModel modelTeslim = (DefaultTableModel) tableTeslim.getModel();

            if (viewRowAktif != -1) {
                modelAktif.removeRow(modelRowAktif);
            } else if (viewRowDagitim != -1) {
                modelDagitim.removeRow(modelRowDagitim);
            } else {
                modelTeslim.removeRow(modelRowTeslim);
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Kayıt başarıyla silindi.",
                    "Bilgi",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // <<<<<<<<<<<<<<<< KURYE BILGILERI >>>>>>>>>>>>>>>
    public void kuryeBilgileri(JTable tableDagitim) {
        // kurye nesneleri olusturma
        Kurye kuryeYurtici = new Kurye("Ahmet Yılmaz", "0555 123 45 67", "02 HK 3285");
        Kurye kuryeMNG = new Kurye("Mehmet Demir", "0555 987 65 43", "34 AB 215");
        Kurye kuryeAras = new Kurye("Ayşe Kaya", "0555 456 78 90", "06 CD 4837");
        Kurye kuryeSurat = new Kurye("Mirac İlik", "0555 654 32 10", "46 KY 823");
        Kurye kuryePTT = new Kurye("Canan Aydın", "0555 321 09 87", "16 GH 159");

        // satir seçilmemişse uyari ver
        if (tableDagitim.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Kurye bilgilerini görüntülemek için dağıtımda olan kargolardan birini seçiniz.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // secili satirin firma bilgisi
        int modelRowKurye = tableDagitim.convertRowIndexToModel(tableDagitim.getSelectedRow());
        DefaultTableModel dagitimModelKurye = (DefaultTableModel) tableDagitim.getModel();
        String getFirma = dagitimModelKurye.getValueAt(modelRowKurye, 3).toString();

        // kurye bilgilerini göster
        StringBuilder kuryeBilgileri = new StringBuilder();
        kuryeBilgileri.append("Kurye Bilgileri:\n\n");

        if (getFirma.equals("Yurtiçi Kargo")) {
            kuryeBilgileri.append("İsim: ").append(kuryeYurtici.getIsim()).append("\n");
            kuryeBilgileri.append("Telefon: ").append(kuryeYurtici.getTelefon()).append("\n");
            kuryeBilgileri.append("Araç Plaka: ").append(kuryeYurtici.getAracPlaka()).append("\n\n");
        } else if (getFirma.equals("MNG Kargo")) {
            kuryeBilgileri.append("İsim: ").append(kuryeMNG.getIsim()).append("\n");
            kuryeBilgileri.append("Telefon: ").append(kuryeMNG.getTelefon()).append("\n");
            kuryeBilgileri.append("Araç Plaka: ").append(kuryeMNG.getAracPlaka()).append("\n\n");
        } else if (getFirma.equals("Aras Kargo")) {
            kuryeBilgileri.append("İsim: ").append(kuryeAras.getIsim()).append("\n");
            kuryeBilgileri.append("Telefon: ").append(kuryeAras.getTelefon()).append("\n");
            kuryeBilgileri.append("Araç Plaka: ").append(kuryeAras.getAracPlaka()).append("\n\n");
        } else if (getFirma.equals("Sürat Kargo")) {
            kuryeBilgileri.append("İsim: ").append(kuryeSurat.getIsim()).append("\n");
            kuryeBilgileri.append("Telefon: ").append(kuryeSurat.getTelefon()).append("\n");
            kuryeBilgileri.append("Araç Plaka: ").append(kuryeSurat.getAracPlaka()).append("\n\n");
        } else if (getFirma.equals("PTT Kargo")) {
            kuryeBilgileri.append("İsim: ").append(kuryePTT.getIsim()).append("\n");
            kuryeBilgileri.append("Telefon: ").append(kuryePTT.getTelefon()).append("\n");
            kuryeBilgileri.append("Araç Plaka: ").append(kuryePTT.getAracPlaka()).append("\n\n");
        } else {
            kuryeBilgileri.append("Seçili kargo firması için kurye bilgisi bulunmamaktadır.\n");
        }

        JOptionPane.showMessageDialog(
                null,
                kuryeBilgileri.toString(),
                "Kurye Bilgileri",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // TEMIZLE
    private void temizle(JTextField gönderici,
            JTextField alici,
            JTextField telefon,
            JComboBox<String> firma,
            JComboBox<String> durum) {
        gönderici.setText("");
        alici.setText("");
        telefon.setText("");
        firma.setSelectedIndex(-1);
        durum.setSelectedIndex(0);
    }
}