package com.example.mshudu;
/**
 * Created by Administrator on 2016/3/14.
 */
public class Game {
    private int[][] sudoku;
    //保存每个数字是否是用户输入的
    private boolean[][] isNewNumber = new boolean[9][9];
    //用来存储每个单元格不可使用的数字
    private int used[][][] = new int[9][9][];
    boolean finished=false;

    public Game() {
        getBaseSudoku();
        sudoku = initSudoku(sudoku, 15);
        calculateAllUsedNums();
    }

    /**
     * 初始化为最原始数独数组
     */
    public void getBaseSudoku() {
        sudoku = new int[][]{
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };
    }

    /**
     * 获取数独指定位置的数字
     * @param x
     * @param y
     * @return int
     */
    private int getNum(int x, int y) {
        return sudoku[x][y];
    }

    /**
     * 获取数独指定位置的数字并转为字符串
     * @param x
     * @param y
     * @return String
     */
    public String getNumStr(int x, int y) {
        int v = getNum(x, y);
        if (v == 0) {
            return "";
        }
        return String.valueOf(v);
    }

    /**
     * 返回数独中指定位置是否可以点击输入
     * @param x
     * @param y
     * @return boolean
     */
    public boolean isAbleToEdit(int x, int y) {
        return isNewNumber[x][y];
    }

    /**
     * 初始化随机数独
     * 随机交换每一行中两个数的位置，打乱数独
     * @param numsArray 原始数独数组
     * @param times 交换次数
     * @return 新的随机数独数组
     */
    private int[][] initSudoku(int[][] numsArray,int times) {
        //交换每一行中这两个数字的位置
        for (int i=0; i<times; i++) {
            //随机生成两个需要交换的数
            int a = (int) ((9) * Math.random() + 1);
            int b = (int) ((9) * Math.random() + 1);
            while (a == b) {
                b = (int) ((9) * Math.random() + 1);
            }

            for (int j=0; j<9; j++) {
                exchangeR(numsArray[j], a, b);
            }

        }
        //交换每一列中两个数字的位置
        for (int i=0; i<times; i++) {
            int a = (int) ((9) * Math.random() + 1);
            int b = (int) ((9) * Math.random() + 1);
            while (a == b) {
                b = (int) ((9) * Math.random() + 1);
            }

            for (int j=0; j<9; j++) {
                exchangeC(numsArray, j, a, b);
            }
        }
        digHoles(20, false);
        return numsArray;
    }

    /**
     * 一维数组中指定的两个数字交换位置
     * @param nums 给定的数组
     * @param a 数字
     * @param b 数字
     * @return 新的数组
     */
    private int[] exchangeR(int[] nums, int a, int b) {
        int temp = 0;
        for (int i=0; i<9; i++) {
            if (temp != 0 && (nums[i] == a || nums[i] == b)) {
                nums[i] = temp;
            } else if (nums[i] == a) {
                temp = a;
                nums[i] = b;
            } else if (nums[i] == b) {
                temp = b;
                nums[i] = a;
            }
        }
        return nums;
    }

    /**
     * 二维数组交换同一列中指定的两个数字的位置
     * @param nums 给定的二维数组
     * @param col 列
     * @param a 数字
     * @param b 数字
     * @return 新的二维数组
     */
    private int[][] exchangeC(int[][] nums, int col, int a, int b) {
        int temp = 0;
        for (int i=0; i<9; i++) {
            if (temp != 0 && (nums[i][col] == a || nums[i][col] == b)) {
                nums[i][col] = temp;
            } else if (nums[i][col] == a) {
                temp = a;
                nums[i][col] = b;
            } else if (nums[i][col] == b) {
                temp = b;
                nums[i][col] = a;
            }
        }
        return nums;
    }

