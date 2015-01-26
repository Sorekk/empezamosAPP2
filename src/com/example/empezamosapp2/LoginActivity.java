package com.example.empezamosapp2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.insready.drupalcloud.ImageLoader;
import com.insready.drupalcloud.JSONServerClient;
import com.insready.drupalcloud.ServiceNotAvailableException;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity
{
  public static final int FAILED = 0;
  public static final int NOTAVAIlABLE = 2;
  public static final int REQUEST_LOGIN = 10001;
  public static final int SUCCEEDED = 1;
  private EditText mPassword;
  private EditText mUsername;

  public static boolean storeUserInfo(SharedPreferences paramSharedPreferences, String paramString1, Activity paramActivity, String paramString2)
  {
    try
    {
      JSONObject localJSONObject1 = new JSONObject(new JSONObject(paramString1).getString("#data"));
      SharedPreferences.Editor localEditor = paramSharedPreferences.edit();
      localEditor.putString("sessionid", localJSONObject1.getString("sessid"));
      JSONObject localJSONObject2 = new JSONObject(localJSONObject1.getString("user"));
      String str = "http://bragasasesinas.mu.zz/" + Uri.encode(localJSONObject2.getString("picture"), "/");
      localEditor.putString("picture", str);
      localEditor.putLong("sessionid_timestamp", new Date().getTime() / 1000L);
      localEditor.putInt("uid", localJSONObject2.getInt("uid"));
      localEditor.putString("name", localJSONObject2.getString("name"));
      localEditor.putString("mail", localJSONObject2.getString("mail"));
      localEditor.putString("pass", paramString2);
      localEditor.commit();
      new ImageLoader(paramActivity).load(str, null);
      return true;
    }
    catch (JSONException localJSONException)
    {
    }
    return false;
  }

  public void onCancelClick(View paramView)
  {
    setResult(0);
    finish();
  }

  public void onConfirmClick(View paramView)
  {
    if ((this.mUsername.getText().length() != 0) && (this.mPassword.getText().length() != 0))
    {
      new login(null).execute(new Object[0]);
      return;
    }
    Toast.makeText(this, "Please put username and password in the login dialog.", 1).show();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.mUsername = ((EditText)findViewById(R.id.login_username));
    this.mPassword = ((EditText)findViewById(R.id.login_password));
    ((TextView)findViewById(2131230721)).setText("Login to GreatBrewers.com");
  }

  public void onHomeClick(View paramView)
  {
    UIUtils.goHome(this);
  }

  public void onRegisterClick(View paramView)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setData(Uri.parse("http://greatbrewers.com/user/register"));
    startActivity(localIntent);
  }

  private class login extends AsyncTask<Object, String, Boolean>
  {
    private ProgressDialog proDialog = new ProgressDialog(LoginActivity.this);

    private login()
    {
    }

    protected Boolean doInBackground(Object[] paramArrayOfObject)
    {
      publishProgress(new String[] { "Signing in to GreatBrewers.com ..." });
      String str1 = LoginActivity.this.getString(2131034114);
      SharedPreferences localSharedPreferences = LoginActivity.this.getSharedPreferences(str1, 0);
      JSONServerClient localJSONServerClient = new JSONServerClient(LoginActivity.this, str1, LoginActivity.this.getString(2131034115), LoginActivity.this.getString(2131034116), LoginActivity.this.getString(2131034117), LoginActivity.this.getString(2131034118), Long.valueOf(Long.parseLong(LoginActivity.this.getString(2131034119))));
      try
      {
        String str3 = localJSONServerClient.userLogin(LoginActivity.this.mUsername.getText().toString(), LoginActivity.this.mPassword.getText().toString());
        str2 = str3;
        return Boolean.valueOf(LoginActivity.storeUserInfo(localSharedPreferences, str2, LoginActivity.this, LoginActivity.this.mPassword.getText().toString()));
      }
      catch (ServiceNotAvailableException localServiceNotAvailableException)
      {
        while (true)
        {
          localServiceNotAvailableException.printStackTrace();
          String str2 = null;
        }
      }
    }

    protected void onPostExecute(Boolean paramBoolean)
    {
      if (paramBoolean.booleanValue())
      {
        LoginActivity.this.setResult(-1);
        LoginActivity.this.finish();
      }
      while (true)
      {
        this.proDialog.dismiss();
        return;
        Toast.makeText(LoginActivity.this, "Wrong username or password. Please try it again.", 1).show();
      }
    }

    protected void onProgressUpdate(String[] paramArrayOfString)
    {
      this.proDialog.setMessage(paramArrayOfString[0]);
      this.proDialog.show();
    }
  }
}

/* Location:           C:\Windows\apktool1.5.2\classes_dex2jar.jar
 * Qualified Name:     com.greatbrewers.beercloud.LoginActivity
 * JD-Core Version:    0.6.2
 */