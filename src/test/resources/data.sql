
-- password are in the form of password<UserLetter>123, unless specified otherwise.

INSERT INTO local-user(email, first_name, last_name, password, username, email_verified)
values ("User@junit.com", "UserA-first_name", "UserA-Last_name","$2a$10$u5pkraruY7688/R1dN/WDOYXI1Fh3lwXFgjuZbxAJKQf4l3sc0uKa","userB", true),
("UserB@junit.com", "UserB-first_name", "UserB-Last_name","$2a$10$jC.BPwAuRMCv7PpH7.byZ.l/aZkYaAJbTOPIFDaqr/Rf7fVsXxef.","userB", true),