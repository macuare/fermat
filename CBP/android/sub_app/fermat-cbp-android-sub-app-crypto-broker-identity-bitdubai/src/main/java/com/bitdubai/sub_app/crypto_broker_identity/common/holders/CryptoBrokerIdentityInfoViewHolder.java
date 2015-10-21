package com.bitdubai.sub_app.crypto_broker_identity.common.holders;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.sub_app.crypto_broker_identity.R;
import com.bitdubai.sub_app.crypto_broker_identity.util.UtilsFuncs;

/**
 * Created by nelson on 01/09/15.
 */
public class CryptoBrokerIdentityInfoViewHolder extends FermatViewHolder {
    private ImageView identityImage;
    private FermatTextView identityName;

    public CryptoBrokerIdentityInfoViewHolder(View itemView) {
        super(itemView);

        identityImage = (ImageView) itemView.findViewById(R.id.crypto_broker_identity_image);
        identityName = (FermatTextView) itemView.findViewById(R.id.crypto_broker_identity_alias);
    }

    public void setImage(byte[] imageInBytes) {
        RoundedBitmapDrawable roundedBitmap = imageInBytes != null ?
                UtilsFuncs.getRoundedBitmap(identityImage.getResources(), imageInBytes) :
                UtilsFuncs.getRoundedBitmap(identityImage.getResources(), R.drawable.person);

        identityImage.setImageDrawable(roundedBitmap);
    }

    public void setText(String text) {
        identityName.setText(text);
    }
}
