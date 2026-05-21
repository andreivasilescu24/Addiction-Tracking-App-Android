package com.example.addictionapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.data.model.AddictionCategory

fun getCategoryIcon(category: AddictionCategory): ImageVector {
    return when (category) {
        AddictionCategory.SMOKING -> Icons.Default.SmokingRooms
        AddictionCategory.ALCOHOL -> Icons.Default.LocalBar
        AddictionCategory.SOCIAL_MEDIA -> Icons.Default.PhoneAndroid
        AddictionCategory.FOOD -> Icons.Default.Restaurant
        AddictionCategory.GAMBLING -> Icons.Default.Casino
        AddictionCategory.OTHER -> Icons.Default.Category
    }
}
