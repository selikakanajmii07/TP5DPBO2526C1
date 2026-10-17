CREATE DATABASE IF NOT EXISTS db_people;
USE db_people;

DROP TABLE IF EXISTS person;

CREATE TABLE person (
    id            VARCHAR(20)  NOT NULL,
    nama          VARCHAR(100) NOT NULL,
    tahun_lahir   INT          NOT NULL,
    kategori      VARCHAR(50)  NOT NULL,
    jenis_kelamin VARCHAR(10)  NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO person (id, nama, tahun_lahir, kategori, jenis_kelamin) VALUES
('P001', 'Budi Santoso',   1992, 'Pasien',            'Pria'),
('P002', 'Siti Aminah',    1997, 'Pasien',            'Wanita'),
('D001', 'Rian Pratama',   1985, 'Dokter',            'Pria'),
('D002', 'Dewi Lestari',   1988, 'Dokter',            'Wanita'),
('N001', 'Ahmad Fauzi',    1995, 'Perawat',           'Pria'),
('N002', 'Nadia Putri',    1994, 'Perawat',           'Wanita'),
('A001', 'Rizky Ramadhan', 1990, 'Apoteker',          'Pria'),
('S001', 'Maya Indah',     1999, 'Staf Administrasi', 'Wanita'),
('P003', 'Hendra Wijaya',  1980, 'Pasien',            'Pria'),
('P004', 'Clarissa Yuni',  2003, 'Pasien',            'Wanita');
