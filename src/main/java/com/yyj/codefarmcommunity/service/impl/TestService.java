package com.yyj.codefarmcommunity.service.impl;

import com.yyj.codefarmcommunity.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * 测试服务
 */
@Service
public class TestService {
    
    /**
     * 测试方法
     * @param name 名称
     * @return 结果
     */
    public String test(String name) {
        return "Hello, " + name;
    }
    
    /**
     * 测试异常方法
     * @param message 异常消息
     * @throws BusinessException 异常
     */
    public void testException(String message) throws BusinessException {
        throw new BusinessException(500, message);
    }
    
    /**
     * 测试计算方法
     * @param a 第一个数
     * @param b 第二个数
     * @return 计算结果
     */
    public int calculate(int a, int b) {
        return a + b;
    }
}
