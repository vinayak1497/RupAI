package com.example.finvoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;
    private OnItemClickListener listener;

    public ChatAdapter(List<ChatMessage> chatMessages, OnItemClickListener listener) {
        this.chatMessages = chatMessages;
        this.listener = listener; // No need for casting now
    }

    public interface OnItemClickListener {
        void onListenClick(int position);
        void onCopyClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_message, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.tvMessage.setText(chatMessage.getMessage());

        // Only show buttons for AI responses
        if (!chatMessage.isUser()) {
            holder.btnListen.setVisibility(View.VISIBLE);
            holder.btnCopy.setVisibility(View.VISIBLE);

            holder.btnListen.setOnClickListener(v -> {
                listener.onListenClick(position); // Notify the listener (AiChatBot)
            });

            holder.btnCopy.setOnClickListener(v -> {
                listener.onCopyClick(position); // Notify the listener (AiChatBot)
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).isUser() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageButton btnListen;
        ImageButton btnCopy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            btnListen = itemView.findViewById(R.id.btnListen);
            btnCopy = itemView.findViewById(R.id.btnCopy);
        }

    }
}
