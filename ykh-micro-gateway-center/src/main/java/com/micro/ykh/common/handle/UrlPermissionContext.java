package com.micro.ykh.common.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.ykh.type.PlatformTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.micro.ykh.utils.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName UrlPermissionContext
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/8 14:07
 * @Version 1.0
 **/
@Component
public class UrlPermissionContext {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlatformUrlPermission platformUrlPermission;

    @Autowired
    private PropertyServiceUrlPermission propertyServiceUrlPermission;

    @Autowired
    private ShopAppUrlPermission shopAppUrlPermission;

    @Autowired
    private ClientAppUrlPermission clientAppUrlPermission;

    public UrlPermissionInterface init(String token) throws JsonProcessingException {
        UrlPermissionInterface urlPermissionInterface = null;
        PlatformTypeEnum typeEnum = getPlatformTypeEnum(token);
        assert typeEnum != null;
        switch (typeEnum) {
            case PLATFORM:
                urlPermissionInterface = platformUrlPermission;
                break;
            case PROPERTY_SERVICE:
                urlPermissionInterface = propertyServiceUrlPermission;
                break;
            case CLIENT_APP:
                urlPermissionInterface = clientAppUrlPermission;
                break;
            case SHOP_APP:
                urlPermissionInterface = shopAppUrlPermission;
                break;
            default:
                break;
        }
        return urlPermissionInterface;
    }

    /**
     * 根据platformType获得枚举类型
     *
     * @param token token
     * @return PlatformTypeEnum
     * @throws JsonProcessingException
     */
    private PlatformTypeEnum getPlatformTypeEnum(String token) throws JsonProcessingException {
        String userJson = (String) redisTemplate.opsForValue().get(token);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
        assert userJson != null;
        Map<String, String> userMap = objectMapper.readValue(userJson, javaType);
        Optional<PlatformTypeEnum> optionalPlatformTypeEnum = EnumUtils.getEnumObject(PlatformTypeEnum.class, e -> e.getValue().equals(userMap.get("platformType")));
        return optionalPlatformTypeEnum.orElse(null);
    }
}
