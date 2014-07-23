package team.dingding.musicgloves.JumpBall;

/**
 * Created by guoyanchang on 2014/7/23.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.activity.MainActivity;

public class Ball {
    /**
     18.          * 球的高
     19.          */
    public static final int HEIGHT = 93;
    /**
     22.          * 球的宽
     23.          */
    public static final int WIDTH = 93;
    private  float STEPLENGTH = 10;//每次运动的间距
    private static final float REDUCEPERCENTAGE =  0.35F;//递减系数
    private int stepReduce ;//每次反向运动的缩短的距离

    private float runX ;//球的位置
    private float runY ;//球的位置
    private BallSurfaceView bsv ;
    private boolean upDirection = false;//if true,up direction,or is down direction
    private float maxHeight ;//当前运动最高的高度
    private Paint paint ;

    Bitmap ballBitmap ;//球的图片
    MainActivity sa ;
    public Ball(float initX , float initY , BallSurfaceView bsv){
        this.runX  = initX;
        this.runY = initY+2;
        maxHeight = initY;
        this.bsv = bsv;
        ballBitmap = BitmapFactory.decodeResource(bsv.getResources(), R.drawable.ball);//加载图片
        paint = new Paint();
        sa = bsv.sportActivity;
    }

    public void onDraw(Canvas canvas) {
        int c = paint.getColor();//保存颜色，之后还原为之前颜色
        boundaryTest();
        if(canvas != null) canvas.drawBitmap(ballBitmap,runX,runY,paint);
        paint.setColor(c);
        move();
    }
    /**
     56.          * 运动
     57.          */
    private void move() {
        if(maxHeight >= (900 - HEIGHT)) {
            return;
        }
        if(upDirection){//向下
            int d=900 - HEIGHT;
            float begin=runY-maxHeight;
            STEPLENGTH=(float)java.lang.Math.sqrt(begin*0.5);
            //STEPLENGTH=begin;
            runY = runY + STEPLENGTH ;
        }else{//向上
            int d=900 - HEIGHT;
            float begin=runY-maxHeight;
            STEPLENGTH=(float)java.lang.Math.sqrt(begin*0.5);
            //STEPLENGTH=begin;
            runY = runY - STEPLENGTH ;
        }
    }

    /**
     70.          * 边界检测，使球不会飞出边界
     71.          */
    private void boundaryTest(){

        if(runY > 900 - HEIGHT){//向下运动到头
            upDirection = !upDirection;//方向置反
            runY = 900 - HEIGHT;
            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY < maxHeight ){//向上运动到头
            upDirection = !upDirection;//方向置反
            if(maxHeight >= (900 - HEIGHT)) return;
            runY = maxHeight +2;

        }
    }
}