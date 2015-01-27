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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
  //Creamos los EditText que luego los apuntamos al xml
  private EditText mPassword;
  private EditText mUsername;

  public static boolean storeUserInfo(SharedPreferences paramSharedPreferences, String paramString1, Activity paramActivity, String paramString2)
  {
    try
    {
      //Consulta las JSON y las tranforam en objetos. No lo tengo del todo claro como funciona. Duda existencial con el "#data" que sale en http://greatbrewers.com/services/json
      JSONObject localJSONObject1 = new JSONObject(new JSONObject(paramString1).getString("#data"));
      SharedPreferences.Editor localEditor = paramSharedPreferences.edit();
      localEditor.putString("sessionid", localJSONObject1.getString("sessid"));
      JSONObject localJSONObject2 = new JSONObject(localJSONObject1.getString("user"));
      String str = "http://" + paramActivity.getResources().getString(R.string.SERVER) + "/" + Uri.encode(localJSONObject2.getString("picture"), "/");
      localEditor.putString("picture", str);
      localEditor.putLong("sessionid_timestamp", new Date().getTime() / 1000L);
      localEditor.putInt("uid", localJSONObject2.getInt("uid"));
      localEditor.putString("name", localJSONObject2.getString("name"));
      localEditor.putString("mail", localJSONObject2.getString("mail"));
      localEditor.putString("pass", paramString2);
      localEditor.commit();
      return true;
    }
    catch (JSONException localJSONException)
    {
    }
    return false;
  }

  public void onConfirmClick(View paramView)
  {
    if ((this.mUsername.getText().length() != 0) && (this.mPassword.getText().length() != 0))
    {
      new login(/*Ellos tenian un null lo e quitado porque no se para que lo necesitan*/).execute(new Object[0]); /*Esto apunta a el AsyncTask no sabemos porque peta*/
      return;
    }
    Toast.makeText(this, "Please put username and password in the login dialog.", 1).show();
  }
  
  
  

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    //Llamamos la activity del XML
    setContentView(R.layout.activity_login);
    //Asignamos los EditText del xml a las variables que emos creado al principo entonces ya podemos trabajar con esas variables
    this.mUsername = ((EditText)findViewById(R.id.login_username));
    this.mPassword = ((EditText)findViewById(R.id.login_password));
    
    // Nos sobra de momento! ((TextView)findViewById(2131230721)).setText("Login to GreatBrewers.com");
  }
  
  
  
  
  

  private class login extends AsyncTask<Object, String, Boolean>
  {
    private ProgressDialog proDialog = new ProgressDialog(LoginActivity.this);

    private login()
    {
    }

    protected Boolean doInBackground(Object[] paramArrayOfObject)
    {
      publishProgress(new String[] { "Signing in to Greatbrewers.com ..." });
      String str1 = LoginActivity.this.getString(R.string.sharedpreferences_name);
      SharedPreferences localSharedPreferences = LoginActivity.this.getSharedPreferences(str1, 0);
      //Conexion con el servidor de drupal, con las llaves de seguridad y el algorithmo de seguridad!!
      JSONServerClient localJSONServerClient = new JSONServerClient(LoginActivity.this, str1, LoginActivity.this.getString(R.string.SERVER), LoginActivity.this.getString(R.string.API_KEY), LoginActivity.this.getString(R.string.DOMAIN), LoginActivity.this.getString(R.string.ALGORITHM), Long.valueOf(Long.parseLong(LoginActivity.this.getString(R.string.SESSION_LIFETIME))));
      
      /*comentario para ver en el logCat si entramos en el AsyncTask*/ Log.d("AsyncTask", str1);
      //Try catch de confiramcion de usuario con drupal, tampoco esta muy claro como funciona.
      try
      {
        String str3 = localJSONServerClient.userLogin(LoginActivity.this.mUsername.getText().toString(), LoginActivity.this.mPassword.getText().toString());
        /*comentario para ver en el logCat si entramos en el TryCatch*/ Log.d("TryCatch", str3);
        String str2 = str3;
        /*comentario para ver en el logCat si entramos en el TryCatch*/ Log.d("TryCatch2", str2);
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
        Toast.makeText(LoginActivity.this, "Wrong username or password. Please try it again.", 1).show();
        return;
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