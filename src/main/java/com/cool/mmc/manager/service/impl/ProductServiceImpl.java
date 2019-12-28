package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.ProductMapper;
import com.cool.mmc.manager.entity.Product;
import com.cool.mmc.manager.service.ProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
