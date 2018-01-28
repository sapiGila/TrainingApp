package com.training.app.view.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.training.app.BuildConfig;
import com.training.app.R;
import com.training.app.contract.PhoneBookContract;
import com.training.app.object.PhoneBook;
import com.training.app.presenter.PhoneBookPresenter;
import com.training.app.util.BitmapResizer;
import com.training.app.util.RealmDB;
import com.training.app.util.Toaster;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by Dell on 7/8/2017.
 */

public class PersonActivity extends AppCompatActivity implements PhoneBookContract.PersonView {

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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.delete_button)
    Button deleteButton;
    @BindView(R.id.save_button)
    Button saveButton;
    private Uri selectedPhotoPath;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private PhoneBook person;
    private static final int REQUEST_PERMISSIONS = 20;
    private PhoneBookContract.Presenter presenter;
    private boolean isEdit = false;
    private String urlImage = "";
    private File newFile;

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
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(captureImage);
            editName.setText(person.getName());
            editPhoneNumber.setText(person.getPhone());
            editEmail.setText(person.getEmail());
            editAddress.setText(person.getAddress());
            deleteButton.setVisibility(View.VISIBLE);
            isEdit = true;
        } else {
            deleteButton.setVisibility(View.GONE);
            isEdit = false;
        }
//        presenter = new PhoneBookPresenter(this, Schedulers.io(), AndroidSchedulers.mainThread());
        presenter = new PhoneBookPresenter(this, Schedulers.io(), AndroidSchedulers.mainThread(),
                new RealmDB());
    }

    private void takePictureWithCamera() {
        // set intent image capture
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // set file path
        File imagePath = new File(getFilesDir(), "images");
        newFile = new File(imagePath, UUID.randomUUID().toString() + ".jpg");
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

    @OnClick({R.id.capture_image, R.id.save_button, R.id.go_to_address, R.id.go_to_email,
            R.id.go_to_phone, R.id.delete_button})
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
            case R.id.go_to_phone:
                checkCallPhonePermission();
                break;
            case R.id.save_button:
                if (person == null) {
                    person = new PhoneBook();
                }
                person.setName(editName.getText().toString());
                person.setAddress(editAddress.getText().toString());
                person.setPhone(editPhoneNumber.getText().toString());
                person.setEmail(editEmail.getText().toString());
                boolean isUpload = true;
                if (newFile == null) {
                    isUpload = false;
                }
                if (isUpload) {
                    File fileCompressed = getFileCompressed();
                    if (fileCompressed != null) {

                        //remote
                        presenter.uploadPhotoPerson(fileCompressed);

                        //local
//                        person.setPicture(fileCompressed.getAbsolutePath());
//                        presenter.setPerson(person, isEdit);
                    }
                } else {

                    //remote
                    if (StringUtils.isNotBlank(person.getPicture())) {
                        person.setPicture(FilenameUtils.getName(person.getPicture()));
                    }
                    if (isEdit) {
                        presenter.editPerson(person);
                    } else {
                        presenter.addPerson(person);
                    }

                    //local
//                    presenter.setPerson(person, isEdit);
                }
                break;
            case R.id.delete_button:
                presenter.deletePerson(String.valueOf(person.getId()));
                break;
        }
    }

    @Nullable
    private File getFileCompressed() {
        File fileCompressed = null;
        try {
            fileCompressed = new Compressor(this)
                    .setQuality(75)
                    .compressToFile(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCompressed;
    }

    private void showAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            address = address.replace(" ", "+");
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void showEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callPhoneNumber(editPhoneNumber.getText().toString());
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Enable Permissions from settings",
                            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }
        }
    }

    private void checkCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(PersonActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (PersonActivity.this, Manifest.permission.CALL_PHONE)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(PersonActivity.this,
                                        new String[]{Manifest.permission
                                                .RECEIVE_SMS},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(PersonActivity.this,
                        new String[]{Manifest.permission
                                .RECEIVE_SMS},
                        REQUEST_PERMISSIONS);
            }
        } else {
            callPhoneNumber(editPhoneNumber.getText().toString());
        }
    }

    private void callPhoneNumber(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            Intent dialIntent = new Intent();
            dialIntent.setAction(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        }
    }

    @Override
    public void doBeforeProcessing() {
        progressBar.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
    }

    @Override
    public void doAfterUploadPhotoPerson(String imageUrl) {
        urlImage = imageUrl;
        progressBar.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        person.setPicture(imageUrl);
        if (isEdit) {
            deleteButton.setVisibility(View.VISIBLE);
            presenter.editPerson(person);
        } else {
            deleteButton.setVisibility(View.GONE);
            presenter.addPerson(person);
        }
    }

    @Override
    public void doAfterProcessing() {
        progressBar.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        if (isEdit) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
        finish();
    }

    @Override
    public void doOnError(String message) {
        progressBar.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        if (isEdit) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
        Toaster.show(this, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
