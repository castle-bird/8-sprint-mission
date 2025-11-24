package com.sprint.mission.discodeit;

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

        List<User> findUsers = userService.findUsers(List.of(createUserId1,createUserId2));
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
