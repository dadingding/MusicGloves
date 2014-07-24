package team.dingding.musicgloves.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import team.dingding.musicgloves.JumpBall.Ball;
import team.dingding.musicgloves.R;

/**
 * Created by Elega on 2014/7/8.
 */
public class StateFragment extends MainActivity.PlaceholderFragment {
    private Ball ball ;
    SurfaceView sfv;
    SurfaceHolder sfh;

    private Ball ball2 ;
    SurfaceView sfv2;
    SurfaceHolder sfh2;
    Button test;
    boolean runnable=true;

    boolean[] isMove=new boolean[10];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state, container, false);
        sfv = (SurfaceView) rootView.findViewById(R.id.SurfaceView01);
        sfv.setZOrderOnTop(true);//设置画布  背景透明
        sfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        sfh = sfv.getHolder();
        ball = new Ball(300, 600, sfv,this);

        sfv2 =(SurfaceView) rootView.findViewById(R.id.SurfaceView02);
        sfv2.setZOrderOnTop(true);
        sfv2.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        sfh2=sfv2.getHolder();
        ball2=new Ball(300,600,sfv2,this);

//        test=(Button)rootView.findViewById(R.id.button2);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testjump(view);
//            }});
//        test=(Button)rootView.findViewById(R.id.button3);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testjump(view);
//            }});

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(runnable){
                    Canvas canvas = null;
                    try{
                        onDraw(canvas);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Canvas canvas2 = null;
                    try{
                        onDraw2(canvas2);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return rootView;
    }

    public static MainActivity.PlaceholderFragment newInstance(int sectionNumber) {
        StateFragment fragment = new StateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    protected void onDraw(Canvas canvas) {
        canvas = sfh.lockCanvas(new Rect(100, 100, 700, 700));// 关键:获取画布

        if(canvas == null) canvas = sfh.lockCanvas();//锁定画布
        Paint p = new Paint();

        int c = p.getColor();
        p.setColor(Color.GRAY);//设置背景白色
        p.setAlpha(0x0);
        if(canvas != null) {
            //canvas.drawRect(300, 300, 800, 800, p);
        }
        p.setColor(c);
        for(int i=0;i<5;i++)
        {
            isMove[i]=getMainActivity().isMoveOfWhich(i);
        }
        ball.onDraw(canvas,isMove[0],isMove[1],isMove[2],isMove[3],isMove[4]);

        for(int i=0;i<5;i++)
        {
            setMove(i);
        }
        sfh.unlockCanvasAndPost(canvas);//释放锁
    }

    protected void onDraw2(Canvas canvas) {
        canvas = sfh2.lockCanvas(new Rect(100, 100, 700, 700));// 关键:获取画布

        if(canvas == null) canvas = sfh2.lockCanvas();//锁定画布
        Paint p = new Paint();

        int c = p.getColor();
        p.setColor(Color.GRAY);//设置背景白色
       // p.setAlpha(0x0);
        if(canvas != null) {
            //canvas.drawRect(100, 600, 700,1200, p);
        }
        p.setColor(c);
        for(int i=5;i<10;i++)
        {
            isMove[i]=getMainActivity().isMoveOfWhich(i);
        }
        ball2.onDraw(canvas,isMove[5],isMove[6],isMove[7],isMove[8],isMove[9]);

        for(int i=5;i<10;i++)
        {
            setMove(i);
        }
        sfh2.unlockCanvasAndPost(canvas);//释放锁
    }
    public void testjump(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(runnable){
                    Canvas canvas = null;
                    try{
                        onDraw(canvas);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Canvas canvas2 = null;
                    try{
                        onDraw2(canvas2);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void setMove(int index){getMainActivity().setMoveOfWhich(index);}
    @Override
    public void onDestroyView() {
        runnable=false;
        super.onDestroyView();
    }

    public void updateText(){

    }
}
