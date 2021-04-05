package metri.amit.sampleapp.view;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import metri.amit.sampleapp.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


/*
* This UI test waits for countries list to be loaded in the first screen.
* Then performs click on 2nd position of the list item.
* Then waits till province list is available in the second screen
* Validated the country name for the clicked position.
* */
@SuppressWarnings("deprecation")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class CountryDetailsTest1 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void countryDetailsTest1() {
        sleep(5000); //approx time required for country list API to respond and update the UI
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                4)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        sleep(3000); //approx time required for province list API to respond and update the UI
        ViewInteraction textView = onView(
                allOf(withId(R.id.countryName), withText("Name: AFGHANISTAN"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Name: AFGHANISTAN")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /*
     * Making thread to sleep for mentioned duration
     * as the during UI testing it takes considerable amount of time
     * for the network call to respond and update the UI elements.
     * Post UI elements are updated, the assertions will be run for validations
     * */
    private void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
