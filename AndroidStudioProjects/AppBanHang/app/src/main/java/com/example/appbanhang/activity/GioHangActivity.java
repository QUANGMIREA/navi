package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbanhang.R;
import com.example.appbanhang.Utils.Utils;
import com.example.appbanhang.adapter.GioHangAdapter;
import com.example.appbanhang.model.EventBus.TinhTongEvent;
import com.example.appbanhang.model.GioHang;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.internal.Util;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter;
    long tongtiensp;
    List<GioHang> gioHangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
         tongtiensp=0;
        for(int i =0;i<Utils.manggiohang.size();i++){
            tongtiensp = tongtiensp+(Utils.manggiohang.get(i).getGiasp()*Utils.manggiohang.get(i).getSoluong());

        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        tongtien.setText(decimalFormat.format(tongtiensp));

    }

    private void initView() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        toolbar = findViewById(R.id.toobar);
        tongtien = findViewById(R.id.txttongtien);
        btnmuahang = findViewById(R.id.btnmuahang);
        recyclerView = findViewById(R.id.recycleviewgiohang);

    }
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.manggiohang.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }
        else{
            adapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(adapter);
        }
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tongtiensp);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void eventTinhTien(TinhTongEvent event){
        if(event != null){
            tinhTongTien();
        }
    }
}