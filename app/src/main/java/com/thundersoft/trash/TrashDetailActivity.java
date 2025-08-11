package com.thundersoft.trash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.thundersoft.trash.Model.TrashResponse;

public class TrashDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TRASH_ITEM = "trash_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trash_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        Intent intent = getIntent();
        TrashResponse.TrashItem trashItem= (TrashResponse.TrashItem) intent.getSerializableExtra(EXTRA_TRASH_ITEM);
        Log.d("TrashDetailActivity", "trashItem"+trashItem);
        initView(trashItem);
    }
    // 初始化控件
    public void initView(TrashResponse.TrashItem trashItem)
    {
        TextView nameTextView = findViewById(R.id.textView1);//名称
        TextView typeTextView = findViewById(R.id.textView2);//类型
        TextView tipTextView = findViewById(R.id.textView4);//单价
        TextView descriptionTextView = findViewById(R.id.textView3);//描述
        nameTextView.setText("垃圾名称："+trashItem.getName());
        typeTextView.setText("垃圾类型："+trashItem.getCategory());
        tipTextView.setText("投放提示："+trashItem.getTip());
        descriptionTextView.setText("垃圾描述："+trashItem.getExplain());
    }




}