package reprator.mobiquity.base_android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * A custom horizontal indeterminate progress bar which displays a smooth colored animation.
 *
 * @author Christophe Beyls
 */
//https://gist.github.com/vipulshah2010/b5796477964b4c608152ee3a7cf04cf1
public class RefreshProgressBar extends View {

    private static final float PROGRESS_BAR_HEIGHT = 4f;

    private final Handler handler;
    private final int mProgressBarHeight;
    boolean mIsRefreshing = false;
    SwipeProgressBar mProgressBar;
    private final Runnable mRefreshUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsRefreshing) {
                mProgressBar.start();
            } else {
                mProgressBar.stop();
            }
        }
    };

    public RefreshProgressBar(Context context) {
        this(context, null, 0);
    }

    public RefreshProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handler = new Handler();
        if (!isInEditMode()) {
            mProgressBar = new SwipeProgressBar(this);
        }
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mProgressBarHeight = (int) (metrics.density * PROGRESS_BAR_HEIGHT + 0.5f);
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacks(mRefreshUpdateRunnable);
        super.onDetachedFromWindow();
    }

    /**
     * Set the four colors used in the progress animation from color resources.
     */
    public void setColorSchemeResources(@ColorRes int colorRes1, @ColorRes int colorRes2, @ColorRes int colorRes3, @ColorRes int colorRes4) {
        final Context context = getContext();
        setColorSchemeColors(ContextCompat.getColor(context, colorRes1), ContextCompat.getColor(context, colorRes2),
                ContextCompat.getColor(context, colorRes3), ContextCompat.getColor(context, colorRes4));
    }

    /**
     * Set the four colors used in the progress animation.
     */
    public void setColorSchemeColors(@ColorInt int color1, @ColorInt int color2, @ColorInt int color3, @ColorInt int color4) {
        mProgressBar.setColorScheme(color1, color2, color3, color4);
    }

    /**
     * @return Whether the progress bar is actively showing refresh progress.
     */
    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    /**
     * Starts or tops the refresh animation. Animation is stopped by default. After the stop animation has completed,
     * the progress bar will be fully transparent.
     */
    public void setRefreshing(boolean refreshing) {
        if (mIsRefreshing != refreshing) {
            mIsRefreshing = refreshing;
            handler.removeCallbacks(mRefreshUpdateRunnable);
            handler.post(mRefreshUpdateRunnable);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (mProgressBar != null) {
            mProgressBar.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mProgressBar != null) {
            mProgressBar.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), mProgressBarHeight);
    }
}