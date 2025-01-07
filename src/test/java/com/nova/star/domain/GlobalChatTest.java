package com.nova.star.domain;

import static com.nova.star.domain.ChatMessageTestSamples.*;
import static com.nova.star.domain.GlobalChatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nova.star.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GlobalChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalChat.class);
        GlobalChat globalChat1 = getGlobalChatSample1();
        GlobalChat globalChat2 = new GlobalChat();
        assertThat(globalChat1).isNotEqualTo(globalChat2);

        globalChat2.setId(globalChat1.getId());
        assertThat(globalChat1).isEqualTo(globalChat2);

        globalChat2 = getGlobalChatSample2();
        assertThat(globalChat1).isNotEqualTo(globalChat2);
    }

    @Test
    void messagesTest() {
        GlobalChat globalChat = getGlobalChatRandomSampleGenerator();
        ChatMessage chatMessageBack = getChatMessageRandomSampleGenerator();

        globalChat.addMessages(chatMessageBack);
        assertThat(globalChat.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getGlobalChat()).isEqualTo(globalChat);

        globalChat.removeMessages(chatMessageBack);
        assertThat(globalChat.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getGlobalChat()).isNull();

        globalChat.messages(new HashSet<>(Set.of(chatMessageBack)));
        assertThat(globalChat.getMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getGlobalChat()).isEqualTo(globalChat);

        globalChat.setMessages(new HashSet<>());
        assertThat(globalChat.getMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getGlobalChat()).isNull();
    }
}
