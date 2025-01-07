package com.nova.star.domain;

import static com.nova.star.domain.ChatMessageTestSamples.*;
import static com.nova.star.domain.GlobalChatTestSamples.*;
import static com.nova.star.domain.IdeaChatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = getChatMessageSample1();
        ChatMessage chatMessage2 = new ChatMessage();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);

        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);

        chatMessage2 = getChatMessageSample2();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    void globalChatTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        GlobalChat globalChatBack = getGlobalChatRandomSampleGenerator();

        chatMessage.setGlobalChat(globalChatBack);
        assertThat(chatMessage.getGlobalChat()).isEqualTo(globalChatBack);

        chatMessage.globalChat(null);
        assertThat(chatMessage.getGlobalChat()).isNull();
    }

    @Test
    void ideaChatTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        IdeaChat ideaChatBack = getIdeaChatRandomSampleGenerator();

        chatMessage.setIdeaChat(ideaChatBack);
        assertThat(chatMessage.getIdeaChat()).isEqualTo(ideaChatBack);

        chatMessage.ideaChat(null);
        assertThat(chatMessage.getIdeaChat()).isNull();
    }
}
