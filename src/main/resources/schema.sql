CREATE TABLE IF NOT EXISTS binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    file_name    VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    content_type VARCHAR(100)             NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   VARCHAR(50) UNIQUE       NOT NULL,
    email      VARCHAR(100) UNIQUE      NOT NULL,
    password   VARCHAR(60)              NOT NULL,
    profile_id uuid,
    CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_status
(
    id             uuid PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone,
    user_id        uuid                     NOT NULL UNIQUE,
    last_active_at timestamp with time zone NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');
CREATE TABLE IF NOT EXISTS channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        channel_type             NOT NULL
);

CREATE TABLE IF NOT EXISTS messages
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    TEXT,
    channel_id uuid                     NOT NULL,
    author_id  uuid,
    CONSTRAINT fk_channel FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS read_status
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      uuid                     NOT NULL,
    channel_id   uuid                     NOT NULL,
    last_read_at timestamp with time zone NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_channel FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    uuid NOT NULL,
    attachment_id uuid NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT fk_attachment FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);


-- 메시지 조회 (채널별, 시간순)
CREATE INDEX idx_messages_channel_created ON messages (channel_id, created_at DESC);
-- 읽기 상태 조회 (사용자+채널)
CREATE INDEX idx_read_status_user_channel ON read_status (user_id, channel_id);
-- 첨부파일 조회
CREATE INDEX idx_message_attachments_message ON message_attachments (message_id);