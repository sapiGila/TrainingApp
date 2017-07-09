package com.training.app.view.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.training.app.BuildConfig;
import com.training.app.R;
import com.training.app.helper.BitmapResizer;
import com.training.app.object.PhoneBook;

import org.parceler.Parcels;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by Dell on 7/8/2017.
 */

public class PersonActivity extends AppCompatActivity {

    @BindView(R.id.capture_image)
    ImageView captureImage;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_phone_number)
    EditText editPhoneNumber;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_address)
    EditText editAddress;
    private Uri selectedPhotoPath;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private PhoneBook person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        person = Parcels.unwrap(getIntent().getParcelableExtra("person"));
        if (person != null) {
            Glide.with(PersonActivity.this)
                    .load(person.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(captureImage);
            editName.setText(person.getName());
            editPhoneNumber.setText(person.getPhone());
            editEmail.setText(person.getEmail());
            editAddress.setText(person.getAddress());
        }
    }

    private void takePictureWithCamera() {
        // set intent image capture
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // set file path
        File imagePath = new File(getFilesDir(), "images");
        File newFile = new File(imagePath, "default_image.jpg");
        if (newFile.exists()) {
            newFile.delete();
        } else {
            newFile.getParentFile().mkdirs();
        }
        selectedPhotoPath = getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

        // set extra intent
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPhotoPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            ClipData clip = ClipData.newUri(getContentResolver(), "A photo", selectedPhotoPath);
            captureIntent.setClipData(clip);
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            setImageViewWithImage();
        }
    }

    private void setImageViewWithImage() {
        captureImage.post(new Runnable() {
            @Override
            public void run() {
                Bitmap pictureBitmap = BitmapResizer.shrinkBitmap(
                        PersonActivity.this,
                        selectedPhotoPath,
                        captureImage.getWidth(),
                        captureImage.getHeight()
                );
                captureImage.setImageBitmap(pictureBitmap);
            }
        });
    }

    @OnClick({R.id.capture_image, R.id.save_button, R.id.go_to_address, R.id.go_to_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.capture_image:
                takePictureWithCamera();
                break;
            case R.id.go_to_address:
                showAddress(editAddress.getText().toString());
                break;
            case R.id.go_to_email:
                showEmail(editEmail.getText().toString());
                break;
            case R.id.save_button:
                break;
        }
    }

    private void showAddress(String address) {
        address = address.replace(" ", "+");
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void showEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
