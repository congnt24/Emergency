package com.congnt.emergencyassistance.view.fragment;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.utility.FileUtil;
import com.congnt.androidbasecomponent.utility.SoundUtil;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.AudioGalleryAdapter;
import com.congnt.emergencyassistance.entity.ItemAudio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 12/12/2016.
 */

public class AudioGalleryFragment extends AwesomeFragment {
    RecyclerView recycler;
    List<ItemAudio> listUrl = new ArrayList<>();
    AudioGalleryAdapter adapter;
    private int prevPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_audio_gallery;
    }

    @Override
    protected void initAll(View rootView) {
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<File> list = FileUtil.getListFile(new File(Environment.getExternalStorageDirectory() + "/" + AppConfig.FOLDER_MEDIA), ".3gp");
        if (list != null) {
            for (File file : list) {
                listUrl.add(new ItemAudio(file.getAbsolutePath(), file.getName()));
            }
        }
        adapter = new AudioGalleryAdapter(getActivity(), listUrl, new AwesomeRecyclerAdapter.OnClickListener<ItemAudio>() {
            @Override
            public void onClick(ItemAudio item, final int position) {
                MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        listUrl.get(position).isPlay = false;
                        adapter.notifyItemChanged(position);
                    }
                };

                if (item.isPlay) {
                    SoundUtil.getInstance().stop();
                    listUrl.get(position).isPlay = false;
                    adapter.notifyItemChanged(position);
                } else {
                    SoundUtil.getInstance().stop();
                    SoundUtil.getInstance().playSound(getActivity(), item.url, listener);
                    listUrl.get(position).isPlay = true;
                    adapter.notifyItemChanged(position);
                    if (prevPosition >= 0 && prevPosition != position) {
                        listUrl.get(prevPosition).isPlay = false;
                        adapter.notifyItemChanged(prevPosition);
                    }
                    prevPosition = position;
                }

                /*if (prevPosition < 0) {
                    SoundUtil.getInstance().stop();
                    SoundUtil.getInstance().playSound(getActivity(), item.url, listener);
                    listUrl.get(position).isPlay = true;
                    adapter.notifyItemChanged(position);
                    prevPosition = position;
//                    adapter.
                } else {
                    SoundUtil.getInstance().stop();
                    if (prevPosition != position) {
                        SoundUtil.getInstance().playSound(getActivity(), item.url, listener);
                        listUrl.get(position).isPlay = true;
                        adapter.notifyItemChanged(position);
                        listUrl.get(prevPosition).isPlay = false;
                        adapter.notifyItemChanged(prevPosition);
                        prevPosition = position;
                    } else {
                        listUrl.get(prevPosition).isPlay = false;
                        adapter.notifyItemChanged(prevPosition);
                        prevPosition = -1;
                    }
                }*/
            }
        });
        recycler.setAdapter(adapter);

    }
}
