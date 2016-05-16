package com.tgs.tecipe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PDFDisplayFragment extends Fragment implements OnLoadCompleteListener, OnPageChangeListener  {

	public static String recipeName="recipe_name";
	public static String recipePath="recipe_path";
	private float per=0;
	//https://www.dropbox.com/s/lq5w2676cte0od5/Mealmaker%20biryani.pdf
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		   View rootView = inflater.inflate(R.layout.activity_main, container, false);
		   
		   PDFView   pdfView=(PDFView)rootView.findViewById(R.id.pdf_id);
		   
		  /* AdView ad = (AdView)rootView.findViewById(R.id.adView);
		   ad.loadAd(new AdRequest());*/

		   try{
			   
			   Bundle extras = getArguments();
		        
		         if(extras.getString(recipePath)!=null)
		         {
		        pdfView.fromAsset(extras.getString(recipePath))
		        
		       // .pages(0, 1, 2, 3, 4, 5)
		        .defaultPage(1)
		        .showMinimap(false)
		        .enableSwipe(true)
		         
		        .onLoad(this)
		        .onPageChange(this)
		        .load();
		         }
			   
		   }catch (Exception e) {
			// TODO: handle exception
			   Log.e(getTag(), e.toString());
			   
		}
		   
		   
		  //  new MyTask().execute("");
		   
	        
		   
	         
		return rootView;
	}
	
	@Override
	public void loadComplete(int nbPages) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageChanged(int page, int pageCount) {
		// TODO Auto-generated method stub
		
	}
	
	class MyTask extends AsyncTask<String, Integer, Integer>
	{
		ProgressDialog bar=null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			bar=new ProgressDialog(getActivity());
			bar.show();
		}

		@Override
		protected Integer doInBackground(String... params) {

			//downloadFile("https://www.dropbox.com/s/lq5w2676cte0od5/Mealmaker%20biryani.pdf");
			downloadFile("https://dl.dropboxusercontent.com/s/lq5w2676cte0od5/Mealmaker%20biryani.pdf");
			
			return null;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			bar.dismiss();
		}
		
	}
	
	
	
	
	File downloadFile(String dwnload_file_path) {
        File file = null;
        int downloadedSize = 0, totalsize;
        try {
 
        	String fileName="new.pdf";
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
 
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
 
            // connect
            urlConnection.connect();
 
            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, fileName);
 
            FileOutputStream fileOutput = new FileOutputStream(file);
 
            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();
 
            // this is the total size of the file which we are
            // downloading
              totalsize = urlConnection.getContentLength();
           Log.i("INFO","Starting PDF download..." +file.getPath());
 
            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];  
            int bufferLength = 0;
 
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                  downloadedSize = bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                /*setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");*/
            }
            // close the output stream when complete //
            fileOutput.close();
            //setText("Download Complete. Open PDF Application installed in the device.");
 
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            //setTextError("Some error occured. Press back and try again.",
        	   e.printStackTrace();
        } catch (final Exception e) {
           /* setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);*/
        	   e.printStackTrace();
        }
        return file;
    }
}
