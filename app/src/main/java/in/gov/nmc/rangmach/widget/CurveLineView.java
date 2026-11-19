package in.gov.nmc.rangmach.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

    public class CurveLineView extends View {

        private Paint paint;
        private Path path;

        public CurveLineView(Context context) {
            super(context);
            init();
        }

        public CurveLineView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CurveLineView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);

            path = new Path();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            // Start point of the curved line
            int startX = 0;
            int startY = height / 2;

            // End point of the curved line
            int endX = width;
            int endY = height / 2;

            // Control points for the curve
            int controlX1 = width / 4;
            int controlY1 = height / 4;
            int controlX2 = width * 3 / 4;
            int controlY2 = height * 3 / 4;

            path.reset();
            path.moveTo(startX, startY);
            path.cubicTo(controlX1, controlY1, controlX2, controlY2, endX, endY);

            canvas.drawPath(path, paint);
        }
    }
