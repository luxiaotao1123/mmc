package com.cool.mmc.manager.mapper;

import com.cool.mmc.manager.entity.PayRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PayRecordMapper extends BaseMapper<PayRecord> {

    @Select("select sum(mpr.money) as money from man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id where 1 = 1 and su.id = #{userId} and mpr.state = 3 and year(mpr.create_time) = year(now())")
    Double selectCountByCurrentYear(@Param("userId") Long userId);

    @Select("select sum(mpr.money) as money from man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id where 1 = 1 and mpr.state = 3 and year(mpr.create_time) = year(now())")
    Double selectCountByCurrentYear();

    @Select("select sum(mpr.money) as money from man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id where 1 = 1 and su.id = #{userId} and mpr.state = 3")
    Double selectMoney(@Param("userId") Long userId);

    @Select("select sum(mpr.money) as money from man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id where 1 = 1 and mpr.state = 3")
    Double selectMoney();


    // 报表 -------
    @Select("SELECT MONTH(mpr.create_time) AS node , sum(mpr.money) AS val FROM man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id WHERE 1 = 1 AND YEAR(mpr.create_time) = #{year} and mpr.state = 3 group by MONTH(mpr.create_time)")
    List<Map<String, Object>> getReportByYear(@Param("year") Integer year);

    @Select("SELECT MONTH(mpr.create_time) AS node , sum(mpr.money) AS val FROM man_pay_record mpr left join man_merchant mm on mpr.merchant_id = mm.id left join sys_user su on su.id = mm.user_id WHERE 1 = 1 AND YEAR(mpr.create_time) = #{year} and mpr.state = 3 and su.id = #{userId} group by MONTH(mpr.create_time)")
    List<Map<String, Object>> getReportByYear(@Param("userId") Long userId, @Param("year") Integer year);

    @Select("SELECT DAYOFMONTH(mpr.create_time) AS node , sum(mpr.money) AS val FROM man_pay_record mpr LEFT JOIN man_merchant mm ON mpr.merchant_id = mm.id LEFT JOIN sys_user su ON su.id = mm.user_id WHERE 1 = 1 AND YEAR(mpr.create_time) = #{year} AND MONTH(mpr.create_time) = #{month} AND mpr.state = 3 GROUP BY DAYOFMONTH(mpr.create_time)")
    List<Map<String, Object>> getReportByMonth(@Param("year") Integer year, @Param("month") Integer month);

    @Select("SELECT DAYOFMONTH(mpr.create_time) AS node , sum(mpr.money) AS val FROM man_pay_record mpr LEFT JOIN man_merchant mm ON mpr.merchant_id = mm.id LEFT JOIN sys_user su ON su.id = mm.user_id WHERE 1 = 1 AND YEAR(mpr.create_time) = #{year} AND MONTH(mpr.create_time) = #{month} AND mpr.state = 3 AND su.id = #{userId} GROUP BY DAYOFMONTH(mpr.create_time)")
    List<Map<String, Object>> getReportByMonth(@Param("userId") Long userId, @Param("year") Integer year, @Param("month") Integer month);

}
