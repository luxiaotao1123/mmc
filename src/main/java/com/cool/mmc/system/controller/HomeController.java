package com.cool.mmc.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.common.web.BaseController;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import com.cool.mmc.manager.service.PayRecordService;
import com.cool.mmc.system.entity.Role;
import com.cool.mmc.system.entity.User;
import com.cool.mmc.system.service.OperateLogService;
import com.cool.mmc.system.service.RoleService;
import com.cool.mmc.system.service.UserLoginService;
import com.cool.mmc.system.service.UserService;
import com.core.annotations.ManagerAuth;
import com.core.common.Arith;
import com.core.common.Cools;
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
public class HomeController extends BaseController {

    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private OauthService oauthService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private PayRecordService payRecordService;

    @RequestMapping("/top")
    @ManagerAuth
    public R top(){
        boolean admin = isAdmin(getUserId());
        int logWeek = payRecordService.selectOrderCountByCurrentWeek(admin?null:getUserId());
        int logTotal = payRecordService.selectOrderCount(admin?null:getUserId());
        int userTotal = userService.selectCount(new EntityWrapper<>());
        int loginWeek = userLoginService.selectCountByCurrentWeek();
        Double moneyYear = payRecordService.selectMoneyByCurrentYear(admin ? null : getUserId());
        Double totalMoney = payRecordService.selectMoney(admin ? null : getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("logTotal", logTotal);
        result.put("logWeek", logWeek);
        result.put("userTotal", userTotal);
        result.put("live", Arith.multiplys(0, Arith.divides(2, loginWeek, userTotal), 100)+"%");
        result.put("moneyYear", moneyYear==null?0.0D:moneyYear);
        result.put("totalMoney", totalMoney==null?0.0D:totalMoney);
        if(!admin){
            Oauth oauth = oauthService.selectList(new EntityWrapper<Oauth>().eq("user_id", getUserId()).eq("status", "1")).get(0);
            if(Cools.isEmpty(oauth.getRatio())){
                result.put("ratio",oauth.getRatio());
            }
            else{
                result.put("ratio",0);
            }
        }
        return R.ok(result);
    }

    /**
     * 报表统计
     * @param type 类型：1，本年月份；2，本月日分
     */
    @RequestMapping("/report")
    @ManagerAuth
    public R top(@RequestParam(defaultValue = "1", value = "type", required = false)Integer type){
        boolean admin = isAdmin(getUserId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // 访问量报表
        List<Map<String, Object>> visitReport;
        // 金额统计报表
        List<Map<String, Object>> moneyReport;
        StatsType statsType = StatsType.get(type);
        if (type == 1) {
            moneyReport = payRecordService.getReport(admin ? null : getUserId(), calendar.get(Calendar.YEAR), null);
            moneyReport = fill(moneyReport, statsType.start, statsType.end);
        } else {
            moneyReport = payRecordService.getReport(admin ? null : getUserId(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            moneyReport = fill(moneyReport, statsType.start, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }


        Map<String, Object> result = new HashMap<>();
        result.put("money", convert(moneyReport, statsType, 2));
        if(!admin){
            Oauth oauth = oauthService.selectList(new EntityWrapper<Oauth>().eq("user_id", getUserId()).eq("status", "1")).get(0);
            result.put("ratio",oauth.getRatio());
        }
        return R.ok(result);
    }
    /**
     * 报表统计
     * @param ouathId 平台Id
     */
    @RequestMapping("/ratioMoney")
    @ManagerAuth
    public Map<String,Object> ratioMoney(Integer ouathId){
        Map<String,Object> map=new HashMap<>();
        Oauth oauth = oauthService.selectOne(new EntityWrapper<Oauth>().eq("id", ouathId));
        Double totalMoney = payRecordService.selectMoney(oauth.getUserId());
        double a=1-oauth.getRatio();
        if(!Cools.isEmpty(totalMoney)){
            map.put("money",totalMoney);
            map.put("ratioMoney",totalMoney*a);
        }
        else{
            map.put("money",0);
            map.put("ratioMoney",0);
        }
        return map;

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

    private boolean isAdmin(Long userId){
        boolean result = false;
        if (userId == 9527) {
            result = true;
        } else {
            User user = userService.selectById(userId);
            Role role = roleService.selectById(user.getRoleId());
            if (role.getCode().toUpperCase().equals("ROOT") || role.getCode().toUpperCase().equals("ADMIN")) {
                result = true;
            }
        }
        return result;
    }

}
