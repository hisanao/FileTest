package filetest.casualdrive.com.filetest;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import com.smrtbeat.SmartBeat;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    public native boolean saveFromJNI( String fFileName, byte[] srcArray );
    public native byte[]  loadFromJNI( String fFileName );
    //
    FileAccessor m_fileAccessor;
    Button       m_saveButton;
    Button       m_loadButton;
    Button       m_saveButtonJNI;
    Button       m_loadButtonJNI;
    Button       m_deleteButton;
    CheckBox     m_absoluteCheckbox;
    TextView     m_statusTextView;
    TextView     m_filepathTextView;
    //
    static final String ACCESS_FILE_NAME = "savefile.bin";
    static final String SAVE_DATA        = "I am savedata!!";
    // 想定される内部データファイルへの絶対パス
    static final String ABSOLUTE_FILE_PATH = "/storage/sdcard0/Android/data/filetest.casualdrive.com.filetest/files/"+ACCESS_FILE_NAME;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_fileAccessor = new FileAccessor(this);

        m_saveButton       = (Button)findViewById( R.id.save_button );
        m_loadButton       = (Button)findViewById( R.id.load_button );
        m_saveButtonJNI    = (Button)findViewById( R.id.save_button2 );
        m_loadButtonJNI    = (Button)findViewById( R.id.load_button2 );
        m_deleteButton     = (Button)findViewById( R.id.delete_button );
        m_statusTextView   = (TextView)findViewById( R.id.status_text_view );
        m_filepathTextView = (TextView)findViewById( R.id.filepath_text_view );
        m_absoluteCheckbox = (CheckBox)findViewById( R.id.absoluteCheckBox );

        String filePath = "FilePath["+m_fileAccessor.getDataPath()+"/"+ACCESS_FILE_NAME+"]";
        m_filepathTextView.setText(filePath.toCharArray(), 0, filePath.length());

        m_saveButton.setOnClickListener(this);
        m_loadButton.setOnClickListener(this);
        m_saveButtonJNI.setOnClickListener(this);
        m_loadButtonJNI.setOnClickListener(this);
        m_deleteButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SmartBeat.notifyOnResume(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        SmartBeat.notifyOnPause(this);
    }

    @Override
    public void onClick(View v)
    {
        if( v == m_saveButton )
        {
            if( m_fileAccessor.write(getAbsoluteFilePath(), SAVE_DATA.getBytes()) ){
                m_statusTextView.setText( R.string.status_save_success );
            }
            else{
                m_statusTextView.setText( R.string.status_save_failed );
            }
        }
        else if( v == m_loadButton )
        {
            byte[] savedata = m_fileAccessor.read(getAbsoluteFilePath());

            if( (savedata != null) && (SAVE_DATA.compareTo( new String(savedata) )==0) ){
                m_statusTextView.setText( R.string.status_load_success );
            }
            else{
                m_statusTextView.setText( R.string.status_load_failed );
            }
        }
        else if( v == m_saveButtonJNI )
        {
            if( saveFromJNI(getAbsoluteFilePath(), SAVE_DATA.getBytes() ) ){
                m_statusTextView.setText( R.string.status_save_success );
            }
            else{
                m_statusTextView.setText( R.string.status_save_failed );
            }
        }
        else if( v == m_loadButtonJNI )
        {
            byte[] savedata = loadFromJNI( getAbsoluteFilePath() );

            if( (savedata != null) && (SAVE_DATA.compareTo( new String(savedata) )==0) ){
                m_statusTextView.setText( R.string.status_load_success );
            }
            else{
                m_statusTextView.setText( R.string.status_load_failed );
            }
        }
        else if( v == m_deleteButton )
        {
            if( m_fileAccessor.delete(getAbsoluteFilePath()) ){
                m_statusTextView.setText( R.string.status_delete_success );
            }
            else{
                m_statusTextView.setText( R.string.status_delete_failed );
            }
        }
    }

    /**
     * セーブデータファイルの絶対パスを取得
     *
     * @return
     */
    String getAbsoluteFilePath()
    {
        if( m_absoluteCheckbox.isChecked() ){
            return ABSOLUTE_FILE_PATH;
        }
        return getExternalFilesDir(null).getAbsolutePath() +"/"+ACCESS_FILE_NAME;
    }
}
