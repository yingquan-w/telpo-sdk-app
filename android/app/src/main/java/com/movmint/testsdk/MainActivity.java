package com.movmint.testsdk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import com.common.apiutil.CommonException;
import com.common.apiutil.nfc.Nfc;
import com.common.apiutil.util.SDKUtil;
import com.common.apiutil.ResultCode;
import com.common.apiutil.util.StringUtil;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.movmint.channel";
    Nfc nfc = new Nfc(this);
    Thread readThread;
    Handler handler;
    private static boolean isChecking = false;
    private final int CHECK_NFC_TIMEOUT = 1;
    private final int SHOW_NFC_DATA     = 2;
    private final byte B_CPU = 3;
    private final byte A_CPU = 1;
    private final byte A_M1  = 2;
    private final String TAG = "NfcActivity_tps900";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKUtil.getInstance(this).initSDK();
        Log.d("MainActivity", "Hi MainActivity has started");
        handler = new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CHECK_NFC_TIMEOUT: {
//                        Toast.makeText(NfcActivity_tps900.this, "未检测到卡片超时 !", Toast.LENGTH_LONG).show();
//                        open_btn.setEnabled(true);
//                        close_btn.setEnabled(false);
//                        check_btn.setEnabled(false);
                    }
                    break;
                    case SHOW_NFC_DATA: {
                        byte[] uid_data = (byte[]) msg.obj;

                        Log.d(TAG, "nfcdata[" + StringUtil.toHexString(uid_data) + "]");

//                        if (uid_data[0] == 0x42 && uid_data[0] == 0x41) {
//                            Log.e(TAG, "Conflict detected: Both Type A and Type B cards present!");
//                            return;
//                        }

//                        if (uid_data[0] == 0x42) {
//                            // TYPE B类（暂时只支持cpu卡）
//                            byte[] atqb = new byte[uid_data[16]];
//                            byte[] pupi = new byte[4];
//                            String type = null;
//
//                            System.arraycopy(uid_data, 17, atqb, 0, uid_data[16]);
//                            System.arraycopy(uid_data, 29, pupi, 0, 4);
//
//                            if (uid_data[1] == B_CPU) {
//                                type = "CPU";
////                                sendApduBtn.setEnabled(true);
////                                getAtsBtn.setEnabled(true);
////                                getUidBtn.setEnabled(true);
//                            } else {
//                                type = "unknow";
//                            }

//                            uid_editText.setText(getString(R.string.card_type) + getString(R.string.type_b) + " " + type +
//                                    "\r\n" + getString(R.string.atqb_data) + StringUtil.toHexString(atqb) +
//                                    "\r\n" + getString(R.string.pupi_data) + StringUtil.toHexString(pupi));

//                        } else if (uid_data[0] == 0x41) {
                        // TYPE A绫伙紙CPU, M1锛�
                        byte[] atqa = new byte[2];
                        byte[] sak = new byte[1];
                        byte[] uid = new byte[uid_data[5]];
                        String type = null;

                        System.arraycopy(uid_data, 2, atqa, 0, 2);//[1]~[2]
                        System.arraycopy(uid_data, 4, sak, 0, 1);//[3]
                        System.arraycopy(uid_data, 6, uid, 0, uid_data[5]);

                        if (uid_data[1] == A_CPU) {
                            type = "CPU";
//                                sendApduBtn.setEnabled(true);
//                                getAtsBtn.setEnabled(true);
//                                getUidBtn.setEnabled(true);
                        } else if (uid_data[1] == A_M1) {
                            type = "M1";
//                                authenticateBtn.setEnabled(true);
                        } else {
                            type = "unknow";
                        }
//
//                            uid_editText.setText(getString(R.string.card_type) + getString(R.string.type_a) + " " + type +
//                                    "\r\n" + getString(R.string.atqa_data) + StringUtil.toHexString(atqa) +
//                                    "\r\n" + getString(R.string.sak_data) + StringUtil.toHexString(sak) +
//                                    "\r\n" + getString(R.string.uid_data) + StringUtil.toHexString(uid));
//                        } else if(uid_data[0] == 0x46){
//                            // F卡
//                            Log.d("tagg", "Felica uid_data:"+StringUtil.toHexString(uid_data));
//                            byte[] idm = new byte[8];
//                            byte[] pmm = new byte[8];
//                            System.arraycopy(uid_data, 33, idm, 0, 8);
//                            System.arraycopy(uid_data, 41, pmm, 0, 8);
//                            Log.d("tagg", "Felica idm:"+StringUtil.toHexString(idm));
//                            Log.d("tagg", "Felica pmm:"+StringUtil.toHexString(pmm));
//                            String type = "Felica card";
////                            uid_editText.setText(getString(R.string.card_type) + getString(R.string.type_c) + " " + type +
////                                    "\r\n" + "idm:" + StringUtil.toHexString(idm) +
////                                    "\r\n" + "pmm:" + StringUtil.toHexString(pmm));
//
//                        }else {
//                            Log.e(TAG, "unknow type card!!");
//                        }
//                    }break;

//                    default:break;
                    }
                }
            }

            ;
        };

    }

    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("sendApduCommand")) {
                                // Call your Java method and get the result
                                sendAPDU();
                                // Send the result back to Dart
                                result.success("");
                            }
                           else {
                                result.notImplemented();
                            }
                        }
                );
    }

