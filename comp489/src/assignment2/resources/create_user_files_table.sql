USE file_sharing;
CREATE TABLE user_files (
    user_id INT,
    file_id INT,
    ip_address VARCHAR(45) NOT NULL, -- IPv6 support (max 45 characters)
    socket_port INT NOT NULL,
    is_owner BOOLEAN DEFAULT FALSE, -- Indicates if the user is the original uploader of the file
    shared_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, file_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files(file_id) ON DELETE CASCADE
);
