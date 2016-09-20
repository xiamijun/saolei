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

	private JFrame mainFrame;	//���ڶ���
	private int[][] data;		//���ݶ�ά����
	private JButton[][] buttons;//��ť��ά����
	private JButton startJB;	//��ʼ��ť
	private Label l;	//��ǩ
	private int row;		//����
	private int col;		//����
	private int mineNumber;	//�׵�����
	private int mineCount;	//��ǰ�ױ��ҳ�������
	private boolean isOver;	//��Ϸ�Ƿ����
	
	public Mine() 
	{
		row=15;
		col=15;
		mainFrame=new JFrame("ɨ��");
		data=new int[row][col];
		buttons=new JButton[row][col];
		startJB=new JButton("start");
		l=new Label("welcome to mine");
		mineNumber=row*col/7;	//�׵������ϸ��ӵ�1/7
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
	
	//������׵ķ���
	public void hashMine() {
		// �õ��������ȷ���׵�λ��
		for(int i=0;i<mineNumber;i++)
		{
			data[(int)(Math.random()*row)][(int)(Math.random()*col)]=-1;
		}
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
			{
				if(data[i][j]==-1)
					continue;
				int sum=0;	//ȷ��ÿ��������Χ�׵�����
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
		//ʵ��mouselistener�еķ�����������굥���¼�
		try {
			if(isOver)
				return;
			if(e.getButton()==MouseEvent.BUTTON3)	//�һ�
			{
				JButton jb=(JButton) e.getSource();	//��ñ���İ�ť����
				if(jb.getText().equals("M"))	//ȡ����ǩ
				{
					jb.setText(" ");
					mineCount--;
					jb.setBackground(Color.WHITE);
				}
				else {
					if(mineCount<mineNumber)	//��־��û����
					{
						jb.setText("M");
						jb.setBackground(Color.BLUE);
						mineCount++;
					}
					else {						//��־�Ѿ�����
						l.setText("the mine flag is over");
					}
				}
			}
			else {
				JButton jb=(JButton) e.getSource();
				mousePress(jb);	//��������¼�
			}
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
		gameOver(false);
	}
	
	private void mousePress(JButton jb) {
		// TODO Auto-generated method stub
		String str[]=jb.getName().split(":");	//�õ��кź��к�
		int i=Integer.parseInt(str[0]);
		int j=Integer.parseInt(str[1]);
		if (data[i][j]==-1) {				//����㵽����
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
