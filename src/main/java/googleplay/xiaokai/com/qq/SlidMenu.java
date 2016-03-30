package googleplay.xiaokai.com.qq;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by 孙晓凯 on 2016/3/27.
 */
public class SlidMenu extends HorizontalScrollView {
    int mScreenWit;//屏幕宽度
    int mRightWithScr;
    LinearLayout mWrap;
    ViewGroup mMenu;
    ViewGroup mContent;
    int mMenuWidth ;
    private boolean flag;
    private boolean isOpen;

    //无自定义属性时调用
    public SlidMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /*
    在代码中直接new时调用此构造
     */
    public SlidMenu(Context context) {
        this(context, null);
    }

    /*
    使用自定义属性时，调用此构造
     */
    public SlidMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //得到屏幕的宽度
        WindowManager winmana = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics metris = new DisplayMetrics();
        winmana.getDefaultDisplay().getMetrics(metris);
        mScreenWit = metris.widthPixels;//得到的是像素

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SlidMenu,defStyleAttr,0);
        int n = array.getIndexCount();
        for(int i=0;i<n;i++){
           int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.SlidMenu_RithtPadding:
                    mRightWithScr = array.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        array.recycle();//

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!flag) {
            mWrap = (LinearLayout) getChildAt(0);//得到此空间中的第一个子控件
            mMenu = (ViewGroup) mWrap.getChildAt(0);//得到menu
            mContent = (ViewGroup) mWrap.getChildAt(1);//得到内容控件

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWit - mRightWithScr;//侧滑菜单的宽度为屏幕宽度减去50dp
            mContent.getLayoutParams().width = mScreenWit;//设置内容控件宽度
            flag = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*
    实现的功能是将menu隐藏，通过设置偏移量
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            this.scrollTo(mMenuWidth, 0);//向左移动
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int scx = getScrollX(); //就是当前view的左上角相对于母视图的左上角的X轴偏移量
                if(scx>=mMenuWidth/2){
                    this.smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }else{
                    this.smoothScrollTo(0,0);
                    isOpen = true;
                }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /*
    打开菜单
     */
    public void openMenu(){
        if(isOpen)return;
        else {
            this.smoothScrollTo(0,0);//打开
            isOpen = true;
        }
    }

    /*
    关闭菜单
     */
    public void closeMenu(){
        if(!isOpen){
            return ;
        }else{
            this.smoothScrollTo(mMenuWidth,0);
            isOpen = false;
        }
    }

    /*
    切换菜单
     */
    public void toggle(){
        if(isOpen){
            closeMenu();
        }else{
            openMenu();
        }
    }

    /**
     * 滚动发生时
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth; // 1 ~ 0

        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // 调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);
        // 设置content的缩放的中心点
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);

    }
}
