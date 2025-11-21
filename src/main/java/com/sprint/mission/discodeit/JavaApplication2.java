package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.UUID;

public class JavaApplication2 {

    public static void main(String[] args) {
        // 메세지 CRUD 테스트를 위함

        UserService userService = new JCFUserService();
        MessageService messageService = new JCFMessageService();

        // 임시 체널 - 채널이 있어야 메세지 보낼 수 있음
        Channel channel1 = new Channel(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "채널 1번방",
                new ArrayList<>()
        );

        // 임시 유저 생성 - 유저가 없으면 메세지가 있을 수 없음
        User user1 = new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동1",
                "골목대장1",
                "홍길동1@gmail.com"
        );

        User user2 = new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동2",
                "골목대장2",
                "홍길동2@gmail.com"
        );
        User user1Insert = userService.insertUser(user1);
        User user2Insert = userService.insertUser(user2);

        // 임시 메세지 생성
        Message message1 = new Message(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "유저1의 메세지1 채널1번방에 씀",
                user1,
                channel1
        );

        Message message2 = new Message(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "유저1의 메세지2 채널1번방에 씀",
                user1,
                channel1
        );

        Message message3 = new Message(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "유저2의 메세지1 채널1번방에 씀",
                user2,
                channel1
        );

        System.out.println("=============메세지 등록==============");
        Message message1Insert = messageService.insertMessage(message1);
        Message message2Insert = messageService.insertMessage(message2);
        Message message3Insert = messageService.insertMessage(message3);
        System.out.println("message1Insert = " + message1Insert);
        System.out.println("message2Insert = " + message2Insert);
        System.out.println("message3Insert = " + message3Insert);

//        System.out.println("\n=============유저1의 메세지들==============");
//        List<Message> messagesByUserId1 = messageService.selectMessagesByUserId(user1Insert.getId());
//        messagesByUserId1.forEach(message -> System.out.println("메세지 내용 = " + message.getMessageContents() + ", 채널 =" + message.getChannel().getChannelName()));
//
//        System.out.println("\n=============유저2의 메세지들==============");
//        List<Message> messagesByUserId2 = messageService.selectMessagesByUserId(user2Insert.getId());
//        messagesByUserId2.forEach(message -> System.out.println(message.getMessageContents()));


    }
}
