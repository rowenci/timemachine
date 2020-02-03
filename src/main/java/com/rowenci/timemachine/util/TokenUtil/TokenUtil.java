package com.rowenci.timemachine.util.TokenUtil;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/3 18:26
 */
public class TokenUtil {
    /**
     * 创建token并放入redis缓存
     * @return
     */
    public static String createToken(){
        String token = TokenProccessor.getInstance().makeToken();
        return token;
    }

    public static boolean judegToken(String token){
        if (token.isEmpty()){
            //传过来的token为空
            return false;
        }
        String db_token = "";
        if (db_token.isEmpty()){
            //缓存中token为空
            return false;
        }
        if (!token.equals(db_token)){
            //token不正确
            return false;
        }
        return true;
    }
}
