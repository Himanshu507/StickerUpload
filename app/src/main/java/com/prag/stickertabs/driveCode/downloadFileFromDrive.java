package com.prag.stickertabs.driveCode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;




public class downloadFileFromDrive extends AsyncTask<String,String,String> {

    private Activity activity;
    private DialogFragment formFragment;

    //    private ImageView imageView;
    private ProgressDialog progressBar;
    ////    private TextView fileSize;
    private File file;
    private int len,downloadLen;
    private String lenghtOfFile,lengthDownloaded, actualFileName,originalFileName;

    public downloadFileFromDrive(Activity activity) {
        this.activity = activity;
    }

    public downloadFileFromDrive(FragmentActivity activity, ProgressDialog progressDialog) {
        this.activity = activity;
        this.progressBar = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.show();

//        this.fileSize = (TextView) view.findViewById(R.id.fileSize);
        /*this.progressMsg = (TextView) view.findViewById(R.id.downloadInfo);
        progressMsg.setVisibility(View.VISIBLE);*/
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            actualFileName = params[1];
            originalFileName = params[2];
            file = new File(params[0]);
            if(!file.exists())
                file.mkdirs();
            file = new File(params[0]+params[1]);
            FileOutputStream fos=new FileOutputStream(file);
            BufferedOutputStream out=new BufferedOutputStream(fos);
            InputStream in= activity.getContentResolver().openInputStream(Uri.parse(params[3]));
            if(in.available()==0){
                return null;
            }
            int maxBufferSize = 10 * 1024;
            byte[] buffer = new byte[maxBufferSize];
            len = 0;
            downloadLen = len;
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
                downloadLen = downloadLen +len;
                publishProgress(new String[]{lengthDownloaded});
            }
            out.flush();
            fos.getFD().sync();
            out.close();
            in.close();

            return file.getPath();
        } catch (FileNotFoundException f){
            Log.e("Error",Log.getStackTraceString(f));

            // Toast.makeText(activity,"File Download Error",Toast.LENGTH_LONG).show();

        } catch (IOException e){
            Log.e("Error",Log.getStackTraceString(e));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
//        fileSize.setText(values[0]+"/"+lenghtOfFile);
    }

    @Override
    protected void onPostExecute(final String s) {
        super.onPostExecute(s);

        try {
            if (s != null) {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                if (extension != null) {
//                    if (extension.equals(TopicFormFragment.PDFFILE) || extension.equals(TopicFormFragment.DOCFILE) ||
//                            extension.equals(TopicFormFragment.DOCXFILE) || extension.equals(TopicFormFragment.XLSFILE) ||
//                            extension.equals(TopicFormFragment.XLSXFILE) || extension.equals(TopicFormFragment.TEXTFILE) ||
//                            extension.equals(TopicFormFragment.CSVFILE)) {
                   }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

