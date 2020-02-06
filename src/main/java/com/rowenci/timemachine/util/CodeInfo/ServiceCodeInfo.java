package com.rowenci.timemachine.util.CodeInfo;

import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/1/18 13:49
 */
@Component
public class ServiceCodeInfo {

    /* success code */
    /**
     * success
     */
    public static final int SUCCESS = 1;

    /**
     * upload message image success
     */
    public static final int UPLOAD_MESSAGE_PIC_SUCCESS = 0;

    /**
     * layui success
     */
    public static final int LAYUI_SUCCESS = 0;


    /* error code */

    /**
     * unknown error
     */
    public static final int UNKNOWN_ERROR = -99;

    /**
     * login error
     */
    public static final int LOGIN_ERROR = -1;

    /**
     * no user error
     */
    public static final int NO_USER = -1;

    /**
     * change password error
     */
    public static final int CHANGE_PASSWORD_ERROR = -1;

    /**
     * logout error
     */
    public static final int LOGOUT_ERROR = -1;

    /**
     * logup error
     */
    public static final int LOGUP_ERROR = -1;

    /**
     * upload error
     */
    public static final int UPLOAD_ERROR = -1;

    /**
     * add message error
     */
    public static final int ADD_MESSAGE_ERROR = -1;

    public String getCodeInfo(int code){
        switch (code){
            case -1 : return "insert error";
            case -2 : return "update error";
            case -3 : return "delete error";
            default : {
                if (code > 0){
                    return "operation success";
                }
                else {
                    return "unknown code";
                }
            }
        }
    }
}
