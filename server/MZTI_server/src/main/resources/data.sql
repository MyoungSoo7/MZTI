insert into Member (id, loginId, password, username, mbti, profileImage)
values (1, 'tae77777', '{bcrypt}$2a$10$5oqr1GqOVJl8PrbbwfcAd.kUV4biF8A5JEdCb60/pvEpmqtcsBtoS', '김태헌', 'ESFJ', null);
insert into Member (id, loginId, password, username, mbti, profileImage)
values (2, 'kimbsu00', '{bcrypt}$2a$10$vDRuMI6X07XSbgQcCTjOwOY6C.ORdvKZwQOK17j64D3A7JxfG37MK', '김병수', 'INTP', null);

insert into FriendRelationship(id, memberId, loginId, username, profileImage, mbti)
values (1, 1, 'QkdQkddl', '빵빵이', null, 'SEXY');
insert into FriendRelationship(id, memberId, loginId, username, profileImage, mbti)
values (2, 1, 'rladhrwl', '김옥지', null, 'ININ');

insert into Question(id, questionContent, questionType)
values (1, "연애할 때 어떻게 함?", "1");
insert into Question(id, questionContent, questionType)
values (2, "키스할 때 어떻게 함?", "0");


insert into Answer(id, mbti, answerContent, questionId)
values (1, "ESFJ", "섹스부터 해야지", 1);

insert into Answer(id, mbti, answerContent, questionId)
values (2, "INFJ", "뽀뽀부터 해야지", 1);

insert into Answer(id, mbti, answerContent, questionId)
values (3, "ESFJ", "ESFJ 답변 2", 1);

insert into Answer(id, mbti, answerContent, questionId)
values (4, "ESFJ", "ESFJ 답변 3", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (5, "ESFJ", "ESFJ 답변 4", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (6, "ESFJ", "ESFJ 답변 5", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (7, "ESFJ", "ESFJ 답변 6", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (8, "ESFJ", "ESFJ 답변 7", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (9, "ESFJ", "ESFJ 답변 8", 1);

insert into Answer(id, mbti, answerContent, questionId)
values (10, "INFJ", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (11, "SEXY", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (12, "AUDI", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (13, "SONNY", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (14, "KANE", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (15, "SALAH", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (16, "HIHI", "뽀뽀부터 해야지", 1);
insert into Answer(id, mbti, answerContent, questionId)
values (17, "SEXSERX", "뽀뽀부터 해야지", 1);


#######

insert into Answer(id, mbti, answerContent, questionId)
values (18, "ESFJ", "2번섹스부터 해야지", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (19, "INFJ", "2번혀부터 넣어야지", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (20, "ESFJ", "2번ESFJ 답변 2", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (21, "ESFJ", "2번ESFJ 답변 3", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (22, "ESFJ", "2번ESFJ 답변 4", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (23, "ESFJ", "2번ESFJ 답변 5", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (24, "ESFJ", "2번ESFJ 답변 6", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (25, "ESFJ", "2번ESFJ 답변 7", 2);
insert into Answer(id, mbti, answerContent, questionId)
values (26, "ESFJ", "2번ESFJ 답변 8", 2);

insert into Answer(id, mbti, answerContent, questionId)
values (27, "INFJ", "INFJ뽀뽀부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (28, "SEXY", "SEXY뽀뽀부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (29, "AUDI", "AUDi뽀뽀부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (30, "SONNY", "강주형부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (31, "KANE", "김병수부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (32, "SALAH", "으아으아부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (33, "HIHI", "쿠쿠부터 해야지",2);
insert into Answer(id, mbti, answerContent, questionId)
values (34, "SEXSERX", "쿠르투번부터 해야지",2);

insert into TestHistory(id, memberId, mbti, score, mbtiResult)
values (1, 1, "ESFJ", 80, "2202");