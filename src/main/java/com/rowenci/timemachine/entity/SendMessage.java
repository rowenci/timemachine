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

}
