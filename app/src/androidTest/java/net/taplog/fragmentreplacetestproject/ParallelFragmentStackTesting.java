package net.taplog.fragmentreplacetestproject;

import android.os.Bundle;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onIdle;
import static android.support.test.espresso.Espresso.pressBack;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ParallelFragmentStackTesting {

    @Rule
    public ActivityTestRule<FragmentManagerTestbedActivity> mActivityTestRule =
            new ActivityTestRule<>(FragmentManagerTestbedActivity.class, true, true);

    /**
     * Test prompted by observed weird behavior in support library 27.0.1:
     * https://issuetracker.google.com/issues/68969155
     * <p>
     * when a fragment is removed and put on the back-stack, the view that was returned from
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} is removed from it's
     * container view using {@link ViewGroup#removeView(View)}
     * <p>
     * A side-effect of the operation is that the child view no longer carries a reference to
     * it's parent. In 27.0.1, that side-effect doesn't happen, the child view keeps a reference
     * to it's containing view.
     * <p>
     * This isn't a problem for fragments that always return a new view from onCreateView, but
     * is problematic for fragments like our {@link net.taplog.fragmentreplacetestproject.StackTestFragment.PersistedView}
     * that retain their view and adapt it manually to new parent views.
     * <p>
     * The error is thrown when the fragment is popped from the backstack and the fragment
     * manager attempts to add the child view to the container again, which causes the following
     * exception:
     * ava.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
     */
    @Test
    public void verifyHandlesFragmentWithPersistedViewRootLikeNormalFragment() {

        onIdle();
        final FragmentManager fm = mActivityTestRule.getActivity().getSupportFragmentManager();

        // add first fragment to container (Note: fragment has a persisted view)
        final Fragment persistedViewFrag = new StackTestFragment.PersistedView();
        fm.beginTransaction()
                .add(R.id.content, persistedViewFrag, null)
                .commit();

        onIdle();

        // add fragment with with backstack
        final Fragment fragment = StackTestFragment.newInstance(1);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_right)
                .replace(R.id.content, fragment, null)
                .addToBackStack(null)
                .commit();

        onIdle();

        // pop the back stack
        pressBack();
        onIdle();

    }


}
