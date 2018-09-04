package com.example.developer.messageservice;
//--------------------------------------------------------
//
// DESCRIPTION
// -----------
// This class is designed as the base activity class
// which all other activities should extend
//
// AUTHOR
// ------
// Lumberjacks Incorperated (2018)
//
//--------------------------------------------------------

public class LoginRequiredActivity extends BaseActivity {

    @Override
    protected void onResume() {
        super.onResume();

        this.redirectUserToLoginScreenIfUserIsNotLoggedIn();
    }

    private void redirectUserToLoginScreenIfUserIsNotLoggedIn() {
        PerformableAction redirectUserToLoginScreen = this.createActionToRedirectUserToLoginScreen();
        this.performActionIfUserIsNotLoggedIn(redirectUserToLoginScreen);
    }

    private PerformableAction createActionToRedirectUserToLoginScreen() {
        return new PerformableAction() {
            @Override
            public void performAction() {
                LoginRequiredActivity.this.moveToActivityOfClass(LoginScreenActivity.class);
            }
        };
    }

    private void performActionIfUserIsNotLoggedIn(final PerformableAction actionToPerformIfUserIsNotLoggedIn) {
        RemoteServerAPI remoteServerAPI = RemoteServerAPI.remoteServerAPIWithContext(this.getApplicationContext());
        remoteServerAPI.checkIfUserIsLoggedInToRemoteServerWithAPIDelegate(new RemoteServerAPI.RemoteServerAPIDelegate() {
            @Override
            public void receiveResponseFromAPI(RemoteServerAPIResponse apiResponse) {
                boolean userIsLoggedIn = apiResponse.responseAsBoolean();
                if (!userIsLoggedIn) actionToPerformIfUserIsNotLoggedIn.performAction();
            }
        });
    }
}
