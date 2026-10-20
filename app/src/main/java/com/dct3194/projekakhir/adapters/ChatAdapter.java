package com.dct3194.projekakhir.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;

    private final List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.isUser() ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).txtUserMessage.setText(message.getText());
        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).txtBotMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    public void addMessage(ChatMessage message) {
        if (messageList != null) {
            messageList.add(message);
            notifyItemInserted(messageList.size() - 1);
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserMessage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserMessage = itemView.findViewById(R.id.txtUserMessage);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView txtBotMessage;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBotMessage = itemView.findViewById(R.id.txtBotMessage);
        }
    }
}
