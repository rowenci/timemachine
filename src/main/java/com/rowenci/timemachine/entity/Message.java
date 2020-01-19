package com.rowenci.timemachine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer messageId;

    private Integer userId;

    /**
     * default : 给未来的一封信
     */
    private String title;

    private String context;

    private Integer means;

    private String target;

    private LocalDateTime sendTime;

    private LocalDateTime writeTime;

    /**
     * 0 false / 1 true
     */
    private Integer isPublic;


}
