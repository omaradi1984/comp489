CREATE DATABASE P2PFileSharing;

USE P2PFileSharing;

CREATE TABLE shared_files (
    id INT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    is_shared BOOLEAN DEFAULT FALSE,
    owner VARCHAR(255) NOT NULL
);