package filetest.casualdrive.com.filetest;

import android.app.Activity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileAccessor {

	private Activity	m_instance;

	/**
	 * コンストラクタ
	 * 
	 * @param instance Contextインスタンス
	 */
	public FileAccessor(Activity instance)
	{
		m_instance = instance;
	}

	
	public String getDataPath()
	{
		return m_instance.getFilesDir().getPath();
	}
	
	
	/**
	 * ファイル読み込み
	 * 
	 * @param fileName 読み込み対象ファイル名
	 * 
	 * @return 読み込まれたデータ。ファイルが存在しない場合はnull画返却される
	 */
	public byte[] read( String fileName )
	{
		FileInputStream fileInputStream = null;
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream      dout = new DataOutputStream(bout);
		

		try
		{
			int size;
			
			File file =  new File(fileName);  //m_instance.getFileStreamPath(fileName);
			
			if( !file.exists() ){
				return null;
			}
			System.out.println( file.toURI().getPath() );
			

			fileInputStream = new FileInputStream(fileName);  //m_instance.openFileInput(fileName);
			
			int fileSize = fileInputStream.available();

			System.out.println( "file size["+fileSize+"]" );

			if( 0 == fileSize ){
				return null;
			}
			
			byte[] readBytes = new byte[fileSize];

			while( (size = fileInputStream.read(readBytes)) != -1 )
			{
				dout.write(readBytes, 0, size);
			}
			return bout.toByteArray();
		}
		catch( FileNotFoundException ex)
		{
			System.out.println( ex );
		}
		catch( Throwable ex)
		{
			System.out.println( ex );
		}
		finally
		{
			try{
				if( fileInputStream != null )
				{
					fileInputStream.close();
					fileInputStream = null;
				}
			}
			catch( Throwable ex ){				
			}
			try{
				if( dout != null )
				{
					dout.close();
					dout = null;
				}
			}
			catch( Throwable ex ){				
			}
			try{
				if( bout != null )
				{
					bout.close();
					bout = null;
				}
			}
			catch( Throwable ex ){				
			}
		}
		return null;
	}
	
	
	/**
	 * ファイル書き込み
	 * 
	 * @param fileName 書き込み対象ファイル名
	 * @param buff     書き込むデータ
	 * 
	 * @return 書き込み成功ならtrueを返却する
	 */
	public boolean write( String fileName, byte[] buff )
	{
		FileOutputStream fileOutputStream = null;
		try
		{
			fileOutputStream = new FileOutputStream(fileName); //m_instance.openFileOutput(fileName, Activity.MODE_PRIVATE);
			fileOutputStream.write(buff);
			return true;
		}
		catch( FileNotFoundException ex)
		{
			System.out.println( ex );
		}
		catch( Throwable ex)
		{
			System.out.println( ex );
		}
		finally
		{
			try{
					
				if( fileOutputStream != null )
				{
					fileOutputStream.flush();
					fileOutputStream.close();
					fileOutputStream = null;
				}
			}
			catch( Throwable ex ){				
			}
		}
		return false;
	}
	
	
	/**
	 * ファイルを削除する
	 * 
	 * @param fileName 削除対象ファイル名
	 * 
	 * @return 削除成功ならtrueを返却
	 */
	public boolean delete( String fileName )
	{
		File file = new File(fileName);
		
		System.out.println( "delete["+file.getPath()+"]" );
		
		return file.delete();
	}
}
