package googleplay.xiaokai.com.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private SlidMenu slidmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //如果继承的是ActionBarActivity或者是AppCompatActivity就会报错。
//        如果你执意要用这个方法，请继承Activity。
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        如果你继承的是AppCompatActivity或ActionBarActivity请调用下面的方法代替上面的方法
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        slidmenu = (SlidMenu) findViewById(R.id.horscrview);
    }
    public void toggle(View view){
        slidmenu.toggle();
    }

}
