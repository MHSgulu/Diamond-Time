package com.tool.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tool.calendar.SQLite.FeedReaderContract;
import com.tool.calendar.SQLite.FeedReaderDbHelper;
import com.tool.calendar.mInterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ScheduleRecyclerViewAdapter mAdapter;
    private static List<Schedule> mDataList = new ArrayList<>();

    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        dbHelper = new FeedReaderDbHelper(mContext);

        mRecyclerView = findViewById(R.id.recycle_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //startActivity(new Intent(MainActivity.this,ToDoActivity.class));
                Intent intent = new Intent(MainActivity.this,ToDoActivity.class);
                //setResult(101);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        //下面的代码写法是页面每次调用onResume()都要从数据库中取出全部数据填充到列表，需要把之前的列表数据清空
        mDataList.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //读取本地SQLite数据库取出数据
                RetrieveDataFromSQLite();
            }
        }).start();

        mAdapter = new ScheduleRecyclerViewAdapter(mDataList);
        //只有创建视图层次结构的原始线程才能接触其视图,UI操作只能在UI主线程中进行！！！
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("您要删除这个日程安排么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(mContext, "当前位置： "+position, Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //定义查询的“ where”部分
                                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
                                //以占位符顺序指定参数。
                                String[] selectionArgs = { mDataList.get(position).getTitle() };
                                //以写入模式获取数据存储库
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                //发出SQL语句
                                //返回受影响的行数
                                int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
                                Log.d(TAG,"deletedRows: "+ deletedRows);

                            }
                        }).start();

                        onResume();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                //显示出对话框
                builder.create().show();
            }
        });

    }


    private void RetrieveDataFromSQLite() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //定义一个投影，该投影指定数据库中的哪些列
        //您将在此查询之后实际使用。
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_START_DATE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_END_DATE
        };

        // Filter results WHERE "title" = '测试'  //筛选结果WHERE“ title” ='测试'
       /* String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "测试" };*/

        //您希望结果如何在结果游标中排序
        String sortOrder = FeedReaderContract.FeedEntry._ID + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // 要查询的表
                projection,             //   要返回的列的数组（传递null以获得全部）
                /*selection*/null,              // WHERE子句的列      声明要返回哪些行的筛选器，格式为*SQL WHERE子句（不包括WHERE本身）   传递空值将返回给定表的所有行。
                /*selectionArgs*/null,          // WHERE子句的值
                null,                   //  不对行进行分组
                null,                   //  不按行组过滤
                /*sortOrder*/null               // 排序顺序
        );



        List itemIds = new ArrayList<>();
        List str1 = new ArrayList();
        List str2 = new ArrayList();
        List str3 = new ArrayList();

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_END_DATE));

            itemIds.add(itemId);
            str1.add(title);
            str2.add(startDate);
            str3.add(endDate);

            Schedule schedule = new Schedule(title,startDate,endDate);
            mDataList.add(schedule);

        }
        Log.d(TAG,"itemIds： "+itemIds);
        Log.d(TAG,"str1： "+str1);
        Log.d(TAG,"str2： "+str2);
        Log.d(TAG,"str3： "+str3);
        Log.d(TAG,"mDataList： "+mDataList);
        cursor.close();

    }


    @Override
    protected void onDestroy() {
        //由于在数据库关闭时，调用 getWritableDatabase() 和 getReadableDatabase() 的成本比较高，
        // 因此只要您有可能需要访问数据库，就应保持数据库连接处于打开状态。
        // 通常情况下，最好在发出调用的 Activity 的 onDestroy() 中关闭数据库。
        dbHelper.close();
        super.onDestroy();
    }






}
