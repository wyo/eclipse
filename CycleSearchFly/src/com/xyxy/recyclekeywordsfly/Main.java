/**
 * ��app�ܹ�ʵ�ֹؼ�ѭ���������ɳ�����������Զ�������������ȹ���
 * �ؼ���keyword��������Ĵ�С����ɫ����������y���е㣬�������굽Y��ľ���Ϊ�뾶�����ʱ������ת
 * �����߳��д���keyword��Ȼ�������߳��з���������Բ���ӵ��Զ����FrameLayout����ʾ
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

	/**�洢��ѡ�ؼ���*/
	 public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW", "Ǧ��",//  
       "����", "���澫��", "MacBook Pro", "ƽ�����", "��ʫ����",//  
       "����ŷ TR-100", "�ʼǱ�", "SPY Mouse", "Thinkpad E40", "�������",//  
       "�ڴ�����", "��ͼ", "����", "����", "����",//  
       "ͨѶ¼", "������", "CSDN leak", "��ȫ", "3D",//  
       "��Ů", "����", "4743G", "����", "����",//  
       "ŷ��", "�����", "��ŭ��С��", "mmShow", "���׹�����",//  
       "iciba", "��ˮ��ϵ", "����App", "������", "365����",//  
       "����ʶ��", "Chrome", "Safari", "�й���Siri", "A5������",//  
       "iPhone4S", "Ħ�� ME525", "���� M9", "�῵ S2500" };  
	 
	 private static final int MIN_SIZE = 15;	//��С�ߴ�
	 private static final int MAX_SIZE = 25;	//���ߴ�
	 
	 private static final int XAXIS = 0;		//�����д��X�������λ�õ�����
	 private static final int YAXIS = 1;		//�����д��Y�������λ�õ�����
	 private static final int LENGTH = 2;		//�����д��TextView���ȵ�λ�õ�����
	 
	 private int[] coordiate = new int[3];		//�������TextView���꼰������Ϣ������
	 
	 private Random random;
	 private String keyword;					//�ؼ���
	 
	 private Button btnStart;					//��ʼ�����������ؼ��ֽ�������Ч��
	 private Button btnStop;					//ֹͣ�������رն�������������еĹؼ���
	 
	 private MyFrameLayout flyWords;					//�Զ���FrameLayout
	 
	 private int width = 0;						//FrameLayout�Ŀ��
	 private int height = 0;					//FrameLayout�ĸ߶�
	 private int duration = 0;					//��������ʱ��
	 
	 private boolean anim = false;				//��־λ���Ƿ���������
	
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
		keyword = keywords[random.nextInt(length)];			//�����ȡһ���ؼ���
		width = MyFrameLayout.width;								//��ȡFrameLayout�ĳ���
		height = MyFrameLayout.height;
		if((width != 0) && (height != 0) && (keyword != null)){
			int size = MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE +1);	//�����С		
			int ranColor = 0xff000000 | random.nextInt(0x0077ffff);			//�����ɫ
			
			duration = 5000 + random.nextInt(5000);							//���ʱ������5��10��֮��
			
			/**����Ӧ������ӵ�TextView��*/
			mTextView.setText(keyword);
			mTextView.setTextSize(size);
			mTextView.setTextColor(ranColor);
			mTextView.setOnClickListener(this);
			
			Paint paint = mTextView.getPaint();
			coordiate[LENGTH] = (int) Math.ceil(paint.measureText(keyword));//��ȡ����
			
			/**�������x,y���꣬����������*/
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
			
			/**����TextView�����ᣬ��TextView��ӵ�FrameLayout����Ӧλ��*/
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
				
			mTextView.showAnimation(coordiate, (height >> 1), duration);	//�����Զ���TextView�����õĶ���
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnStart){				//�����ʼ�������½����̣߳�����TextView����������
			flyWords.clearAllView();
			anim = true;
			MyThread mThread = new MyThread();
			mThread.start();
		}
		else if(v.getId() == R.id.btnStop){			//���ֹͣ�������رն�����������
			anim = false;
			flyWords.clearAllView();
		}
		/**��ʱ�޷�ʵ��*/
		else if(v instanceof TextView){				//�����ĳ���ؼ��֣��������������
			String keyword = ((TextView) v).getText().toString();
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("http:/www.google.com.hk/#q=" + keyword));
			startActivity(intent);
		}
	}
	
	/**
	 * �Զ������߳��࣬��������TextView,����������
	 * @author Administrator
	 *
	 */
	class MyThread extends Thread{

		@Override
		public void run() {
			while(anim){
				runOnUiThread(new Runnable() {		//��View����ӻ�ɾ����ֻ����UI Thread�����̣߳��н���
					
					@Override
					public void run() {
						if(flyWords.getChildCount() <= 20){		//������Ļ����ʾ�Ĺؼ��ֲ�����20��
							MyTextView mTextView = new MyTextView(Main.this);	//����TextView
							setRandomProperties(mTextView);		//����������ԣ�����������
						}
						flyWords.clear();						//����ɼ���ΪGONE��TextView
					}
				});
				try {
					sleep(1000);								//���1���д���һ��TextView
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}
