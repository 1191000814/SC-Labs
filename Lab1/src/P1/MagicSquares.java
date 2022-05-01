package src.P1;

import java.io.*;

public class MagicSquares {
    public static void main(String[] args) throws IOException{
        String inputFileName = "D:\\Download\\Spring2021_HITCS_SC_Lab1-master\\P1\\5.txt";
        int[][] square = readSquare(inputFileName);
        print(square);
        System.out.println(isLegalMagicSquare(inputFileName));
        String outputFileName = "6.txt";
        writeSquare(generateMagicSquare(5),outputFileName);
        System.out.println(isLegalMagicSquare(outputFileName));
    }

    /**
     * 判别一个矩阵是不是MagicSquare
     *
     * @param fileName 打开的文件名
     * @return 是否幻方矩阵
     * @throws IOException 输入异常
     */
    public static boolean isLegalMagicSquare(String fileName) throws IOException{
        //首先先把文件中的字符串读到数组中
        int[][] square;
        //若是空文件数组则无需判断,直接返回false
        if((square = readSquare(fileName)) == null)
            return false;
        int i, j, sum = 0, sum1, n = square.length;
        for(i = 0; i < n; i++) // 计算sum
            sum += square[0][i];

        for(i = 0; i < n; i++){ // 计算行
            sum1 = 0;
            for(j = 0; j < n; j++)
                sum1 += square[i][j];
            if(sum1 != sum)
                return false;
        }

        for(j = 0; j < n; j++){ // 计算列
            sum1 = 0;
            for(i = 0; i < n; i++)
                sum1 += square[i][j];
            if(sum1 != sum)
                return false;
        }

        sum1 = 0;
        for(i = 0; i < n; i++) // 反对角线
            sum1 += square[i][i];
        if(sum1 != sum)
            return false;

        sum1 = 0;
        for(i = 0; i < n; i++){ // 正对角线
            sum1 += square[i][n - i - 1];
        }
        return sum1 == sum;
    }

    /**
     * 从把文件内容中读到矩阵中
     * <p>
     * 自己写了一个读文件专用函数,没有使用readLine和split函数
     *
     * @param fileName 输入的文件名
     * @return 文件读出的矩阵
     * @throws IOException 输入异常
     */
    public static int[][] readSquare(String fileName) throws IOException{
        File file = new File(fileName);
        int n = 1;//数组维度
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(fileName);
        }catch(FileNotFoundException e){
            System.out.println("该文件路径不存在");
            System.exit(0);
        }
        char c;
        //第一遍读字符,求数组维度,按制表符数量
        while((c = (char)fis.read()) != '\n'){
            if(c == '\t')
                n++;
        }
        fis.close();//清除上次的读取过程
        //特别注意每行的结尾两个字符是\n,没有\t
        fis = new FileInputStream(file);
        int[][] square = new int[n][n];// 获取n的值,便可以声明数组了
        int i = 0, j = 0, t;
        StringBuilder num;
        //读入全部内容
        while(i < n){
            num = new StringBuilder();
            t = fis.read();
            while(t >= 48 && t <= 57){
                num.append((char)t);
                t = fis.read();
            }
            if(t == 10 && j != n - 1){
                System.out.println("第" + (i + 1) + "行的元素数量不正确");
                return null;
            }else if(t != 9 && t != 10 && t != -1){
                System.out.println("第" + (i + 1) + "行的第" + (j + 1) + "个元素后不是'\\t'");
                return null;
            }
            try{
                if(num.length() > 0)
                    square[i][j] = Integer.parseInt(String.valueOf(num));
            }catch(NumberFormatException e){
                System.out.println("第" + (i + 1) + "行第" + (j + 1) + "个元素后输入了不是数字的符号");
                return null;
            }
            j++;
            if(j == n){//读完一行换一行
                i++;
                j = 0;
            }
        }
        fis.close();
        return square;
    }

    /**
     * 把一个二维数组按上述格式写到一个文本文件中
     *
     * @param square 二维数组名
     * @param fileName 输入的文件名
     * @throws IOException 输入异常
     */
    public static void writeSquare(int[][] square,String fileName) throws IOException{
        if(square == null){
            System.out.println("读入的文件是空的");
            return;
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        int n = square.length, i, j;
        for(i = 0; i < n; i++){
            for(j = 0; j < n; j++){
                fos.write(String.valueOf(square[i][j]).getBytes());
                if(j < n-1)
                    fos.write('\t');
                else
                    fos.write('\n');
            }
        }
    }

    /**
     * 根据n维度生成一个MagicSquare
     *
     * @param n 矩阵维度
     * @return 一个矩阵
     * @throws ArrayIndexOutOfBoundsException 数组越界异常
     */
    public static int[][] generateMagicSquare(int n) throws ArrayIndexOutOfBoundsException{
        // 该函数的功能可总结为,把1-n^2个数一次放入矩阵中,先选定一个起始位置,
        // 如果上一次的位置是(x,y),则下一次的位置是(x-1,y+1),并合适地加减5使x,y在[0,4]之间
        // 此规律使生成的矩阵是幻方矩阵
        int[][] magic = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;//row:行 col:列 square:元素数量
        for(i = 1; i <= square; i++){
            try{
                magic[row][col] = i;
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("数组的维度n不能是偶数");
                return null;
            }catch(NegativeArraySizeException e){
                System.out.println("数组的维度不能是负数");
                return null;
            }
            if(i % n == 0)// 已经到了这行最后一个,就跳到下一行
                row++;
            else{// 当i不是这行最后一个时
                if(row == 0)// 每次都把row减1,如果row已经是第一行,就把它移到最后一行
                    row = n - 1;
                else
                    row--;
                if(col == (n - 1))// 每次都把col加1,如果col已经是最后一行,就把它移到第一行
                    col = 0;
                else
                    col++;
            }
        }
        for(i = 0; i < n; i++){//打印magic数组
            for(j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }
        return magic;
    }

    /**
     * 将二维数组按人性化的格式打印出来
     *
     * @param square 二维数组名
     */
    public static void print(int[][] square){
        if(square == null){
            System.out.println("二维数组是空的");
            return;
        }
        for(int[] intA: square)
            for(int j = 0; j < square.length; j++){
                System.out.print(intA[j] + " ");
                if(j == square.length - 1)
                    System.out.println();
            }
    }
}