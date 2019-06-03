package com.example.blelibrary.blelibrary.EventBus;

public class WriteData {
    byte[]data=new byte[10];
    public WriteData(byte[]data){
      this.data=data;
    }
    public byte[] data(){
        return data;
    }
}
