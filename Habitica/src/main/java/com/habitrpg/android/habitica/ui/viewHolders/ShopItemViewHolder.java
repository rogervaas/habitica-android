package com.habitrpg.android.habitica.ui.viewHolders;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.habitrpg.android.habitica.HabiticaBaseApplication;
import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.models.shops.ShopItem;
import com.habitrpg.android.habitica.ui.helpers.DataBindingUtils;
import com.habitrpg.android.habitica.ui.views.CurrencyView;
import com.habitrpg.android.habitica.ui.views.HabiticaIconsHelper;
import com.habitrpg.android.habitica.ui.views.shops.PurchaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.imageView)
    SimpleDraweeView imageView;
    @BindView(R.id.buyButton)
    View buyButton;
    @BindView(R.id.priceLabel)
    CurrencyView priceLabel;

    @BindView(R.id.item_detail_indicator)
    TextView itemDetailIndicator;

    @BindView(R.id.pin_indicator)
    public
    ImageView pinIndicator;

    public String shopIdentifier;
    private ShopItem item;
    private boolean isPinned;

    private Context context;

    Drawable lockedDrawable;
    Drawable limitedDrawable;
    Drawable countDrawable;

    public ShopItemViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
        itemView.setClickable(true);

        lockedDrawable = new BitmapDrawable(context.getResources(), HabiticaIconsHelper.imageOfItemIndicatorLocked());
        limitedDrawable = new BitmapDrawable(context.getResources(), HabiticaIconsHelper.imageOfItemIndicatorLimited());
        countDrawable = new BitmapDrawable(context.getResources(), HabiticaIconsHelper.imageOfItemIndicatorNumber());
        pinIndicator.setImageBitmap(HabiticaIconsHelper.imageOfPinnedItem());
    }

    public void bind(ShopItem item, boolean canBuy) {
        this.item = item;
        buyButton.setVisibility(View.VISIBLE);

        DataBindingUtils.loadImage(this.imageView, item.getImageName());

        if (item.getUnlockCondition() == null || !item.getLocked()) {
            priceLabel.setText(String.valueOf(item.getValue()));
            priceLabel.setCurrency(item.getCurrency());
            if (item.getCurrency() == null) {
                buyButton.setVisibility(View.GONE);
            }
        } else {
            priceLabel.setText(item.getUnlockCondition().readableUnlockConditionId());
        }

        itemDetailIndicator.setText(null);
        itemDetailIndicator.setVisibility(View.GONE);
        if (item.isLimited()) {
            itemDetailIndicator.setBackground(limitedDrawable);
            itemDetailIndicator.setVisibility(View.VISIBLE);
        }

        priceLabel.setLocked(item.getLocked() || !canBuy);
        if (item.getLocked()) {
            itemDetailIndicator.setBackground(lockedDrawable);
            itemDetailIndicator.setVisibility(View.VISIBLE);
        }
    }

    public void setItemCount(int count) {
        if (count > 0) {
            itemDetailIndicator.setText(String.valueOf(count));
            itemDetailIndicator.setBackground(countDrawable);
            itemDetailIndicator.setVisibility(View.VISIBLE);
        }
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
        pinIndicator.setVisibility(isPinned ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        PurchaseDialog dialog = new PurchaseDialog(context, HabiticaBaseApplication.getComponent(), item);
        dialog.shopIdentifier = shopIdentifier;
        dialog.setIsPinned(isPinned);
        dialog.show();
    }
}
