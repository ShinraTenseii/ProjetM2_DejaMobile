package com.example.thibault.apptest;
import android.os.AsyncTask;
import javax.net.ssl.*;
import java.lang.Object;
import java.net.InetSocketAddress;

// AsyncTask<Params, Progress, Result> type

private class SocketAsync extends AsyncTask<SSLSocket, Integer, Integer> {
        @override
        protected int doInBackground( SSLSocket socket){
            try {
                socket.connect(new InetSocketAddress("10.0.2.2", 1599), 5000);
                socket.startHandshake();
            }
            catch (Exception e) {
                e.printStackTrace();
                 }
            return 0;
        };

        @override
        protected void onPostExecute( int result){

        }

    }
}
