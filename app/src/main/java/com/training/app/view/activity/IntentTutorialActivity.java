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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.app.BuildConfig;
import com.training.app.R;
import com.training.app.helper.BitmapResizer;
import com.training.app.helper.Toaster;
import com.training.app.model.Contact;

import org.parceler.Parcels;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;
import static com.training.app.helper.VariableUtils.BITMAP_HEIGHT;
import static com.training.app.helper.VariableUtils.BITMAP_WIDTH;
import static com.training.app.helper.VariableUtils.CONTACT_PERSON;
import static com.training.app.helper.VariableUtils.IMAGE_URI_KEY;

/**
 * Created by Dell on 7/4/2017.
 */

public class IntentTutorialActivity extends AppCompatActivity {

    private Uri selectedPhotoPath;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private boolean pictureTaken;
    private ImageView captureImage;
    private Button buttonNext;
    private EditText editName;
    private EditText editPhoneNumber;
    private static final String MIME_TYPE_IMAGE = "image/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_tutorial);
        editName = (EditText) findViewById(R.id.edit_name);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        captureImage = (ImageView) findViewById(R.id.capture_image);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureWithCamera();
            }
        });
        buttonNext = (Button) findViewById(R.id.next_button);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextScreen();
            }
        });
        checkReceivedIntent();
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
        captureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selectedPhotoPath);
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
                        IntentTutorialActivity.this,
                        selectedPhotoPath,
                        captureImage.getWidth(),
                        captureImage.getHeight()
                );
                captureImage.setImageBitmap(pictureBitmap);
            }
        });
        pictureTaken = true;
    }

    private void moveToNextScreen() {
        if (pictureTaken) {
            Contact contact = new Contact(editName.getText().toString(), editPhoneNumber.getText().toString());
            Intent nextScreenIntent = new Intent(this, ShareTutorialActivity.class);
            nextScreenIntent.putExtra(IMAGE_URI_KEY, selectedPhotoPath);
            nextScreenIntent.putExtra(BITMAP_WIDTH, captureImage.getWidth());
            nextScreenIntent.putExtra(BITMAP_HEIGHT, captureImage.getHeight());
            nextScreenIntent.putExtra(CONTACT_PERSON, Parcels.wrap(contact));
            startActivity(nextScreenIntent);
        } else {
            Toaster.show(this, R.string.capture_picture);
        }
    }

    private void checkReceivedIntent() {
        Intent imageRecievedIntent = getIntent();
        String intentAction = imageRecievedIntent.getAction();
        String intentType = imageRecievedIntent.getType();

        if (Intent.ACTION_SEND.equals(intentAction) && intentType != null) {
            if (intentType.startsWith(MIME_TYPE_IMAGE)) {
                selectedPhotoPath = imageRecievedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                setImageViewWithImage();
            }
        }
    }
}
