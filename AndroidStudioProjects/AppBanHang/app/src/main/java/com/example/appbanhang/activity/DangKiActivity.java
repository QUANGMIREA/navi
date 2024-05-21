package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.Utils.Utils;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.internal.Util;

public class DangKiActivity extends AppCompatActivity {
    EditText email,pass,repass,mobile,username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControll();
    }

    private void initControll() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_emai = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_user = username.getText().toString().trim();

        if(TextUtils.isEmpty(str_emai)){
            Toast.makeText(getApplicationContext(),"You have not entered your Email yet",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"You have not entered your Password yet",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"You have not entered your Repassword yet",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_mobile)){
            Toast.makeText(getApplicationContext(),"You have not entered your Mobile number yet",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(str_user)){
            Toast.makeText(getApplicationContext(),"You have not entered your UserName number yet",Toast.LENGTH_SHORT).show();
        }else  {
            if(str_pass.equals(str_repass)){
                //postdata
                compositeDisposable.add(apiBanHang.dangKi(str_emai,str_pass,str_user,str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                              userModel -> {
                                    if(userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_emai);
                                        Utils.user_current.setPass(str_pass);
                                        Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),userModel.getMessages(),Toast.LENGTH_SHORT).show();

                                    }
                              }  ,
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();

            }
        }



    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email= findViewById(R.id.email);
        pass= findViewById(R.id.pass);
        repass= findViewById(R.id.repass);
        username= findViewById(R.id.username);
        mobile= findViewById(R.id.mobile);
        button= findViewById(R.id.btndangki);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}