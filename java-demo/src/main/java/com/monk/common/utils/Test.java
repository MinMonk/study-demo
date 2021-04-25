/**
 * 
 * 文件名：Test.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.utils;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年12月27日 下午6:57:15
 */
public class Test {
    
    public static void main(String[] args) {
        String str = "{changeTime:\"2018-09-12 00:00:00\",acckey:\"y47681jkx9021xzoq0028663gfropl186043\"}";
        String out = JsonFormart(str, 3);
        System.out.println(out);
    }
    
    /**
     * 格式化JSON字符串
     * @param jsonStr json字符串
     * @param level 缩进等级
     * @return 格式化后的JSON字符串
     * @author Monk
     * @date 2019年12月27日 下午7:13:06
     */
    public static String JsonFormart(String jsonStr, int level) {
        // 存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        jsonForMatStr.append("\n");
        for (int index = 0; index < jsonStr.length(); index++)// 将字符串中的字符逐个按行输出
        {
            // 获取s中的每个字符
            char c = jsonStr.charAt(index);
            // System.out.println(s.charAt(index));

            // level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            // 遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
            case '{':
            case '[':
                jsonForMatStr.append(c + "\n");
                level++;
                break;
            case ',':
                jsonForMatStr.append(c + "\n");
                break;
            case '}':
            case ']':
                jsonForMatStr.append("\n");
                level--;
                jsonForMatStr.append(getLevelStr(level));
                jsonForMatStr.append(c);
                break;
            default:
                jsonForMatStr.append(c);
                break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

}
