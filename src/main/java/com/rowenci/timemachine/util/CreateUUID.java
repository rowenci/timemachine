package com.rowenci.timemachine.util;

import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/6 16:14
 */
public class CreateUUID {
    public String create(){
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString();
        String id = "";
        if (uid != null && !"".equals(uid)) {
            for (int i = 0; i < uid.length(); i++) {
                if (uid.charAt(i) >= 48 && uid.charAt(i) <= 57) {
                    id += uid.charAt(i);
                }
            }
        }
        return id;
    }
}
