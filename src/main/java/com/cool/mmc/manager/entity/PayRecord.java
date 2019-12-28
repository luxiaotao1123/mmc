package com.cool.mmc.manager.entity;

import com.core.common.Cools;import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.core.common.SpringUtils;
import com.cool.mmc.manager.service.ProductService;
import com.baomidou.mybatisplus.annotations.TableField;
import com.core.common.SpringUtils;
import com.cool.mmc.manager.service.MerchantService;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("man_pay_record")
public class PayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属接口
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 所属商户
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 金额
     */
    private Double money;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

    public PayRecord() {}

    public PayRecord(Long productId,Long merchantId,Double money,Date createTime) {
        this.productId = productId;
        this.merchantId = merchantId;
        this.money = money;
        this.createTime = createTime;
    }

//    PayRecord payRecord = new PayRecord(
//            null,    // 所属接口[非空]
//            null,    // 所属商户[非空]
//            null,    // 金额[非空]
//            null    // 添加时间[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductId$(){
        ProductService service = SpringUtils.getBean(ProductService.class);
        Product product = service.selectById(this.productId);
        if (!Cools.isEmpty(product)){
            return product.getName();
        }
        return null;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getMerchantId$(){
        MerchantService service = SpringUtils.getBean(MerchantService.class);
        Merchant merchant = service.selectById(this.merchantId);
        if (!Cools.isEmpty(merchant)){
            return merchant.getName();
        }
        return null;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCreateTime$(){
        if (Cools.isEmpty(this.createTime)){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}