INSERT INTO users (login_id, password_hash, email, nickname, location)
VALUES ('user1', 'hash123', 'user1@test.com', '테스터1', '서울');

INSERT INTO user_setting (trigger_word, emergency_trigger_word, is_voice_trained, user_id)
VALUES ('정리하자면', '잠시만요', false, 1);

INSERT INTO emergency_contacts (name, phone_number, user_setting_id)
VALUES ('엄마', '010-1111-2222', 1);