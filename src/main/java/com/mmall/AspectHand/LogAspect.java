package com.mmall.AspectHand;

import com.alibaba.fastjson.JSONObject;
import com.mmall.common.ServerResponse;
import com.mmall.entity.User;
import com.mmall.utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 拦截controller下的所有方法
     */
    @Pointcut("execution(public * com.mmall.controller..*.*(..))")
    public void loginRequired() {

    }

    // 前置通知
    @Before("loginRequired()")
    public void loginBefore(JoinPoint joinPoint) {

        //获取到请求的属性
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取到请求对象
        HttpServletRequest request = attributes.getRequest();

        //URL：根据请求对象拿到访问的地址
        logger.info("url=" + request.getRequestURL());
        //获取请求的方法，是Get还是Post请求
        logger.info("method=" + request.getMethod());
        //ip：获取到访问
        logger.info("ip=" + request.getRemoteAddr());
        //获取被拦截的类名和方法名
        logger.info("class=" + joinPoint.getSignature().getDeclaringTypeName() +
                "and method name=" + joinPoint.getSignature().getName());
        //参数
        logger.info("参数=" + joinPoint.getArgs().toString());

    }

    @AfterReturning(returning = "object", pointcut = "loginRequired()")
    public void doAfterReturning(Object object) {

        System.out.println("方法的返回值 : " + object);
    }

    // 方法发生异常时执行该方法
    @AfterThrowing(throwing = "e", pointcut = "loginRequired()")
    public void throwsExecute(JoinPoint joinPoint, Exception e) {

        System.err.println("方法执行异常 : " + e.getMessage());
    }

    // 后置通知
    @After("loginRequired()")
    public void afterInform() {

        System.out.println("后置通知结束");
    }

    /**
     * 拦截器具体实现
     *
     * @param
     * @return
     */
    @Around("loginRequired()") //指定拦截器规则；
    @ResponseBody
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        //如果该方法上没有权限注解，直接调用目标方法
        if (isLoginRequired(method) == true || adminIsLoginRequired(method) == true) {
            boolean loginResult = (boolean) isLogin(request).get("isLogin");

            if (loginResult == false) {
                return ServerResponse.createByErrorCodeMessage(100, "用户未登录");
            }else {
                if ((Integer) isLogin(request).get("role") == 0 && adminIsLoginRequired(method) == true) {
                    return ServerResponse.createByErrorCodeMessage(100, "当前用户不是管理员");
                }
            }
        }
        return pjp.proceed();
    }

    /**
     * 判断一个方法普通用户是否需要登录
     *
     * @param method 方法
     * @return
     */
    private boolean isLoginRequired(Method method) {
        boolean result = false;
        if (method.isAnnotationPresent(LoginRequired.class)) {
            result = method.getAnnotation(LoginRequired.class).loginRequired();
        }
        return result;
    }

    /**
     * 判断一个方法管理员用户是否需要登录
     *
     * @param method 方法
     * @return
     */
    private boolean adminIsLoginRequired(Method method) {
        boolean result = false;
        if (method.isAnnotationPresent(AdminLoginRequired.class)) {
            result = method.getAnnotation(AdminLoginRequired.class).loginRequired();
        }
        return result;
    }


    //判断是否已经登录
    private Map<String, Object> isLogin(HttpServletRequest request) {
        String token = request.getHeader("token");
        //默认没登录普通管理员
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("role", 0);
        map.put("isLogin", false);
        if (null == token || "" == token) {
            return map;
        }
        String res = redisUtil.get(token) + "";
        User user = JSONObject.parseObject(res, User.class);
        if (user != null) {
            map.put("isLogin", true);
            if (user.getRole() == 1) {
                map.put("role", 1);
                return map;
            }
            return map;
        }
        return map;
    }
}
