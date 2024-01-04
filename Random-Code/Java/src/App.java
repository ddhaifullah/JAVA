import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class App {
    // Daftar buku yang tersedia
    private static Buku[] daftarBuku = { 
        new Buku("Harry Potter"),
        new Buku("Lord of the Rings"),
        new Buku("To Kill a Mockingbird")
    };

    // Daftar buku yang sedang dipinjam
    private static Peminjaman[] bukuDipinjam = new Peminjaman[10];

    private static int jumlahPeminjaman = 0; // Untuk menampung jumlah peminjaman

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Menu Utama, Silahkan pilih menu yang tersedia");
            System.out.println("1. Pinjam Buku");
            System.out.println("2. Kembalikan Buku");
            System.out.println("3. Keluar");
            System.out.println("----------------------------------------------");
            System.out.print("Masukkan Jawaban: ");
            String inMenu = input.nextLine();

            switch (inMenu) {
                case "1":
                    pinjamBuku(input);
                    break;
                case "2":
                    kembalikanBuku(input);
                    break;
                case "3":
                    System.out.println("Terima kasih. Sampai jumpa!");
                    input.close();
                    System.exit(0);
                default:
                    System.out.println("Menu tidak tersedia");
            }
        }
    }


    // Method untuk meminjam buku
    public static void pinjamBuku(Scanner input) {
        System.out.print("Masukkan judul buku yang ingin dipinjam(boleh tidak lengkap): ");
        String kataKunci = input.nextLine();

        boolean bukuDitemukan = false;

        System.out.println("Buku yang ditemukan:");

        for (int i = 0; i < daftarBuku.length; i++) {
            if (daftarBuku[i].getJudul().toLowerCase().contains(kataKunci.toLowerCase())) {
                bukuDitemukan = true;
                System.out.println((i + 1) + ". " + daftarBuku[i].getJudul());
            }
        }

        if (bukuDitemukan) {
            System.out.print("Pilih buku yang ingin dipinjam: ");
            int nomorBuku = Integer.parseInt(input.nextLine());

            if (nomorBuku >= 1 && nomorBuku <= daftarBuku.length) {
                Buku bukuDipilih = daftarBuku[nomorBuku - 1];

                System.out.print("Nama Lengkap: ");
                String namaPeminjam = input.nextLine();

                System.out.print("NIM: ");
                String nimPeminjam = input.nextLine();

                // Mengambil tanggal pinjam
                Date tanggalPinjam = new Date();

                // Menghitung tanggal pengembalian (contoh: 7 hari setelah tanggal pinjam)
                long tanggalPengembalianMillis = tanggalPinjam.getTime() + (7 * 24 * 60 * 60 * 1000);
                Date tanggalPengembalian = new Date(tanggalPengembalianMillis);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                System.out.println("\n");
                System.out.println("----------------------------------------------");
                System.out.println("Berikut adalah detail peminjaman Anda:");
                System.out.println("----------------------------------------------");
                System.out.println("Anda meminjam buku\t: " + bukuDipilih.getJudul());
                System.out.println("Nama Peminjam\t\t: " + namaPeminjam);
                System.out.println("NIM\t\t\t: " + nimPeminjam);
                System.out.println("Tanggal Pinjam\t\t: " + dateFormat.format(tanggalPinjam));
                System.out.println("Tanggal Pengembalian\t: " + dateFormat.format(tanggalPengembalian));
                // System.out.println("Kode Unik\t\t: " + generateKodeUnik());
                System.out.println("----------------------------------------------");
                System.out.println("\n");

                // Menambahkan data peminjaman ke dalam array
                bukuDipinjam[jumlahPeminjaman] = new Peminjaman(
                    bukuDipilih,
                    namaPeminjam,
                    nimPeminjam,
                    tanggalPinjam,
                    tanggalPengembalian,
                    generateKodeUnik()
                );
                jumlahPeminjaman++;
            } else {
                System.out.println("Nomor buku tidak valid.");
            }
        } else {
            System.out.println("Buku tidak ditemukan.");
        }
    }


    // Method untuk mengembalikan buku
    public static void kembalikanBuku(Scanner input) {
        if (jumlahPeminjaman == 0) {
            System.out.println("Anda belum meminjam buku apapun.");
            return;
        }

        System.out.println("Buku yang sedang Anda pinjam:");

        for (int i = 0; i < jumlahPeminjaman; i++) {
            System.out.println((i + 1) + ". " + bukuDipinjam[i].getJudulBuku() + " (Kode Unik: " + bukuDipinjam[i].getKodeUnik() + ")");
        }

        System.out.print("Masukkan nomor buku yang ingin dikembalikan: ");
        int nomorBuku = Integer.parseInt(input.nextLine());

        if (nomorBuku >= 1 && nomorBuku <= jumlahPeminjaman) {
            Peminjaman peminjaman = bukuDipinjam[nomorBuku - 1];

            System.out.print("Masukkan kode unik buku: ");
            String kodeUnikInput = input.nextLine();

            if (kodeUnikInput.equals(peminjaman.getKodeUnik())) {
                System.out.println("Anda mengembalikan buku: " + peminjaman.getJudulBuku());

                Date tanggalKembali = new Date();

                // Menghitung denda jika pengembalian terlambat
                if (tanggalKembali.after(peminjaman.getTanggalPengembalian())) {
                    long selisihMillis = tanggalKembali.getTime() - peminjaman.getTanggalPengembalian().getTime();
                    int selisihHari = (int) (selisihMillis / (24 * 60 * 60 * 1000)); // ini untuk menghitung selisih hari dari tanggal pengembalian dengan tanggal kembali
                    int denda = selisihHari * 10000;

                    System.out.println("Anda mengembalikan buku terlambat.");
                    System.out.println("Denda yang harus dibayar: Rp " + denda);
                } else {
                    System.out.println("\n");
                    System.out.println("Terima kasih telah mengembalikan buku tepat waktu.");
                    System.out.println("Detail Buku yang dikembalikan:");
                    System.out.println("Judul Buku: " + peminjaman.getJudulBuku());
                    System.out.println("\n\n");
                }

                // Menghapus data peminjaman dari daftar
                hapusPeminjaman(nomorBuku - 1);
            } else {
                System.out.println("\n");
                System.out.println("Kode unik buku tidak sesuai.");
                System.out.println("Silahkan coba lagi.");
                System.out.println("\n");
            }
        } else {
            System.out.println("Nomor buku tidak valid.");
        }
    }

    public static void hapusPeminjaman(int index) {
        for (int i = index; i < jumlahPeminjaman - 1; i++) {
            bukuDipinjam[i] = bukuDipinjam[i + 1];
        }
        jumlahPeminjaman--;
    }

    public static String generateKodeUnik() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(10000));
    }
}

class Buku {
    private String judul;

    public Buku(String judul) {
        this.judul = judul;
    }

    public String getJudul() {
        return judul;
    }
}


// Class untuk menyimpan data peminjaman
class Peminjaman {
    private Buku buku;
    private String namaPeminjam;
    private String nimPeminjam;
    private Date tanggalPinjam;
    private Date tanggalPengembalian;
    private String kodeUnik;

    public Peminjaman(Buku buku, String namaPeminjam, String nimPeminjam, Date tanggalPinjam, Date tanggalPengembalian, String kodeUnik) {
        this.buku = buku;
        this.namaPeminjam = namaPeminjam;
        this.nimPeminjam = nimPeminjam;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalPengembalian = tanggalPengembalian;
        this.kodeUnik = kodeUnik;
    }


    // Getter
    public String getJudulBuku() {
        return buku.getJudul();
    }

    public String getKodeUnik() {
        return kodeUnik;
    }

    public Date getTanggalPengembalian() {
        return tanggalPengembalian;
    }
}
