package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.WrappedDrawableApi14;

@RequiresApi(19)
class WrappedDrawableApi19 extends WrappedDrawableApi14 {
    WrappedDrawableApi19(Drawable drawable) {
        super(drawable);
    }

    WrappedDrawableApi19(WrappedDrawableApi14.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public WrappedDrawableApi14.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateKitKat(this.mState, (Resources) null);
    }

    private static class DrawableWrapperStateKitKat extends WrappedDrawableApi14.DrawableWrapperState {
        DrawableWrapperStateKitKat(@Nullable WrappedDrawableApi14.DrawableWrapperState orig, @Nullable Resources res) {
            super(orig, res);
        }

        @NonNull
        public Drawable newDrawable(@Nullable Resources res) {
            return new WrappedDrawableApi19(this, res);
        }
    }
}
