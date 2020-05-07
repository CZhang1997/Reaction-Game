package com.cxz173430.reactiongame;
/**
 * This class is for the game view and draw the shapes
 * @Author:     Churong Zhang
 * @Date:       3/29/2020
 * @Class:      CS 6326
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class GameTableView extends View {

    // the paint to paint the canvas
    private TextPaint mTextPaint;

    // activity variables
    private long lastRecordedTime; // lastRecordedTime use to check when to add random amount of shapes into the game
    private long score;             // the score is number of correct shape has touched
    private int shapeLost;          // the number of correct shape disappear before the player touch it
    private long timeUsed;          // the time in millisecond that the player used to complete 10 shapes
    private boolean gamePause;

    // set up the game define what shape and color to touch
    public void setCircleSelected(boolean circleSelected) {
        this.circleSelected = circleSelected;
    }
    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
    }

    // game variables that define shape and color
    private boolean circleSelected;
    private int colorSelected;

    // estimate game width and height
    final int gameWidth = 900;
    final int gameHeight = 1000;
    public final int shapesToPop = 10;  // the number of correct shapes need to touch to end the game

    private int waitRefreshTime;    // random generate wait time to add shapes to the game

    // define thread variables
    private Runnable gameRunner;
    Handler drawHandler;
    int frameRate;

    Random random;  // random variable
    private ArrayList<Shape> shapes;    // the list of shapes to display in the game
                    // colors         Red,          Orange,     Yellow,     Green,          Blue,       Purple, and White
    private int [] colors = new int[]{Color.RED, 0xFFFFA500, Color.YELLOW, Color.GREEN, Color.BLUE, 0xFF800080, Color.WHITE};

    // system auto generate codes for custom view
    public GameTableView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameTableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameTableView, defStyle, 0);

        a.recycle();

        System.out.println("Width: " + this.getWidth() + " height: " + this.getHeight());
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // define class variables
        random = new Random();
        shapes = new ArrayList<>();
        drawHandler = new Handler();
        lastRecordedTime = 0;
        score = 0;
        shapeLost = 0;
        timeUsed = 0;
        frameRate = 100;
        gamePause = false;

        waitRefreshTime = (random.nextInt(5) + 3) * 1000;
        gameRunner = new Runnable() {
            @Override
            public void run() {
                // add shapes to the game
                addShapes();
                invalidate();
            }
        };
    }
    public void restart(boolean circle, int color)
    {
        gamePause = true;
        circleSelected = circle;
        colorSelected = color;
        score = 0;
        shapeLost = 0;
        timeUsed = 0;
    }

    private void addShapes()
    {
        // wait for a random time before adding shapes to the game
        if(lastRecordedTime + waitRefreshTime > System.currentTimeMillis())
            return;

        // randomly add 6 to 12 shapes to the game
        int size = random.nextInt(7) + 6;
        int i = 0;
        while(i < size)
        {
            // set a maximum number of shapes to 25
            if(shapes.size() > 25)
            {
                // wait for 5 second more
                lastRecordedTime = System.currentTimeMillis() + 5000;
                return;
            }
            addShape();
            i++;
        }
        // random generate wait time for 3 to 5 second
        waitRefreshTime = (random.nextInt(5) + 3) * 1000;
        // get the current time as last record time
        lastRecordedTime = System.currentTimeMillis();
    }

    /**
     * check if two shapes are overlap
     * @param s1    first shape
     * @param s2    second shape
     * @return true if they are overlap
     */
    private boolean equals(Shape s1, Shape s2)
    {
        int minDistanceBetween = 35;    // set an extra distance for two shape
        // get their width in the screen
        int widthInPx1 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, s1.getWidth(), getResources().getDisplayMetrics());
        int widthInPx2 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, s2.getWidth(), getResources().getDisplayMetrics());

        // get their position
        int x1 = s1.getPosX();
        int y1 = s1.getPosY();
        int x2 = s2.getPosX();
        int y2 = s2.getPosY();

        // if the shape is square, then need to fix the position to center of the shape
        if(!s1.isCircle())
        {
            x1 = x1 + (widthInPx1 / 2);
            y1 = y1 + (widthInPx1 / 2);
        }
        if(!s2.isCircle())
        {
            x2 = x2 + (widthInPx2 / 2);
            y2 = y2 + (widthInPx2 / 2);
        }
        // get the length of the two radius
        int R1R2 = widthInPx1 / 2 + widthInPx2 / 2;
        // get the distance between two points
        int distance = (int)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

        // if R1R2 + minDistanceBetween < distance then they are not overlap
        if(R1R2 + minDistanceBetween < distance)
            return false;
        else
            return true;
    }
    // add a shape to the game
    public void addShape()
    {
        boolean exist = true;
        Shape s = new Shape();
        while(exist) // keep looping util a shape does not overlap with any shape in the list
        {
            // random generate the width from 32 to 64
            int width = random.nextInt(33) + 32;

            // random generate the shape's location
            float diaPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
            int tSizePx =  (int)diaPixels;
            int posX = random.nextInt(gameWidth - tSizePx) + tSizePx;   // x value
            boolean circle = random.nextBoolean();          // circle or square
            int posY = random.nextInt(gameHeight - tSizePx) + tSizePx; // y value
            int color = random.nextInt(colors.length);      // random color
            int liveTime = (random.nextInt(5) + 3) * 1000;   // random live time from 3 to 8 second for the shape
            long curTime = System.currentTimeMillis(); // shape's birth time
            // construct the new shape
            s = new Shape(circle, posX,posY,color, width, curTime, curTime + liveTime);
            // check if this new shape overlap with another shape in the list
            exist = false;
            for(Shape shape : shapes)
            {
                if(equals(shape, s))
                {
                    exist = true;
                    break;
                }
            }
        }
        shapes.add(s);
    }

    /**
     * remove a shape base on the position of where the player touch
     * @param posX  x pos
     * @param posY  y pos
     * @return
     */
    public long[] removeShape(float posX, float posY)
    {
        // if the player already touch enough shape then stop the game
        if(score == shapesToPop)
            return new long[]{score, timeUsed, shapeLost};
        gamePause = false;
        // check if that position has a shape
        for(int i = 0; i < shapes.size(); i ++)
        {
            Shape s = shapes.get(i);

            // prepare the shape's information
            int pixInScreen = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, s.getWidth(), getResources().getDisplayMetrics());
            int leftX = s.getPosX();
            int topY = s.getPosY();
            if(s.isCircle())
            {   // fix the position of it is a circle
                leftX = leftX - pixInScreen / 2;
                topY = topY - pixInScreen / 2;
            }
            // check if the point is inside this shape
            if((posX >= leftX && posX <= leftX + pixInScreen) &&
                    (posY >= topY && posY <= topY + pixInScreen))
            {   // there is a shape at this postion, check if is the correct shape
                if(s.isCircle() == circleSelected && s.getColor() == colorSelected)
                {
                    score += 1;
                    timeUsed += System.currentTimeMillis() - s.getBirthTime();
                }
                else
                {
                    // wrong clicked
                }
                // remove the shape, and add a new shape to the game
                shapes.remove(i);
                addShape();
                break;
            }
        }
        return new long[]{score, timeUsed, shapeLost};
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // set the background as a dark color
        super.setBackgroundColor(Color.BLACK);
        // redraw all shapes and check if there are still alive
        for(int i = shapes.size() - 1; i >= 0; i --)
        {
            Shape s = shapes.get(i);
            // check if the current time is greater than or equal to the shape's dead time
            if(s.getDeadTime() <= System.currentTimeMillis())
            {
                if(s.isCircle() == circleSelected && s.getColor() == colorSelected)
                {
                    if(!gamePause) // if game was not pause then keep counting the score
                    {
                        shapeLost ++;
                        timeUsed += s.getDeadTime() - s.getBirthTime();
                    }

                }
                shapes.remove(i);
                // remove this shape
                continue;
            }
            // draw the shape
            mTextPaint.setColor(colors[s.getColor()]);
            int widthInPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, s.getWidth(), getResources().getDisplayMetrics());
            if(s.isCircle())
            {
                canvas.drawCircle(s.getPosX(),s.getPosY(), widthInPx / 2, mTextPaint);
            }
            else
            {
                canvas.drawRect(s.getPosX(), s.getPosY(), s.getPosX()+ widthInPx, s.getPosY() + widthInPx, mTextPaint);
            }
        }
        drawHandler.postDelayed(gameRunner, frameRate);
    }
}
