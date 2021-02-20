package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1, e2;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String VerificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //e1 contains phone number
        e1 = (EditText) findViewById(R.id.editTextPhoneNumber);
        e2 = (EditText) findViewById(R.id.editTextCode);
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {

            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);

                VerificationCode = s;
                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void send_sms(View v)
    {
        String number = e1.getText() .toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS, this, mCallback
        );

    }


    public void SignInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "User Signed In Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verify(View v)
    {
        String inputCode = e2.getText().toString();
        if(VerificationCode.equals(""))
        {
            verifyPhoneNumber(VerificationCode, inputCode);
        }
    }

    public void verifyPhoneNumber(String verifyCode, String inputCode)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, inputCode);
        SignInWithPhone(credential);
    }
}