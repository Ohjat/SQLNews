package com.example.sqlightnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    CheckBox showPassword;
    EditText txtLogin, txtPassword;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initialize();
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void initialize() {
        showPassword = findViewById(R.id.showPassword);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        databaseHelper = new DatabaseHelper(this);
    }

    public void enterClick(View view) {
        Intent intent = new Intent(this, AllNewsActivityAdministrator.class);
        Cursor res = databaseHelper.getData(txtLogin.getText().toString().trim(), txtPassword.getText().toString().trim());
        if (res.getCount() == 0) {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        while (res.moveToNext()) {
            if (res.getString(7).equals("Администратор")) {
                intent.putExtra("Id", res.getInt(0));
                startActivity(intent);
            } else {
                startActivity(new Intent(this, AllNewsActivity.class));
            }
        }
    }

    public void registrationClick(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}