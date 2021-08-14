package com.xujialin.Utils;

import java.util.UUID;

/**
 * @author XuJiaLin
 * @date 2021/8/7 21:41
 */
public class UUidUtils {
    public static String generateuuid(){
        return UUID.randomUUID().toString().replace("-","").substring(0,15);
    }


}
