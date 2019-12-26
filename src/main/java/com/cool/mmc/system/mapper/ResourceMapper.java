package com.cool.mmc.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cool.mmc.system.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

}
