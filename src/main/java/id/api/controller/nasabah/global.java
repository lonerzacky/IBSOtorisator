package id.api.controller.nasabah;

import id.api.global.GlobalSelect;
import id.api.helper.ConnectionPool;
import id.api.helper.ConnectionPoolSys;
import id.api.model.Otorisasi;
import id.api.model.User;
import id.app.constanta.ConstantaString;
import id.app.global.AppConfig;
import id.app.global.AppGlobal;
import org.apache.commons.dbutils.DbUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.sql.*;

public class global {


    public static JSONObject setLogin(User user) {
        JSONObject jParams = new JSONObject();
        JSONObject nParams = new JSONObject();
        JSONArray jArrayNasabah = new JSONArray();
        Connection conn = null;
        try {
            conn = ConnectionPoolSys.getConnection();
            String username = user.get_username();
            String password = user.get_password();
            String user_id;

            String query = "SELECT user_id from sys_daftar_user where user_name=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                jParams.put("response_code", ConstantaString.global.apiErrorCode);
                jParams.put("response_message", "Login Gagal ! Username yang anda masukkan salah.");
                jParams.put("response_data", "[]");
                return jParams;
            }

            String query1 = "SELECT user_id,user_name,nama_lengkap,unit_kerja,jabatan,user_code " +
                    " from sys_daftar_user where user_name=? and user_web_password=?";
            PreparedStatement ps2 = conn.prepareStatement(query1);
            ps2.setString(1,username);
            ps2.setString(2,password);
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                jParams.put("response_code", ConstantaString.global.apiErrorCode);
                jParams.put("response_message", "Login Gagal ! Password yang anda masukkan salah.");
                jParams.put("response_data", "[]");
                return jParams;
            } else {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("user_id", rs2.getString("user_id"));
                jsonObj.put("user_name", rs2.getString("user_name"));
                jsonObj.put("nama_lengkap", rs2.getString("nama_lengkap"));
                jsonObj.put("unit_kerja", rs2.getString("unit_kerja"));
                jsonObj.put("jabatan", rs2.getString("jabatan"));
                jsonObj.put("user_code", rs2.getString("user_code"));
                user_id = rs2.getString("user_id");
                jArrayNasabah.add(jsonObj);
            }

            nParams.put("detail", jArrayNasabah);
            String query2 = "update sys_daftar_user set fcm_token=? where user_id=? ";
            PreparedStatement ps3 = conn.prepareStatement(query2);
            ps3.setString(1, user.get_fcm_token());
            ps3.setString(2,user_id);
            ps3.execute();


