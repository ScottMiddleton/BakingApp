package com.example.scott.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BasicAppFunctionsTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule =
            new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public void clickRecyclerViewItem_launchesDetailActivity() {

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

        intended(hasComponent(DetailActivity.class.getName()));
    }

    @Test
    public void clickRecyclerViewItem_DisplaysCorrectViews() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.ingredients_tv))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_recycler_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickStepRecyclerViewItem_opensCorrectActivityAndStep() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));


        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasComponent(StepDetailActivity.class.getName()));
        intended(hasExtraWithKey(DetailActivity.RECIPE_EXTRA));
        intended(hasExtraWithKey(DetailActivity.STEP_INDEX_EXTRA));

        onView(withId(R.id.step_title))
                .check(matches(withText("Step 1")));

    }

    @Test
    public void clickStepRecyclerViewItem_CheckPrevAndNextButtonText() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));


        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.previous_step_button))
                .check(matches(withText("Back to Introduction")));

        onView(withId(R.id.next_step_button))
                .check(matches(withText("Go to step 2")));
    }
}
