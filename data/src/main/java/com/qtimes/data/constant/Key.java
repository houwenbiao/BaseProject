package com.qtimes.data.constant;

/**
 * Created by liutao on 2017/1/3.
 */

public class Key {
    //存放用户信息相关的key
    public interface Account {
        String USERNAME = "key_usr_username";
        String PASSWORD = "key_usr_password";
        String ACCESS_TOKEN = "key_usr_access_token";
        String DEVICE_NAME = "key_usr_device_name";
        String TOKEN_EXPIRED_IN = "key_usr_token_expired_in";
        String SEX = "key_usr_sex";
        String AGE = "key_usr_age";
        String AVATAR = "key_usr_avatar";
        String NICKNAME = "key_usr_nickname";
        String HEXCOLOR = "key_usr_hexcolor";
        String LOGIN_STATE = "key_usr_login_state";
    }

    // 存放设备信息相关的key
    public interface Device {
        String UUID = "key_dev_uuid";
        String NAME = "key_dev_name";
        String TYPE = "key_dev_type";
        String SN = "key_dev_sn";
        String OS = "key_dev_os";
        String SID = "key_dev_sid";
        String GID = "key_dev_gid";
        String PID = "key_dev_pid";
        String IS_AUTHENTICATE = "key_dev_is_authenticate";//设备认证状态
        String MODE_KEY = "key_model_key";//model_key
    }


    //APK相关的key
    public interface Apk {
        String SERVER_VERSIONCODE = "key_server_versioncode";
        String SERVER_PATCH_VERSIONCODE = "key_server_patch_versioncode";
        String PATCH_DOWNLOAD_ID = "key_patch_download_id";
    }

}
