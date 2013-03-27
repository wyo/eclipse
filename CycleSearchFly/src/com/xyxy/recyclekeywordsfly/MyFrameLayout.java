/**
 * ����FrameLayout��ʵ�ֻ�ȡ��VIEW�������Լ�����VIEW������ȹ���
 */

package com.xyxy.recyclekeywordsfly;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyFrameLayout extends FrameLayout implements OnGlobalLayoutListener{

	public static int width;		//��ȡFrameLayout�Ŀ��
	public static int height;		//��ȡFrameLayout�ĸ߶�
	
	/**
	 * ���캯��
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public MyFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyFrameLayout(Context context) {
		super(context);
		init();
	}
	
	/**
	 * �Ƴ��ɼ���ΪGONE������Child view
	 * Note:һ��ʹ��index�Ӵ�С��˳���Ƴ�
	 */
	public void clear(){
		int childSize = getChildCount();
		for(int i = (childSize - 1); i >= 0; i--){					
			final TextView goneTextView = (TextView) getChildAt(i);
			if(goneTextView.getVisibility() == View.GONE){
				removeView(goneTextView);
			}
		}
	}
	
	/**
	 * �Ƴ���FrameLayout�����е���view
	 * Note:һ��ʹ��index�Ӵ�С��˳���Ƴ�
	 */
	public void clearAllView(){
		int childSize = getChildCount();
		for(int i = (childSize - 1); i >= 0; i--){
			removeViewAt(i);
		}
	}
	
	/**
	 * ��ȡ��View������
	 */
	@Override
	public int getChildCount() {
		return super.getChildCount();
	}
	
	/**
	 * ��ʼ�������OnGloaBalLayoutListener���Ի�Layout�ĳߴ�
	 */
	private void init(){
		getViewTreeObserver().addOnGlobalLayoutListener(this);	//���OnGlobalLayoutListenr������
	}
	
	/**
	 * ͨ��ʵ��onGlobalLayoutListener�ӿڷ������layout�ĳߴ�
	 */
	@Override
	public void onGlobalLayout() {
		int tempWidth = getWidth();
		int tempHeight = getHeight();
		if(tempWidth != width){
			width = tempWidth;
			//Log.d("MultiThread", "tempWidth:" + tempWidth + " Width:" + width);
		}
		if(tempHeight != height){
			height = tempHeight;
			//Log.d("MultiThread", "tempHeight:" + tempHeight + " Height:" + height);
		}
	}
}
