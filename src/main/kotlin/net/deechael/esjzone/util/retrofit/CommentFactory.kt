package net.deechael.esjzone.util.retrofit

import net.deechael.esjzone.comment.Comment
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CommentFactory : Converter.Factory(), Converter<Comment, String> {

    override fun convert(value: Comment): String {
        return "${value.id}-${value.senderId}"
    }

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<Comment, String> {
        return this
    }

}