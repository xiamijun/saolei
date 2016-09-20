import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.applet.Main;


public class Mine extends MouseAdapter {

	private JFrame mainFrame;	//窗口对象
	private int[][] data;		//数据二维数组
	private JButton[][] buttons;//按钮二维数组
	private JButton startJB;	//开始按钮
	private Label l;	//标签
	private int row;		//行数
	private int col;		//列数
	private int mineNumber;	//雷的数量
	private int mineCount;	//当前雷被找出的数量
	private boolean isOver;	//游戏是否结束
	
	public Mine() 
	{
		row=15;
		col=15;
		mainFrame=new JFrame("扫雷");
		data=new int[row][col];
		buttons=new JButton[row][col];
		startJB=new JButton("start");
		l=new Label("welcome to mine");
		mineNumber=row*col/7;	//雷的数量上格子的1/7
	}
	
	public void init() {
		JPanel north=new JPanel();	
		JPanel center=new JPanel();
		JPanel south=new JPanel();
		
		north.setLayout(new FlowLayout());
		center.setLayout(new FlowLayout());
		south.setLayout(new GridLayout(row,col,4,4));
		
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(north,BorderLayout.NORTH);
		mainFrame.add(center,BorderLayout.CENTER);
		mainFrame.add(south,BorderLayout.SOUTH);
		
		north.add(l);
		
		startJB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				for(int i=0;i<row;i++)
					for(int j=0;j<col;j++)
					{
						buttons[i][j].setText(" ");
						buttons[i][j].setBackground(Color.WHITE);
						data[i][j]=0;
						isOver=false;
					}
				hashMine();
				mineCount=0;
				l.setText("let's go");
			}
		});
		center.add(startJB);
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
			{
				buttons[i][j]=new JButton(" ");
				buttons[i][j].setName(i+":"+j);
				buttons[i][j].setSize(10, 30);
				buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].addMouseListener(this);
				south.add(buttons[i][j]);
			}
		hashMine();
	}
	
	public void start() {
		mainFrame.setSize(800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
	//随机布雷的方法
	public void hashMine() {
		// 得到随机数，确定雷的位置
		for(int i=0;i<mineNumber;i++)
		{
			data[(int)(Math.random()*row)][(int)(Math.random()*col)]=-1;
		}
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
			{
				if(data[i][j]==-1)
					continue;
				int sum=0;	//确定每个格子周围雷的数量
				for(int m=-1;m<=1;m++)
					for(int n=-1;n<=1;n++)
					{
						if(i+m>=0&&j+n>=0&&i+m<=row&&j+n<col)
						{
							if(data[i+m][j+n]==-1)
								sum++;
						}
					}
			}
	}

	private void gameOver(boolean over) {
		if(over==true)
		{
			for(int i=0;i<row;i++)
				for(int j=0;j<col;j++)
				{
					if(data[i][j]==-1)
					{
						buttons[i][j].setText("M");
						buttons[i][j].setBackground(Color.RED);
					}
				}
			l.setText("-_-");
			isOver=true;
			return;
		}
		int sumPress=0;
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
			{
				if(!buttons[i][j].getText().equals(" "))
				{
					sumPress++;
				}
			}
		if(sumPress==row*col)
		{
			int sum=0;
			for(int i=0;i<row;i++)
				for(int j=0;j<col;j++)
				{
					if(data[i][j]==-1&&buttons[i][j].getText().equals("M"))
						sum++;
				}
			if(sum>=mineNumber)
			{
				System.out.println(mineNumber);
				l.setText("^_^");
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		//实现mouselistener中的方法，监听鼠标单击事件
		try {
			if(isOver)
				return;
			if(e.getButton()==MouseEvent.BUTTON3)	//右击
			{
				JButton jb=(JButton) e.getSource();	//获得被点的按钮对象
				if(jb.getText().equals("M"))	//取消标签
				{
					jb.setText(" ");
					mineCount--;
					jb.setBackground(Color.WHITE);
				}
				else {
					if(mineCount<mineNumber)	//标志还没用完
					{
						jb.setText("M");
						jb.setBackground(Color.BLUE);
						mineCount++;
					}
					else {						//标志已经用完
						l.setText("the mine flag is over");
					}
				}
			}
			else {
				JButton jb=(JButton) e.getSource();
				mousePress(jb);	//左键单击事件
			}
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		gameOver(false);
	}
	
	private void mousePress(JButton jb) {
		// TODO Auto-generated method stub
		String str[]=jb.getName().split(":");	//得到行号和列号
		int i=Integer.parseInt(str[0]);
		int j=Integer.parseInt(str[1]);
		if (data[i][j]==-1) {				//如果点到雷了
			gameOver(true);
			return;
		} else {
			jb.setText(data[i][j]+" ");
			jb.setBackground(Color.YELLOW);
			if (data[i][j]==0) {
				for (int m = -1; m <=1; m++) {
					for (int n = -1; n <=1; n++) {
						if (i+m>=0&&j+n>=0&&i+m<row&&j+n<col) {
							if (buttons[i+m][j+n].getText().equals(" ")) {
								mousePress(buttons[i+m][j+n]);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mine mine=new Mine();
		mine.init();
		mine.start();
	}

}
