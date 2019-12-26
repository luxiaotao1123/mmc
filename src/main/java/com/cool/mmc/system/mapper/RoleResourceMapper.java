package com.cool.mmc.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cool.mmc.system.entity.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

}
