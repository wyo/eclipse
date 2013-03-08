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
	
	private static final int IDX_X = 0;				//�����д洢X�����ֵ��λ��
	private static final int IDX_Y = 1;				//�����д洢Y�����ֵ��λ��
	private static final int IDX_TXT_LENGTH = 2;	//�����д洢�ı����ȵ�ֵ��λ��
	private static final int IDX_DIS_Y = 3;			//�����д洢�ı���Y���е�ľ���
	
	public	static final int ANIMATION_IN = 1;		//�������ڵĶ�������
	public  static final int ANIMATION_OUT = 2;		//��������Ķ�������
	
	/**λ�ƶ������ͣ�����Χ�ƶ��������**/
	private static final int OUTSIDE_TO_LOCATION = 1;
	/**λ�ƶ������ͣ���������ƶ�����Χ**/
	private static final int LOCATION_TO_OUTSIDE = 2;
	/**λ�ƶ������ͣ����е��ƶ��������**/
	private static final int CENTER_TO_LOCATION = 3;
	/**λ�ƶ������ͣ���������ƶ������ĵ�**/
	private static final int LOCATION_TO_CENTER = 4;
	
	private static final int ANIM_DURATION = 801;
	public  static final int MAX = 10;
	private static final int TEXT_SIZE_MAX = 25;	//�������
	private static final int TEXT_SIZE_MIN = 15;	//��С����
	
	private OnClickListener itemClickListener;
	private Interpolator interpolator;
	private static AlphaAnimation animAlpha2Opaque;									//������
	private static AlphaAnimation animAlpha2Transparent;							//����ʧ
	/**���ţ��ɴ���������������Ŵ���0����������������С��0**/
	private static ScaleAnimation animScaleLarge2Normal, animScaleNormal2Large,		
		animScaleZero2Normal, animScaleNormal2Zero;	
	
	private Vector<String> vecKeywords;		//�洢Ҫ���ԵĹؼ���
	private int width, height;				//��Ļ�ĳ���
	
	/*
	 * �������ƶ�������ʾ����go2Show()�и�true,����������
	 * ��show()�и�false
	 */
	private boolean enableShow;
	private Random random;
	
	/**
	 * �ı���Ϣ������Ļ�Ķ�������
	 * 
	 * @see ANIMATION_IN
	 * @see ANIMATION_OUT
	 * @see LOCATION_TO_OUTSIDE
	 * @see OUTSIDE_TO_LOCATION
	 * @see LOCATION_TO_CENTER
	 * @see CENTER_TO_LOCATION
	 */
	private int txtAnimInType, txtAnimOutType;
	/**�洢ǰһ������������ʱ��**/
	private long lastStartAnimTime;
	/**��������ʱ��**/
	private int animDuration;
	
	/**
	 * �����������Ĺ��캯��
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
	 * �����������Ĺ��캯��
	 * @param context
	 * @param attrs
	 */
	public FlyWords(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	/**
	 * ��һ�������Ĺ��캯��
	 * @param context
	 */
	public FlyWords(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	/**
	 * �Զ���ȡ��Ļ�Ŀ�Ⱥ͸߶�
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
	 * ��ʼ����Ӧ����
	 */
	private void init(){
		lastStartAnimTime = 01;
		animDuration = ANIM_DURATION;
		random = new Random();
		vecKeywords = new Vector<String>(MAX);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		interpolator = AnimationUtils.loadInterpolator(getContext(), 
				android.R.anim.decelerate_interpolator);
		animAlpha2Opaque = new AlphaAnimation(0.0f, 1.0f);		//͸������͸��
		animAlpha2Transparent = new AlphaAnimation(1.0f, 0.0f);	//��͸����͸��
		animScaleLarge2Normal = new ScaleAnimation(2, 1, 2, 1);	//�ɴ���С��������С
		animScaleNormal2Large = new ScaleAnimation(1, 2, 1, 2);	//��������С�Ŵ�
		animScaleNormal2Zero = new ScaleAnimation(1, 0, 1, 0);	//��������С��С��0
		animScaleZero2Normal = new ScaleAnimation(0, 1, 0, 1);	//��0�Ŵ�������С
	}
	
	/**
	 * �ж��Ƿ������ʾ������true�����ԣ�false��������
	 * @param animType
	 * @return
	 */
	public boolean go2Show(int animType){
		if((System.currentTimeMillis() - lastStartAnimTime) > 0){	//����ǰʱ�������һ�ζ�������ʱ��ʱ�������������ʾ
			enableShow = true;	//������ʾ��־λ
			/**�ж϶�����������*/
			if(animType == ANIMATION_IN){				
				txtAnimInType = OUTSIDE_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_CENTER;
			}
			else if(animType == ANIMATION_OUT){
				txtAnimInType = CENTER_TO_LOCATION;
				txtAnimOutType = LOCATION_TO_OUTSIDE;
			}
			disapper();
			boolean result = show();	//������ʾ�����ж��Ƿ���ʾ�ɹ�
			return result;
		}
		return false;
	}
	
	private boolean show() {
		if((width > 0) && (height > 0) && (vecKeywords != null) && (vecKeywords.size() > 0)
				&& enableShow){				//�ж��Ƿ�������ʾ��������
			enableShow = false;				//�ر�������ʾ��־λ��ֱ����һ��������ʾ�ٿ���
			lastStartAnimTime = System.currentTimeMillis();	//������ʾ���޸���һ����ʾʱ��
			int xCenter = width >> 1;
			int yCenter = height >> 1;		//�����е�����
			int size = vecKeywords.size();	//��Ҫ��ʾ���ı���Ϣ������
			int xItem = width / size;
			int yItem = height / size;		//����ÿһ��item���ı���Ϣ��ƽ����ʹ�õĸ߶ȺͿ��
			
			Log.d("FLYWORDS_DEBUGE", "------------------width= " + width + 
					" height= " + height + " xItem= " + xItem + " yItem= " + yItem
					+ "-------------------");
			
			LinkedList<Integer> listX = new LinkedList<Integer>();	//�洢��ѡ��x����ֵ
			LinkedList<Integer> listY = new LinkedList<Integer>();	//�洢��ѡ��y����ֵ
			
			for(int i = 0; i < size; i++){
				listX.add(i * xItem);
				listY.add(i * yItem);
			}
			
			LinkedList<TextView> listTxtTop = new LinkedList<TextView>();
			LinkedList<TextView> listTxtBottom = new LinkedList<TextView>();
			
			for(int i = 0; i != size; i++){
				String keyword = vecKeywords.get(i);					//��ȡҪ��ʾ�Ĺؼ���
				int ranColor = 0xff000000 | random.nextInt(0x0077ffff);	//�����ɫ
				int[] xy = randomXY(random, listX, listY);		//���λ�ã���ֵ����Ҫ����
				int textSize = TEXT_SIZE_MIN + random.nextInt(TEXT_SIZE_MAX - TEXT_SIZE_MIN + 1);	//�������
				final TextView txtView = new TextView(getContext());	//ʵ����TextView
				
				txtView.setOnClickListener(itemClickListener);
				txtView.setText(keyword);
				txtView.setTextColor(ranColor);
				txtView.setTextSize(textSize);
				txtView.setShadowLayer(2, 2, 2, 0xff696969);
				txtView.setGravity(Gravity.CENTER);
				/**��ȡ�ı���Ϣ�Ŀ��*/
				Paint paint = txtView.getPaint();
				int strWidth = (int) Math.ceil(paint.measureText(keyword));
				xy[IDX_TXT_LENGTH] = strWidth;
				/**���е�һ������������x����*/
				if((strWidth + xy[IDX_X]) > (width - (xItem >> 1))){
					int baseX = width - strWidth;
					xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);	//�����ұ�Ե�ص��ĸ���
				}
				else if(xy[IDX_X] == 0){
					xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);	//�������Ե�ص��ĸ���
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
	 * ����Y���꣬�������������õ�TextView��ʾ����Ļ��
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
			
			/**���еڶ�������������Y����*/
			int yDistance = iXY[IDX_Y] - yCenter;
			int yMove = Math.abs(yDistance);
			inner: for(int k = i - 1; k > 0; k--){
				int[] kXY = (int[])listTxtView.get(k).getTag();
				int startX = kXY[IDX_X];
				int endX = startX + kXY[IDX_TXT_LENGTH];
				
				if(yDistance * (kXY[IDX_Y] - yCenter) > 0){	//��ͬ��
					if(isMixed(startX, endX, iXY[IDX_X], iXY[IDX_X] + iXY[IDX_TXT_LENGTH])){	//�ж��Ƿ��ཻ
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
				
				sortXYList(listTxtView, i + 1);	//���Ѿ�����������������
			}
			
			//������ʾ����������TextView��ʾ����Ļ��
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
	 * �ж��Ƿ��ཻ
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
	 * ����TextView Y�������е�ľ���� listTxtView��������
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
	 * �����listX��listY��ȡ��һ��ֵ�����һ�����귵��
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
	 * �����Ļ�����пɼ��벻�ɼ���TextView;
	 * ��ʹ��getChildAt(i)ʱ��ֻ��ʹ��i--,������ʹ��i++,��ΪҪremoveView
	 * ��remove��ɺ󣬱�ɾ����view֮�������view����ǰ�������ԣ�����view��index�������˸ı�
	 * ���ʹ��i++,�������޷����ĳЩview,�������ɻ����getChildAt(i)����Ϊ�յ����
	 */
	private void disapper(){
		int size = getChildCount();								//���TextView����
		for(int i = size - 1; i >= 0; i--){
			final TextView txt = (TextView) getChildAt(i);		//��ȡ��i��TextView
			
			if(txt.getVisibility() == View.GONE){				//�ж����Ƿ�ɼ�
				removeView(txt);								//����Ѿ����ɼ�����ֱ���Ƴ����TextView
				continue;										//�Ƴ�TextView����������ѭ��
			}
			
			FrameLayout.LayoutParams layParams = 
					(LayoutParams)txt.getLayoutParams();		//��õ�ǰTextView������
			
			/*Log.d("FLYWORDS_DEBUGE", txt.getText() + " leftM= " + 
					layParams.leftMargin + " topM= " + layParams.topMargin +
					" width= " + layParams.width);*/
			
			/**�洢��ǰTextView��X(leftMargin), Y(topMargin)����ֵ�Լ����ֵ*/
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
				 * �������������õ�ǰTextView��ʧ��
				 */
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					txt.setOnClickListener(null);		//ȡ��������
					txt.setFocusable(false);			//ȡ���۽�
					txt.setVisibility(View.GONE);		//ʹ�䲻�ɼ����ȴ����Ƴ�
				}
			});
		}
	}
	
	/**
	 * ��ȡAnimationSetʵ��������alpha,translate,duration�Ȳ���
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	��ʼ���õ�ʵ��animSet
	 */
	private AnimationSet getAnimationSet(int[] xy, int xCenter, int yCenter,
			int type) {
		// TODO Auto-generated method stub
		AnimationSet animSet = new AnimationSet(true);
		animSet.setInterpolator(interpolator);
		TranslateAnimation translate;
		switch (type) {
		case OUTSIDE_TO_LOCATION:							//����Χ�ƶ��������
			animSet.addAnimation(animAlpha2Opaque);			//��͸���䲻͸��
			animSet.addAnimation(animScaleLarge2Normal);	//�ɴ��С
			translate = new TranslateAnimation(
					(xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, 
							(xy[IDX_Y] - (yCenter >> 1)) << 1, 0);
			animSet.addAnimation(translate);
			break;
			
		case LOCATION_TO_OUTSIDE:							//�������������Χ
			animSet.addAnimation(animAlpha2Transparent);	//�ɲ�͸����͸��
			animSet.addAnimation(animScaleNormal2Large);	//��С���
			translate = new TranslateAnimation(0, 
					(xy[IDX_X] + (xy[IDX_TXT_LENGTH] >> 1) - xCenter) << 1, 0, 
							(xy[IDX_Y] - (yCenter >> 1)) << 1);
			animSet.addAnimation(translate);
			break;
			
		case CENTER_TO_LOCATION:							//�������ƶ��������
			animSet.addAnimation(animAlpha2Opaque);			//��͸���䲻͸��
			animSet.addAnimation(animScaleZero2Normal);		//��0���������С
			translate = new TranslateAnimation(
					(-xy[IDX_X] + xCenter), 0, 
							(-xy[IDX_Y] + yCenter), 0);
			animSet.addAnimation(translate);
			break;
			
		case LOCATION_TO_CENTER:							//��������ƶ����е�
			animSet.addAnimation(animAlpha2Transparent);	//�ɲ�͸����͸��
			animSet.addAnimation(animScaleNormal2Zero);		//��������С��Ϊ0
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
	 * ��vecKeywords��װ��׼����ʾ���ı���Ϣ
	 * @param keyword ��װ���ı���Ϣ 
	 * @return result��ָʾ�Ƿ�װ��ɹ�
	 */
	public boolean feedKeywords(String keyword){
		boolean result = false;
		if(vecKeywords.size() < MAX){
			result = vecKeywords.add(keyword);
		}
		return result;
	}
	
	/**
	 * ��ȡ��ǰ�Ķ�������ʱ��
	 * @return animDuration
	 */
	public int getDuration(){
		return animDuration;
	}
	
	/**
	 * ���ö�������ʱ��
	 * @param duration
	 */
	public void setDuration(int duration){
		animDuration = duration;
	}
	
	/**
	 * ��õ�ǰҪ��ʾ���ı���Ϣ
	 * @return vecKeywords
	 */
	public Vector<String> getKeywords(){
		return vecKeywords;
	}
	
	/**
	 * ���vecKeywords�б�����ı���Ϣ
	 */
	public void clearKeywords(){
		vecKeywords.clear();
	}
	
	/**
	 * ֱ��������е�TextView.�����֮ǰ������ʾ����	
	 */
	public void clearAllView(){
		removeAllViews();
	}
	
	/**
	 * ���ü�����
	 * @param listener
	 */
	public void setOnItemClickListener(OnClickListener listener){
		itemClickListener = listener;
	}
}
