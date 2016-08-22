package com.example.shiwei.giftofnumen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.shiwei.giftofnumen.gift.GiftFragment;
import com.example.shiwei.giftofnumen.hot.HotFragment;
import com.example.shiwei.giftofnumen.kaifu.KaiFuFragment;
import com.example.shiwei.giftofnumen.spice.SpiceFragment;

public class MainActivity extends MyActivity {

    private RadioGroup radioGroup;
    private RadioButton maingift;
    private FragmentManager fragmentManger;
    private Fragment gift,hot,kaifu,spice;
    private boolean isExit=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                isExit=false;
            }
        }
    };
    private DrawerLayout drawlayout;
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManger = getSupportFragmentManager();
        initView();
        maingift.setChecked(true);
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.mainactivity_radiogroup);
        maingift = (RadioButton)findViewById(R.id.mainactivity_gift);
        linear=(LinearLayout)findViewById(R.id.mainactivity_slide_linear);
        drawlayout=(DrawerLayout)findViewById(R.id.mainactivity_drawlayout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //管理Fragmentmanger
                FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
                hideFramgment(fragmentTransaction);
                switch (i) {
                    case R.id.mainactivity_gift:
                        if (gift == null) {
                            gift = new GiftFragment();
                            fragmentTransaction.add(R.id.mainactivity_framelayout, gift);
                        } else {
                            fragmentTransaction.show(gift);
                        }
                        break;
                    case R.id.mainactivity_kaifu:
                        if (kaifu == null) {
                            kaifu = new KaiFuFragment();
                            fragmentTransaction.add(R.id.mainactivity_framelayout, kaifu);
                        } else {
                            fragmentTransaction.show(kaifu);
                        }
                        break;
                    case R.id.mainactivity_hot:
                        if (hot == null) {
                            hot = new HotFragment();
                            fragmentTransaction.add(R.id.mainactivity_framelayout, hot);
                        } else {
                            fragmentTransaction.show(hot);
                        }
                        break;
                    case R.id.mainactivity_spice:
                        if (spice == null) {
                            spice = new SpiceFragment();
                            fragmentTransaction.add(R.id.mainactivity_framelayout, spice);
                        } else {
                            fragmentTransaction.show(spice);
                        }
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }

    private void hideFramgment(FragmentTransaction fragmentTransaction) {
       if (gift!=null){
           fragmentTransaction.hide(gift);
       }
        if (kaifu!=null){
            fragmentTransaction.hide(kaifu);
        }
        if (hot!=null){
            fragmentTransaction.hide(hot);
        }
        if (spice!=null){
            fragmentTransaction.hide(spice);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      if (keyCode==KeyEvent.KEYCODE_BACK){
            if (drawlayout.isDrawerOpen(linear)){
                drawlayout.closeDrawer(linear);
            }else {
                exit();
            }
            return  false;
      }else {
          return super.onKeyDown(keyCode, event);
      }
    }

    private void exit() {
        if (!isExit){
            isExit=true;
            Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0,2000);
        }else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(-1);
        }
    }
}
