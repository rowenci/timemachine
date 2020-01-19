package com.rowenci.timemachine.service;

import com.rowenci.timemachine.entity.GoodList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
public interface IGoodListService extends IService<GoodList> {

    int addGood(GoodList goodList);

    int deleteGood(GoodList goodList);

    List<GoodList> getGoodList(int user_id);

}
