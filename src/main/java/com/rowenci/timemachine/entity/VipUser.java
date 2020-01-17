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
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VipUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer leftDays;

    /**
     * 1 true / 0 false
     */
    private Integer status;

    private Integer lever;


}
