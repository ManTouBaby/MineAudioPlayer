package com.hrw.mineaudioplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrw.audiolibrary.AudioPlayView;
import com.hrw.smartview.adapter.SmartAdapter;
import com.hrw.smartview.adapter.SmartVH;
import com.hrw.smartview.listener.OnSmartItemClickListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnSmartItemClickListener<Music> {
    List<Music> musicList = new ArrayList<>();

    RecyclerView rvShowMusicList;
    AudioPlayView audioPlay;
    SmartAdapter smartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvShowMusicList = findViewById(R.id.show_music);
        audioPlay = findViewById(R.id.apv_play_music);

        rvShowMusicList.setLayoutManager(new LinearLayoutManager(this));
        rvShowMusicList.setHasFixedSize(true);
        rvShowMusicList.setAdapter(smartAdapter =new SmartAdapter<Music>(R.layout.item_audio) {
            @Override
            protected void bindView(SmartVH holder, Music musicBaseAudioBean, int position) {
                TextView tvAudioName =holder.getViewById(R.id.tv_audio_name);
                TextView tvAudioCreateTime = holder.getViewById(R.id.tv_create_times);
                tvAudioName.setText(musicBaseAudioBean.title);
            }

        });
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            initListView();
        } else {
            EasyPermissions.requestPermissions(this, "获取文件必须权限", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    //nitListView()实现对手机中MediaDataBase的扫描
    private void initListView() {
        musicList.clear();
        //获取ContentResolver的对象，并进行实例化
        ContentResolver resolver = getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER); //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，后面的是关于select筛选条件，这里填土null就可以了
        //游标归零
        if(cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));            //获取歌名
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));         //获取歌唱者
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));           //获取专辑名
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));            //获取专辑图片id
                int length = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //创建Music对象，并赋值
                Music music = new Music();
                music.length = length;
                music.title = title;
                music.artist = artist;
                music.album = album;
                music.path = path;
                music.albumBip = getAlbumArt(albumID);
                music.initAudioData();
                //将music放入musicList集合中
                musicList.add(music);
            }  while (cursor.moveToNext());
        }else {
            Toast.makeText(this, "本地没有音乐哦", Toast.LENGTH_SHORT).show();
        }
        cursor.close();                                                                         //关闭游标
        smartAdapter.setDate(musicList);
        smartAdapter.setOnSmartItemClickListener(this);
    }

    //获取专辑图片的方法
    private Bitmap getAlbumArt(int album_id) {                              //前面我们只是获取了专辑图片id，在这里实现通过id获取掉专辑图片
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur =getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang6);
        }
        return bm;
    }

    @Override
    public void onSmartItemClick(Music o, int position) {
        audioPlay.play(o);
    }
}