    /**
     * 给数独棋盘挖坑
     * @param nums 挖坑数量或初始数字数量
     * @param isHoles 是否是坑的数量，false则表示初始数字的数量
     */
    private void digHoles(int nums, boolean isHoles) {
        int holes = isHoles?nums:81-nums;
        int holesPerRow = holes/9;
        int rows[][] = new int [1][9];
        changeTo(rows, 0, -1, holes%9);
        for (int i=0; i<9; i++) {
            changeTo(this.sudoku, i, 0, rows[0][i] == 0? holesPerRow: holesPerRow + 1);
        }
    }

    /**
     * 将二维数组中某个数组的随机几个数字改成目标数字
     * @param arr 二维数组
     * @param index 一维数组位置
     * @param target 目标数字
     * @param count 随机元素个数
     */
    private void changeTo(int[][] arr, int index, int target, int count) {
        for (int i=0; i<count; i++) {
            int rand = (int) (Math.random()*arr[index].length);
            if (arr[index][rand] == target) {
                i--;
            } else {
                arr[index][rand] = target;
                if (arr.length > 1) {
                    this.isNewNumber[index][rand] = true;
                }
            }
        }
    }

    /**
     * 计算数独中每个位置不可使用的数字
     */
    public void calculateAllUsedNums() {
        for (int x=0; x<9; x++) {
            for (int y=0; y<9; y++) {
                used[x][y] = calculateUsedNums(x, y);
            }
        }
    }

    /**
     * 计算数独中指定位置不可使用的数字
     * @param x
     * @param y
     * @return
     */
    public int[] calculateUsedNums(int x, int y) {
        int nums[] = new int[9];

        //添加该位置所在列已经使用过的数字
        for (int i=0; i<9; i++) {
            if (i == x) {
                continue;
            }
            int temp = getNum(i, y);
            if (temp != 0) {
                nums[temp-1] = temp;
            }
        }

        //添加该位置所在行已经使用过的数字
        for (int i=0; i<9; i++) {
            if (i ==y) {
                continue;
            }
            int temp = getNum(x, i);
            if (temp !=0) {
                nums[temp-1] = temp;
            }
        }

        //添加该位置所在3*3方格中已经使用过的数字
        int startX = (x/3)*3;
        int startY = (y/3)*3;
        for (int i=startX; i<startX+3; i++) {
            for (int j=startY; j<startY+3; j++) {
                if (i==x && j==y) {
                    continue;
                }
                int temp = getNum(i, j);
                if (temp !=0) {
                    nums[temp-1] = temp;
                }
            }
        }

        //删掉usedNums中的0
        int unused = 0;
        for (int i:nums) {
            if (i != 0) {
                unused ++;
            }
        }
        int usedNums[] = new int[unused];
        unused = 0;
        for (int i:nums) {
            if (i != 0) {
                unused ++;
                usedNums[unused-1] = i;
            }
        }
        return usedNums;
    }

    /**
     * 获取数独中指定位置不可使用的数字数组
     * @param x
     * @param y
     * @return int[]
     */
    protected int[] getUsedNumbers(int x, int y) {
        return used[x][y];
    }

    /**
     * 数独中设置指定位置的数字
     * @param x
     * @param y
     * @param number
     */
    private void setNumber(int x, int y, int number) {
        sudoku[x][y] = number;
    }

    public boolean setNumberIfValid(int x, int y, int value) {
        int numbers[] = getUsedNumbers(x, y);
        if (value != 0) {
            for (int number:numbers) {
                if (value == number) {
                    return false;
                }
            }
        }
        setNumber(x, y, value);
        calculateAllUsedNums();
        return true;
    }
    public void  reduNumber(int x,int y,int value){


//        int numbers[]=getUsedNumbers(x,y);
        setNumber(x,y,0);
        calculateAllUsedNums();
//        return true;
    }
    public int[][] getAllNumber(){
        return sudoku;
    }

    public boolean isFinishedGame(){
        for (int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if (sudoku[i][j]==0)

                    return false;
            }
        finished=true;
        return finished;
    }
}