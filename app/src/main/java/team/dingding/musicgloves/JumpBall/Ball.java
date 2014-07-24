package team.dingding.musicgloves.JumpBall;

/**
 * Created by guoyanchang on 2014/7/23.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceView;

import team.dingding.musicgloves.R;
import team.dingding.musicgloves.activity.MainActivity;
import team.dingding.musicgloves.activity.StateFragment;

public class Ball{
    /**
     18.          * 球的高
     19.          */
    public static final int HEIGHT = 93;
    StateFragment SF;
    /**
     22.          * 球的宽
     23.          */
    public static final int WIDTH = 93;
    private  float STEPLENGTH = 10;//每次运动的间距

    private float runX_1 ;//球1的位置
    private float runY_1 ;//球1的位置
    private float runX_2 ;//球2的位置
    private float runY_2 ;//球2的位置
    private float runX_3 ;//球3的位置
    private float runY_3 ;//球3的位置
    private float runX_4 ;//球4的位置
    private float runY_4 ;//球4的位置
    private float runX_5 ;//球5的位置
    private float runY_5 ;//球5的位置

    private boolean move1=false;
    private boolean move2=false;
    private boolean move3=false;
    private boolean move4=false;
    private boolean move5=false;

    SurfaceView sfv;;

    private boolean upDirection1 = false;//判断向上还是向下运动，false：向下；true：向上
    private boolean upDirection2 = false;//判断向上还是向下运动，false：向下；true：向上
    private boolean upDirection3 = false;//判断向上还是向下运动，false：向下；true：向上
    private boolean upDirection4 = false;//判断向上还是向下运动，false：向下；true：向上
    private boolean upDirection5 = false;//判断向上还是向下运动，false：向下；true：向上

    private float maxHeight ;//当前运动最高的高度
    private float highest=0;
    private Paint paint ;

    Bitmap ballBitmap ;//球的图片
    public Ball(float initX , float initY , SurfaceView sfv,StateFragment sf){
        this.SF=sf;
        this.runX_1  = initX;
        this.runY_1 = initY-HEIGHT;

        this.runX_2  = initX+100;
        this.runY_2 = initY-HEIGHT;

        this.runX_3  = initX+200;
        this.runY_3 = initY-HEIGHT;

        this.runX_4  = initX+300;
        this.runY_4 = initY-HEIGHT;

        this.runX_5  = initX+400;
        this.runY_5 = initY-HEIGHT;
        highest=initY;
        maxHeight = 250;

        this.sfv = sfv;
        ballBitmap = BitmapFactory.decodeResource(sfv.getResources(), R.drawable.ball);//加载图片
        paint = new Paint();
    }

    public void onDraw(Canvas canvas,boolean isMove1,boolean isMove2,boolean isMove3,boolean isMove4,boolean isMove5) {
        int c = paint.getColor();//保存颜色，之后还原为之前颜色
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if(this.move1==false)
        {
            this.move1=isMove1;
        }
        else if(this.move1==true&&isMove1==true){
            upDirection1=false;
        }


        if(this.move2==false)
        {
            this.move2=isMove2;
        }
        else if(this.move2==true&&isMove2==true){
            upDirection2=false;
        }


        if(this.move3==false)
        {
            this.move3=isMove3;
        }
        else if(this.move3==true&&isMove3==true){
            upDirection3=false;
        }


        if(this.move4==false)
        {
            this.move4=isMove4;
        }
        else if(this.move4==true&&isMove4==true){
            upDirection4=false;
        }


        if(this.move5==false)
        {
            this.move5=isMove5;
        }
        else if(this.move5==true&&isMove5==true){
            upDirection5=false;
        }

//        this.move1=isMove1;
//        this.move2=isMove2;
//        this.move3=isMove3;
//        this.move4=isMove4;
//        this.move5=isMove5;

        boundaryTest();
        move();
        if(canvas != null){
            canvas.drawBitmap(ballBitmap,runX_1,runY_1,paint);
            canvas.drawBitmap(ballBitmap,runX_2,runY_2,paint);
            canvas.drawBitmap(ballBitmap,runX_3,runY_3,paint);
            canvas.drawBitmap(ballBitmap,runX_4,runY_4,paint);
            canvas.drawBitmap(ballBitmap,runX_5,runY_5,paint);
        }
        paint.setColor(c);

    }
    /**
     56.          * 运动
     57.          */
    private void move() {
        if(move1) {
            if (upDirection1) {//向下

                float begin = runY_1 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_1 = runY_1 + STEPLENGTH;
            } else {//向上

                float begin = runY_1 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_1 = runY_1 - STEPLENGTH;
            }
        }//小球1的运动

        if(move2) {
            if (upDirection2) {//向下

                float begin = runY_2 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_2 = runY_2 + STEPLENGTH;
            } else {//向上

                float begin = runY_2 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_2 = runY_2 - STEPLENGTH;
            }
        }


        if(move3) {
            if (upDirection3) {//向下

                float begin = runY_3 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_3 = runY_3 + STEPLENGTH;
            } else {//向上

                float begin = runY_3 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_3 = runY_3 - STEPLENGTH;
            }
        }


        if(move4) {
            if (upDirection4) {//向下

                float begin = runY_4 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_4 = runY_4 + STEPLENGTH;
            } else {//向上

                float begin = runY_4 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_4 = runY_4 - STEPLENGTH;
            }
        }
        if(move5) {
            if (upDirection5) {//向下

                float begin = runY_5 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_5 = runY_5 + STEPLENGTH;
            } else {//向上

                float begin = runY_5 - maxHeight;
                STEPLENGTH = (float) java.lang.Math.sqrt(begin);
                //STEPLENGTH=begin;
                runY_5 = runY_5 - STEPLENGTH;
            }
        }
    }

    /**
     70.          * 边界检测，使球不会飞出边界
     71.          */
    private void boundaryTest(){

        if(runY_1 > highest - HEIGHT){//向下运动到头
            upDirection1 = !upDirection1;//方向置反
            runY_1 = highest - HEIGHT;
            this.move1=false;
            //SF.setMove(0);

            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY_1 < maxHeight ){//向上运动到头
            upDirection1 = !upDirection1;//方向置反

            runY_1 = maxHeight +2;

        }

        if(runY_2 > highest - HEIGHT){//向下运动到头
            upDirection2 = !upDirection2;//方向置反
            runY_2 = highest - HEIGHT;
            this.move2=false;
            //SF.setMove(1);
            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY_2 < maxHeight ){//向上运动到头
            upDirection2 = !upDirection2;//方向置反

            runY_2 = maxHeight +2;

        }

        if(runY_3 >highest - HEIGHT){//向下运动到头
            upDirection3 = !upDirection3;//方向置反
            runY_3 = highest - HEIGHT;
            this.move3=false;
            //SF.setMove(2);
            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY_3 < maxHeight ){//向上运动到头
            upDirection3 = !upDirection3;//方向置反

            runY_3 = maxHeight +2;

        }

        if(runY_4 > highest - HEIGHT){//向下运动到头
            upDirection4 = !upDirection4;//方向置反
            runY_4 = highest - HEIGHT;
            this.move4=false;
           // SF.setMove(3);
            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY_4 < maxHeight ){//向上运动到头
            upDirection4 = !upDirection4;//方向置反

            runY_4 = maxHeight +2;

        }

        if(runY_5 > highest - HEIGHT){//向下运动到头
            upDirection5 = !upDirection5;//方向置反
            runY_5 = highest - HEIGHT;
            this.move5=false;
            //SF.setMove(4);
            //stepReduce = (int) (maxHeight * REDUCEPERCENTAGE);
            // maxHeight = maxHeight + stepReduce ;//最大高度递减

        }
        if(runY_5 < maxHeight ){//向上运动到头
            upDirection5 = !upDirection5;//方向置反

            runY_5 = maxHeight +2;

        }
    }
}