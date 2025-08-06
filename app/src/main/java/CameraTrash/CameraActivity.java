package CameraTrash;
import org.json.JSONObject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.thundersoft.trash.Model.TrashResponse;
import com.thundersoft.trash.R;
import com.thundersoft.trash.adapter.SearchGoodsAdapter;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CameraActivity extends AppCompatActivity {
    private Button bt;
    private ImageView imageView;
    private File currentPhotoFile; // 保存当前照片文件引用
    private ExecutorService executorService;
    RecyclerView recyclerView3;
    private SearchGoodsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        bt = findViewById(R.id.button3);
        recyclerView3=findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchGoodsAdapter();

        executorService = Executors.newSingleThreadExecutor();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    /**
     * 启动拍照意图，调用系统相机进行拍照
     * 该方法创建拍照Intent，生成照片文件，并启动系统相机应用
     */
    private void dispatchTakePictureIntent() {
        // 创建拍照意图
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 检查是否有应用可以处理拍照意图
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // 创建用于存储照片的文件
                photoFile = createImageFile();
                currentPhotoFile = photoFile; // 保存文件引用
            } catch (IOException ex) {
                // 处理异常
                Toast.makeText(this, "创建照片文件失败: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            // 如果照片文件创建成功，则设置拍照意图的输出路径并启动拍照
            if (photoFile != null) {
                // 获取照片文件的URI
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.thundersoft.trash.fileprovider",
                        photoFile);
                // 将照片输出路径添加到意图中
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // 启动拍照活动，请求码为1
                startActivityForResult(takePictureIntent, 1);
            } else {
                Toast.makeText(this, "照片文件创建失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 检查请求码是否为拍照请求
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 拍照成功，显示照片
            if (currentPhotoFile != null && currentPhotoFile.exists()) {
                // 重新加载图片以确保显示最新内容
                imageView.setImageURI(null);
                imageView.setImageURI(Uri.fromFile(currentPhotoFile));
                imageView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "图片已保存", Toast.LENGTH_SHORT).show();

                // 在后台线程中执行图像识别
                performImageRecognition();
            } else {
                Toast.makeText(this, "照片文件不存在", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            // 用户取消拍照
            Toast.makeText(this, "拍照已取消", Toast.LENGTH_SHORT).show();
        } else {
            // 拍照失败
            Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void performImageRecognition() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 使用实际的照片文件路径调用百度AI识别
                    String result = connectnetwork.cn(currentPhotoFile.getAbsolutePath());
                    Log.d("百度AI结果", "百度AI结果: " + result);

                    // 解析百度AI识别结果
                    if (result != null) {
                        PhotoResponse photoResponse = new Gson().fromJson(result, PhotoResponse.class);
//                        if (photoResponse.getLogid() != null &&
//                                photoResponse.getResult() != null &&
//                                !photoResponse.getResult().isEmpty()) {
if(true==true){
                            // 获取识别到的第一个关键词
                            String keyword = photoResponse.getResult().get(4).getKeyword();
                            Log.d("关键词", "识别关键词: " + keyword);


                            // 在主线程中显示识别结果
                            runOnUiThread(() -> {

                                Toast.makeText(CameraActivity.this, "识别到: " + keyword, Toast.LENGTH_SHORT).show();
                                queryTrashClassification(keyword);
                            });

                            // 调用天行API查询垃圾分类信息
                            queryTrashClassification(keyword);
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(CameraActivity.this, "未能识别图片内容", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(CameraActivity.this, "图像识别失败", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    Log.e("CameraActivity", "图像识别失败", e);
                    runOnUiThread(() -> {
                        Toast.makeText(CameraActivity.this, "图像识别失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void queryTrashClassification(String keyword) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://apis.tianapi.com/lajifenlei/index?key=4302ff6ee4741ea1e46bd2e710a7daa6&word=" + keyword)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("CameraActivity", "天行API请求失败", e);
                runOnUiThread(() -> {
                    Toast.makeText(CameraActivity.this, "查询垃圾分类失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.d("CameraActivity", "天行API返回数据: " + responseData);

                        TrashResponse trashResponse = new Gson().fromJson(responseData, TrashResponse.class);
                        if (trashResponse.getCode() == 200 && trashResponse.getResult() != null) {
                            // 显示垃圾分类结果
                            runOnUiThread(() -> {
//                                String resultMsg = "垃圾名称: " + trashResponse.getResult().getList().get(0).getName() + "\n" +
//                                        "分类: " + trashResponse.getResult().getList().get(0).getCategory() + "\n" +
//                                        "解释: " + trashResponse.getResult().getList().get(0).getExplain();
//                                Toast.makeText(CameraActivity.this, resultMsg, Toast.LENGTH_LONG).show();
                                adapter.setDataList(trashResponse.getResult().getList());
                                recyclerView3.setAdapter(adapter);
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(CameraActivity.this, "未找到相关垃圾分类信息", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (Exception e) {
                        Log.e("CameraActivity", "解析天行API响应失败", e);
                        runOnUiThread(() -> {
                            Toast.makeText(CameraActivity.this, "解析响应失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.e("CameraActivity", "天行API请求失败，状态码: " + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(CameraActivity.this, "请求失败，状态码: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // 创建一个临时文件
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "JPEG_",  /* 前缀 */
                ".jpg",   /* 后缀 */
                storageDir /* 文件夹 */
        );
        return image;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
