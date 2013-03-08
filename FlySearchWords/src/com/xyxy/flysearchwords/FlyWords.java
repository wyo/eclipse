package com.xyxy.flysearchwords;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FlyWords extends FrameLayout implements OnGlobalLayoutListener {
	
	private static final int IDX_X = 0;				//数组中存储X坐标的值的位置
	private static final int IDX_Y = 1;				//数组中存储Y坐标的值的位置
	private static final int IDX_TXT_LENGTH = 2;	//数组中存储文本长度的值的位置
	private static final int IDX_DIS_Y = 3;			//数组中存储文本到Y轴中点的距离
	
	public	static final int ANIMATION_IN = 1;		//由外至内的动画类型
	public  static final int ANIMATION_OUT = 2;		//由内至外的动画类型
	
	/**位移动画类型，由外围移动至坐标点**/
	private static final int OUTSIDE_TO_LOCATION = 1;
	/**位移动画类型，由坐标点移动至外围**/
	private static final int LOCATION_TO_OUTSIDE = 2;
	/**位移动画类型，由中点移动至坐标点**/
	private static final int CENTER_TO_LOCATION = 3;
	/**位移动画类型，由坐标点移动至中心点**/
	private static final int LOCATION_TO_CENTER = 4;
	
	private static final int ANIM_DURATION = 801;
	public  static final int MAX = 10;
	private static final int TEXT_SIZE_MAX = 25;	//最大字体
	private static final int TEXT_SIZE_MIN = 15;	//最小字体
	
	private OnClickListener itemClickListener;
	private Interpolator interpolator;
	private static AlphaAnimation animAlpha2Opaque;									//逐渐显现
	private static AlphaAnimation animAlpha2Transparent;							//逐渐消失
	/**缩放：由大变正常，则正常放大，由0到正常，由正常缩小到0**/
	private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large,		
		animScaleZero2Normal, animScaleNormal2Zero;	
	
	private Vector<String> vecKeywords;		//存储要明显的关键字
	private int width, height;				//屏幕的长宽
	
	/*
	 * 用来控制动画的显示，在go2Show()中赋true,启动动画，
	 * 在show()中赋false
	 */
	private boolean enableShow;
	private Random random;
	
	/**
	 * 文本信息进出屏幕的动画类型
	 * 
	 * @see ANIMATION_IN
	 * @see ANIMATION_OUT
	 * @see LOCATION_TO_OUTSIDE
	 * @see OUTSIDE_TO_LOCATION
	 * @see LOCATION_TO_CENTER
	 * @see CENTER_TO_LOCATION
	 */
	private int txtAnimInType, txtAnimOutType;
	/**存储前一次启动动画的时间**/
	private long lastStartAnimTime;
	/**动画运行时间**/
	private int animDuration;
	
	/**
	 * 带三个参数的构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FlyWords(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	/**
	 * 带两个参数的构造函数
	 * @param context
	 * @param attrs
	 */
	public FlyWords(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	/**
	 * 带一个参数的构造函数
	 * @param context
	 */
	public FlyWords(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	/**
	 * 自动获取屏幕的宽度和高度
	 * @param tempWidth, tempHeight, width, height;
	 * @see android.view.ViewTreeObserver.OnGlobalLayoutListener#onGlobalLayout()
	 */
	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		int tempWidth = getWidth();
		int tempHeight = getHeight();
		if(width != tempWidth){
			width = tempWidth;
		}
		if(height != tempHeight){
			height = tempHeight;
		}
	}
	
	/**
	 * 初始化相应参数
	 */
	private void init(){
		lastStartAnimTime = 01;
		animDuration = ANIM_DURATION;
		random = new Random();
		vecKeywords = new Vector<String>(MAX);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		interpolator = AnimationUtils.loadInterpolator(getContext(), 
				android.R.anim.decelerate_interpolator);
		animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);		//透明到不透明
		animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);	//不透明到透明
		animScaleLarge2Normal = new ScaleAnimation(2, 1, 2, 1);	//由大缩小至正常大小
		animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);	//由正常大小放大
		animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);	//由正常大小缩小至0
		animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);	//由0放大到正常大小
	}
	
	/**
	 * 判断是否可以显示动画，true：可以，false：不可以
	 * @param animType
	 * @return
	 */
	public boolean go2Show(int animType){
		if((System.currentTimeMillis() - lastStartAnimTime) > 0){	//当当前时间大于上一次动画启动时间时，则可以启动显示
			enableShow = true;	//启动显示标志位
			/**判断动画进入类型*/
			if(animType == ANIMATION_IN){				
				txtAnimInType = OUTSIDE_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_CENTER;
			}
			else if(animType == ANIMATION_OUT){
				txtAnimInType = CENTER_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_OUTSIDE;
			}
			disapper();
			boolean result = show();	//启动显示，并判断是否显示成功
			return result;
		}
		return false;
	}
	
	private boolean show() {
		if((width > 0) && (height > 0) && (vecKeywords != null) && (vecKeywords.size() > 0)
				&& enableShow){				//判断是否满足显示动画条件
			enableShow = false;				//关闭启动显示标志位，直到下一次启动显示再开启
			lastStartAnimTime = System.currentTimeMillis();	//启动显示，修改上一次显示时间
			int xCenter = width >> 1;
			int yCenter = height >> 1;		//计算中点坐标
			int size = vecKeywords.size();	//将要显示的文本信息的数量
			int xItem = width / size;
			int yItem = height / size;		//计算每一个item（文本信息）平均能使用的高度和宽度
			
			Log.d("FLYWORDS_DEBUGE", "------------------width= " + width + 
					" height= " + height + " xItem= " + xItem + " yItem= " + yItem
					+ "-------------------");
			
			LinkedList<Integer> listX = new LinkedList<Integer>();	//存储备选的x坐标值
			LinkedList<Integer> listY = new LinkedList<Integer>();	//存储备选的y坐标值
			
			for(int i = 0; i < size; i++){
				listX.add(i * xItem);
				listY.add(i * yItem);
			}
			
			LinkedList<TextView> listTxtTop = new LinkedList<TextView>();
			LinkedList<TextView> listTxtBottom = new LinkedList<TextView>();
			
			for(int i = 0; i != size; i++){
				String keyword = vecKeywords.get(i);					//获取要显示的关键字
				int ranColor = 0xff000000 | random.nextInt(0x0077ffff);	//随机颜色
				int[] xy = randomXY(random, listX, listY);		//随机位置，糙值，需要修正
				int textSize = TEXT_SIZE_MIN + random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN + 1);	//随机字体
				final TextView txtView = new TextView(getContext());	//实例化TextView
				
				txtView.setOnClickListener(itemClickListener);
				txtView.setText(keyword);
				txtView.setTextColor(ranColor);
				txtView.setTextSize(textSize);
				txtView.setShadowLayer(2, 2, 2, 0xff696969);
				txtView.setGravity(Gravity.CENTER);
				/**获取文本信息的宽度*/
				Paint paint = txtView.getPaint();
				int strWidth = (int) Math.ceil(paint.measureText(keyword));
				xy[IDX_TXT_LENGTH] = strWidth;
				/**进行第一次修正，修正x坐标*/
				if((strWidth + xy[IDX_X]) > (width - (xItem >> 1))){
					int baseX = width - strWidth;
					xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);	//减少右边缘重叠的概率
				}
				else if(xy[IDX_X] == 0){
					xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);	//减少左边缘重叠的概率
				}
				xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);
				txtView.setTag(xy);
				if(xy[IDX_Y] > yCenter){
					listTxtBottom.add(txtView);
				}
				else{
					listTxtTop.add(txtView);
				}
			}
			attach2Screen(listTxtBottom, xCenter, yCenter, yItem);
			attach2Screen(listTxtTop, xCenter, yCenter, yItem);
			return true;
		}
		return false;
	}
	
	/**
	 * 修正Y坐标，并把最终修正好的TextView显示到屏幕上
	 * @param listTxtView
	 * @param xCenter
	 * @param yCenter
	 * @param yItem
	 */
	private void attach2Screen(LinkedList<TextView> listTxtView, int xCenter,
			int yCenter, int yItem) {
		int size = listTxtView.size();
		sortXYList(listTxtView, size);
		for(int i = 0; i != size; i++){
			TextView txtView = listTxtView.get(i);
			int[] iXY = (int[])txtView.getTag();
			
			Log.d("FLYWORDS_DEBUGE", "fix[  " + txtView.getText() + "  ] x:" +  
		            iXY[IDX_X] + " y:" + iXY[IDX_Y] + " r2="  
		            + iXY[IDX_DIS_Y]);  
			
			/**进行第二次修正，修正Y坐标*/
			int yDistance = iXY[IDX_Y] - yCenter;
			int yMove = Math.abs(yDistance);
			inner: for(int k = i - 1; k > 0; k--){
				int[] kXY = (int[])listTxtView.get(k).getTag();
				int startX = kXY[IDX_X];
				int endX = startX + kXY[IDX_TXT_LENGTH];
				
				if(yDistance * (kXY[IDX_Y] - yCenter) > 0){	//在同侧
					if(isMixed(startX, endX, iXY[IDX_X], iXY[IDX_X] + iXY[IDX_TXT_LENGTH])){	//判断是否相交
						int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);
						if(tmpMove > yItem){
							yMove = tmpMove;
						}
						else{
							yMove = 0;
						}
						break inner;
					}
				}
			}
			if(yMove > yItem){
				int maxMove = yMove - yItem;
				int randomMove = random.nextInt(maxMove);
				int realMove = Math.max(randomMove, maxMove >> 1) * yDistance / Math.abs(yDistance);
				iXY[IDX_Y] -= realMove;
				iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);
				
				sortXYList(listTxtView, i + 1);	//对已经修正过的数据重排
			}
			
			//设置显示参数，并把TextView显示到屏幕上
			FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			layParams.gravity = Gravity.LEFT | Gravity.TOP;
			layParams.leftMargin = iXY[IDX_X];
			layParams.topMargin = iXY[IDX_Y];
			addView(txtView, layParams);
			AnimationSet animSet = getAnimationSet(iXY, xCenter, yCenter, txtAnimInType);
			txtView.startAnimation(animSet);
		}
	}
	
	/**
	 * 判断是否相交
	 * @param startA
	 * @param endA
	 * @param startB
	 * @param endB
	 * @return
	 */
	private boolean isMixed(int startA, int endA, int startB, int endB) {
		boolean result = false;
		if (startB >= startA && startB <= endA) {  
            result = true;  
        } else if (endB >= startA && endB <= endA) {  
            result = true;  
        } else if (startA >= startB && startA <= endB) {  
            result = true;  
        } else if (endA >= startB && endA <= endB) {  
            result = true;  
        }  
		return result;
	}

	/**
	 * 根据TextView Y坐标与中点的距离对 listTxtView进行排序
	 * @param listTxtView
	 * @param endIdx
	 */
	private void sortXYList(LinkedList<TextView> listTxtView, int endIdx) {
		for(int i = 0; i != (endIdx - 1); i++){
			for(int k = i + 1; k != endIdx; k++){
				if(((int[])listTxtView.get(k).getTag())[IDX_DIS_Y] < 
						((int[])listTxtView.get(i).getTag())[IDX_DIS_Y]){
					TextView iTmp = listTxtView.get(i);
					TextView kTmp = listTxtView.get(k);
					listTxtView.set(i, kTmp);
					listTxtView.set(k, iTmp);
				}
			}
		}
	}

	/**
	 * 随机从listX和listY中取出一个值，组成一个坐标返回
	 * @param ran
	 * @param listX
	 * @param listY
	 * @param xItem
	 * @return
	 */
	private int[] randomXY(Random ran, LinkedList<Integer> listX,
			LinkedList<Integer> listY) {
		int arr[] = new int[4];
		arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
		arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
		return arr;
	}

	/**
	 * 清除屏幕上所有可见与不可见的TextView;
	 * 在使用getChildAt(i)时，只能使用i--,而不能使用i++,因为要removeView
	 * 当remove完成后，被删除的view之后的所有view都向前串，所以，其后的view的index都发生了改变
	 * 如果使用i++,则会造成无法获得某些view,并且最后可会出现getChildAt(i)返回为空的情况
	 */
	private void disapper(){
		int size = getChildCount();								//获得TextView数量
		for(int i = size - 1; i >= 0; i--){
			final TextView txt = (TextView) getChildAt(i);		//获取第i个TextView
			
			if(txt.getVisibility() == View.GONE){				//判断其是否可见
				removeView(txt);								//如果已经不可见，则直接移除这个TextView
				continue;										//移除TextView，跳过本次循环
			}
			
			FrameLayout.LayoutParams layParams = 
					(LayoutParams)txt.getLayoutParams();		//获得当前TextView的属性
			
			/*Log.d("FLYWORDS_DEBUGE", txt.getText() + " leftM= " + 
					layParams.leftMargin + " topM= " + layParams.topMargin +
					" width= " + layParams.width);*/
			
			/**存储当前TextView的X(leftMargin), Y(topMargin)坐标值以及宽度值*/
			int[] xy = new int[]{layParams.leftMargin, layParams.topMargin, layParams.width};
			AnimationSet animSet = getAnimationSet(xy, (width >> 1), (height >> 1), txtAnimOutType);
			txt.startAnimation(animSet);
			animSet.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				/**
				 * 当动画结束，让当前TextView消失。
				 */
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					txt.setOnClickListener(null);		//取消监听器
					txt.setFocusable(false);			//取消聚焦
					txt.setVisibility(View.GONE);		//使其不可见，等待被移除
				}
			});
		}
	}
	
	/**
	 * 获取AnimationSet实例，设置alpha,translate,duration等参数
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	初始化好的实例animSet
	 */
	private AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter,
			int type) {
		// TODO Auto-generated method stub
		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator(interpolator);
		TranslateAnimation translate;
		switch (type) {
		case OUTSIDE_TO_LOCATION:							//从外围移动至坐标点
			animSet.addAnimation(animAlpha2Opaque);			//由透明变不透明
			animSet.addAnimation(animScaleLarge2Normal);	//由大变小
			translate = new TranslateAnimation(
					(xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, 
							(xy[IDX_Y] - (yCenter >> 1)) << 1, 0);
			animSet.addAnimation(translate);
			break;
			
		case LOCATION_TO_OUTSIDE:							//从坐标点移至外围
			animSet.addAnimation(animAlpha2Transparent);	//由不透明变透明
			animSet.addAnimation(animScaleNormal2Large);	//由小变大
			translate = new TranslateAnimation(0, 
					(xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, 
							(xy[IDX_Y] - (yCenter >> 1)) << 1);
			animSet.addAnimation(translate);
			break;
			
		case CENTER_TO_LOCATION:							//由中心移动至坐标点
			animSet.addAnimation(animAlpha2Opaque);			//由透明变不透明
			animSet.addAnimation(animScaleZero2Normal);		//由0变大到正常大小
			translate = new TranslateAnimation(
					(-xy[IDX_X] + xCenter), 0, 
							(-xy[IDX_Y] + yCenter), 0);
			animSet.addAnimation(translate);
			break;
			
		case LOCATION_TO_CENTER:							//从坐标点移动到中点
			animSet.addAnimation(animAlpha2Transparent);	//由不透明变透明
			animSet.addAnimation(animScaleNormal2Zero);		//由正常大小变为0
			translate = new TranslateAnimation(
					0, (-xy[IDX_X] + xCenter), 0, (-xy[IDX_Y] + yCenter));
			animSet.addAnimation(translate);
			break;

		default:
			break;
		}
		animSet.setDuration(animDuration);
		return animSet;
	}

	/**
	 * 往vecKeywords中装入准备显示的文本信息
	 * @param keyword 待装入文本信息 
	 * @return result，指示是否装入成功
	 */
	public boolean feedKeywords(String keyword){
		boolean result = false;
		if(vecKeywords.size() < MAX){
			result = vecKeywords.add(keyword);
		}
		return result;
	}
	
	/**
	 * 获取当前的动画运行时间
	 * @return animDuration
	 */
	public int getDuration(){
		return animDuration;
	}
	
	/**
	 * 设置动画运行时间
	 * @param duration
	 */
	public void setDuration(int duration){
		animDuration = duration;
	}
	
	/**
	 * 获得当前要显示的文本信息
	 * @return vecKeywords
	 */
	public Vector<String> getKeywords(){
		return vecKeywords;
	}
	
	/**
	 * 清除vecKeywords中保存的文本信息
	 */
	public void clearKeywords(){
		vecKeywords.clear();
	}
	
	/**
	 * 直接清楚所有的TextView.在清除之前不会显示动画	
	 */
	public void clearAllView(){
		removeAllViews();
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setOnItemClickListener(OnClickListener listener){
		itemClickListener = listener;
	}
}
