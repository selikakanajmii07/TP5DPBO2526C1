public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    private String jenisKelamin;

    // Constructor kosong
    public Person() {
    }

    // Constructor berparameter
    public Person(String id, String nama, int tahunLahir, String kategori, String jenisKelamin) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.jenisKelamin = jenisKelamin;
    }

    // Getter
    public String getId() { return id; }
    public String getNama() { return nama; }
    public int getTahunLahir() { return tahunLahir; }
    public String getKategori() { return kategori; }
    public String getJenisKelamin() { return jenisKelamin; }

    // Setter
    public void setId(String id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
}
