# TP5DPBO2526C1
Saya Selika Kanajmi dengan NIM 2408495 mengerjakan TP 5 dalam mata kuliah Desain Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak akan melakukan kecurangan seperti yang telah di spesifikasikan

PENJELASAN
DESAIN DAN FUNGSI PROGRAM
- class Person, class ini dibuat untuk menampung data satu orang yaitu id, nama, tahunLahir, kategori, dan jenisKelamin. Semua atribut private dan diakses lewat getter dan setter.
- class Database, class ini dibuat untuk mengurus koneksi ke MySQL lewat JDBC. Method selectQuery dipakai untuk SELECT dan method insertUpdateDeleteQuery dipakai untuk INSERT, UPDATE, dan DELETE.
- class PeopleMenu, class ini dibuat untuk window utama. Atribut database dalam class ini dibentuk dari class Database karena semua operasi CRUD langsung dijalankan ke database. Atribut selectedId menyimpan ID asli baris yang dipilih untuk dipakai di query UPDATE dan DELETE.
- file db_people.sql, berisi perintah membuat database db_people, tabel person, dan 10 data awal.

ALUR
1. main membuat objek PeopleMenu, Constructor langsung membuat objek Database yang terhubung ke db_people kalau koneksi gagal muncul dialog error lalu program berhenti.
2. Untuk Create isi form lalu klik Add, kalau ada kolom kosong muncul dialog error, kalau ID sudah ada muncul dialog error, Kalau lolos data di-INSERT ke database tabel dimuat ulang dan form dikosongkan.
3. Untuk Update klik satu baris tabel tombol Add berubah jadi Update dan tombol Delete muncul ubah data lalu klik Update, setelah divalidasi data di-UPDATE di database dan tabel dimuat ulang.
4. Untuk Delete klik satu baris lalu klik Delete, setelah user memilih Yes di pop-up konfirmasi data di-DELETE dari database dan tabel dimuat ulang.
5. Tombol Cancel membatalkan pilihan baris dan mengosongkan form.

DIALOG ERROR
- Ada kolom input yang masih kosong, muncul saat insert maupun update.
- ID sudah ada di database, muncul saat insert dan saat update kalau ID diganti ke ID yang sudah dipakai.
- Tahun lahir bukan angka atau di luar rentang 1900 sampai tahun sekarang.
- Koneksi ke database gagal, misalnya MySQL belum menyala.

DOKUMENTASI
Dokumentasi ada di folder Dokumentasi
