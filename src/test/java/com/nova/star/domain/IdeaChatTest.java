package com.nova.star.domain;

import static com.nova.star.domain.ChatMessageTestSamples.*;
import static com.nova.star.domain.IdeaChatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IdeaChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdeaChat.class);
        IdeaChat ideaChat1 = getIdeaChatSample1();
        IdeaChat ideaChat2 = new IdeaChat();
        assertThat(ideaChat1).isNotEqualTo(ideaChat2);

        ideaChat2.setId(ideaChat1.getId());
        assertThat(ideaChat1).isEqualTo(ideaChat2);

        ideaChat2 = getIdeaChatSample2();
        assertThat(ideaChat1).isNotEqualTo(ideaChat2);
    }

    @Test
    void messagesTest() {
        IdeaChat ideaChat = getIdeaChatRandomSampleGenerator();
        ChatMessage chatMessageBack = getChatMessageRandomSampleGenerator();

        ideaChat.addMessages(chatMessageBack);
        assertThat(ideaChat.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getIdeaChat()).isEqualTo(ideaChat);

        ideaChat.removeMessages(chatMessageBack);
        assertThat(ideaChat.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getIdeaChat()).isNull();

        ideaChat.messages(new HashSet<>(Set.of(chatMessageBack)));
        assertThat(ideaChat.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getIdeaChat()).isEqualTo(ideaChat);

        ideaChat.setMessages(new HashSet<>());
        assertThat(ideaChat.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getIdeaChat()).isNull();
    }
}
