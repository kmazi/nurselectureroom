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
        tabs.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LectureFragment(), "Lecture");
        adapter.addFragment(new ObjectiveFragment(), "Objective");
        adapter.addFragment(new TheoryFragment(), "Theory");
        viewPager.setAdapter(adapter);
    }
}
