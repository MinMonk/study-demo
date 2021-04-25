/**
 * 
 * 文件名：PassWordGenerator.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

enum CharactorType {
    LOWERCASE, UPPERCASE, NUMBER
}

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2020年4月2日 上午10:46:35
 */
public class PasswordGenerator {
    private static final int letterRange = 26;
    private static final int numberRange = 10;
    private static final Random random = new Random();
    // 密码的长度
    private int passwordLength;
    // 密码包含字符的最少种类
    private int minVariousType;

    public PasswordGenerator(int passwordLength, int minVariousType) {
        if (minVariousType > CharactorType.values().length)
            minVariousType = CharactorType.values().length;
        if (minVariousType > passwordLength)
            minVariousType = passwordLength;
        this.passwordLength = passwordLength;
        this.minVariousType = minVariousType;
    }

    public String generateRandomPassword() {
        char[] password = new char[passwordLength];
        List<Integer> pwCharsIndex = new ArrayList<Integer>();
        for (int i = 0; i < password.length; i++) {
            pwCharsIndex.add(i);
        }
        List<CharactorType> takeTypes = new ArrayList(Arrays.asList(CharactorType.values()));
        List<CharactorType> fixedTypes = Arrays.asList(CharactorType.values());
        int typeCount = 0;
        while (pwCharsIndex.size() > 0) {
            int pwIndex = pwCharsIndex.remove(random.nextInt(pwCharsIndex.size()));// 随机填充一位密码
            Character c;
            if (typeCount < minVariousType) {// 生成不同种类字符
                c = generateCharacter(takeTypes.remove(random.nextInt(takeTypes.size())));
                typeCount++;
            } else {// 随机生成所有种类密码
                c = generateCharacter(fixedTypes.get(random.nextInt(fixedTypes.size())));
            }
            password[pwIndex] = c.charValue();
        }
        return String.valueOf(password);
    }

    private Character generateCharacter(CharactorType type) {
        Character c = null;
        int rand;
        switch (type) {
        case LOWERCASE:// 随机小写字母
            rand = random.nextInt(letterRange);
            rand += 97;
            c = new Character((char) rand);
            break;
        case UPPERCASE:// 随机大写字母
            rand = random.nextInt(letterRange);
            rand += 65;
            c = new Character((char) rand);
            break;
        case NUMBER:// 随机数字
            rand = random.nextInt(numberRange);
            rand += 48;
            c = new Character((char) rand);
            break;
        }
        return c;
    }

    public static void main(String[] args) {
        System.out.println(new PasswordGenerator(32, 3).generateRandomPassword());
    }
}