//    private String yourJavaMethod(String input) {
//        // Example Java method logic
//        try {
//            nfc.open();
//        } catch (CommonException e) {
//            e.printStackTrace();
//        }
//        checking();
//        return sendAPDUData();
//    }

    private void sendAPDU() {
        try {
            nfc.open();
            Log.d(TAG, "NFC Opened");
            if(!isChecking){
                readThread = new ReadThread();
                readThread.start();
                sendAPDUData();
            }
        } catch (CommonException e) {
            e.printStackTrace();
        }
    }


    private class ReadThread extends Thread {
        byte[] nfcData = null;


        public void run() {
            isChecking = true;
            while (isChecking){
                try {
                    nfcData = nfc.activate(10 * 1000); // 10s
                    if (null != nfcData) {
                        handler.sendMessage(handler.obtainMessage(SHOW_NFC_DATA, nfcData));
                        isChecking = false;
                        break;
                    } else {
                        Log.d(TAG, "Check MagneticCard timeout...");
                        handler.sendMessage(handler.obtainMessage(CHECK_NFC_TIMEOUT, null));
                    }
                }
                catch (CommonException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendAPDUData() {
        byte[] pSendAPDU;
        byte[] result = null;
        String apduStr;
        int length = 0;
        pSendAPDU = toByteArray((String) "00A404000E325041592E5359532F444446303100");
        length = pSendAPDU.length;
        try {
            result = nfc.transmit(pSendAPDU, length);

        } catch (CommonException e) {
            e.printStackTrace();
        }
        Log.d("Result", StringUtil.toHexString(result));
    }


    public static String toHexString(byte[] data) {
        if (data == null) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < data.length; ++i) {
                String string = Integer.toHexString(data[i] & 255);
                if (string.length() == 1) {
                    stringBuilder.append("0");
                }

                stringBuilder.append(string.toUpperCase());
            }

            return stringBuilder.toString();
        }
    }

    public static byte[] toByteArray(String hexString) {
        int hexStringLength = hexString.length();
        byte[] byteArray = null;
        int count = 0;
        char c;
        int i;

        // Count number of hex characters
        for (i = 0; i < hexStringLength; i++) {
            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        boolean first = true;
        int len = 0;
        int value;
        for (i = 0; i < hexStringLength; i++) {
            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {
                    byteArray[len] = (byte) (value << 4);
                } else {
                    byteArray[len] |= value;
                    len++;
                }
                first = !first;
            }
        }
        return byteArray;
    }
}
