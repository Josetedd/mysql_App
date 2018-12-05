package eddie.icloud.com.mysql_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ServiceFragment extends Fragment {

    //Fragments can be attached in an activity

    // fragment uses onCreateView as the class entry point while activity uses onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // connect fragment layout
        View root = inflater.inflate(R.layout.services_fragment, container, false);

        // return the layout
        return root;
    }
}// end
