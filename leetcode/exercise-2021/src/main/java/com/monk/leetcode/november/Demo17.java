package com.monk.leetcode.november;

/**
 * 递归实现八皇后Demo
 */
public class Demo17 {

    /**
     * 地图的单元格数
     */
    private final static int cellNum = 8;

    /**
     * 共有多少种摆放方法
     */
    private static int num = 1;

    /**
     * 定义一个二维数组,当做地图
     * 0表示该位置没有放置皇后
     * 1表示该位置放置了皇后
     */
    private static int[][] map = new int[cellNum][cellNum];

    public static void main(String[] args) {
        play(0);
    }

    /**
     * 放置皇后
     * @param row 行号
     */
    private static void play(int row){
        for (int i = 0; i < cellNum; i++) {
            if(check(row, i)){
                // 如果这个位置可以放置皇后就放置皇后
                map[row][i] = 1;
                if(row == cellNum -1){
                    //如果是最后一行了,说明八皇后放置好了,就输出打印出来
                    print();

                    // 为了还可以继续打印其他八皇后的摆放方法,在当期八皇后摆放好后,去掉上一行的皇后,继续下一种摆放方法的递归
                    // map[row][i] = 0;
                }else{
                    // 如果不是最后一行,就进行下一行的皇后摆放
                    play(row+1);

                    // 如果当前行不能摆放皇后,那么就取消上一行的皇后位置,换一个位置继续摆放
                    // map[row][i] = 0;
                }
                // 由于43和49两行代码分别在if-else中,故提到if-else的外面简化代码
                map[row][i] = 0;
            }
        }
    }

    /**
     * 检查该坐标能否放置皇后
     * @param row   行号
     * @param column   列号
     * @return 该坐标能否放置皇后
     */
    private static boolean check(int row, int column){
        /**
         * 正常检查地图上的一个点位是否能够放置皇后,需要检查东,东南,南,西南,西,西北,东北.北八个方向,
         * 但是由于程序是一行一行的放置皇后,所有在程序中需要检查的位置,只有三个方向(西北,北,东北),
         * 而其他的五个方向无须检查,程序设定每一行只会放置一个黄后,故正东和正西方向无须检查,
         * 而西南,正南,东南三个方向还没有开始放置皇后,所以是空的,就不需要进行检查
         */

        /**
         * 1. 检查西北方向的格子是否可以放置皇后
         * 坐标的变化规则:行号,列号同时减小
         */
        for(int i = row-1, j = column-1; i >=0 && j>=0; i--,j--){
            if(1 == map[i][j]){
                return false;
            }
        }

        /**
         * 2. 检查正北方向的格子是否可以放置皇后
         * 坐标的变化规则:行号减小,列号不变
         */
        for (int i = row - 1; i >= 0; i--) {
            if(1 == map[i][column]){
                return false;
            }
        }


        /**
         * 3. 检查东北方向的格子是否可以放置皇后
         * 坐标的变化规则:行号变小,列号变大
         */
        for(int i = row-1, j = column+1; i>=0 && j <= cellNum-1; i--,j++){
            if(1 == map[i][j]){
                return false;
            }
        }

        return true;
    }


    /**
     * 打印八皇后地图
     */
    private static void print(){
        System.out.println("第" + num + "种摆放方法");
        num++;
        for (int i = 0; i < cellNum; i++) {
            for (int j = 0; j < cellNum; j++) {
                System.out.printf("%3d", map[i][j]);
            }
            System.out.println();
        }
    }
}
