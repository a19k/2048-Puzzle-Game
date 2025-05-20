package com.example.rma_2_anis_karic;

/*
public class TileAnimator extends RecyclerView.ItemAnimator {

    private final List<MoveInfo> pendingMoves = new ArrayList<>();
    private final List<MergeInfo> pendingChanges = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> pendingAdditions = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> pendingRemovals = new ArrayList<>();

    private final List<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();

    private long mAddDuration = 120; // Milliseconds
    private long mRemoveDuration = 120;
    private long mMoveDuration = 120;
    private long mChangeDuration = 150;


    @Override
    public boolean isRunning() {
        return !mAddAnimations.isEmpty() ||
                !mMoveAnimations.isEmpty() ||
                !mChangeAnimations.isEmpty() ||
                !mRemoveAnimations.isEmpty();
    }

    @Override
    public void endAnimations() {
        cancelAll(mAddAnimations);
        cancelAll(mMoveAnimations);
        cancelAll(mChangeAnimations);
        cancelAll(mRemoveAnimations);
        dispatchAnimationsFinished();
    }
    private void cancelAll(List<RecyclerView.ViewHolder> viewHolders) {
        for (int i = viewHolders.size() - 1; i >= 0; i--) {
            final View view = viewHolders.get(i).itemView;
            view.animate().cancel();
        }
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
        item.itemView.animate().cancel();

        if (mAddAnimations.remove(item))dispatchAnimationsFinished();
        if (mMoveAnimations.remove(item))dispatchAnimationsFinished();
        if (mChangeAnimations.remove(item))dispatchAnimationsFinished();
        if (mRemoveAnimations.remove(item))dispatchAnimationsFinished();
    }

    @Override
    public void runPendingAnimations() {
        boolean addPending = !mAddAnimations.isEmpty();
        boolean movePending = !mMoveAnimations.isEmpty();
        boolean changePending = !mChangeAnimations.isEmpty();
        boolean removePending = !mRemoveAnimations.isEmpty();

        if (!addPending && !movePending && !changePending && !removePending) return;


        for (RecyclerView.ViewHolder holder : pendingRemovals) {
            animateRemoveImpl(holder);
        }
        pendingRemovals.clear();

        for (RecyclerView.ViewHolder holder : pendingAdditions){
            animateAddImpl(holder);
        }
        pendingAdditions.clear();

        /*for (MoveInfo holder : pendingMoves){
            animateMoveImpl(holder);
        }
        pendingMoves.clear();*/

        /*for (MergeInfo holder : pendingChanges){
            animateChangeImpl(holder);
        }
        pendingChanges.clear();
    }

    // Helper to reset view properties to their default (non-animated) state
    private void resetAnimationProperties(View view) {
        view.setTranslationX(0f);
        view.setTranslationY(0f);
        view.setAlpha(1f);
        view.setScaleX(1f);
        view.setScaleY(1f);
        // Add any other properties you might animate here
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
        final int left = viewHolder.itemView.getLeft();
        final int top = viewHolder.itemView.getTop();

        // Log.d("TileAnimator", "PreLayout: Pos=" + viewHolder.getAdapterPosition() +
        //         " Left=" + left + " Top=" + top +
        //         " TransX=" + viewHolder.itemView.getTranslationX() +
        //         " TransY=" + viewHolder.itemView.getTranslationY());

        // For moves, we use MoveInfo to store the starting position.
        // For changes (merges), we might store old data in MergeChangeInfo.
        if ((changeFlags & FLAG_MOVED) != 0) {
            return new MoveInfo(left, top);
        } else if ((changeFlags & FLAG_CHANGED) != 0) {
            // For change animations, you might want a custom payload or store content here
            return new MergeInfo(); // Or a custom class that stores oldValue
        }

        // Default ItemHolderInfo is returned if no specific info is needed
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int left = viewHolder.itemView.getLeft();
        final int top = viewHolder.itemView.getTop();

        // Log.d("TileAnimator", "PostLayout: Pos=" + viewHolder.getAdapterPosition() +
        //         " Left=" + left + " Top=" + top +
        //         " TransX=" + viewHolder.itemView.getTranslationX() +
        //         " TransY=" + viewHolder.itemView.getTranslationY());

        // Return a generic ItemHolderInfo as postLayoutInfo provides left/top
        return super.recordPostLayoutInformation(state, viewHolder);
        // Or if you need to pass custom data, you can return a custom info type here too.
        // For a simple move, the default ItemHolderInfo provides left/top/right/bottom.
    }

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        // This method is called for newly added items
        final View view = viewHolder.itemView;

        // Reset any previous animations or states
        resetAnimationProperties(view);

        // Start from invisible/scaled down
        view.setAlpha(0f);
        view.setScaleX(0.5f); // Start half-size
        view.setScaleY(0.5f);

        mAddAnimations.add(viewHolder); // Track this animation
        view.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(mAddDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.setListener(null); // Clear listener
                        resetAnimationProperties(view); // Reset to default state
                        dispatchAddFinished(viewHolder);
                        mAddAnimations.remove(viewHolder);
                        dispatchAnimationFinished(viewHolder); // Final notification
                    }
                })
                .start();
        return true; // We are handling the animation
    }
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        if (!(preLayoutInfo instanceof MoveInfo) || postLayoutInfo == null){
            dispatchAnimationFinished(viewHolder);
            return false;
        }

        MoveInfo pre = (MoveInfo) preLayoutInfo;
        int fromX = pre.fromX;
        int fromY = pre.fromY;
        int toX = postLayoutInfo.left;
        int toY = postLayoutInfo.top;

        if (fromX == toX && fromY == toY){
            dispatchAnimationFinished(viewHolder);
            return false;
        }

        viewHolder.itemView.setTranslationX(fromX - toX);
        viewHolder.itemView.setTranslationY(fromY - toY);

        ViewPropertyAnimator animator = viewHolder.itemView.animate();
        animator.translationX(0f).
                translationY(0f).
                setDuration(1000).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewHolder.itemView.setTranslationX(0f);
                        viewHolder.itemView.setTranslationY(0f);
                        mMoveAnimations.remove(viewHolder);
                        dispatchAnimationFinished(viewHolder);
                    }
                }).start();

        mMoveAnimations.add(viewHolder);
        return true;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return true;
    }

    private void animateMoveImpl(RecyclerView.ViewHolder holder){}
    private void animateAddImpl(RecyclerView.ViewHolder holder){}
    private void animateChangeImpl(MergeInfo holder){}
    private void animateRemoveImpl(RecyclerView.ViewHolder holder){}

    private static class MoveInfo extends ItemHolderInfo{
        int fromX;
        int fromY;
        public MoveInfo(int fromX,int fromY){
            this.fromX = fromX;
            this.fromY = fromY;
        }
    }

    private static class MergeInfo extends ItemHolderInfo {
        int oldValue;
        int newValue;
        MergeInfo(int oldValue, int newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }

}*/
