package com.uet.wifiposition.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.uet.wifiposition.utils.*;


import java.util.List;

/**
 * Created by ducnd on 21/01/2017.
 */

public abstract class BaseFragment extends Fragment implements ViewFragment {
    protected boolean mIsDestroyView = true;
    protected int mAnimationContinueId;
    private View mProgress;
    protected boolean isResume;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mIsDestroyView = false;
        View view = inflater.inflate(getLayoutMain(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        initComponents(view);
        setEvents(view);
    }

    public void setAnimationContitueId(int runAnimationContitue) {
        mAnimationContinueId = runAnimationContitue;
    }

    protected void setProgress(View progress) {
        mProgress = progress;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (mAnimationContinueId != 0) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), mAnimationContinueId);
            mAnimationContinueId = 0;
            return animation;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * Implement ViewUI interface
     */
    @Override
    public void showProgress() {
        if (mProgress != null) {
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (mProgress != null) {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        getBaseActivity().showMessage(message);
    }

    @Override
    public void showMessage(int messageId) {
        getBaseActivity().showMessage(messageId);
    }


    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


    @Override
    public void reload(Bundle bundle) {

    }

    @Override
    public void onBackRoot() {
        getBaseActivity().onBackRoot();
    }

    @Override
    public boolean isDestroyView() {
        return mIsDestroyView;
    }


    @Override
    public void onDestroyView() {
        mIsDestroyView = true;
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            isResume = true;
        }
    }

    @Override
    public void onPause() {
        isResume = false;
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isResume = !hidden;
    }

    public static Fragment openFragment(FragmentManager manager, FragmentTransaction transaction, Class<? extends BaseFragment> clazz, Bundle bundle,
                                        boolean hasAddbackstack, boolean hasCommitTransaction, AnimationSwitchScreen animations,
                                        int fragmentContent) {
        String tag = clazz.getName();
        Fragment fragment;
        try {
            //if added backstack
            fragment = manager.findFragmentByTag(tag);
            if (hasAddbackstack) {
                if (fragment == null || !fragment.isAdded()) {
                    fragment = clazz.newInstance();
                    fragment.setArguments(bundle);
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(), animations.getOpenExit(), animations.getCloseEnter(), animations.getCloseExit());
                    }
                    transaction.add(fragmentContent, fragment, tag);
                } else {
                    transaction.show(fragment);
                }
                transaction.addToBackStack(tag);

            } else {
                if (fragment != null) {
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(), animations.getOpenExit(), animations.getCloseEnter(), animations.getCloseExit());
                    }
                    transaction.show(fragment);
                } else {
                    fragment = clazz.newInstance();
                    fragment.setArguments(bundle);
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(), animations.getOpenExit(), animations.getCloseEnter(), animations.getCloseExit());
                    }
                    transaction.add(fragmentContent, fragment, tag);
                }
            }

            if (hasCommitTransaction) {
                ViewUtils.commitTransactionFragment(transaction, manager);
            }
            return fragment;

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void openFragment(FragmentManager manager, FragmentTransaction transaction, BaseFragment fragment, Bundle bundle,
                                    boolean hasAddbackstack, boolean hasCommitTransaction, AnimationSwitchScreen animations,
                                    int fragmentContent) {
        String tag = fragment.getClass().getName();
        fragment.setArguments(bundle);
        if (animations != null) {
            transaction.setCustomAnimations(animations.getOpenEnter(), animations.getOpenExit(), animations.getCloseEnter(), animations.getCloseExit());
        }
        transaction.add(fragmentContent, fragment, tag);

        if (hasAddbackstack) {
            transaction.addToBackStack(tag);
        }
        if (hasCommitTransaction) {
            ViewUtils.commitTransactionFragment(transaction, manager);
        }
    }

    public static void hideFragment(FragmentManager manager,
                                    FragmentTransaction transaction, AnimationSwitchScreen animation,
                                    boolean hasAddBackstack, boolean hasCommit, String tag) {
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment != null && fragment.isVisible()) {
            transaction.setCustomAnimations(animation.getOpenEnter(), animation.getOpenExit(), animation.getCloseEnter(), animation.getCloseExit());
            transaction.hide(fragment);
            if (hasAddBackstack) {
                transaction.addToBackStack(tag);
            }
            if (hasCommit) {
                ViewUtils.commitTransactionFragment(transaction, manager);
            }
        }
    }

    public static void removeFragment(FragmentManager manager, FragmentTransaction transaction, AnimationSwitchScreen animation,
                                      boolean hasAddBackStack, boolean hasCommit, String tag) {
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment.isVisible()) {
                transaction.setCustomAnimations(animation.getOpenEnter(), animation.getOpenExit(), animation.getCloseEnter(), animation.getCloseExit());
            }
            transaction.remove(fragment);
            if (hasAddBackStack) {
                transaction.addToBackStack(tag);
            }
            if (hasCommit) {
                ViewUtils.commitTransactionFragment(transaction, manager);
            }
        }
    }

    public static BaseFragment getCurrenFragment(FragmentManager fragmentManager) {
        List<Fragment> frags = fragmentManager.getFragments();
        if (frags != null) {
            for (int i = frags.size() - 1; i >= 0; i--) {
                Fragment fr = frags.get(i);
                if (fr != null && fr.isVisible() && fr.getTag() != null) {
                    return (BaseFragment) fr;
                }
            }
        }

        return null;
    }


}
