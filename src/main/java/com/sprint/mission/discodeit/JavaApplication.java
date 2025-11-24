package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static void userCRUDTest(UserService userService) {
        System.out.println("====유저 관련====");

        UUID createUserId1 = userService.create(new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동1",
                "1234"
        ));
        UUID createUserId2 = userService.create(new User(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                "홍길동2",
                "1234"
        ));

        User findUser = userService.find(createUserId1);
        System.out.println("조회[단건]: " + findUser);

        List<User> findUsers = userService.findUsers(List.of(createUserId1, createUserId2));
        System.out.println("조회[다건]: " + findUsers);

        findUser.setName("홍길동1 수정");
        userService.update(findUser);
        User findUser2 = userService.find(findUser.getId());
        System.out.println("조회[단건]: " + findUser2);

        userService.delete(findUser.getId());
        User findUser3 = userService.find(findUser.getId());
        System.out.println("조회[단건]: " + findUser3);

    }

    public static void channelCRUDTest(ChannelService channelService) {
        System.out.println("\n====채널 관련====");

        UUID createChannelId1 = channelService.create(
                new Channel(
                        UUID.randomUUID(),
                        System.currentTimeMillis(),
                        "코드잇 백엔드 채널",
                        "코드잇 백엔드 수업을 듣는 사람들을 위한 채널입니다.",
                        new User(
                                UUID.randomUUID(),
                                System.currentTimeMillis(),
                                "홍길동1",
                                "1234"
                        )
                ));
        UUID createChannelId2 = channelService.create(
                new Channel(
                        UUID.randomUUID(),
                        System.currentTimeMillis(),
                        "코드잇 프론트엔드 채널",
                        "코드잇 프론트엔드 수업을 듣는 사람들을 위한 채널입니다.",
                        new User(
                                UUID.randomUUID(),
                                System.currentTimeMillis(),
                                "홍길동2",
                                "1234"
                        )
                ));


        Channel findChannel = channelService.find(createChannelId1);
        System.out.println("조회[단건]: " + findChannel);

        List<Channel> findChannels = channelService.findChannels(List.of(createChannelId1, createChannelId2));
        System.out.println("조회[다건]: " + findChannels);

        findChannel.setName("코드잇 백엔드 채널(수정테스트)");
        findChannel.setDescription("코드잇 백엔드 수업을 듣는 사람들을 위한 채널입니다.(수정테스트)");
        channelService.update(findChannel);
        Channel findChannel2 = channelService.find(findChannel.getId());
        System.out.println("조회[단건] = " + findChannel2);

        channelService.delete(findChannel.getId());
        Channel findChannel3 = channelService.find(findChannel.getId());
        System.out.println("조회[단건] = " + findChannel3);
    }

    public static void messageCRUDTest(MessageService messageService) {

    }


    public static void main(String[] args) {
        // 서비스 초기화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();


        // 테스트
        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService);
    }
}
