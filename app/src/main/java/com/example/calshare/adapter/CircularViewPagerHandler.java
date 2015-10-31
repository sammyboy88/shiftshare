package com.example.calshare.adapter;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by root on 23/08/15.
 */
public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        public static final int                 SET_ITEM_DELAY = 300;

        private ViewPager                       mViewPager;
        private ViewPager.OnPageChangeListener  mListener;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            //Log.i("CircularViewPagerHand", "constructor");
            mViewPager = viewPager;
            mViewPager.setCurrentItem(1, false);
        }

        public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
            //Log.i("CircularViewPagerHand", "setOnPageChangeListener");
            mListener = listener;
        }

        @Override
        public void onPageSelected(final int position) {
            //Log.i("CircularViewPagerHand", "onPageSelected " + Integer.toString(position));
            handleSetCurrentItemWithDelay(position);
            invokeOnPageSelected(position);
        }

        private void handleSetCurrentItemWithDelay(final int position) {
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleSetCurrentItem(position);
                }
            }, SET_ITEM_DELAY);
        }

        private void handleSetCurrentItem(final int position) {
            //Log.i("CircularViewPagerHand", "handleSetCurrentItem " + Integer.toString(position));
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if(position == 0) {
                //Log.i("CircularViewPagerHand", "handleSetCurrentItem setting to " + Integer.toString(lastPosition - 1));
                mViewPager.setCurrentItem(lastPosition - 1, false);
            } else if(position == lastPosition) {
                //Log.i("CircularViewPagerHand", "handleSetCurrentItem setting to " + Integer.toString(1));
                mViewPager.setCurrentItem(1, false);
            }
        }

        private void invokeOnPageSelected(final int position) {
            Log.i("CircularViewPagerHand", "invokeOnPageSelected " + Integer.toString(position));
            if(mListener != null) {
                mListener.onPageSelected(position - 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            //Log.i("CircularViewPagerHand", "onPageScrollStateChanged " + Integer.toString(state));
            invokeOnPageScrollStateChanged(state);
        }

        private void invokeOnPageScrollStateChanged(final int state) {
            if(mListener != null) {
                mListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            invokeOnPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        private void invokeOnPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            if(mListener != null) {
                mListener.onPageScrolled(position - 1, positionOffset, positionOffsetPixels);
            }
        }
    }

