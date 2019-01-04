package com.mazimia.mobile.nurselectureroom;

import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LectureContentActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_content);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        setUpViewPager(viewPager);

        // Set divider for tabs
        View root = tabs.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorAccent));
            drawable.setSize(4, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        // Add viewpager to tab layout
        tabs.setupWithViewPager(viewPager);
        //tabs.getTabAt(0).setIcon(R.drawable.study_icon);
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Get objects from previous activity via intent
        String lectureTitle = getIntent().getStringExtra("lecTitle");
        String lecNote = getIntent().getStringExtra("lecNote");
        String sectionId = getIntent().getStringExtra("sectionId");
        // Fragments
        LectureFragment lec = new LectureFragment();
        ObjectiveFragment obj = ObjectiveFragment.createObjFragment(sectionId);
        TheoryFragment theory = TheoryFragment.createQueFragmentWithSectionId(sectionId);


        // get objects from previous activity into the fragments
        Bundle lectureBundle = new Bundle();

        // pub objects into the bundles for the fragments
        lectureBundle.putString("lecTitle", lectureTitle);
        lectureBundle.putString("lecNote", lecNote);



        // Attach the bundles to the fragments
        lec.setArguments(lectureBundle);

        // Add fragments to the adapter
        adapter.addFragment(lec, "Lecture");
        adapter.addFragment(obj, "Objective");
        adapter.addFragment(theory, "Theory");
        viewPager.setAdapter(adapter);
    }
}
