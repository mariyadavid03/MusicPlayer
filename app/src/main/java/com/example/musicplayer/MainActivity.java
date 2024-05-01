package com.example.musicplayer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    SongMainAdapter adapter;
    List<Song> songList = new ArrayList<>();
    ActivityResultLauncher<String> storagePermissionLancher;
    final String permission = Manifest.permission.READ_MEDIA_AUDIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvAllSongsView);

        Log.d(TAG, "Requesting storage permission");
        storagePermissionLancher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted->{
           if(granted){
               Log.d(TAG, "Storage permission granted");
               fetchSongs();
           }
           else{
               Log.d(TAG, "Storage permission denied");
               userResponse();
           }
        });

        storagePermissionLancher.launch(permission);
    }

    private void userResponse() {
        if (ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission granted in userResponse()");
            fetchSongs();
        } else {
            if(shouldShowRequestPermissionRationale(permission)){
                new AlertDialog.Builder(this).setTitle("Requesting Media Files Permission")
                        .setMessage("Allow to Fetch Songs and Images From Device")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Launching permission request from dialog");
                                storagePermissionLancher.launch(permission);
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Permission denied from dialog");
                                Toast.makeText(getApplicationContext(), "Access Denied", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else{
                Log.d(TAG, "Permission cancelled from userResponse()");
                Toast.makeText(this, "Permission Cancelled", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void fetchSongs() {
        Log.d(TAG, "Fetching songs...");
        List<Song> songs =  new ArrayList<>();
        Uri mediaStoreUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            mediaStoreUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else{
            mediaStoreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
        };

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        try (Cursor cursor = getContentResolver().query(mediaStoreUri,projection,null,null,sortOrder)){
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int dNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int titleColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int durationColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int albumIDColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

            while(cursor.moveToNext()){
                long id = cursor.getLong(idColumn);
                long albumId = cursor.getLong(albumIDColumn);
                String dName = cursor.getString(dNameColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                int duration = cursor.getInt(durationColumn);

                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
                Uri artwork = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);

                dName = dName.substring(0,dName.lastIndexOf("."));

                Song song = new Song(title,artist,album,uri,artwork,duration);
                songs.add(song);

            }

            showSongs(songs);

        }
    }

    private void showSongs(List<Song> songs) {
        Log.d(TAG, "Displaying songs...");
        if(songs.size()==0){
            Toast.makeText(this, "No Songs Found", Toast.LENGTH_SHORT).show();
            return;
        }

        songList.clear();
        songList.addAll(songs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SongMainAdapter(this, songs);
        recyclerView.setAdapter(adapter);
    }
}