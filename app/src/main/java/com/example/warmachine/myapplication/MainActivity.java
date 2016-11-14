package com.example.warmachine.myapplication;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.warmachine.myapplication.Database.History;
import com.example.warmachine.myapplication.Database.WordContract;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.vstechlab.easyfonts.EasyFonts;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,   View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public TextToSpeech tts;
    public ImageButton btnSpeak;
    static String jsonResponse = " ";
    static String[] result;
    static String STRING_URL;
    EditText et;
    static String word;
    private FloatingActionButton btnSpeak1;
    protected static final int RESULT_SPEECH = 1;
    ProgressBar pb;
    static String[] myStrings;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv1);



        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());


        tv.setTypeface(EasyFonts.captureIt(this));
        tts = new TextToSpeech(this, this);
        btnSpeak1 = (FloatingActionButton) findViewById(R.id.sb);
        btnSpeak1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    et.setText("");
                } catch (ActivityNotFoundException a) {

                }
            }
        });
        pb = (ProgressBar) findViewById(R.id.ss);

        btnSpeak = (ImageButton) findViewById(R.id.b2);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                speakOut();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }



    private void handleSignInResult(GoogleSignInResult result) {
//
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

        }
    }



    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.History:
                Intent intent = new Intent(MainActivity.this, History.class);
                startActivity(intent);
                return true;
            case R.id.Setting:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
                btnSpeak.setEnabled(true);
                speakOut();}

        } else {
        }
    }

    public void speakOut() {

        et = (EditText) findViewById(R.id.et1);
        String text = et.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void Meaning(View view) {

        et = (EditText) findViewById(R.id.et1);
        word = et.getText().toString();

        RequestAsynctask ra = new RequestAsynctask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = prefs.getString(getString(R.string.pref_lang_key),getString(R.string.default_value));
        ra.execute(lang);
        insertWord();


    }

    public class RequestAsynctask extends AsyncTask <String, Void, String[]>
    {
        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
            rl1.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {

            String apikey = "dict.1.1.20161026T042454Z.e4ee0900b0e325c4.c27358b16e947cd8921b06d697127e3afaae09ef";

            final String BASE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?";
            final String API_KEY = "key";
            final String LANGUAGE = "lang";
            final String WORD = "text";

            try{
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, apikey)
                        .appendQueryParameter(LANGUAGE, params[0])
                        .appendQueryParameter(WORD, word)
                        .build();

                STRING_URL = builtUri.toString();
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            } catch(Exception e){
            }

            URL url = Request.createUrl(STRING_URL);
            try{
                jsonResponse = Request.makeHttpRequest(url);

            }catch(Exception e) {
            }
            result = Request.jsonParse(jsonResponse);
            return result;

        }


        @Override
        protected void onPostExecute(String[] s) {

            Intent intent = new Intent(MainActivity.this, MeaningActivity.class);
            myStrings = new String[]  {s[0], s[1], s[2], s[3], s[4], s[5]};
            intent.putExtra("strings", s);
            startActivity(intent);

            pb.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
                    rl1.setVisibility(View.VISIBLE);}
            }, 2000);

            super.onPostExecute(s);

        }
    }

    public void insertWord(){
        et = (EditText) findViewById(R.id.et1);
        String name = et.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(WordContract.WordEntry.COLUMN_WORD_NAME,name);

        Uri newUri = getContentResolver().insert(WordContract.WordEntry.CONTENT_URI, values);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    et.setText(text.get(0));
                }
                break;
            }

        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
}