package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Song> songList;
    Context context;

    public SongMainAdapter(Context context, List<Song> songList){
        this.context = context;
        this.songList = songList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_music_item, viewGroup,false);
        return new SongViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Song song = songList.get(position);
            SongViewHolder viewHolder = (SongViewHolder) holder;

            if(song != null){
                if(song.songTitle != null){
                    viewHolder.title.setText(song.songTitle);
                } else{
                    viewHolder.title.setText("Unknown Title");
                }
                if(song.songArtist != null){
                    viewHolder.artist.setText(song.songArtist);
                } else{
                    viewHolder.artist.setText("Unknown Artist");
                }
                if(song.songAlbum != null){
                    viewHolder.album.setText(song.songAlbum);
                } else{
                    viewHolder.artist.setText("Unknown Album");
                }

                viewHolder.duration.setText(String.valueOf(song.duration));

                Uri artwork = song.getSongArtwork();

                if(artwork!=null){
                    viewHolder.artwork.setImageURI(artwork);
                    if(viewHolder.artwork.getDrawable() == null){
                        viewHolder.artwork.setImageResource(R.drawable.default_artwork);
                    }
                }

                //On CLIck TODO
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static class SongViewHolder extends RecyclerView.ViewHolder{
        TextView title, artist, album, duration;
        ImageView artwork;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            artwork = itemView.findViewById(R.id.imgSongArtwork);
            title = itemView.findViewById(R.id.txtMainTitle);
            artist = itemView.findViewById(R.id.txtMainSinger);
            album = itemView.findViewById(R.id.txtMainAlbum);
            duration = itemView.findViewById(R.id.txtMainTime);
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchSongs(List<Song> filteredSongs){
        songList = filteredSongs;
        notifyDataSetChanged();
    }
}
