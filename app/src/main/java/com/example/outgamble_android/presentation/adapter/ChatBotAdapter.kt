package com.example.outgamble_android.presentation.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.outgamble_android.R
import com.example.outgamble_android.data.gemini.repsonse.GeminiChatMessage
import java.util.regex.Pattern

class ChatBotAdapter(private val messages: MutableList<GeminiChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserMessage: TextView = itemView.findViewById(R.id.tvChat)
        fun bind(chatMessage: GeminiChatMessage) {
            tvUserMessage.text = chatMessage.message
        }
    }

    inner class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBotMessage: TextView = itemView.findViewById(R.id.tvChat)
        fun bind(chatMessage: GeminiChatMessage) {
            tvBotMessage.text = formatMarkdownToSpannable(chatMessage.message)
            tvBotMessage.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bc_consultation_sender, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bc_consultation_receiver, parent, false)
            BotViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is BotViewHolder) {
            holder.bind(message)
        }
    }

    fun setMessages(newMessages: List<GeminiChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun addMessage(newMessage: GeminiChatMessage) {
        messages.add(newMessage)
        notifyItemInserted(messages.size - 1)
    }

    fun updateMessage(position: Int, newText: String) {
        val oldMsg = messages[position]
        messages[position] = GeminiChatMessage(
            message = newText,
            isUser = oldMsg.isUser
        )
        notifyItemChanged(position)
    }

    // === MARKDOWN PARSER ===
    fun formatMarkdownToSpannable(input: String): SpannableStringBuilder {
        val sb = SpannableStringBuilder(input)

        // Bold: **text**
        applyPattern(sb, "\\*\\*(.+?)\\*\\*") { s, st, en ->
            s.setSpan(StyleSpan(Typeface.BOLD), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // Italic: *text* (tapi bukan **)
        applyPattern(sb, "(?<!\\*)\\*(?!\\*)(.+?)(?<!\\*)\\*(?!\\*)") { s, st, en ->
            s.setSpan(StyleSpan(Typeface.ITALIC), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        // Italic: _text_
        applyPattern(sb, "_(.+?)_") { s, st, en ->
            s.setSpan(StyleSpan(Typeface.ITALIC), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // Strikethrough: ~~text~~
        applyPattern(sb, "~~(.+?)~~") { s, st, en ->
            s.setSpan(StrikethroughSpan(), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // Inline code: `code`
        applyPattern(sb, "`(.+?)`") { s, st, en ->
            s.setSpan(TypefaceSpan("monospace"), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(
                BackgroundColorSpan(Color.argb(30, 0, 0, 0)),
                st, en,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Links: [text](http...)
        val linkPattern = Pattern.compile("\\[(.+?)\\]\\((http.+?)\\)")
        val matcher = linkPattern.matcher(sb)
        while (matcher.find()) {
            val text = matcher.group(1) ?: ""
            val url = matcher.group(2) ?: ""
            val start = matcher.start(1)
            val end = matcher.end(1)

            sb.setSpan(URLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            // replace full match with just text (hapus bracket + url)
            sb.replace(matcher.start(), matcher.end(), text)
        }

        return sb
    }

    // Helper buat apply regex
    private fun applyPattern(
        sb: SpannableStringBuilder,
        regex: String,
        spanApplier: (SpannableStringBuilder, Int, Int) -> Unit
    ) {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(sb)
        val ranges = mutableListOf<Pair<IntRange, String>>()

        while (matcher.find()) {
            val fullMatch = matcher.group(0) ?: continue
            val inner = matcher.group(1) ?: continue
            val start = matcher.start(1)
            val end = matcher.end(1)

            spanApplier(sb, start, end)
            ranges.add((matcher.start()..matcher.end() - 1) to inner)
        }

        // Replace dari belakang biar index gak geser
        for ((range, inner) in ranges.asReversed()) {
            sb.replace(range.first, range.last + 1, inner)
        }
    }
}