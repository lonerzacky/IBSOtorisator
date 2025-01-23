package id.api.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Otorisasi {
    private String _otorisator_id;
    private String _tgl_trans;
    private String _tgl_awal;
    private String _tgl_akhir;
    private String _user_id_pengirim;
    private String _user_id_tujuan;
    private String _kode_kantor;
    private String _keterangan;
    private String _status;
    private String _password;
}
