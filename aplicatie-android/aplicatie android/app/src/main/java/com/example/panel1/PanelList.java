package com.example.panel1;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/*import androidx.appcompat.app.AlertDialog;**/
public class PanelList extends AppCompatActivity {

    GridView gridView;
    ArrayList<panel> list;
    PanelListAdapter adapter = null;
    public ImageView imageView;
    SearchView sv;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_list_activity);

        sv = (SearchView) findViewById(R.id.searchView1);
        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new PanelListAdapter(this, R.layout.panel_items, list);
        gridView.setAdapter(adapter);

        // preia date din sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM panel");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nume = cursor.getString(1);
            String promotie = cursor.getString(2);
            String clasa = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new panel(nume, promotie,clasa, image, id));
        }
        adapter.notifyDataSetChanged();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });





        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(PanelList.this);

                dialog.setTitle("Choose an action");
                AlertDialog.Builder builder = dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM panel");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here

                            showDialogUpdate(PanelList.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM panel");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }

                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;

            }


            ImageView imageView;
            private void showDialogUpdate(Activity activity, final int position){

                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.update_panel_activity);
                dialog.setTitle("Update");

                imageView = (ImageView) dialog.findViewById(R.id.imageView);
                final EditText edtnume = (EditText) dialog.findViewById(R.id.edtnume);
                final EditText edtclasa = (EditText) dialog.findViewById(R.id.edtclasa);
                final EditText edtpromotie = (EditText) dialog.findViewById(R.id.edtpromotie);
                Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

                // set width for dialog
                int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
                // set height for dialog
                int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // request photo library
                        ActivityCompat.requestPermissions(
                                PanelList.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                888
                        );
                    }
                });


                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            MainActivity.sqLiteHelper.updateData(
                                    edtnume.getText().toString().trim(),
                                    edtpromotie.getText().toString().trim(),
                                    edtclasa.getText().toString().trim(),
                                    MainActivity.imageViewToByte(imageView),
                                    position
                            );
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Update successfully!!!",Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception error) {
                            Log.e("Update error", error.getMessage());
                        }
                        updatePanelList();
                    }
                });
            }

            private void showDialogDelete(final int idpanel){
                final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(PanelList.this);

                dialogDelete.setTitle("Warning!!");
                dialogDelete.setMessage("Are you sure you want to this delete?");
                dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            MainActivity.sqLiteHelper.deleteData(idpanel);
                            Toast.makeText(getApplicationContext(), "Delete successfully!!!",Toast.LENGTH_SHORT).show();
                        } catch (Exception e){
                            Log.e("error", e.getMessage());
                        }
                        updatePanelList();
                    }
                });

                dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogDelete.show();
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void updatePanelList() {

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM panel");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nume = cursor.getString(1);
            String promotie = cursor.getString(2);
            String clasa = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new panel(nume, promotie,clasa, image, id));
        }
        adapter.notifyDataSetChanged();
    }
}
