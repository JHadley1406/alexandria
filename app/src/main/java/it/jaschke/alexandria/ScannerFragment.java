package it.jaschke.alexandria;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;


//import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Josiah Hadley on 10/28/2015.
 */
public class ScannerFragment extends DialogFragment implements ZXingScannerView.ResultHandler {

    public interface BarcodeScanListener{
        public void onBarcodeScanned(String code);
    }

    private final String LOG_TAG = ScannerFragment.class.getSimpleName();
    private String mCode;
    private BarcodeScanListener mListener;
    private ZXingScannerView mScannerView;

    public void onCreate(Bundle args){
        super.onCreate(args);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        setRetainInstance(true);
    }

    public static ScannerFragment newInstance(BarcodeScanListener listener,String code){
        ScannerFragment fragment = new ScannerFragment();
        fragment.mCode = code;
        fragment.mListener = listener;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        if(!result.getBarcodeFormat().equals(BarcodeFormat.ISBN13)
                && !result.getBarcodeFormat().equals(BarcodeFormat.ISBN10)
                && !result.getBarcodeFormat().equals(com.google.zxing.BarcodeFormat.EAN_13)){
            Toast.makeText(getActivity().getApplicationContext()
                    , R.string.no_isbn
                    , Toast.LENGTH_LONG).show();
            mScannerView.startCamera();
        } else {
            mCode = result.getText();
            mListener.onBarcodeScanned(mCode);
            mScannerView.stopCamera();
            dismiss();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }
}
