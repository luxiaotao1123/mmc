package com.cool.mmc.manager.mapper;

import com.cool.mmc.manager.entity.Merchant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MerchantMapper extends BaseMapper<Merchant> {

    @Select("select * from man_merchant where 1=1 and product_id=2 order by id desc limit 0,1")
    Merchant poll(@Param("productId")Long productId);

}
