package com.backbase.android.flow.common.extensions

fun HashMap<String, String?>.mapValues(source: Any?){
    source?.let { model ->
        model::class.java.declaredFields.forEach { modelField ->
            modelField.isAccessible = true
            (modelField.get(model) as? String)?.let { value ->
                this[modelField.name] = value
            }
        }
    }
}

fun Any.mapValues(source: Any?){
    source?.let { model ->
        model::class.java.declaredFields.forEach { modelField ->
            this::class.java.declaredFields
                .filter { it.name == modelField.name }
                .forEach { field ->
                    field.isAccessible = true
                    modelField.isAccessible = true
                    modelField.get(model)?.let { value ->
                        field.set(this, value)
                    }
                }
        }
    }
}