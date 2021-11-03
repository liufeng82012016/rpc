package com.my.liufeng.rpc.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author liufeng
 * @Description: 解析ip格式 ***.***.***.***:port
 * @since 2021/5/27 20:20
 */
public class IpUtil {
    private static final Pattern NUMBER_REGEX = Pattern.compile("^[0-9]*$");

    /**
     * 将地址拆分为数组 127.0.0.1:80 ---> ["127.0.0.1","80"]
     * todo 使用url解析
     */
    public static String[] splitAddress(String addr) {
        Objects.requireNonNull(addr);
        String[] addressInfo = addr.split(":");
        if (addressInfo.length != 2 || !NUMBER_REGEX.matcher(addressInfo[1]).matches()) {
            throw new RuntimeException("Invalid address " + addr);
        }
        return addressInfo;
    }
}
