package Ness.Backend.domain.chat.entity;

public enum ChatType {
    // 3개의 타입 존재: AI(chatGPT의 답변), USER(유저가 타이핑으로 채팅한 경우), STT(유저가 whisper로 채팅한 경우)
    AI, USER, STT
}
