package com.example.aesculapius.ui.statistics

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.aesculapius.R

internal enum class UISystem(@StringRes val labelResourceID: Int, @DrawableRes val iconResourceID: Int) {
    Compose(R.string.compose, R.drawable.ic_compose),
    Views(R.string.views, R.drawable.ic_views),
}