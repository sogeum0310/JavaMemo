package com.sogeum0310.javamemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SettingActivity extends AppCompatActivity {
    TextView cancel, submit, backup, recover;
    private static final int MY_PERMISSION_STORAGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cancel = findViewById(R.id.cancel);
        submit= findViewById(R.id.summit);
        backup = findViewById(R.id.backup);
        recover = findViewById(R.id.recover);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        System.out.println("backup2222222222222222222222222222222222222222222");
                        File currentDB = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db");
                        File currentDB2 = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db-shm");
                        File currentDB3 = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db-wal");
                        File backupDB = new File(sd, "/MEMO/MEMO.db");
                        File backupDB2 = new File(sd, "/MEMO/MEMO.db-shm");
                        File backupDB3 = new File(sd, "/MEMO/MEMO.db-wal");

                        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MEMO" );
                        dir.mkdirs();

                        if(currentDB2.exists()){
                            FileChannel src2 = new FileInputStream(currentDB2).getChannel();
                            FileChannel dst2 = new FileOutputStream(backupDB2).getChannel();
                            dst2.transferFrom(src2, 0, src2.size());
                            src2.close();
                            dst2.close();
                        }
                        if(currentDB3.exists()){
                            FileChannel src3 = new FileInputStream(currentDB3).getChannel();
                            FileChannel dst3 = new FileOutputStream(backupDB3).getChannel();
                            dst3.transferFrom(src3, 0, src3.size());
                            src3.close();
                            dst3.close();
                        }

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());

                        src.close();
                        dst.close();
                        Toast.makeText(SettingActivity.this, "백업이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SettingActivity.this, "백업 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        File currentDB = new File(sd, "/MEMO/MEMO.db");
                        File restoreDB = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db");
                        File currentDB3 = new File(sd, "/MEMO/MEMO.db-wal");
                        File currentDB2 = new File(sd, "/MEMO/MEMO.db-shm");
                        File restoreDB2 = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db-shm");
                        File restoreDB3 = new File(data, "/data/com.sogeum0310.javamemo/databases/MEMO.db-wal");

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(restoreDB).getChannel();
                        dst.transferFrom(src, 0, src.size());


                        if (currentDB2.exists()) {
                            FileChannel src2 = new FileInputStream(currentDB2).getChannel();
                            FileChannel dst2 = new FileOutputStream(restoreDB2).getChannel();
                            dst2.transferFrom(src2, 0, src2.size());
                            currentDB2.delete();
                            src2.close();
                            dst2.close();
                        }
                        if (currentDB3.exists()) {
                            FileChannel src3 = new FileInputStream(currentDB3).getChannel();
                            FileChannel dst3 = new FileOutputStream(restoreDB3).getChannel();
                            dst3.transferFrom(src3, 0, src3.size());
                            currentDB3.delete();
                            src3.close();
                            dst3.close();
                        }

                        src.close();
                        dst.close();


                        Toast.makeText(SettingActivity.this, "복구가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        //앱재시작
//                        PackageManager packageManager = getPackageManager();
//                        Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
//                        ComponentName componentName = intent.getComponent();
//                        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
//                        startActivity(mainIntent);
//                        System.exit(0);
                    }
                } catch (Exception e) {
                    Toast.makeText(SettingActivity.this, "복구 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(){

        //api 30 R version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(Environment.isExternalStorageManager()){

            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }else
            //30 < version
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(SettingActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
//                        android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }
}