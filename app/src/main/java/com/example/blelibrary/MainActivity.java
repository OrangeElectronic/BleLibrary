package com.example.blelibrary;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blelibrary.blelibrary.EventBus.ReadData;
import com.example.blelibrary.blelibrary.EventBus.RebackData;
import com.example.blelibrary.blelibrary.EventBus.SerchDevice;
import com.example.blelibrary.blelibrary.EventBus.WriteData;
import com.example.blelibrary.blelibrary.Server.BleServiceControl;
import com.example.blelibrary.blelibrary.Server.ScanDevice;
import com.example.blelibrary.blelibrary.tool.FormatConvert;

import org.greenrobot.eventbus.Subscribe;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ScanDevice scan=new ScanDevice();
    Timer timer;
    BleServiceControl bleServiceControl=new BleServiceControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan.setmBluetoothAdapter(this);
    }
        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0]!=0){
                Toast.makeText(this, "必須打開定位才能啟用此服務", Toast.LENGTH_LONG).show();
            }else{ scan.RequestPermission();}
        }
    }
//----------------------------------------When find Device it will callback on here----------------------------------------
@Subscribe
    public void Event(SerchDevice a){
        try{
            if(a.getDevic().getName().contains("HT430")){
                Toast.makeText(this, "找到新裝置"+a.getDevic().getName(), Toast.LENGTH_LONG).show();
                scan.scanLeDevice(false);
                bleServiceControl.connect(a.getDevic().getAddress(),this);
            }
        }catch (Exception e){Log.w("error",e.getMessage());}
}
//----------------------------------------Use bleServiceControl.WriteCmd(HexString,uuid).to write Data ----------------------------------------
  @Subscribe
  public void Event(RebackData a){
      try{
          Log.w("WriteReback","Data:"+ FormatConvert.bytesToHex(a.getReback()));
      }catch (Exception e){Log.w("error",e.getMessage());}
  }
  //----------------------------------------Use bleServiceControl.ReadCmd(uuid).to read Data----------------------------------------
  @Subscribe
  public void Event(ReadData a){
      try{
          Log.w("ReadReback","Data:"+ FormatConvert.bytesToHex(a.getReback()));
      }catch (Exception e){Log.w("error",e.getMessage());}
  }
  //----------------------------------------Use bleServiceControl.WriteCmd will callbcak this method to confirm is writed susscessfully----------------------------------------
    @Subscribe
    public void Event(WriteData a){
        try{
            Log.w("Write","Data:"+ FormatConvert.bytesToHex(a.data()));
        }catch (Exception e){Log.w("error",e.getMessage());}
    }


}
