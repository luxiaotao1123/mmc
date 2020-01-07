package com.cool.mmc.common;

import com.core.generators.CoolGenerator;

/**
 * Created by vincent on 2019-06-04
 */
public class CodeBuilder {

    public static void main(String[] args) throws Exception {
        CoolGenerator generator = new CoolGenerator();
        generator.url="47.111.25.152:3306/mmc";
        generator.username="root";
        generator.password="liU8huhUmznydHlq";
        generator.table="man_timer";
        generator.packagePath="com.cool.mmc.manager";
        generator.build();
    }

}
