package com.coin.uua.client.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @ClassName CaptchaConfig
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-05 17:09
 * @Version V1.0
 **/
@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha defaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "Arial, Courier, 宋体, 楷体, 黑体");
        properties.setProperty("kaptcha.textproducer.char.space", "2");
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        properties.setProperty("kaptcha.noise.color", "black");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackgroud");
        properties.setProperty("kaptcha.session.key", "kaptcha_session_key");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    public void test() {
        String text = defaultKaptcha().createText();
        BufferedImage image = defaultKaptcha().createImage(text);
        
    }

}
