package com.example.developer.messageservice;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;

public class LoginScreenActivity extends BaseActivity {

    private EditText usernameEditTextField;
    private EditText passwordEditTextField;


    private final int LOGIN_SCREEN_UI_LAYOUT = R.layout.login_activity_screen;

    @Override
    protected void initialiseActivity() {
        this.setContentView(LOGIN_SCREEN_UI_LAYOUT);
        this.initialiseUserInputFields();
        this.initialiseLoginButton();
    }

    private void initialiseUserInputFields() {
        this.usernameEditTextField = (EditText) this.findViewById(R.id.usernameEditText);
        this.passwordEditTextField = (EditText) this.findViewById(R.id.passwordEditText);
    }

    private void initialiseLoginButton() {
        Button loginButton = (Button) this.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this.whenLoginButtonIsPressedThenSendLoginRequestToRemoteServer());
    }

    private View.OnClickListener whenLoginButtonIsPressedThenSendLoginRequestToRemoteServer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginScreenActivity.this.sendLoginRequestToRemoteServer();
            }
        };
    }

    private void sendLoginRequestToRemoteServer() {
        String inputtedUsernameForLoginRequest = usernameEditTextField.getText().toString();
        String inputtedPasswordForLoginRequest = passwordEditTextField.getText().toString();
        RemoteServerAPI remoteServerAPI = RemoteServerAPI.remoteServerAPIWithContext(this.getApplicationContext());
        remoteServerAPI.loginToRemoteServerWithUsernamePasswordAndAPIDelegate(inputtedUsernameForLoginRequest, inputtedPasswordForLoginRequest, this.createAPIDelegateToFinishActivityWhenLoginHasCompleted());
    }

    RemoteServerAPI.RemoteServerAPIDelegate createAPIDelegateToFinishActivityWhenLoginHasCompleted() {
        return new RemoteServerAPI.RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                LoginScreenActivity.this.finish();
            }
        };
    }
}