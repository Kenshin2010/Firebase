package vn.manroid.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {


    //291868114586979

    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuthen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Moi nguoi sua lai facebook
        FacebookSdk.setApplicationId("291868114586979");
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        btnLoginFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnLoginFacebook.setReadPermissions("email", "public_profile");

        btnLoginFacebook.setOnClickListener(this);
        //Đối tượng này hỗ trợ cho việc khởi tọa login trên face
        callbackManager = CallbackManager.Factory.create();

        mAuthen = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        btnLoginFacebook.registerCallback(callbackManager,
                login_CallBack);
    }

    private FacebookCallback<LoginResult> login_CallBack
            = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Login vào face của bạn thành công
            //Như tạo user lên firebase
            AuthCredential credential =
                    FacebookAuthProvider.getCredential(
                            loginResult.getAccessToken().getToken());

            mAuthen.signInWithCredential(credential)
                    .addOnCompleteListener(LogInActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        //Tạo user vào firebase thành công
                                        Intent main = new Intent(LogInActivity.this,MainActivity.class);
                                        startActivity(main);

                                        finish();
                                    }
                                }
                            });


        }

        @Override
        public void onCancel() {

            //Hủy việc login
        }

        @Override
        public void onError(FacebookException error) {

            //Login có lỗi xảy ra
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
