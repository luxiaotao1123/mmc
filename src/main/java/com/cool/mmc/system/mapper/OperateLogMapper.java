package com.cool.mmc.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cool.mmc.system.entity.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OperateLogMapper extends BaseMapper<OperateLog> {

    @Select("select count(1) from sys_operate_log where yearweek(date_format(create_time,'%Y-%m-%d')) = yearweek(now());")
    int selectCountByCurrentWeek();

    @Select("select MONTH(create_time) as node , count(1) as val from sys_operate_log where 1 = 1 and year(create_time) = #{year} group by MONTH(create_time)")
    List<Map<String, Object>> getReportByYear(@Param("year") Integer year);

    @Select("select DAYOFMONTH(create_time) as node,count(1) as val from sys_operate_log where 1 = 1 and year(create_time) = #{year} and month(create_time) = #{month} group by DAYOFMONTH(create_time)")
    List<Map<String, Object>> getReportByMonth(@Param("year") Integer year, @Param("month") Integer month);

}
