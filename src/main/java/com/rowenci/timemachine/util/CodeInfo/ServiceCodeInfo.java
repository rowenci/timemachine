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
     *
     */
    public static final int LOGOUT_ERROR = -1;

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
