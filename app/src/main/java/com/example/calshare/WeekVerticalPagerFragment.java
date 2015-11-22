package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grantlandchew.view.VerticalPager;

import org.joda.time.LocalDate;

import java.util.Locale;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class WeekVerticalPagerFragment extends Fragment {

    private LocalDate[] localDates;
    private static final int ARRAY_SIZE = 7;
    private WeekGridAdapter weekGridAdapter;
    private VerticalViewPager verticalViewPager;

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.i("WeekFragment", "onCreateView");
        View v = inflater.inflate(R.layout.test_week_layout, container, false);
        verticalViewPager = (VerticalViewPager) v.findViewById(R.id.week_vertical_view_pager);
//        final LinearLayout list = (LinearLayout) v.findViewById(R.id.innerlist);
//
//        TextView text;
//
//        for(int i = 0; i < 30; i++ ) {
//            text = new TextView(this.getActivity());
//            text.setText("test: "+i);
//            text.setTextSize(30);
//            list.addView(text);
//        }
        verticalViewPager.setAdapter(new DummyAdapter(getFragmentManager()));

        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });

        return v;
    }

    public class DummyAdapter extends FragmentPagerAdapter {

        public DummyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return WeekFragment.instantiate(WeekVerticalPagerFragment.this.getActivity(), TestWeekFragment.class.getName());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "PAGE 1";
                case 1:
                    return "PAGE 2";
                case 2:
                    return "PAGE 3";
                case 3:
                    return "PAGE 4";
                case 4:
                    return "PAGE 5";
            }
            return null;
        }

    }


}
