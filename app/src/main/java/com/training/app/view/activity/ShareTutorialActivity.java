package com.training.app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.app.R;
import com.training.app.helper.BitmapResizer;
import com.training.app.helper.Toaster;
import com.training.app.object.Contact;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.training.app.helper.VariableUtils.BITMAP_HEIGHT;
import static com.training.app.helper.VariableUtils.BITMAP_WIDTH;
import static com.training.app.helper.VariableUtils.CONTACT_PERSON;
import static com.training.app.helper.VariableUtils.IMAGE_URI_KEY;

/**
 * Created by Dell on 7/4/2017.
 */

public class ShareTutorialActivity extends AppCompatActivity {

    private ImageView captureImage;
    private TextView txtName;
    private TextView txtPhoneNumber;
    private TextView txtHeight;
    private TextView txtWidth;
    private Button shareButton;
    private Button saveButton;
    private static final String FILE_SUFFIX_JPG = ".jpg";
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 42;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_tutorial);
        captureImage = (ImageView) findViewById(R.id.capture_image);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtPhoneNumber = (TextView) findViewById(R.id.txt_phone_number);
        txtHeight = (TextView) findViewById(R.id.txt_height);
        txtWidth = (TextView) findViewById(R.id.txt_width);
        shareButton = (Button) findViewById(R.id.share_button);
        saveButton = (Button) findViewById(R.id.save_button);
        final Uri imageUri = getIntent().getParcelableExtra(IMAGE_URI_KEY);
        int width = getIntent().getIntExtra(BITMAP_WIDTH, captureImage.getWidth());
        int height = getIntent().getIntExtra(BITMAP_HEIGHT, captureImage.getHeight());
        final Contact contact = Parcels.unwrap(getIntent().getParcelableExtra(CONTACT_PERSON));
        txtName.setText(contact.getName());
        txtPhoneNumber.setText(contact.getPhoneNumber());
        txtHeight.setText("Height : " + height);
        txtWidth.setText("Width : " + width);
        final Bitmap bitmap = uriToBitmap(imageUri);
        Bitmap selectedImageBitmap = BitmapResizer.shrinkBitmap(this, imageUri,
                width, height);
        captureImage.setImageBitmap(selectedImageBitmap);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(imageUri, "Share Image", contact.getName() + "\n" + contact.getPhoneNumber());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermissions(bitmap);
            }
        });
    }

    private void shareImage(Uri uriImage,
                            String subject,
                            String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uriImage);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, subject)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void askForPermissions(Bitmap bitmap) {
        @PermissionChecker.PermissionResult int permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        } else {
            saveImageToGallery(bitmap);
        }
    }

    private void saveImageToGallery(Bitmap memeBitmap) {
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                memeBitmap + FILE_SUFFIX_JPG);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            memeBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toaster.show(this, R.string.save_image_failed);
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(imageFile));
        sendBroadcast(mediaScanIntent);
        Toaster.show(this, R.string.save_image_succeeded);
    }
}
