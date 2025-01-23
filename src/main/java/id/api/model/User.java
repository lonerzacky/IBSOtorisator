package id.api.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String _username;
    private String _kodeKantor;
    private String _password;
    private String _nama_lengkap;
    private String _fcm_token;
    private String _jabatan;
    private String _user_code;
    private String _user_id;
}
