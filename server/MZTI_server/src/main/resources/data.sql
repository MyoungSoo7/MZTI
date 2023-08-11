insert into member (id, loginId, password, username, mbti, profileImage)
values (1, 'tae77777', '{bcrypt}$2a$10$5oqr1GqOVJl8PrbbwfcAd.kUV4biF8A5JEdCb60/pvEpmqtcsBtoS', '김태헌','ESFJ', null);

insert into FriendRelationShip(id, memberId, username, profileImage, mbti)
values (1, 1, '빵빵이', null, 'SEXY');
insert into FriendRelationShip(id, memberId, username, profileImage, mbti)
values (2, 1, '김옥지', null, 'ININ');



#     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
#     private Long id;
#
# @ManyToOne
#     @JoinColumn(name = "memberId")
#     private Member member;
#
# private String username;
# private String profileImage;
# private String mbti;

