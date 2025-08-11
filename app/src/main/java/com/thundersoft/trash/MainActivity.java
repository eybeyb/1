package com.thundersoft.trash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.thundersoft.trash.Model.TrashResponse;
import com.thundersoft.trash.adapter.SearchGoodsAdapter;
import com.thundersoft.trash.databinding.ActivityMainBinding;

import java.io.IOException;

import CameraTrash.CameraActivity;
import Common.defenddoudong;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements defenddoudong {
    private long lastClickTime = 0;
    //
    @Override
    // 防抖动
    public boolean isFastclick(int choice) {
        boolean flag =true ;
        switch (choice) {
            case 0:
                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >=5000) {
                    flag = false;
                }
                lastClickTime = curClickTime;
                break;
            case 1:
                break;
            default:
                break;
        }
        return flag;
    }
    private Handler handler = new Handler();
    private Runnable searchRunnable;
    private EditText et;
private Button bt,camerabt;
private TextView tv;
    private RecyclerView recyclerView;
    private SearchGoodsAdapter adapter;
private ActivityMainBinding binding;
String goods=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        camerabt=binding.button2 ;
        camerabt.setOnClickListener(v -> {
            jumptoCameraActivity();

            // 跳转到相机界面
        });
        recyclerView = binding.recyclerView2;
        adapter = new SearchGoodsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        et = binding.editTextText;
        bt = binding.button;
        ListenChangeText();
    }

    // 将TextWatcher定义为独立的内部类

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getData(String goods){
        //OKHttp连接网络访问api
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://apis.tianapi.com/lajifenlei/index?key=4302ff6ee4741ea1e46bd2e710a7daa6&word="+goods)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("MainActivity", responseData);//测试打印数据
                    try {
                        TrashResponse trashResponse = new Gson().fromJson(responseData, TrashResponse.class);
                        if (trashResponse.getCode() == 200 && trashResponse.getResult() != null) {
                            runOnUiThread(() -> {
                                adapter.setDataList(trashResponse.getResult().getList());
                                recyclerView.setAdapter(adapter);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
  public void ListenChangeText(){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                s=et.getText();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                s=et.getText();
                if(s.toString().trim().equals("")){
                    recyclerView.setAdapter(null);
                }
               getData(s.toString().trim());
            }
        });

  }
  public void jumptoCameraActivity(){
      Intent intent = new Intent(MainActivity.this, CameraActivity.class);
      startActivity(intent);
  }
}
