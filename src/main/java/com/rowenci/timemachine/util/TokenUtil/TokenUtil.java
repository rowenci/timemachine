package com.rowenci.timemachine.util.TokenUtil;

import com.rowenci.timemachine.util.redis.RedisUtil;

import javax.annotation.Resource;

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


}
