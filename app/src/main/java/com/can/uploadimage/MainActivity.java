package com.can.uploadimage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.can.uploadimage.api.APIService;
import com.can.uploadimage.api.Result;
import com.can.uploadimage.api.RetroClient;
import com.can.uploadimage.permission.PermissinCheker;
import com.can.uploadimage.permission.permissionActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final String[] PERMISSION_READ_STORAGE = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE};

    Context context;

    View parentView;
    ImageView imageVi;
    TextView te;

    String imagePath;

    PermissinCheker permissinCheker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = getApplicationContext();

        parentView = findViewById(R.id.vvvv);

        permissinCheker = new PermissinCheker(this);

        te = (TextView)findViewById(R.id.textView);
        imageVi= (ImageView) findViewById(R.id.imageView);

        FloatingActionButton act = (FloatingActionButton) findViewById(R.id.fla);
        assert act != null;

        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(imagePath)) {

                    if (INternetConnection.checkConnection(context)) {
                        uploadImage();
                    } else {
                        Snackbar.make(parentView, "warning notice SnackBar", Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(parentView, "lampirkan file untuk di unggah", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadImage() {

        final ProgressDialog p  ;
        p = new ProgressDialog(this);
        p.setMessage("title iUpload Foto");
        p.show();

        APIService s = RetroClient.getService();

        File f = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                p.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getResult().equals("success"))
                        Snackbar.make(parentView, "upload Success", Snackbar.LENGTH_INDEFINITE).show();
                    else
                        Snackbar.make(parentView, "Gagal Upload Image", Snackbar.LENGTH_INDEFINITE).show();
                }else {
                    Snackbar.make(parentView, "Upload Image Gagal", Snackbar.LENGTH_INDEFINITE).show();
                }

                imagePath = "";
                te.setVisibility(View.VISIBLE);
//                imageVi.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                p.dismiss();



            }
        });
    }
    public void showImagePopup(View v){
        if (permissinCheker.lacksPermissions(PERMISSION_READ_STORAGE)){
            startPermissionActivity(PERMISSION_READ_STORAGE);

        }else {

            Intent qq = new Intent(Intent.ACTION_PICK);
            qq.setType("image/*");
            startActivityForResult(Intent.createChooser(qq, "Pilih Foto"), 100);



         //   final Intent galleryIntent = new Intent();
         //   galleryIntent.setType("image/*");
         //   galleryIntent.setAction(Intent.ACTION_PICK);

         //   final Intent pilihanIntent = Intent.createChooser(galleryIntent, "pilih foto");
         //   startActivityForResult(pilihanIntent, 1010);
        }
    }

    private void startPermissionActivity(String[] permissionReadStorage) {
        permissionActivity.startActivityForResult(this, 0, permissionReadStorage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode ==100 && resultCode == Activity.RESULT_OK){
            if (data == null){
                Snackbar.make(parentView, "unable to pick Image", Snackbar.LENGTH_INDEFINITE).show();
                return;

            }else {
                Snackbar.make(parentView, "image dapat", Snackbar.LENGTH_SHORT).show();
            }
            Uri selectImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor c =getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
            if (c !=null){
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePathColumn[0]);
                imagePath = c.getString(columnIndex);

                Picasso.with(context).load(new File(imagePath)).into(imageVi);

                Snackbar.make(parentView, "String Reselect", Snackbar.LENGTH_SHORT).show();
                c.close();

                te.setVisibility(View.GONE);
                imageVi.setVisibility(View.VISIBLE);
            }else {
                te.setVisibility(View.VISIBLE);
                imageVi.setVisibility(View.GONE);
                Snackbar.make(parentView, "unable to load Image", Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }
}
