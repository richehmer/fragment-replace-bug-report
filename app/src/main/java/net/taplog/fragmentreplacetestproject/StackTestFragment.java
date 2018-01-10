package net.taplog.fragmentreplacetestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ehmer, R.G. on 8/2/17.
 */
public class StackTestFragment extends Fragment {

    private static final String KEY_STACK_ID = "net.taplog.stackId";
    public int stackId;
    public int stackIdx;

    public static StackTestFragment newInstance(int stackId) {
        Bundle b = new Bundle();
        b.putInt(KEY_STACK_ID, stackId);
        StackTestFragment stackTestFragment = new StackTestFragment();
        stackTestFragment.setArguments(b);
        return stackTestFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_number, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ViewGroup) view).removeView(view);
        TextView stackNumber = view.findViewById(R.id.stack_number);
        TextView stackIndex = view.findViewById(R.id.stack_index);
        Bundle arguments = getArguments();
        arguments = arguments == null ? new Bundle() : arguments;
        stackId = arguments.getInt(KEY_STACK_ID, -1);
        stackIdx = getFragmentManager().getBackStackEntryCount();
        stackNumber.setText(String.format("Stack %s", stackId));
        stackIndex.setText(String.format("Index %s", stackIdx));
        TextView restoredStateText = view.findViewById(R.id.restored_state_description);
        restoredStateText.setText(savedInstanceState != null
                ? "restored instance"
                : "new instance");

    }

    /**
     * Fragment to simulate a fragment that persists it's view across config changes. This class
     * doesn't actually do anything to adapt the view to it's new parent, so won't work
     * for config changes, but is useful for testing view management during
     * backstack/fragment operations, specifically a bug seen in support
     * library 27.0.1
     */
    public static class PersistedView extends StackTestFragment {

        View persistedView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (persistedView == null) {
                persistedView = super.onCreateView(inflater, container, savedInstanceState);
            }
            return persistedView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        @Override
        public void onDetach() {
            super.onDetach();
            persistedView = null;
        }

    }

}
