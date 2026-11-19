package in.gov.nmc.rangmach.widget;


import android.content.Context;
import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.ScaleGestureDetector;
import android.widget.LinearLayout;

public class PinchZoomView extends LinearLayout {

        private ScaleGestureDetector scaleGestureDetector;
        private float scaleFactor = 1.0f;

        public PinchZoomView(Context context) {
            super(context);
            init(context);
        }

        public PinchZoomView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            return scaleGestureDetector.onTouchEvent(event);
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 3.0f)); // Limit the scale factor (adjust as needed)
                applyScaleAndTranslation();
                return true;
            }
        }

        private void applyScaleAndTranslation() {
            // Iterate through all the child views and apply the scaling
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setScaleX(scaleFactor);
                getChildAt(i).setScaleY(scaleFactor);
            }
        }
    }