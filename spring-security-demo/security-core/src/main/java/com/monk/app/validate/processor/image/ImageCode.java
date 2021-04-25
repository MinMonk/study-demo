package com.monk.app.validate.processor.image;

import com.monk.app.validate.bean.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @ClassName ImageCode
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
public class ImageCode extends ValidateCode {

    private BufferedImage bufferedImage;

    public ImageCode(String code, long expireIn, BufferedImage bufferedImage) {
        super(code, expireIn);
        this.bufferedImage = bufferedImage;
    }

    public ImageCode(String code, LocalDateTime expireTime, BufferedImage bufferedImage) {
        super(code, expireTime);
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

}
