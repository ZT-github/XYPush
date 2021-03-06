package cn.xinyisoft.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class XinyiPush {
    static final String SP_PUSH = "XYPush";

    static String SOCKET_IP = "192.168.10.188";
    static int SOCKET_PORT = 8124;

    static boolean isLog = true;

    /**
     * 连接
     *
     * @param uid   用户id
     * @param token 设备编号
     */
    public static void connect(Context context, int uid, String token) {
        Editor editor = context.getSharedPreferences(SP_PUSH, Context.MODE_PRIVATE).edit();

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            editor.putInt("XINYI_APPID", appInfo.metaData.getInt("XINYI_APPID"));
            editor.putString("XINYI_SECRET", appInfo.metaData.getString("XINYI_SECRET"));
            editor.apply();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        editor.putInt("uid", uid);
        editor.putString("token", token);
        editor.apply();

        if (XinyiSocketService.Companion.getSocketService() == null) {
            XinyiSocketService.Companion.setSocketService(new Intent(context, XinyiSocketService.class));
        } else {
            context.stopService(XinyiSocketService.Companion.getSocketService());
        }
        context.startService(XinyiSocketService.Companion.getSocketService());
    }

    /**
     * 断开连接
     */
    public static void disconnect(Context context) {
        if (XinyiSocketService.Companion.getSocketService() != null) {
            context.stopService(XinyiSocketService.Companion.getSocketService());
        }

        SharedPreferences sp = context.getSharedPreferences(SP_PUSH, Context.MODE_PRIVATE);
        String xgToken = sp.getString("xgToken", "");
        Editor editor = sp.edit();
        editor.clear().apply();
        editor.putString("xgToken", xgToken).apply();
    }

    public static class Builder {

        private Context context;

        private SharedPreferences sp;

        public Builder(Context context) {
            this.context = context;
            sp = context.getSharedPreferences(SP_PUSH, Context.MODE_PRIVATE);
        }

        /**
         * 初始化socket IP地址
         * 默认：192.168.10.188
         */
        public Builder socketIp(String ip) {
            sp.edit().putString("socketIp", ip).apply();
            return this;
        }

        /**
         * 初始化socket端口
         * 默认：8124
         */
        public Builder socketPort(int port) {
            sp.edit().putInt("socketPort", port).apply();
            return this;
        }

        /**
         * 是否打印socket相关信息
         * 默认：true
         */
        public Builder isLog(boolean isLog) {
            XinyiPush.isLog = isLog;
            return this;
        }

        /**
         * 连接
         *
         * @param uid   用户id
         * @param token 设备编号
         */
        public void connect(int uid, String token) {
            XinyiPush.connect(context, uid, token);
        }
    }
}
