package mps.jm.com.musicselectlibrar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

import java.io.File;

/**
 * The type Music part select view.
 *
 * @author sunbaixin QQ:283122529
 * @name MusicPartSelect
 * @class name ：com.jm.mps
 * @class describe
 * @class describe
 * @time 2017 /11/29 下午4:24
 * @change
 * @chang time
 */
public class MusicPartSelectView extends FrameLayout implements View.OnTouchListener {
    public String TAG = MusicPartSelectView.class.getName();
    private ImageView partImg;
    //上一次滑动的距离
    public int lastX;
    //当次按下的距离
    public int downX;
    //遮罩image
    private ImageView selectImg;
    //音乐的时长(秒)
    private int musicTime = 180;
    //音轨的高度
    private int parHeight;
    //音轨的宽度
    private int parWidth;
    //遮罩的宽度
    private int selectImgWidth;
    //视频的时长(秒)
    private int videoTime = 5;
    //当前拖动到视频的多少秒
    private int currentTime;
    //音轨图本地路径
    private String partPath;


    private OnTimeChangeListener timeChangeListener;

    /**
     * Instantiates a new Music part select view.
     *
     * @param context the context
     */
    public MusicPartSelectView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Music part select view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public MusicPartSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Music part select view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public MusicPartSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Init part img.初始化音轨图片
     */
    public void initPartImg() {
        partImg = new ImageView(getContext());
        Bitmap bitmap = BitmapFactory.decodeFile(partPath);
        parHeight = bitmap.getHeight();
        parWidth = bitmap.getWidth();
        partImg.setImageBitmap(bitmap);
        addView(partImg);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) partImg.getLayoutParams();
        params.height = (int) getRawSize(parHeight);//设置当前控件布局的高度
        params.width = (int) getRawSize(parWidth);//设置当前控件布局的高度
        params.gravity = Gravity.CENTER_VERTICAL;
        partImg.setLayoutParams(params);//将设置好的布局参数应用到控件中

    }


    /**
     * Init select img. 初始化遮罩图片
     */
    public void initSelectImg() {
        selectImg = new ImageView(getContext());
        selectImg.setBackgroundResource(R.mipmap.icon_music_select);
        addView(selectImg);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) selectImg.getLayoutParams();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.WRAP_CONTENT;//设置当前控件布局的高度
        params.gravity = Gravity.CENTER;
        selectImg.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }

    /**
     * Init.初始化
     */
    public void init() {
        initPartImg();
        initSelectImg();
        calculateSelectImgWidth();
        lastX = getScWidth() / 2 - selectImgWidth / 2;
        ViewHelper.setTranslationX(partImg, lastX);
        partImg.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int rawX = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 上一次离开时的坐标
                downX = rawX;
                break;
            case MotionEvent.ACTION_MOVE:
                // 两次的偏移量
                int offsetX = rawX - downX + lastX;
                ViewHelper.setTranslationX(partImg, offsetX);
                currentTime = (int) (musicTime * (((double) (-((-(getScWidth() / 2 - selectImgWidth / 2)) + offsetX)) / (double) +getRawSize(parWidth))));
                if (currentTime + videoTime > musicTime) {
                    currentTime = musicTime - videoTime;
                } else if (currentTime < 0) {
                    currentTime = 0;
                }
                timeChangeListener.onChange(currentTime);
                break;
            case MotionEvent.ACTION_UP:
                if (rawX - downX + lastX > getScWidth() / 2 - selectImgWidth / 2) {
                    lastX = getScWidth() / 2 - selectImgWidth / 2;
                    ViewHelper.setTranslationX(partImg, lastX);
                } else if (rawX - downX + lastX < -((partImg.getWidth() - (getScWidth() / 2))) + selectImgWidth / 2) {
                    lastX = -((partImg.getWidth() - (getScWidth() / 2)) - selectImgWidth / 2);
                    ViewHelper.setTranslationX(partImg, lastX);
                } else {
                    lastX += rawX - downX;
                }
                currentTime = (int) (musicTime * (((double) (-((-(getScWidth() / 2 - selectImgWidth / 2)) + lastX)) / (double) +getRawSize(parWidth))));
                if (currentTime + videoTime > musicTime) {
                    currentTime = musicTime - videoTime;
                } else if (currentTime < 0) {
                    currentTime = 0;
                }
                timeChangeListener.onChange(currentTime);
                timeChangeListener.onEnd(currentTime);
                break;
            default:
                break;

        }
        return true;
    }


    /**
     * Sets data. 设置数据
     *
     * @param musicPath the music path 音乐的本地路径
     * @param partPath  the part path 音轨图本地路径
     * @param videoTime the video time 视频的时长(秒)
     * @param listener  the listener 回调
     */
    public void setData(String musicPath, String partPath, int videoTime, OnTimeChangeListener listener) {
        this.musicTime = MediaPlayer.create(getContext(), Uri.fromFile(new File(musicPath))).getDuration();
        this.timeChangeListener = listener;
        this.videoTime = videoTime;
        this.partPath = partPath;
        removeAllViews();
        init();

    }


    /**
     * The interface On time change listener. 拖动监听
     */
    public interface OnTimeChangeListener {
        /**
         * On change.
         *
         * @param currentTime the current time 改变时候的监听
         */
        void onChange(int currentTime);

        /**
         * On end.
         *
         * @param currentTime the current time 结束时候的监听
         */
        void onEnd(int currentTime);
    }


    /**
     * Calculate select img width.视频遮罩的宽度
     */
    public void calculateSelectImgWidth() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) selectImg.getLayoutParams();
        params.height = LayoutParams.WRAP_CONTENT;
        selectImgWidth = (int) getRawSize((parWidth / musicTime * videoTime + 20));
        params.width = selectImgWidth;
        selectImgWidth = (int) (selectImgWidth - getRawSize(20));
        params.gravity = Gravity.CENTER;
        selectImg.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }

    /**
     * Gets sc width.获取屏幕的宽度
     *
     * @return the sc width 屏幕的宽度
     */
    public int getScWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();

    }


    /**
     * Gets raw size. 代码设置dp
     *
     * @param value the value 数值
     * @return the raw size 返回的dp
     */
    public float getRawSize(float value) {
        Resources res = this.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, res.getDisplayMetrics());
    }

}
