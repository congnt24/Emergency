package com.etiennelawlor.imagegallery.library;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etiennelawlor.imagegallery.library.activities.FullScreenImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.etiennelawlor.imagegallery.library.view.GridSpacesItemDecoration;

import java.util.ArrayList;

/**
 * Created by etiennelawlor on 6/10/16.
 */

public class ImageGalleryFragment extends Fragment implements ImageGalleryAdapter.OnImageClickListener, ImageGalleryAdapter.ImageThumbnailLoader {

    // region Constants
    public static final String KEY_IMAGES = "KEY_IMAGES";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String KEY_TITLE = "KEY_TITLE";
    // endregion
    private static ImageGalleryAdapter.ImageThumbnailLoader imageThumbnailLoader;
    // region Views
    private Toolbar toolbar;
    // endregion
    private RecyclerView recyclerView;
    // region Member Variables
    private ArrayList<String> images;
    private String title;
    private GridSpacesItemDecoration gridSpacesItemDecoration;
    // endregion

    // region Constructors
    public ImageGalleryFragment() {
    }
    // endregion

    // region Factory Methods
    public static ImageGalleryFragment newInstance(Bundle extras) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static ImageGalleryFragment newInstance() {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    public static void setImageThumbnailLoader(ImageGalleryAdapter.ImageThumbnailLoader loader) {
        imageThumbnailLoader = loader;
    }

    // region Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            images = getArguments().getStringArrayList(KEY_IMAGES);
            title = getArguments().getString(KEY_TITLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        return rootView;
    }
    // endregion

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        setUpRecyclerView();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recyclerView.removeItemDecoration(gridSpacesItemDecoration);
        setUpRecyclerView();
    }
    // endregion

    // region ImageGalleryAdapter.OnImageClickListener Methods
    @Override
    public void onImageClick(int position) {
        Intent intent = new Intent(getContext(), FullScreenImageGalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, images);
        bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, position);
        intent.putExtras(bundle);

        startActivity(intent);
    }
    // endregion

    // region ImageGalleryAdapter.ImageThumbnailLoader Methods
    @Override
    public void loadImageThumbnail(ImageView iv, String imageUrl, int dimension) {
        imageThumbnailLoader.loadImageThumbnail(iv, imageUrl, dimension);
    }

    // region Helper Methods
    private void bindViews() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    private void setUpRecyclerView() {
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(getActivity())) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numOfColumns));
        gridSpacesItemDecoration = new GridSpacesItemDecoration(ImageGalleryUtils.dp2px(getActivity(), 2), numOfColumns);
        recyclerView.addItemDecoration(gridSpacesItemDecoration);
        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(images);
        imageGalleryAdapter.setOnImageClickListener(this);
        imageGalleryAdapter.setImageThumbnailLoader(this);

        recyclerView.setAdapter(imageGalleryAdapter);
    }
    // endregion
}
