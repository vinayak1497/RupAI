package com.example.finvoice;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.speech.tts.TextToSpeech;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.TextPart;

import java.util.ArrayList;
import java.util.List;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class AiChatBot extends AppCompatActivity implements ChatAdapter.OnItemClickListener {

    private EditText etPrompt;
    private ImageButton btnSend;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_KEY = "AIzaSyCzNfaMSGCvoCVjrFdUo2fAV8yXNK9WK-g";
    private TextToSpeech tts;
    private boolean isSpeaking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat_bot);

        etPrompt = findViewById(R.id.etPrompt);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);

        chatAdapter = new ChatAdapter(chatMessages, this); // Pass "this" as listener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> {
            String userInput = etPrompt.getText().toString().trim();
            if (!userInput.isEmpty()) {
                if (isFinanceRelated(userInput)) {
                    addMessage(userInput, true);
                    generateText(userInput);
                } else {
                    addMessage("❌ I can only answer finance-related questions.", false);
                }
                etPrompt.setText("");
            }
        });
        tts = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS){
                // You can set the language here if needed
                // tts.setLanguage(Locale.US);
            } else {
                Log.e("TTS", "Initialization failed!");
            }
        });
    }

    private void speakOut(String text) {
        String processedText = text.replaceAll("[\\p{Punct}]", ""); // Remove all punctuation
        tts.speak(processedText, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("AI Response", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onListenClick(int position) {
        ChatMessage message = chatMessages.get(position);
        if (isSpeaking) {
            // If already speaking, stop
            tts.stop();
            isSpeaking = false;
        } else {
            // If not speaking, start
            speakOut(message.getMessage());
            isSpeaking = true; // Set the flag to true
        }
    }

    @Override
    public void onCopyClick(int position) {
        ChatMessage message = chatMessages.get(position);
        copyToClipboard(message.getMessage());
        Toast.makeText(AiChatBot.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void generateText(String prompt) {
        GenerativeModel model = new GenerativeModel(MODEL_NAME, API_KEY);

        // 🔹 Dynamic & Structured Prompt for Finance Accuracy
        String systemPrompt = "You are a financial assistant specializing in personal finance, investments, SIP, mutual funds, banking, stock market, and Indian financial regulations. Always provide fact-based answers and avoid speculation. If uncertain, say 'I'm not sure, please consult a financial expert. Remeber the user is from rural community and hence use simple language while having Humanly conversion. Keep it short and simple. Don't use bold or italic in response";

        String finalPrompt = systemPrompt + "\nUser: " + prompt;

        model.generateContent(finalPrompt, new Continuation<GenerateContentResponse>() {
            @Override
            public void resumeWith(Object result) {
                if (result instanceof GenerateContentResponse) {
                    Content content = ((GenerateContentResponse) result).getCandidates().get(0).getContent();
                    if (content != null) {
                        String responseText = ((TextPart) content.getParts().get(0)).getText();

                        // 🔹 Validate AI response before displaying
                        if (isFinanceResponse(responseText) && !isHallucinated(responseText)) {
                            runOnUiThread(() -> addMessage(responseText, false));
                        } else {
                            runOnUiThread(() -> addMessage("⚠️ I'm not sure about this. Please consult a financial expert.", false));
                        }
                    }
                } else if (result instanceof Throwable) {
                    Log.e("GeminiAPI", "Error generating response", (Throwable) result);
                }
            }

            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }
        });
    }

    private void addMessage(String message, boolean isUser) {
        chatMessages.add(new ChatMessage(message, isUser));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerView.scrollToPosition(chatMessages.size() - 1);

        // Stop speaking when a new message is added
        if (isSpeaking) {
            tts.stop();
            isSpeaking = false;
        }
    }

    // ✅ Improved Finance Query Detection
    private boolean isFinanceRelated(String query) {
        String[] financeKeywords = {
                "finance", "financial planning", "investment", "returns", "risk management", "wealth", "savings", "passive income",
                "financial literacy", "financial goals", "bank account", "fixed deposit", "FD", "recurring deposit", "RD",
                "credit card", "debit card", "overdraft", "net banking", "interest rate", "savings account", "current account",
                "NEFT", "RTGS", "UPI", "SWIFT", "wire transfer", "stock market", "stocks", "shares", "equity", "mutual fund",
                "mutual funds", "SIP", "Systematic Investment Plan", "NAV", "Net Asset Value", "ETF", "Exchange Traded Fund",
                "index fund", "IPO", "Initial Public Offering", "blue-chip stocks", "small-cap", "mid-cap", "large-cap",
                "technical analysis", "fundamental analysis", "derivatives", "futures", "options", "intraday trading", "portfolio",
                "personal loan", "home loan", "car loan", "education loan", "mortgage", "EMI", "Equated Monthly Installment",
                "credit score", "CIBIL score", "collateral", "refinance", "debt consolidation", "life insurance", "health insurance",
                "term insurance", "ULIP", "Unit Linked Insurance Plan", "premium", "claim settlement", "coverage", "income tax",
                "GST", "Goods and Services Tax", "TDS", "Tax Deducted at Source", "tax return", "tax rebate", "tax exemption",
                "80C deduction", "capital gains tax", "property tax", "wealth tax", "corporate tax", "pension", "PPF",
                "Public Provident Fund", "NPS", "National Pension System", "EPF", "Employee Provident Fund", "annuity", "gratuity",
                "inflation", "deflation", "recession", "GDP", "Gross Domestic Product", "fiscal policy", "monetary policy",
                "repo rate", "reverse repo rate", "MF", "FD", "Full form", "emi"
        };

        for (String keyword : financeKeywords) {
            if (query.toLowerCase().contains(keyword.toLowerCase())) { // Convert keyword to lowercase
                return true;
            }
        }
        return false;
    }

    // ✅ Improved Finance Response Validation
    private boolean isFinanceResponse(String response) {
        String[] financeKeywords = {
                "money", "investment", "banking", "stock", "loan", "budget", "crypto", "interest rate", "market", "funds", "returns", "sip", "SIP", "insurance", "mutual fund", "financial planning"
        };

        for (String keyword : financeKeywords) {
            if (response.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    // ✅ AI Hallucination Detection (Rejects Unrealistic or Fake Answers)
    private boolean isHallucinated(String response) {
        String[] hallucinationIndicators = {
                "I believe", "It might be", "Possibly", "I'm not sure", "Maybe", "I think", "It could be", "Most people say", "Some say"
        };

        for (String phrase : hallucinationIndicators) {
            if (response.toLowerCase().contains(phrase)) {
                return true;
            }
        }
        return false;
    }
}

