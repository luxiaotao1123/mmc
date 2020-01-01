package com.cool.mmc.common.utils;

import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.pay.TPaymentService;
import com.core.common.SpringUtils;

/**
 * Created by vincent on 2019-04-19
 */
public class PayUtils {

	//服务名后缀
	private static final String serviceSuffix = "Service";

	/**
	 * 根据公司名称获取相应的支付服务
	 * @param company
	 * @return
	 */
	public static TPaymentService getPaymentService(PayCompanyType company) {
		return (TPaymentService) SpringUtils.getBean(company.toString() + serviceSuffix);
	}

}
