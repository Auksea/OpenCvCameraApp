package com.example.opencvcameraapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule <MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void BackButtonClick()
    {
        onView(withId(R.id.go_to_gallery)).perform(click());
        onView(withId(R.id.goBack)).perform(click());
        onView(withId(R.id.MainActivityID)).check(matches(isDisplayed()));
    }

    @Test
    public void GalleryButtonClicked()
    {
        onView(withId(R.id.go_to_gallery)).perform(click());
        onView(withId(R.id.GalleryActivityID)).check(matches(isDisplayed()));
    }
}