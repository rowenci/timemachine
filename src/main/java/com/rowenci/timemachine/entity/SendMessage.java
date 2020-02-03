package com.rowenci.timemachine.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 *  message send to front
 * </p>
 *
 * @author rowenci
 * @since 2020/1/18 13:52
 */
@Data
@Component
public class SendMessage implements Serializable {

    private int code;

    private String result;

    private Object data;

    private String description;

    public SendMessage initMessage(SendMessage sendMessage, int code, Object data,String result, String description){
        sendMessage.setCode(code);
        sendMessage.setResult(result);
        sendMessage.setData(data);
        sendMessage.setDescription(description);
        return sendMessage;
    }

}
