package com.cool.mmc.manager.mapper;

import com.cool.mmc.manager.entity.BusinessApi;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BusinessApiMapper extends BaseMapper<BusinessApi> {

}