            jParams.put("response_code", ConstantaString.global.apiSuccessCode);
            jParams.put("response_message", "LOGIN SUKSES");
            jParams.put("response_data", nParams);

        } catch (SQLException | IOException e) {
            jParams.put("response_code", ConstantaString.global.apiErrorCode);
            jParams.put("response_message", "FAILED LOGIN");
            jParams.put("response_data", e.getMessage());
            return jParams;
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return jParams;
    }

    public static JSONObject sendOto(Otorisasi oto) {
        JSONObject jParams = new JSONObject();
        Connection conn = null;
        boolean ret;
        try {
            conn = ConnectionPoolSys.getConnection();
            String fcmToken = GlobalSelect.getValByKeyValString("fcm_token", "sys_daftar_user", "user_id", oto.get_user_id_tujuan(), conn);
            if (fcmToken.equalsIgnoreCase("")){
                ret=false;
            }else{
                ret = AppGlobal.pushNotification(fcmToken, oto.get_keterangan(),oto);
            }

            if (ret){
                jParams.put("response_code", ConstantaString.global.apiSuccessCode);
                jParams.put("response_message", "PENGIRIMAN OTORISASI SUKSES");
                jParams.put("response_data", "[]");
            }else{
                jParams.put("response_code", ConstantaString.global.apiSuccessCode);
                jParams.put("response_message", "PENGIRIMAN OTORISASI GAGAL");
                jParams.put("response_data", "[]");
            }



        } catch (SQLException | IOException e) {
            jParams.put("response_code", ConstantaString.global.apiErrorCode);
            jParams.put("response_message", "FAILED SEND OTORISASI");
            jParams.put("response_data", e.getMessage());
            return jParams;
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return jParams;
    }

    public static JSONObject updateOto(Otorisasi oto) {
        JSONObject jParams = new JSONObject();
        Connection conn = null;
        Connection connSys = null;
        try {
            connSys = ConnectionPoolSys.getConnection();
            conn = ConnectionPool.getConnection();

            String user_otorisator = GlobalSelect.getValByKeyValString("id_otorisator", "app_otorisasi", "id_otorisasi", oto.get_otorisator_id(), conn);
            String password;
            if (AppConfig.get_BPRCode().equals("IBS GEN 2")){
                password = oto.get_password();
            }else{
                password = GlobalSelect.getValByKeyValString("user_password", "sys_daftar_user", "user_id", user_otorisator, connSys);
            }

            String query1 = "update app_otorisasi set status=?,password=? where id_otorisasi=? ";
            PreparedStatement ps = conn.prepareStatement(query1);
            if (oto.get_status().equals("1")){
                if (AppConfig.get_BPRCode().equals("IBS GEN 2")){
                    ps.setString(1, "1");
                }else{
                    ps.setString(1, "2");
                }
                ps.setString(2, password);
            }else{
                if (AppConfig.get_BPRCode().equals("IBS GEN 2")){
                    ps.setString(1, "3");
                }else{
                    ps.setString(1, "1");
                }
                ps.setString(2, "rejected");
            }
            ps.setString(3, oto.get_otorisator_id());
            ps.execute();

            jParams.put("response_code", ConstantaString.global.apiSuccessCode);
            jParams.put("response_message", "UPDATE OTORISASI SUKSES");
            jParams.put("response_data", "[]");

        } catch (SQLException | IOException e) {
            jParams.put("response_code", ConstantaString.global.apiErrorCode);
            jParams.put("response_message", "FAILED SEND OTORISASI");
            jParams.put("response_data", e.getMessage());
            return jParams;
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(connSys);
        }

        return jParams;
    }

    public static JSONObject listOto(Otorisasi oto) {
        JSONObject jParams = new JSONObject();
        JSONObject nParams = new JSONObject();
        JSONArray jArrayNasabah = new JSONArray();
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            String query1 = getString(oto);
            PreparedStatement ps = conn.prepareStatement(query1);
            ResultSet rs = ps.executeQuery(query1);
            while (rs.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id_otorisasi", rs.getString("id_otorisasi"));
                jsonObj.put("tgl_otorisator", rs.getString("tgl_otorisator"));
                jsonObj.put("keterangan", rs.getString("keterangan"));
                jsonObj.put("status", rs.getString("status"));
                Connection connsys = ConnectionPoolSys.getConnection();
                String userName = GlobalSelect.getValByKeyValString("nama_lengkap","sys_daftar_user","user_id",rs.getString("user_id"),connsys);
                DbUtils.closeQuietly(connsys);
                jsonObj.put("user_id", userName);
                jArrayNasabah.add(jsonObj);
            }
            nParams.put("detail", jArrayNasabah);
            jParams.put("response_code", ConstantaString.global.apiSuccessCode);
            jParams.put("response_message", "GET DATA OTORISASI SUKSES");
            jParams.put("response_data", nParams);

        } catch (SQLException | IOException e) {
            jParams.put("response_code", ConstantaString.global.apiErrorCode);
            jParams.put("response_message", "FAILED GET DATA OTORISASI");
            jParams.put("response_data", e.getMessage());
            return jParams;
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return jParams;
    }

    private static String getString(Otorisasi oto) {
        String query1 = "select id_otorisasi,user_id,id_otorisator,tgl_otorisator,keterangan,status " +
                "from app_otorisasi where tgl_otorisator>='"+ oto.get_tgl_awal()+"' " +
                "and tgl_otorisator<='"+ oto.get_tgl_akhir()+"' " +
                "and id_otorisator='"+ oto.get_user_id_pengirim()+"' ";
        if (!oto.get_status().equalsIgnoreCase("")){
            if (AppConfig.get_BPRCode().equalsIgnoreCase("IBS GEN 2")){
                if (oto.get_status().equals("1")){
                    query1 += " and status='1' and password<>''";
                }else if (oto.get_status().equals("2")){
                    query1 += " and status='2' and password<>''";
                }else{
                    query1 += " and status='0'";
                }
            }else{
                if (oto.get_status().equals("1")){
                    query1 += " and status='2' and password<>''";
                }else if (oto.get_status().equals("2")){
                    query1 += " and status='1' and password<>''";
                }else{
                    query1 += " and status='0'";
                }
            }
        }
        return query1;
    }


}
