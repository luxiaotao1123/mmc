package com.cool.mmc.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.system.service.OperateLogService;
import com.cool.mmc.system.service.UserLoginService;
import com.cool.mmc.system.service.UserService;
import com.core.common.Arith;
import com.core.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by vincent on 2019-12-06
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginService userLoginService;

    @RequestMapping("/top")
    public R top(){
        int logTotal = operateLogService.selectCount(new EntityWrapper<>());
        int logWeek = operateLogService.selectCountByCurrentWeek();
        int userTotal = userService.selectCount(new EntityWrapper<>());
        int loginWeek = userLoginService.selectCountByCurrentWeek();

        Map<String, Object> result = new HashMap<>();
        result.put("logTotal", logTotal);
        result.put("logWeek", logWeek);
        result.put("userTotal", userTotal);
        result.put("live", Arith.multiplys(0, Arith.divides(2, loginWeek, userTotal), 100)+"%");
        return R.ok(result);
    }


    @RequestMapping("/report")
    public R top(@RequestParam(defaultValue = "1", value = "type", required = false)Integer type){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        List<Map<String, Object>> report;
        StatsType statsType = StatsType.get(type);
        if (type == 1) {
            report = operateLogService.getReport(calendar.get(Calendar.YEAR), null);
            report = fill(report, statsType.start, statsType.end);
        } else {
            report = operateLogService.getReport(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            report = fill(report, statsType.start, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }


        Map<String, Object> result = new HashMap<>();
        result.put("visits", convert(report, statsType, 2));
        return R.ok(result);
    }


    /**
     * 自动补零
     */
    private List<Map<String, Object>> fill(List<Map<String, Object>> list, int start, int end){
        for (int i = start ; i <= end; i++){
            boolean exist = false;
            for (Map seq : list){
                if (Integer.parseInt(String.valueOf(seq.get("node"))) == i){
                    exist = true;
                }
            }
            if (!exist){
                HashMap<String, Object> map = new HashMap<>();
                map.put("node", i);
                map.put("val", 0);
                list.add(map);
            }
        }
        list.sort(Comparator.comparingInt(o -> (int) o.get("node")));
        return list;
    }

    /**
     * x轴单位转换
     * @param dot y轴数值保留小数位
     */
    private List<Map<String, Object>> convert(List<Map<String, Object>> list, StatsType statsType, int dot){
        for (Map<String, Object> map : list){
            Object val = map.get("val");
            map.put("val", Arith.multiplys(dot, 1, (Number) val));
            Object node = map.get("node");
            switch (statsType){
                case MONTH:
                    map.put("node", node + "号");
                    break;
                case YEAR:
                    map.put("node", node + "月");
                    break;
                default:
                    break;
            }
        }
        return list;
    }

    enum StatsType{

        YEAR(1,1, 12),
        MONTH(2,1, 30),
        ;

        int id;
        int start;
        int end;
        StatsType(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

        static StatsType get(int id) {
            StatsType[] values = StatsType.values();
            for (StatsType statsType : values){
                if (statsType.id == id){
                    return statsType;
                }
            }
            throw new RuntimeException("找不到StatsType类型");
        }

    }

}
