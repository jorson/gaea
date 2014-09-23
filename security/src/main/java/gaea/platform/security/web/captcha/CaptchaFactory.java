package gaea.platform.security.web.captcha;


import gaea.foundation.core.config.SystemConfig;
import gaea.platform.security.support.SecurityConstant;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CaptchaFactory extends ListImageCaptchaEngine {
    private static final Logger logger = Logger.getLogger(CaptchaFactory.class);
    protected static final String NUMERIC_CHARS = "123456789";// 去掉 0

    protected static final String UPPER_ASCII_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ";// 去掉 O

    protected static final String LOWER_ASCII_CHARS = "abcdefghijkmnpqrstuvwxyz";// 去掉 ol

    /**
     * 默认的使用到的字符。
     */
    public static final String DEFAULT_CAPTCHA_CHARS = NUMERIC_CHARS + UPPER_ASCII_CHARS + LOWER_ASCII_CHARS;
    /**
     * 验证码图片宽度的默认值
     */
    public static final int DEFAULT_CAPTCHA_WIDTH = 50;
    /**
     * 验证码图片高度的默认值
     */
    public static final int DEFAULT_CAPTCHA_HEIGHT = 25;
    /**
     * 验证码字符长度最小值的默认值
     */
    public static final int DEFAULT_CAPTCHA_WORDS_MIN = 4;
    /**
     * 验证码字符长度最大值的默认值
     */
    public static final int DEFAULT_CAPTCHA_WORDS_MAX = 4;
    /**
     * 验证码字体大小最小值的默认值
     */
    public static final int DEFAULT_CAPTCHA_FONTSIZE_MIN = 18;
    /**
     * 验证码字体大小最大值的默认值
     */
    public static final int DEFAULT_CAPTCHA_FONTSIZE_MAX = 18;

    private static CaptchaFactory instance;
    /**
     * 背景信息列表
     */
    private List<BackgroundGenerator> backgroundGeneratorList;
    /**
     * 字符串信息列表
     */
    private List<TextPaster> textPasterList;
    /**
     * 字体信息列表
     */
    private List<FontGenerator> fontGeneratorList;
    private ImageCaptcha imageCaptcha = null;

    private CaptchaFactory() {
        buildInitialFactories();
    }

    public static CaptchaFactory getInstance() {
        if (instance == null) {
            instance = new CaptchaFactory();
        }
        return instance;
    }

    /**
     * 初始化参数
     */
    protected void buildInitialFactories() {
        String usedChars = SystemConfig.Instance.getProperty(SecurityConstant.CAPTCHA_CHARS, DEFAULT_CAPTCHA_CHARS);
        int width = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_WIDTH, DEFAULT_CAPTCHA_WIDTH);
        int height = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_HEIGHT, DEFAULT_CAPTCHA_HEIGHT);
        int minWords = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_WORDS_MIN, DEFAULT_CAPTCHA_WORDS_MIN);
        int maxWords = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_WORDS_MAX, DEFAULT_CAPTCHA_WORDS_MAX);
        int minFontSize = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_FONTSIZE_MIN, DEFAULT_CAPTCHA_FONTSIZE_MIN);
        int maxFontSize = SystemConfig.Instance.getIntProperty(SecurityConstant.CAPTCHA_FONTSIZE_MAX, DEFAULT_CAPTCHA_FONTSIZE_MAX);

        this.backgroundGeneratorList = new ArrayList<BackgroundGenerator>();
        this.textPasterList = new ArrayList<TextPaster>();
        this.fontGeneratorList = new ArrayList<FontGenerator>();

        this.backgroundGeneratorList.add(new GradientBackgroundGenerator(Integer.valueOf(width), Integer.valueOf(height),
                Color.PINK, Color.LIGHT_GRAY));
        this.backgroundGeneratorList
                .add(new GradientBackgroundGenerator(Integer.valueOf(width), Integer.valueOf(height), Color.WHITE, Color.RED));
        this.backgroundGeneratorList.add(new GradientBackgroundGenerator(Integer.valueOf(width), Integer.valueOf(height),
                Color.ORANGE, Color.LIGHT_GRAY));
        this.backgroundGeneratorList.add(new GradientBackgroundGenerator(Integer.valueOf(width), Integer.valueOf(height),
                Color.CYAN, Color.LIGHT_GRAY));

        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.DARK_GRAY));
        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.BLUE));
        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.GREEN));
        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.MAGENTA));
        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.BLACK));
        this.textPasterList.add(new RandomTextPaster(Integer.valueOf(minWords), Integer.valueOf(maxWords), Color.WHITE));

        this.fontGeneratorList.add(new RandomFontGenerator(Integer.valueOf(minFontSize), Integer.valueOf(maxFontSize)));

        WordGenerator wordGenerator = new RandomWordGenerator(usedChars);

        for (Iterator<FontGenerator> fontIter = fontGeneratorList.iterator(); fontIter.hasNext(); ) {
            FontGenerator font = fontIter.next();
            for (Iterator<BackgroundGenerator> backIter = backgroundGeneratorList.iterator(); backIter.hasNext(); ) {
                BackgroundGenerator back = backIter.next();
                for (Iterator<TextPaster> textIter = textPasterList.iterator(); textIter.hasNext(); ) {
                    TextPaster parser = textIter.next();
                    WordToImage word2image = new ComposedWordToImage(font, back, parser);
                    ImageCaptchaFactory factory = new GimpyFactory(wordGenerator, word2image);
                    addFactory(factory);
                }
            }
        }
    }

    /**
     * Write the captcha image of current user to the servlet response
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws java.io.IOException
     */
    public void writeCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        imageCaptcha = getNextImageCaptcha();
        HttpSession session = request.getSession();
        session.setAttribute(SecurityConstant.CAPTCHA_SESSION_KEYNAME, imageCaptcha);
        BufferedImage image = (BufferedImage) imageCaptcha.getChallenge();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "must-revalidate");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0L);
            String accept = request.getHeader("Accept");
            logger.debug("Accept:" + accept);
            if (accept.indexOf("image/png") != -1) {
                logger.debug("out write: image/png");
                response.setContentType("image/png");
                outputStream = response.getOutputStream();
                ImageIO.write(image, "png", outputStream);
            } else {
                logger.debug("out write: image/jpg");
                response.setContentType("image/jpg");
                outputStream = response.getOutputStream();
                ImageIO.write(image, "jpg", outputStream);
                // //修改Win2003报IO错误的问题
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
//                encoder.encode(image);
            }

        } catch (Exception ex) {
            logger.error("Write the captcha image error", ex);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    logger.error("OutputStream close error", ex);
                }
            }
            imageCaptcha.disposeChallenge();
        }
    }
}