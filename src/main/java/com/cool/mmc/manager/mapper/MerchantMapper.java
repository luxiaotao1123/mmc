package com.cool.mmc.manager.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cool.mmc.manager.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MerchantMapper extends BaseMapper<Merchant> {

    Merchant poll(@Param("productId")Long productId);

}
