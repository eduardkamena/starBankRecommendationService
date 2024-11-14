package pro.sky.recommendation_service.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class CacheKeyGeneratorConfig implements KeyGenerator {

    @NotNull
    public Object generate(Object target, Method method, @NotNull Object... params) {
        return target.getClass().getSimpleName() + "_"
                + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
    }

}
