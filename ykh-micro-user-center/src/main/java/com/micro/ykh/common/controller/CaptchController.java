package com.micro.ykh.common.controller;

import com.google.code.kaptcha.Producer;
import com.micro.ykh.common.sms.R;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.utils.sign.Base64;
import com.micro.ykh.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CaptchController
 * @Description 获取验证码
 * @Author xiongh
 * @Date 2020/12/22 11:55
 * @Version 1.0
 **/
@RestController
public class CaptchController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/captcha_image")
    public R getCode() throws IOException {

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = FwtConstant.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码

        capStr = code = captchaProducer.createText();
        image = captchaProducer.createImage(capStr);


        redisTemplate.opsForValue().set(verifyKey, code, FwtConstant.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return R.error(e.getMessage());
        }

        Map<String,Object> map = new HashMap<>();
        map.put("uuid",uuid);
        map.put("img", Base64.encode(os.toByteArray()));
        return R.ok(map);
    }
}
