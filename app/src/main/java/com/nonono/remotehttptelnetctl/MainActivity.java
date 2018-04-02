package com.nonono.remotehttptelnetctl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String getresult = "";
    boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText urlportev = findViewById(R.id.urlport);
        final EditText cmdev = findViewById(R.id.cmd);
        final RadioGroup radiofuncrg = findViewById(R.id.radiofunc);
        radiofuncrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radiofuncrg.findViewById(i);
                int index = radiofuncrg.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        urlportev.setText("http://www.google.com:80/search?");
                        cmdev.setText("q=test");
                        break;
                    case 1:
                        urlportev.setText("http://search.yahoo.com:80");
                        cmdev.setText("p=test");
                        break;
                    case 2:
                        urlportev.setText("127.0.0.1");
                        cmdev.setText("run");
                        break;
                    default:
                        urlportev.setText("error");
                        cmdev.setText("error");
                        break;
                }
            }
        });
        final Button sendbtn = findViewById(R.id.send);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String DOURL, DOCMD;
                final RadioButton httpget = findViewById(R.id.http_GET);
                final RadioButton httppost = findViewById(R.id.http_POST);
                final RadioButton telnet = findViewById(R.id.telnet);
                final TextView resulttv = findViewById(R.id.result);
                final TextView docmdtv = findViewById(R.id.docmd);

                if (httpget.isChecked()) {
                    DOURL = (new StringBuilder()).append(urlportev.getText()).append(cmdev.getText()).toString();
                    docmdtv.setText(DOURL);
                    resulttv.clearComposingText();
                    finish = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnectionAPI http = new HttpURLConnectionAPI();
                            try {
                                getresult = http.sendGet(DOURL);
                                finish = true;
                            } catch (Exception e) {
                                getresult = e.toString();
                                finish = true;
                            }
                        }
                    }).start();
                    int counter = 0;
                    while (!finish) {
                        resulttv.setText("WAIT...".concat(Integer.toString(counter)));
                        counter++;
                    }
                    finish = false;
                    resulttv.setText(getresult);
                } else if (httppost.isChecked()) {
                    DOURL = (new StringBuilder()).append(urlportev.getText()).toString();
                    DOCMD = (new StringBuilder()).append(cmdev.getText()).toString();
                    docmdtv.setText((new StringBuilder()).append(DOURL).append(" | ").append(DOCMD).toString());
                    resulttv.clearComposingText();
                    finish = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnectionAPI http = new HttpURLConnectionAPI();
                            try {
                                getresult = http.sendPost(DOURL, DOCMD);
                                finish = true;
                            } catch (Exception e) {
                                getresult = e.toString();
                                finish = true;
                            }
                        }
                    }).start();
                    int counter = 0;
                    while (!finish) {
                        resulttv.setText("WAIT...".concat(Integer.toString(counter)));
                        counter++;
                    }
                    finish = false;
                    resulttv.setText(getresult);
                } else if (telnet.isChecked()) {
                    DOURL = (new StringBuilder()).append(urlportev.getText()).toString();
                    DOCMD = (new StringBuilder()).append(cmdev.getText()).toString();
                    docmdtv.setText((new StringBuilder()).append("telnet://").append(DOURL).append(" | ").append(DOCMD).toString());
                    resulttv.clearComposingText();
                    finish = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TelnetSocketConnectionAPI telnet = new TelnetSocketConnectionAPI();
                            try {
                                String pParams = {DOURL, DOCMD};
                                telnet.execute(pParams);
                                //getresult = telnet.telnetcmd(DOURL, DOCMD);
                                finish = true;
                            } catch (Exception e) {
                                getresult = e.toString();
                                finish = true;
                            }
                        }
                    }).start();
                    int counter = 0;
                    while (!finish) {
                        resulttv.setText("WAIT...".concat(Integer.toString(counter)));
                        counter++;
                    }
                    finish = false;
                    resulttv.setText(getresult);
                }
            }
        });
    }
}
