package team.dingding.musicgloves.JumpBall;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import team.dingding.musicgloves.activity.MainActivity;

public class BallSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    MainActivity sportActivity ;//调用该SurfaceView的上下文引用
    private Ball ball ;//小球
    SurfaceHolder holder ;

    public BallSurfaceView(Context context) {
        super(context);
        this.sportActivity = (MainActivity)context ;
        ball = new Ball(100, 800, this);
        holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(canvas == null) canvas = holder.lockCanvas();//锁定画布
        Paint p = new Paint();

        int c = p.getColor();
        p.setColor(Color.GRAY);//设置背景白色
        if(canvas != null) {
            canvas.drawRect(200, 200, 300, 300, p);

        }
        p.setColor(c);
        ball.onDraw(canvas);
        holder.unlockCanvasAndPost(canvas);//释放锁
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,                               int height) {

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new RefreshThread().start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private class RefreshThread extends Thread{

        @Override
        public void run() {

            while(true){
                Canvas canvas = null;
                try{
                    onDraw(canvas);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }

    }

} 