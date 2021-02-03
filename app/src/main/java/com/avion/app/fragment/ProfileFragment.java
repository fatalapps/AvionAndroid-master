package com.avion.app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.avion.app.AlertWithTwoBtns;
import com.avion.app.ExifTransformation;
import com.avion.app.MainActivity;
import com.avion.app.MainApi;
import com.avion.app.R;
import com.avion.app.action.EmailConf;
import com.avion.app.unit.NekConfigs;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class ProfileFragment extends Fragment {

    private CircleImageView profile_image;
    private ImageButton profile_edit_avatar_imagebtn;
    private EditText profile_name_edittext;
    private EditText profile_phone_edittext;
    private EditText profile_email_edittext;
    private Button profile_done_btn;
    private ProgressBar profile_progressBar;
    private TextView profile_balance;
    private static Context cont;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseUser currentUser;
    private Uri userProfileImageUrl;
    private EditText profile_email;
    private AlertWithTwoBtns alertWithTwoBtns;
    private MainApi mapi;
    private String email = "undefined";
    private static final int TAKE_PICTURE = 1;
    public static int is_photo = 0;
    protected FirebaseFirestore db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private void showProgress() {
        profile_progressBar.setVisibility(View.VISIBLE);
        profile_done_btn.setVisibility(View.INVISIBLE);
    }

    private void hideProgress() {
        profile_progressBar.setVisibility(View.GONE);
        profile_done_btn.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root_view, @Nullable Bundle savedInstanceState) {
        profile_image = root_view.findViewById(R.id.profile_image);
        profile_edit_avatar_imagebtn = root_view.findViewById(R.id.profile_edit_avatar_imagebtn);
        profile_name_edittext = root_view.findViewById(R.id.profile_name_edittext);
        profile_phone_edittext = root_view.findViewById(R.id.profile_phone_edittext);
        profile_email_edittext = root_view.findViewById(R.id.profile_email_edittext);
        profile_done_btn = root_view.findViewById(R.id.profile_done_btn);
        profile_progressBar = root_view.findViewById(R.id.profile_progressBar);
        profile_email = root_view.findViewById(R.id.profile_mail_edittext);
        profile_balance = root_view.findViewById(R.id.profile_balance);

        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        MaskFormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(profile_phone_edittext);
        profile_balance.setText(MainActivity.bbalance);
        db = FirebaseFirestore.getInstance();

        // startProgress();
        db.collection("users_mails")
                //.whereEqualTo("user_phone", NekConfigs.getUserPgone())
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        EmailConf email_data = documentSnapshot.toObject(EmailConf.class);
                        email_data.setPhone(documentSnapshot.getId());
                        if (email_data.getPhone().equals(NekConfigs.getUserPgone())) {
                            email = email_data.getEmail();
                            MainActivity.logstr(email + " matched with " + NekConfigs.getUserPgone());
                            NekConfigs.mail = email;
                            updateUI();
                        } else {
                            MainActivity.logstr(email_data.getPhone() + " != " + NekConfigs.getUserPgone());
                        }
                    }
                    //stopPrograss();
                });
        MainActivity.logstr("GOT: " + email);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        updateUI();

        profile_done_btn.setOnClickListener(onDoneClick);
        profile_edit_avatar_imagebtn.setOnClickListener(onEditAvatarClick);
        profile_phone_edittext.setEnabled(false);

        hideProgress();


    }

    private void updateUI() {
        if (currentUser == null) return;
        String displayName = currentUser.getDisplayName();
        if (displayName != null)
            profile_name_edittext.setText(displayName);
        String phoneNumber = currentUser.getPhoneNumber();
        if (phoneNumber != null)
            profile_phone_edittext.setText(phoneNumber);
        if (email != "undefined")
            profile_email.setText(email);
        Uri photoUrl = currentUser.getPhotoUrl();
        userProfileImageUrl = photoUrl;
        if (MainActivity.userPhoto != null)
            Picasso.get().load(MainActivity.userPhoto).rotate(90).into(profile_image);
        Logger.d("PHONE:\r\n" + currentUser.getPhoneNumber() + " ");
    }

    private View.OnClickListener onDoneClick = view -> {

        showProgress();
        final String[] msg = {"Данные обновлены"};
        if (!profile_email.getText().toString().trim().equals(email) || profile_email.getText().toString().trim().length() == 0) {
            if (isEmailValid(profile_email.getText().toString().trim()) || profile_email.getText().toString().trim().length() == 0) {
                MainActivity.logstr("NEW EMAIL!");
                Map<String, Object> mapN = new HashMap<>();
                mapN.put("email", profile_email.getText().toString().trim());
                db.collection("users_mails")
                        .document(NekConfigs.getUserPgone())
                        .set(mapN, SetOptions.merge())
                        .addOnCompleteListener(task -> {
                            NekConfigs.mail = profile_email.getText().toString().trim();
                            Log.d("NekFCM", "Add to db ok");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("NekFCM", "Add to db problem " + e.getLocalizedMessage());
                        });
            } else msg[0] = getString(R.string.invalid_email);
        } else
            MainActivity.logstr("OLD EMAIL! " + email + " == " + profile_email.getText().toString());
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(profile_name_edittext.getText().toString())
                //.setPhotoUri(userProfileImageUrl)
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hideProgress();
                        MainActivity.setNewUserName(profile_name_edittext.getText().toString());
                        Snackbar.make(view, msg[0], Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });


    };

    private Uri imageUri;

    private View.OnClickListener onEditAvatarClick = view -> {
        if (alertWithTwoBtns == null) {
            alertWithTwoBtns = new AlertWithTwoBtns(
                    Objects.requireNonNull(getContext()),
                    getString(R.string.choose_image),
                    getString(R.string.choose_options),
                    getString(R.string.camera),
                    getString(R.string.galery),
                    (dialog, which) -> {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = Objects.requireNonNull(getActivity()).getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PICTURE);
                        is_photo = 1;
                        MainActivity.is_photo = 1;
                    },
                    (dialog, which) -> {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, TAKE_PICTURE);
                        is_photo = 0;
                        MainActivity.is_photo = 0;
                    }
            );
        }
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                22);
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Logger.d("onRequestPermissionsResult");
        if (grantResults.length == 2) {
            alertWithTwoBtns.show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = Objects.requireNonNull(getActivity()).managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Picasso.get().load(data.getData()).transform(new ExifTransformation(this.getContext(), data.getData())).into(profile_image);
                MainActivity.setNewUserPhoto(data.getData());
                if (is_photo == 1) MainActivity.getcircle().setRotation(90);


                uploadAvatar(data.getData());


            } else {
                Picasso.get().load(imageUri).transform(new ExifTransformation(this.getContext(), imageUri)).into(profile_image);
                MainActivity.setNewUserPhoto(imageUri);
                if (is_photo == 1) MainActivity.getcircle().setRotation(90);

                uploadAvatar(imageUri);
            }
        }
    }

    @SuppressLint("CheckResult")
    private void uploadAvatar(Uri data) {
        // showProgress();
        Completable.fromAction(() -> {
            if (data == null)
                return;
            if (mStorageRef == null) {
                mStorageRef = FirebaseStorage.getInstance().getReference();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            StorageReference riversRef = mStorageRef.child("users/" + timeStamp + ".jpg");
            riversRef.putFile(data)
                    .addOnSuccessListener(taskSnapshot -> {
                        hideProgress();
                        riversRef.getDownloadUrl()
                                .addOnCompleteListener(task -> {
                                    userProfileImageUrl = task.getResult();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(userProfileImageUrl)
                                            .build();
                                    currentUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(taskN -> {
                                                if (taskN.isSuccessful()) {
                                                    //hideProgress();
                                                }
                                            });
                                });
                    });
        })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.d("ON_SUBSCRIBE");
                    }

                    @Override
                    public void onComplete() {
                        Picasso.get().load(userProfileImageUrl).fetch();
                        Logger.d("ON_COMPLETE" + is_photo);
                        if (is_photo == 1) {
                            profile_image.setRotation(90);

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("ON_ERROR");
                    }
                });
    }

    private void handleImageOrientation(Uri currentPhotoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath.getEncodedPath());
        Bitmap rotatedBitmap;
        try {
            ExifInterface ei = new ExifInterface(currentPhotoPath.getEncodedPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 0);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            if (rotatedBitmap != bitmap) {
                FileOutputStream fOut = new FileOutputStream(currentPhotoPath.getEncodedPath());
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
            }
            bitmap.recycle();
            rotatedBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
