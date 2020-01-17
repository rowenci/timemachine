package com.rowenci.timemachine.util.CodeInfo;

/**
 * <p>
 *  code list
 * </p>
 *
 * @author rowenci
 * @since 2020/1/17 21:15
 */
public class CodeInfo {

    /* success code */

    /**
     * select success
     */
    public static final int SELECT_SUCCESS = 1;

    /**
     * insert success
     */
    public static final int INSERT_SUCCESS = 2;

    /**
     * update success
     */
    public static final int UPDATE_SUCCESS = 3;

    /**
     * delete success
     */
    public static final int DELETE_SUCCESS = 4;


    /* error code */

    /**
     * unknown error
     */
    public static final int UNKNOWN_ERROR = -99;

    /**
     * entity already exists
     */
    public static final int ENTITY_EXISTS = 0;

    /**
     * entity not exists
     */
    public static final int ENTITY_NOT_EXISTS = -1;

    /**
     * insert error
     */
    public static final int INSERT_ERROR = -2;

    /**
     * update error
     */
    public static final int UPDATE_ERROR = -3;

    /**
     * delete error
     */
    public static final int DELETE_ERROR = -4;

    /**
     * login error
     */
    public static final int LOGIN_ERROR = -1;

    public String getCodeInfo(int code){
        switch (code){
            case -1 : return "insert error";
            case -2 : return "update error";
            case -3 : return "delete error";
            default : return "unknown code";
        }
    }

}
