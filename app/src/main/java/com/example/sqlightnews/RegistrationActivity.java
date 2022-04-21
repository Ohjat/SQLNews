package com.example.sqlightnews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    Calendar date;
    DatePickerDialog.OnDateSetListener picker;
    Spinner spnRoles;
    CheckBox showPassword;
    EditText txtUserSurname, txtUserName, txtUserPatronymic, txtDateOfBirth, txtLogin, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialize();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.roles));
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnRoles.setAdapter(adapter);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dauOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dauOfMonth);
                txtDateOfBirth.setText(DateUtils.formatDateTime(getApplicationContext(),
                        date.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
            }
        };
        txtDateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(RegistrationActivity.this, picker,
                            date.get(Calendar.YEAR),
                            date.get(Calendar.MONTH),
                            date.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
        txtDateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
            }
        });
    }

    private void initialize() {
        date = Calendar.getInstance();
        showPassword = findViewById(R.id.showPassword);
        txtUserSurname = findViewById(R.id.txtUserSurname);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserPatronymic = findViewById(R.id.txtUserPatronymic);
        txtDateOfBirth = findViewById(R.id.txtDateOfBirth);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        spnRoles = findViewById(R.id.spnRoles);
        databaseHelper = new DatabaseHelper(this);
    }

    public void registerClick(View view) {
        if (TextUtils.isEmpty(txtUserSurname.getText()) || TextUtils.isEmpty(txtUserName.getText()) || TextUtils.isEmpty(txtUserPatronymic.getText())) {
            Toast.makeText(this, "Введите ФИО", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.getTimeInMillis() >= System.currentTimeMillis()) {
            Toast.makeText(this, "Некорректный ввод даты", Toast.LENGTH_SHORT).show();
            return;
        }
        Boolean checkInsertData = databaseHelper
                .insertUser(txtUserSurname.getText().toString().trim(), txtUserName.getText().toString().trim(),
                        txtUserPatronymic.getText().toString().trim(), txtDateOfBirth.getText().toString().trim(),
                        txtLogin.getText().toString().trim(), txtPassword.getText().toString().trim(), spnRoles.getSelectedItem().toString());
        if (checkInsertData) {
            Cursor res = databaseHelper.getData(txtLogin.getText().toString().trim(), txtPassword.getText().toString().trim());
            if (res.getCount() == 0) {
                return;
            }
            while (res.moveToNext()) {
                if (res.getString(7).equals("Администратор")) {
                    startActivity(new Intent(this, AllNewsActivityAdministrator.class)
                            .putExtra("Id", res.getInt(0)));
                } else {
                    startActivity(new Intent(this, AllNewsActivity.class));
                }
                finish();
            }
        }
    }
}