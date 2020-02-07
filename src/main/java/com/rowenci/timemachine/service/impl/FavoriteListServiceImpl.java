package com.rowenci.timemachine.service.impl;

import com.rowenci.timemachine.entity.FavoriteList;
import com.rowenci.timemachine.mapper.FavoriteListMapper;
import com.rowenci.timemachine.service.IFavoriteListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rowenci
 * @since 2020-02-07
 */
@Service
public class FavoriteListServiceImpl extends ServiceImpl<FavoriteListMapper, FavoriteList> implements IFavoriteListService {

}
