package com.data

import androidx.room.TypeConverter
import com.data.model.AddictionCategory

class Converters {
    @TypeConverter
    fun fromCategory(category: AddictionCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): AddictionCategory {
        return AddictionCategory.valueOf(value)
    }
}
