package com.cool.mmc.manager.mapper;

import com.cool.mmc.manager.entity.PayRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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

}
