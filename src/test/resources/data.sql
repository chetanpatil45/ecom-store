
-- password are in the form of password<UserLetter>123, unless specified otherwise.

INSERT INTO local_user(email, first_name, last_name, password, username, email_verified)
VALUES
('userA@junit.com', 'UserA-first_name', 'UserA-Last_name', '$2a$10$GlqorCkmiTW9aQJBkoNfp.K8O6FckJ8911smkJpwvmlBp6chNbkpq', 'UserA', true),
('userB@junit.com', 'UserB-first_name', 'UserB-Last_name', '$2a$10$U.CDB/lYDKkokKSyZ6v0CufjxP69i3MihQDUM/5X54Khmspadnmqe', 'UserB', false);