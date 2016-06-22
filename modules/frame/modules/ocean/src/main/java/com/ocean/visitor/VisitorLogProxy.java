package com.ocean.visitor;

import com.ocean.exception.ShardException;
import com.ocean.visitor.mysql.AbstractMysqlVisitor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * SQL解析日志打印
 */
public class VisitorLogProxy {

    private static Logger logger = LoggerFactory.getLogger(VisitorLogProxy.class);

    /**
     * 打印SQL解析调用树
     * @param target 待增强类
     * @param <T> 泛型
     * @return 增强后的新类的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T enhance(final Class<T> target) {
        if (logger.isTraceEnabled()) {
            Enhancer result = new Enhancer();
            result.setSuperclass(target);
            result.setCallback(new VisitorHandler());
            return (T) result.create();
        } else {
            try {
                return target.newInstance();
            } catch (Exception ex) {
                logger.error("create Visitor exception: {}", ex);
                throw new ShardException(ex);
            }
        }
    }

    private static class VisitorHandler implements MethodInterceptor {

        private StringBuilder hierarchyIndex = new StringBuilder();

        private Integer depth = 0;

        @Override
        public Object intercept(final Object enhancedObject, final Method method, final Object[] arguments, final MethodProxy methodProxy) throws Throwable {
            if (isPrintable(method)) {
                hierarchyIn();
                logger.trace("{}visit node: {}", hierarchyIndex, arguments[0].getClass());
                logger.trace("{}visit argument: {}", hierarchyIndex, arguments[0]);
            }
            Object result = methodProxy.invokeSuper(enhancedObject, arguments);
            if (isPrintable(method)) {
                AbstractMysqlVisitor visitor = (AbstractMysqlVisitor) enhancedObject;
                logger.trace("{}endVisit node: {}", hierarchyIndex, arguments[0].getClass());
                logger.trace("{}endVisit result: {}", hierarchyIndex, visitor.getParseContext().getParsedResult());
                logger.trace("{}endVisit condition: {}", hierarchyIndex, visitor.getParseContext().getCurrentConditionContext());
                logger.trace("{}endVisit SQL: {}", hierarchyIndex, visitor.getSqlBuilder());
                hierarchyOut();
            }
            return result;
        }

        private boolean isPrintable(final Method method) {
            return logger.isTraceEnabled() && "visit".equals(method.getName());
        }

        private void hierarchyIn() {
            hierarchyIndex.append("  ").append(++depth).append(" ");
        }

        private void hierarchyOut() {
            hierarchyIndex.delete(hierarchyIndex.length() - 3 - depth.toString().length(), hierarchyIndex.length());
            depth--;
        }
    }
}
