package com.difeng.mines;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MineSweep {
	public int zoneX;          //雷区的起始位置
	public int zoneY;
	public int zoneDimesion;  //雷区大小
	private int MinesNum;                    //雷的数目
	private int rows;                        //雷盘的行数
	private int cols;                        //雷盘的列数
	private int [][] arr;                    //雷盘的初始矩阵
	private int [][] markArr;                //游戏过程中的用于中间计算的矩阵
	private int [][] safeArr;
	boolean isRun = false;   //游戏结束的标志
	boolean preOk = false;
	private Random rand=new Random();        //随机数生成器
	GameView gameView;
	private Bitmap minesBitmap;
	boolean isWon = false;
	public MineSweep(GameView gameView){
		this.gameView = gameView;
		MinesNum = 10;
		rows = 9;
		cols = 9;
		arr = new int[9][9];
		markArr = new int[9][9];
		safeArr = new int[9][9];
		zoneX = 10;          //雷区的起始位置
		zoneY = 10;
		zoneDimesion = Config.SCREEN_WIDTH - 20;
		minesBitmap = BitmapFactory.decodeResource(gameView.getResources(),R.drawable.mines);
		minesBitmap = Bitmap.createScaledBitmap(minesBitmap,Config.CELL_DIMENSION,Config.CELL_DIMENSION,true);
		//初始化雷区
		initGame();
	}

	public MineSweep(int MinesNum, int rows, int cols,GameView gameView) {
		this.gameView = gameView;
		this.MinesNum = MinesNum;
		this.rows = rows;
		this.cols = cols;
		arr=new int[rows][cols];
		markArr=new int[rows][cols];
		safeArr = new int[9][9];
		zoneX = 10;          //雷区的起始位置
		zoneY = 10;
		zoneDimesion = Config.SCREEN_WIDTH - 20;
		minesBitmap = BitmapFactory.decodeResource(gameView.getResources(),R.drawable.mines);
		minesBitmap = Bitmap.createScaledBitmap(minesBitmap,Config.CELL_DIMENSION,Config.CELL_DIMENSION,true);
		initGame();
	}
    public void drawSelf(Canvas canvas,Paint paint){
        paint.setColor(Color.RED);
    

		paint.setColor(Color.BLUE);
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(arr[i][j]!=0){
					if(arr[i][j]==9){
						paint.setColor(Color.RED);
						//画雷
						//g.fillOval(j*20,i*20,20,20);
						canvas.drawBitmap(minesBitmap,zoneX + j * Config.CELL_DIMENSION,zoneY + i*Config.CELL_DIMENSION , paint);
					}else if(arr[i][j]==1){
						paint.setColor(Color.GREEN);
						String str = arr[i][j] + "";
						//写雷区数字
						canvas.drawText(str,zoneX + j * Config.CELL_DIMENSION + Config.CELL_DIMENSION * 1/8,zoneY + i*Config.CELL_DIMENSION + Config.CELL_DIMENSION*4/5, paint);
						//g.drawString(str,j*20+7,i*20+15);
					}else if(arr[i][j]==2){
						paint.setColor(Color.YELLOW);
						String str=arr[i][j]+"";
						//写雷区数字
						canvas.drawText(str,zoneX + j * Config.CELL_DIMENSION + Config.CELL_DIMENSION * 1/4,zoneY + i*Config.CELL_DIMENSION + Config.CELL_DIMENSION*4/5, paint);
					}else{
						paint.setColor(Color.RED);
						String str=arr[i][j]+"";
						//写雷区数字
						canvas.drawText(str,zoneX + j * Config.CELL_DIMENSION + Config.CELL_DIMENSION * 1/4,zoneY + i*Config.CELL_DIMENSION + Config.CELL_DIMENSION*4/5, paint);
					}
				}
				if(safeArr[i][j] != 1){
					paint.setColor(Color.WHITE);
					canvas.drawRect(zoneX + j * Config.CELL_DIMENSION,zoneY + i * Config.CELL_DIMENSION,zoneX + (j + 1) * Config.CELL_DIMENSION,zoneY + (i + 1) * Config.CELL_DIMENSION, paint);
				}
				
			}
		}
		paint.setColor(Color.LTGRAY);
		for(int i = 0; i<= this.rows; i++){
    		canvas.drawLine(zoneX ,zoneY + i * (Config.CELL_DIMENSION),zoneX + zoneDimesion, zoneY + i * (Config.CELL_DIMENSION), paint);
    	}
		for(int i=0;i<=  this.cols;i++){
			canvas.drawLine(zoneX + i * (Config.CELL_DIMENSION), zoneY ,zoneX +  i * (Config.CELL_DIMENSION), zoneY + zoneDimesion,paint);
		}
    }

	public void initGame(){
		//将雷随机放到雷区中
		initArr();
		//初始化雷区的矩阵
		initZoneAndMarkMines();
		//初始化各个状态
		initState();
		//恢复用于中间计算的矩阵到初始状态
		resolve();
	}
	//初始化各个状态
	private void initState(){
		this.preOk = true;
		this.isRun = false;
		this.isWon = false;
	}
	//雷区周围的标记区初始化
	public  void initZoneAndMarkMines(){
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(arr[i][j]==9){
					markLei(i,j);
				}
			}
		}
	}
	//判断此位置处有没有雷
	public  boolean hasMines(int r,int c){
		if(r>-1 && r<rows && c>-1 && c<cols){
			//有雷
			if(arr[r][c]==9)return true;
		}else{
			//越界
			return true;
		}
		return false;
	}

	//一个区块周围的雷数的计算
	public  void markLei(int i,int j){
		if(!hasMines(i-1,j-1))
			arr[i-1][j-1]++;	
		if(!hasMines(i-1,j))
			arr[i-1][j]++;
		if(!hasMines(i-1,j+1))
			arr[i-1][j+1]++;

		if(!hasMines(i,j-1))
			arr[i][j-1]++;
		if(!hasMines(i,j+1))
			arr[i][j+1]++;

		if(!hasMines(i+1,j-1))
			arr[i+1][j-1]++;
		if(!hasMines(i+1,j))
			arr[i+1][j]++;
		if(!hasMines(i+1,j+1))
			arr[i+1][j+1]++;
	}
	//随机产生雷将雷部署到雷区
	public void initArr(){
		int r=0,l=0;
		//初始化雷区矩阵和清扫标记矩阵
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				arr[i][j]=0;
				safeArr[i][j]=0;
			}
		}
		//布置雷到雷区
		for(int i=0;i<MinesNum;i++){
			r=rand.nextInt(rows);
			l=rand.nextInt(cols);
			if(isEmpty(r,l)){
				arr[r][l]=9;
			}else{
				i--;
				continue;
			}
		}
	}
	public  boolean isEmpty(int r,int l){
		if(arr[r][l] == 0){
			return true;
		}
		return 	false;
	}
	/**
	 * 判断是否越界
	 * @param r  所点小块的在雷盘中的行
	 * @param l  所点小块的在雷盘中的列
	 * @return
	 */
    private boolean isBianJie(int r, int l) {
		if (r >-1 && r <rows && l >-1 && l <cols) {
			if (markArr[r][l] != 88 && markArr[r][l] != 8 && markArr[r][l] != 0)
				return false;
		} 
		return true;
	}
	//找无雷的边界
	private  void findBianJie() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (markArr[i][j] == 88) {
					if (!isBianJie(i - 1, j - 1))
						markArr[i - 1][j - 1]=44;
					if (!isBianJie(i - 1, j))
						markArr[i - 1][j]=44;
					if (!isBianJie(i - 1, j + 1))
						markArr[i - 1][j + 1]=44;
					if (!isBianJie(i, j - 1))
						markArr[i][j - 1]=44;
					if (!isBianJie(i, j + 1))
						markArr[i][j + 1]=44;
					if (!isBianJie(i + 1, j - 1))
						markArr[i + 1][j - 1]=44;
					if (!isBianJie(i + 1, j))
						markArr[i + 1][j]=44;
					if (!isBianJie(i + 1, j + 1))
						markArr[i + 1][j + 1]=44;
				}
			}
		}
	}
	//清除一个区块周围无雷区的覆盖物
	public void clearOne(int i, int j) {
		//将当前位置设置为无雷的标志
		markArr[i][j] = 88; 
		//清除无雷块左边的区块
		if (i > -1 && i < rows && j - 1 > -1 && j - 1 < cols && markArr[i][j - 1] != 88) {
			if (markArr[i][j - 1] == 0) {
				markArr[i][j - 1] = 88;
				clearOne(i, j - 1);
			}

		}
		//清除无雷块上边的区块
		if (i - 1 > -1 && i - 1 < rows && j > -1 && j < cols && markArr[i - 1][j] != 88) {
			if (markArr[i - 1][j] == 0) {
				markArr[i - 1][j] = 88;
				clearOne(i - 1, j);
			}
		}
		//清除无雷块右边的区块
		if (i > -1 && i < rows && j + 1 > -1 && j + 1 < cols && markArr[i][j + 1] != 88) {
			if (markArr[i][j + 1] == 0) {
				markArr[i][j + 1] = 88;
				clearOne(i, j + 1);
			}
		}
		//清除无雷块下边的区块
		if (i + 1 > -1 && i + 1 < rows && j > -1 && j < cols && markArr[i + 1][j] != 88) {
			if (markArr[i + 1][j] == 0) {
				markArr[i + 1][j] = 88;
				clearOne(i + 1, j);
			}
		}
	}
	//恢复用于中间计算的矩阵到初始状态
	public void resolve(){
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				markArr[i][j] = arr[i][j];
			}
		}
	}
	//当点到一个无雷的区块时，将它周围相连无雷区及其无雷区一周区域上的覆盖物清除
	public void clearNoThunderZone(int r,int l){
		clearOne(r,l) ;
		findBianJie();
		for(int i = 0;i < rows;i++){
			for(int j = 0;j < cols;j++){
				if(markArr[i][j] == 88 || markArr[i][j] == 44){
					safeArr[i][j] = 1;
				}
			}
		}
		  System.out.println("--------------markArr---------------");
		    for(int t=0;t<markArr.length;t++){
				for(int k=0;k<markArr.length;k++){
					System.out.print(markArr[t][k]+" ");
				}
				System.out.println();
			}
		resolve();
	}

	public void sweep(Point point) {
		int i = point.row;
		int j = point.col;
		if(safeArr[i][j] == 1){
			return;
		}
		if(isRun){
			if(arr[i][j] == 9){
				//显示所有雷
				viewAllMines();
				isRun = false;
				isWon = false;
				gameView.fail();
				gameView.invalidate();
				return;
			}else if(arr[i][j] == 0){
				this.clearNoThunderZone(i,j);
				System.out.println("--------------arr---------------");
				for(int t=0;t<markArr.length;t++){
					for(int k=0;k<markArr.length;k++){
						System.out.print(arr[t][k]+" ");
					}
					System.out.println();
				}
				System.out.println("--------------safeArr---------------");
				for(int t=0;t<markArr.length;t++){
					for(int k=0;k<markArr.length;k++){
						System.out.print(safeArr[t][k]+" ");
					}
					System.out.println();
				}
			}else{
				safeArr[i][j] = 1;
			}
			gameView.invalidate();
			if(isWon()){
				isRun = false;
				isWon = false;
				gameView.win();
			}
		}
	}
	//显示出所有雷
	private void viewAllMines(){
		for(int i = 0;i< this.rows; i++){
			for(int j = 0;j<this.cols; j++){
				if(arr[i][j] == 9){
					safeArr[i][j] = 1;
				}
			}
		}
	}
	
	//判断输赢
	public boolean isWon(){
		int count = 0;
		for(int i = 0;i < this.rows; i++){
			for(int j = 0;j < this.cols; j++){
				if( safeArr[i][j] == 0){
					count++;
				}
			}
		}
		if(count == 10){
			return true;
		}
		return false;
	}
}
