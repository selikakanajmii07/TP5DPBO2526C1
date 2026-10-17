import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox kategoriComboBox;
    private JPanel genderPanel;
    private JRadioButton priaRadioButton;
    private JRadioButton wanitaRadioButton;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;

    // koneksi ke database MySQL
    private Database database;
    private int selectedIndex = -1;
    private String selectedId = "";
    private ButtonGroup genderGroup;

    // ===== main =====
    public static void main(String[] args) {
        PeopleMenu menu;
        try {
            menu = new PeopleMenu();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null,
                    "Tidak dapat terhubung ke database db_people.\n"
                            + "Pastikan MySQL sudah menyala, db_people.sql sudah di-import,\n"
                            + "dan mysql-connector-j sudah ditambahkan ke project.\n\n"
                            + "Detail: " + ex.getMessage(),
                    "Koneksi Database Gagal", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }

        // atur ukuran window
        menu.setSize(750, 650);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ===== Constructor =====
    public PeopleMenu() {
        setTitle("PeopleData - Data Rumah Sakit");

        // koneksi ke database
        database = new Database();

        // isi tabel dari database
        peopleTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Pasien", "Dokter", "Perawat", "Apoteker", "Staf Administrasi" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // radio button Pria/Wanita dijadikan satu group supaya hanya bisa pilih salah satu
        genderGroup = new ButtonGroup();
        genderGroup.add(priaRadioButton);
        genderGroup.add(wanitaRadioButton);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // saat tombol delete ditekan = tampilkan konfirmasi dulu
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    return;
                }
                String nama = namaField.getText().trim();
                int pilihan = JOptionPane.showConfirmDialog(
                        null,
                        "Yakin ingin menghapus data \"" + nama + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                // data hanya dihapus kalau user memilih "Yes"
                if (pilihan == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                int row = peopleTable.rowAtPoint(e.getPoint());
                if (row == -1) {
                    return;
                }
                selectedIndex = row;

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curGender = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                // simpan ID asli untuk dipakai di query UPDATE/DELETE
                selectedId = curId;

                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                if (curGender.equals("Pria")) {
                    priaRadioButton.setSelected(true);
                } else {
                    wanitaRadioButton.setSelected(true);
                }

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin" };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // isi tabel dengan hasil query
        try (ResultSet resultSet = database.selectQuery("SELECT * FROM person ORDER BY id")) {
            int i = 1;
            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = i;
                row[1] = resultSet.getString("id");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("tahun_lahir");
                row[4] = resultSet.getString("kategori");
                row[5] = resultSet.getString("jenis_kelamin");
                tmp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tmp;
    }

    private String validasiForm() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahun = tahunLahirField.getText().trim();

        // cek kolom yang masih kosong
        String kosong = "";
        if (id.isEmpty()) kosong += "ID, ";
        if (nama.isEmpty()) kosong += "Nama, ";
        if (tahun.isEmpty()) kosong += "Tahun Lahir, ";
        if (kategoriComboBox.getSelectedIndex() == 0) kosong += "Kategori, ";
        if (!priaRadioButton.isSelected() && !wanitaRadioButton.isSelected()) kosong += "Jenis Kelamin, ";

        if (!kosong.isEmpty()) {
            return "Kolom berikut masih kosong:\n" + kosong.substring(0, kosong.length() - 2);
        }

        // cek tahun lahir
        try {
            int tahunLahir = Integer.parseInt(tahun);
            int tahunSekarang = Year.now().getValue();
            if (tahunLahir < 1900 || tahunLahir > tahunSekarang) {
                return "Tahun lahir harus antara 1900 dan " + tahunSekarang + "!";
            }
        } catch (NumberFormatException ex) {
            return "Tahun lahir harus berupa angka!";
        }
        return null;
    }

    // Cek apakah ID sudah ada di database
    private boolean idSudahAda(String id) {
        try (ResultSet rs = database.selectQuery("SELECT COUNT(*) FROM person WHERE id = ?", id)) {
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getGenderTerpilih() {
        return priaRadioButton.isSelected() ? "Pria" : "Wanita";
    }

    private void tampilkanError(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ===== CREATE =====
    public void insertData() {
        // error kalau masih ada kolom yang kosong / tidak valid
        String error = validasiForm();
        if (error != null) {
            tampilkanError(error);
            return;
        }

        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String gender = getGenderTerpilih();

        try {
            // error kalau ID sudah dipakai
            if (idSudahAda(id)) {
                tampilkanError("ID \"" + id + "\" sudah ada! Gunakan ID yang lain.");
                return;
            }

            // tambahkan data ke database
            database.insertUpdateDeleteQuery(
                    "INSERT INTO person (id, nama, tahun_lahir, kategori, jenis_kelamin) VALUES (?, ?, ?, ?, ?)",
                    id, nama, tahunLahir, kategori, gender);
        } catch (RuntimeException ex) {
            tampilkanError("Gagal menambahkan data ke database.\n" + ex.getMessage());
            return;
        }

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    // ===== UPDATE =====
    public void updateData() {
        // error kalau masih ada kolom yang kosong
        String error = validasiForm();
        if (error != null) {
            tampilkanError(error);
            return;
        }

        // ambil data dari form
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        int tahunLahir = Integer.parseInt(tahunLahirField.getText().trim());
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String gender = getGenderTerpilih();

        try {
            // kalau ID diganti, ID barunya tidak boleh sama dengan data lain
            if (!id.equalsIgnoreCase(selectedId) && idSudahAda(id)) {
                tampilkanError("ID \"" + id + "\" sudah ada! Gunakan ID yang lain.");
                return;
            }

            // ubah data di database
            database.insertUpdateDeleteQuery(
                    "UPDATE person SET id = ?, nama = ?, tahun_lahir = ?, kategori = ?, jenis_kelamin = ? WHERE id = ?",
                    id, nama, tahunLahir, kategori, gender, selectedId);
        } catch (RuntimeException ex) {
            tampilkanError("Gagal mengubah data di database.\n" + ex.getMessage());
            return;
        }

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil diubah");
    }

    // ===== DELETE =====
    public void deleteData() {
        try {
            // hapus data dari database
            database.insertUpdateDeleteQuery("DELETE FROM person WHERE id = ?", selectedId);
        } catch (RuntimeException ex) {
            tampilkanError("Gagal menghapus data dari database.\n" + ex.getMessage());
            return;
        }

        // update tabel
        peopleTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        genderGroup.clearSelection();

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);
        selectedIndex = -1;
        selectedId = "";
    }
}
