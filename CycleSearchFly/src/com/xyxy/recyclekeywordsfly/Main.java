/**
 * 此app能够实现关键循环随机飞入飞出，当被点击自动打开浏览器搜索等功能
 * 关键字keyword具有随机的大小和颜色，并能绕着y轴中点，以其坐标到Y轴的距离为半径的随机时长的旋转
 * 在子线程中创建keyword，然后在主线程中分配随机属性并添加到自定义的FrameLayout上显示
 */

package com.xyxy.recyclekeywordsfly;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener{

	/**存储备选关键字*/
	 public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW", "铅笔",//  
       "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛",//  
       "卡西欧 TR-100", "笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人",//  
       "内存清理", "地图", "导航", "闹钟", "主题",//  
       "通讯录", "播放器", "CSDN leak", "安全", "3D",//  
       "美女", "天气", "4743G", "戴尔", "联想",//  
       "欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",//  
       "iciba", "油水关系", "网游App", "互联网", "365日历",//  
       "脸部识别", "Chrome", "Safari", "中国版Siri", "A5处理器",//  
       "iPhone4S", "摩托 ME525", "魅族 M9", "尼康 S2500" };  
	 
	 private static final int MIN_SIZE = 15;	//最小尺寸
	 private static final int MAX_SIZE = 25;	//最大尺寸
	 
	 private static final int XAXIS = 0;		//数组中存放X轴坐标的位置的索引
	 private static final int YAXIS = 1;		//数组中存放Y轴坐标的位置的索引
	 private static final int LENGTH = 2;		//数组中存放TextView长度的位置的索引
	 
	 private int[] coordiate = new int[3];		//用来存放TextView坐标及长度信息的数组
	 
	 private Random random;
	 private String keyword;					//关键字
	 
	 private Button btnStart;					//开始按键，启动关键字进出动画效果
	 private Button btnStop;					//停止按键，关闭动画，并清空所有的关键字
	 
	 private MyFrameLayout flyWords;					//自定义FrameLayout
	 
	 private int width = 0;						//FrameLayout的宽度
	 private int height = 0;					//FrameLayout的高度
	 private int duration = 0;					//动画运行时间
	 
	 private boolean anim = false;				//标志位，是否启动动画
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		random = new Random();
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop  = (Button) findViewById(R.id.btnStop);
		
		flyWords = (MyFrameLayout) findViewById(R.id.flyWordsLayout01);
		flyWords.setFocusable(true);
		flyWords.setFocusableInTouchMode(true);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		
	}
	
	private void setRandomProperties(MyTextView mTextView){
		int length = keywords.length;
		keyword = keywords[random.nextInt(length)];			//随机获取一个关键字
		width = MyFrameLayout.width;								//获取FrameLayout的长宽
		height = MyFrameLayout.height;
		if((width != 0) && (height != 0) && (keyword != null)){
			int size = MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE +1);	//随机大小		
			int ranColor = 0xff000000 | random.nextInt(0x0077ffff);			//随机颜色
			
			duration = 5000 + random.nextInt(5000);							//随机时长，在5至10秒之间
			
			/**把相应属性添加到TextView上*/
			mTextView.setText(keyword);
			mTextView.setTextSize(size);
			mTextView.setTextColor(ranColor);
			mTextView.setOnClickListener(this);
			
			Paint paint = mTextView.getPaint();
			coordiate[LENGTH] = (int) Math.ceil(paint.measureText(keyword));//获取长度
			
			/**随机分配x,y坐标，并进行修正*/
			int x = random.nextInt(width);
			if(x <= 0){
				x += coordiate[LENGTH] + (coordiate[LENGTH] >> 1);
			}
			else if(x >= (width - coordiate[LENGTH])){
				x -= (coordiate[LENGTH] >> 1);
				while(x >= (width - coordiate[LENGTH])){
					x -= (coordiate[LENGTH] >> 1);
				}
			}
			coordiate[XAXIS] = x;
			coordiate[YAXIS] = (height >> 1) + (height >> 2) + random.nextInt(height >> 2);
			
			/**根据TextView的坐轴，将TextView添加到FrameLayout的相应位置*/
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.leftMargin = coordiate[XAXIS];
			params.topMargin = coordiate[YAXIS];
			flyWords.addView(mTextView, params);
			/*mTextView.setAlpha(0.1f);
			mTextView.animate().alpha(1.0f).setInterpolator(decInterpolator)
								.scaleX(1).scaleY(1).setDuration(duration)
								.translationY((-coordiate[YAXIS] + (height >>1)))
								.setListener(this);*/
				
			mTextView.showAnimation(coordiate, (height >> 1), duration);	//启动自定义TextView中设置的动画
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnStart){				//点击开始按键，新建子线程，创建TextView并启动动画
			flyWords.clearAllView();
			anim = true;
			MyThread mThread = new MyThread();
			mThread.start();
		}
		else if(v.getId() == R.id.btnStop){			//点击停止按键，关闭动画，并清屏
			anim = false;
			flyWords.clearAllView();
		}
		/**暂时无法实现*/
		else if(v instanceof TextView){				//当点击某个关键字，启动浏览器搜索
			String keyword = ((TextView) v).getText().toString();
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("http:/www.google.com.hk/#q=" + keyword));
			startActivity(intent);
		}
	}
	
	/**
	 * 自定义子线程类，用来创建TextView,并启动动画
	 * @author Administrator
	 *
	 */
	class MyThread extends Thread{

		@Override
		public void run() {
			while(anim){
				runOnUiThread(new Runnable() {		//子View的添加或删除，只能在UI Thread（主线程）中进行
					
					@Override
					public void run() {
						if(flyWords.getChildCount() <= 20){		//控制屏幕上显示的关键字不超过20个
							MyTextView mTextView = new MyTextView(Main.this);	//创建TextView
							setRandomProperties(mTextView);		//分配随机属性，并启动动画
						}
						flyWords.clear();						//清除可见性为GONE的TextView
					}
				});
				try {
					sleep(1000);								//间隔1秒中创建一个TextView
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}
