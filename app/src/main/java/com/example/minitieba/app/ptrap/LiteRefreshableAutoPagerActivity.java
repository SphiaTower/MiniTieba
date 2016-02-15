package com.example.minitieba.app.ptrap;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.minitieba.app.R;
import tower.sphia.auto_pager_recycler.lib.AutoPagerFragment;

/**
 * Created by Voyager on 8/11/2015.
 */
public abstract class LiteRefreshableAutoPagerActivity extends AppCompatActivity {


    public void onClickElevate(View view) {
        showElevatorDialog();
    }

    public void showElevatorDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the diaif(QingUApp.DEBUG) Log.
        DialogFragment newFragment = MyDialogFragment.newInstance(((AutoPagerFragment) getSupportFragmentManager().findFragmentById(R.id.flRecyclerContainer)).getAutoPagerManager().getLastPageIndex());
        newFragment.show(ft, "dialog");

    }

    protected abstract String getCustomTitle();


    protected int getContentView() {
        return R.layout.page_refreshable_pager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        setSupportActionBar(((Toolbar) findViewById(R.id.mainToolbar)));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getCustomTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flRecyclerContainer);
        if (fragment == null) {
            ((ViewGroup) findViewById(R.id.flRecyclerContainer)).addView(new View(this));
            getSupportFragmentManager().beginTransaction().add(R.id.flRecyclerContainer, initFragment()).commit();
        }
    }


    protected abstract Fragment initFragment();

    /**
     * Created by Voyager on 8/4/2015.
     */
    public static class MyDialogFragment extends DialogFragment {
        int mNum;

        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        public static MyDialogFragment newInstance(int num) {
            MyDialogFragment f = new MyDialogFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("num");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
           /* switch ((mNum-1)%6) {
                case 1: style = DialogFragment.STYLE_NO_TITLE; break;
                case 2: style = DialogFragment.STYLE_NO_FRAME; break;
                case 3: style = DialogFragment.STYLE_NO_INPUT; break;
                case 4: style = DialogFragment.STYLE_NORMAL; break;
                case 5: style = DialogFragment.STYLE_NORMAL; break;
                case 6: style = DialogFragment.STYLE_NO_TITLE; break;
                case 7: style = DialogFragment.STYLE_NO_FRAME; break;
                case 8: style = DialogFragment.STYLE_NORMAL; break;
            }
            switch ((mNum-1)%6) {
                case 4: theme = android.R.style.Theme_Holo; break;
                case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
                case 6: theme = android.R.style.Theme_Holo_Light; break;
                case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
                case 8: theme = android.R.style.Theme_Holo_Light; break;
            }*/
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog, container, false);
            View tv = v.findViewById(R.id.tvDialog);
            ((TextView) tv).setText("" + mNum);
            EditText et = (EditText) v.findViewById(R.id.etDialog);
            et.setText(mNum + "");
            // Watch for button clicks.
            Button button = (Button) v.findViewById(R.id.show);
            button.setOnClickListener(v1 -> {
                int page = Integer.valueOf(String.valueOf(et.getText()));
                ((AutoPagerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.flRecyclerContainer)).loadPage(page);
                dismiss();
                // When button is clicked, call up to owning activity.
            });

            return v;
        }
    }
}

