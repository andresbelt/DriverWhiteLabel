package co.com.autolagos.rtaxi.local.driver.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ViewGpsPermision extends FrameLayout {
    public ViewGpsPermision(Context context) {
        super(context);
    }

    public ViewGpsPermision(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGpsPermision(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        return true;
        //If you return true all touch events are disabled.
       // Return false to let them work normally
    }
}
