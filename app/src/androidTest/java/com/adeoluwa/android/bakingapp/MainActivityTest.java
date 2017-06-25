package com.adeoluwa.android.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Merlyne on 6/24/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

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
    @Test
    public void mainActivityTest() {
        /*ViewInteraction textView = onView(
                allOf(withId(R.id.txt_recipe_name), withText("Nutella Pie"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_recipes),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));*/

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_recipes), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
    }
    /*@Test
    public void mainActivityTest2(){
        onView(allOf(withId(R.id.rv_recipes), withParent(allOf(withId(R.id.swipeContainer), withParent(withId(R.id.swipeContainer)))),
                isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }*/
}
