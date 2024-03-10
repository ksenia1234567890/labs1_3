package com.example.labs1_3.FragmentsMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.labs1_3.R;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class FragmentCamera extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        this.setEnterTransition(inflater.inflateTransition(R.transition.slide_left));
        this.setExitTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    ImageView imageCamera;
    MaterialButton btnOpenCamera;
    Uri uriImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        imageCamera = view.findViewById(R.id.imageCamera);
        btnOpenCamera = view.findViewById(R.id.btnOpenCamera);

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCamera.setImageURI(null);

                imageCamera.setTranslationX(-1000f);

                File fileImage = new File(getActivity().getFilesDir(), "picFromCamera");
                uriImage = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", fileImage);

                getImg.launch(uriImage);
            }
        });

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.putExtra(Intent.EXTRA_STREAM, uriImage);
                getContext().startActivity(Intent.createChooser(share, null));
            }
        });

        return view;
    }

    ActivityResultLauncher<Uri> getImg = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean success) {
            if(success){
                imageCamera.setImageURI(uriImage);

                imageCamera.animate().translationXBy(1000f).setDuration(500).setStartDelay(250).start();
            }
        }
    });
}