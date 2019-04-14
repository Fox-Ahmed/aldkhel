package com.aldkhel.aldkhel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aldkhel.aldkhel.utils.Consts;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Consts.APP_FONT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_login);

        findViewById(R.id.tvForgetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = getLayoutInflater().inflate(R.layout.dialog_forget, null);
                final EditText etEmailForeget = view.findViewById(R.id.etEmail);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setView(view);
                builder.setTitle("هل نسيت كلمة المرور ؟");
                builder.setPositiveButton("متابعة", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etEmailForeget.getText().toString().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "الرجاء ملئ الاماكن الخالية", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();
                        sendForgetEmail(etEmailForeget.getText().toString());
                    }
                });
                builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

            }
        });

        findViewById(R.id.bRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.bLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "الرجاء ملئ الاماكن الخالية", Toast.LENGTH_SHORT).show();
                    return;
                }

                doLogin(email, password);
            }
        });

    }

    private void doLogin(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        AndroidNetworking.post(Consts.API_URL + "write/login.php")
                .setPriority(Priority.HIGH)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dialog.dismiss();
                        Log.d(TAG, response.toString());

                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this, R.string.connection_err, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendForgetEmail(String email) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        AndroidNetworking.post(Consts.API_URL + "write/forget_password.php")
                .setPriority(Priority.HIGH)
                .addBodyParameter("email", email)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dialog.dismiss();
                        Log.d(TAG, response.toString());

                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this, R.string.connection_err, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
