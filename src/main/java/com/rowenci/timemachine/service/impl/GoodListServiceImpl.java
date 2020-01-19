package com.rowenci.timemachine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rowenci.timemachine.entity.GoodList;
import com.rowenci.timemachine.mapper.GoodListMapper;
import com.rowenci.timemachine.service.IGoodListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rowenci.timemachine.util.CodeInfo.CRUDCodeInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-01-19
 */
@Service
public class GoodListServiceImpl extends ServiceImpl<GoodListMapper, GoodList> implements IGoodListService {

    @Resource
    private GoodListMapper goodListMapper;

    @Resource
    private CRUDCodeInfo crudCodeInfo;

    @Override
    public int addGood(GoodList goodList) {
        goodListMapper.insert(goodList);
        return crudCodeInfo.INSERT_SUCCESS;
    }

    @Override
    public int deleteGood(GoodList goodList) {
        QueryWrapper qw = new QueryWrapper();
        Map map = new HashMap();
        map.put("user_id", goodList.getUserId());
        map.put("good_user_id", goodList.getGoodUserId());
        map.put("message_id", goodList.getMessageId());
        qw.allEq(map);
        goodListMapper.delete(qw);
        return crudCodeInfo.DELETE_SUCCESS;
    }

    @Override
    public List<GoodList> getGoodList(int user_id) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", user_id);
        List<GoodList> list = goodListMapper.selectList(qw);
        return list;
    }

}
